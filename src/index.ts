import fs from "fs"
import {GetRoleCredentialsCommand, SSOClient} from "@aws-sdk/client-sso";
import {loadSharedConfigFiles} from "@aws-sdk/shared-ini-file-loader";

if (process.argv.length < 3) {
  console.error("Specify profile name as parameter")
  process.exit(1)
}

async function main(profile: string) {
  const credentialsCacheFile = `${process.env["HOME"]}/.aws/sso/cache/6cd2b2dcd05b0cd585381193b0b81dbf3e62d5b2.json`
  const {accessToken, region} = JSON.parse(await fs.promises.readFile(credentialsCacheFile, "utf-8"))

  const sharedConfig = await loadSharedConfigFiles()
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
      console.log(`AWS_ACCESS_KEY_ID=${accessKeyId};`)
      console.log(`AWS_SECRET_ACCESS_KEY=${secretAccessKey};`)
      console.log(`AWS_SESSION_TOKEN=${sessionToken};`)
      console.log(`AWS_DEFAULT_REGION=${region};`)
      console.log(`export AWS_ACCESS_KEY_ID AWS_SECRET_ACCESS_KEY AWS_SESSION_TOKEN AWS_DEFAULT_REGION;`)
    }
  } finally {
    ssoClient.destroy()
  }
}

main(process.argv[2]).then(
  () => {
    process.exit(0)
  },
  err => {
    console.error(`Fatal error`, err)
    process.exit(1)
  }
)
