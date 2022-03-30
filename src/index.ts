import fs from "fs"
import {GetRoleCredentialsCommand, SSOClient} from "@aws-sdk/client-sso";
import {loadSharedConfigFiles} from "@aws-sdk/shared-ini-file-loader";

async function main(profile?: string) {
  const credentialsCacheFile = `${process.env["HOME"]}/.aws/sso/cache/6cd2b2dcd05b0cd585381193b0b81dbf3e62d5b2.json`
  const {accessToken, region} = JSON.parse(await fs.promises.readFile(credentialsCacheFile, "utf-8"))

  const sharedConfig = await loadSharedConfigFiles()
  if (profile) {
    const ssoConfig = sharedConfig.configFile[profile]
    const ssoClient = new SSOClient({region})
    try {
      const response = await ssoClient.send(new GetRoleCredentialsCommand({
        accountId: ssoConfig.sso_account_id,
        roleName: ssoConfig.sso_role_name,
        accessToken
      }))

      if (response.roleCredentials) {
        const {accessKeyId, secretAccessKey, sessionToken} = response.roleCredentials
        process.stdout.write(`AWS_ACCESS_KEY_ID=${accessKeyId};\n`)
        process.stdout.write(`AWS_SECRET_ACCESS_KEY=${secretAccessKey};\n`)
        process.stdout.write(`AWS_SESSION_TOKEN=${sessionToken};\n`)
        process.stdout.write(`AWS_DEFAULT_REGION=${region};\n`)
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
