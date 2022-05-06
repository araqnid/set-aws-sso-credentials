import fs from "fs"
import {GetRoleCredentialsCommand, GetRoleCredentialsCommandOutput, SSOClient} from "@aws-sdk/client-sso";
import {loadSharedConfigFiles} from "@aws-sdk/shared-ini-file-loader";
import {Profile} from "@aws-sdk/types";
import {spawn} from "child_process";

async function loadAccessToken(): Promise<string> {
  const credentialsCacheFile = `${process.env["HOME"]}/.aws/sso/cache/6cd2b2dcd05b0cd585381193b0b81dbf3e62d5b2.json`
  const {accessToken} = JSON.parse(await fs.promises.readFile(credentialsCacheFile, "utf-8"))
  return accessToken
}

async function attemptSSOLogin(profileName: string) {
  return new Promise((resolve, reject) => {
    const command = spawn("aws", ["--profile", profileName, "sso", "login"], {
      stdio: ["inherit", "pipe", "pipe"]
    })

    command.stdout.setEncoding("utf-8")
    command.stdout.on("data", data => {
      for (const line of data.split('\n')) {
        process.stderr.write(`aws sso login: ${line}\n`)
      }
    })

    command.stderr.setEncoding("utf-8")
    command.stderr.on("data", data => {
      for (const line of data.split('\n')) {
        process.stderr.write(`aws sso login:(stderr): ${line}\n`)
      }
    })

    command.on("error", err => {
      reject(err)
    })

    command.on("close", exitCode => {
      if (exitCode === 0)
        resolve(undefined)
      else
        reject(new Error(`aws command terminated with exit code ${exitCode}`))
    })
  })
}

async function getRoleCredentialsPossiblyLogin(ssoClient: SSOClient, ssoConfig: Profile, profileName: string): Promise<GetRoleCredentialsCommandOutput> {
  try {
    return await ssoClient.send(new GetRoleCredentialsCommand({
      accountId: ssoConfig.sso_account_id,
      roleName: ssoConfig.sso_role_name,
      accessToken: await loadAccessToken()
    }));
  } catch (err: any) {
    if (err["$fault"] === "client" && err["$metadata"].httpStatusCode === 401) {
      await attemptSSOLogin(profileName)
      return await ssoClient.send(new GetRoleCredentialsCommand({
        accountId: ssoConfig.sso_account_id,
        roleName: ssoConfig.sso_role_name,
        accessToken: await loadAccessToken()
      }));
    }
    throw err
  }
}

async function main(profile?: string) {
  const sharedConfig = await loadSharedConfigFiles()
  if (profile) {
    const ssoConfig = sharedConfig.configFile[profile]
    if (!ssoConfig) throw new Error(`No such profile in $HOME/.aws/config: ${profile}`)
    const ssoClient = new SSOClient({region: ssoConfig.sso_region})
    try {
      const response = await getRoleCredentialsPossiblyLogin(ssoClient, ssoConfig, profile)

      if (response.roleCredentials) {
        const {accessKeyId, secretAccessKey, sessionToken} = response.roleCredentials
        process.stdout.write(`AWS_ACCESS_KEY_ID=${accessKeyId};\n`)
        process.stdout.write(`AWS_SECRET_ACCESS_KEY=${secretAccessKey};\n`)
        process.stdout.write(`AWS_SESSION_TOKEN=${sessionToken};\n`)
        process.stdout.write(`AWS_DEFAULT_REGION=${ssoConfig.sso_region};\n`)
        process.stdout.write(`export AWS_ACCESS_KEY_ID AWS_SECRET_ACCESS_KEY AWS_SESSION_TOKEN AWS_DEFAULT_REGION;\n`)
      }
    } finally {
      ssoClient.destroy()
    }
  }
  else {
    for (const [name, value] of Object.entries(sharedConfig.configFile)) {
      if (value.sso_start_url) {
        process.stdout.write(`${name}\n`)
      }
    }
  }
}

const [, , profile] = process.argv
main(profile).then(
  () => {
    process.exit(0)
  },
  err => {
    console.error(`Fatal error`, err)
    process.exit(1)
  }
)
