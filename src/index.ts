import fs from "fs"
import {readlines} from "readlines"
import {SSO} from "@aws-sdk/client-sso";

if (process.argv.length < 3) {
  console.error("Specify profile name as parameter")
  process.exit(1)
}

async function loadProfileSSOConfig(profile: string): Promise<Record<string, string>> {
  const configFile = `${process.env["HOME"]}/.aws/config`
  const lines = await new Promise<string[]>(((resolve, reject) => {
    readlines(configFile, (err: any, lines: string[]) => {
      if (err) reject(err)
      else resolve(lines)
    })
  }))
  let section: string = "default"
  const settings: Record<string, string> = {}
  for (const line of lines) {
    const sectionMatch = line.match(/^\[(.+)]$/)
    if (sectionMatch) {
      section = sectionMatch[1]
      continue
    }
    const settingMatch = line.match(/^([^ ]+) *= *(.+)/)
    if (settingMatch && section === `profile ${profile}`) {
      settings[settingMatch[1]] = settingMatch[2]
    }
  }
  if (Object.keys(settings).length === 0) {
    throw new Error(`Profile "${profile}" not found in ${configFile}`)
  }
  return settings
}

async function main(profile: string) {
  const credentialsCacheFile = `${process.env["HOME"]}/.aws/sso/cache/6cd2b2dcd05b0cd585381193b0b81dbf3e62d5b2.json`
  const {accessToken, region} = JSON.parse(await fs.promises.readFile(credentialsCacheFile, "utf-8"))

  const ssoConfig = await loadProfileSSOConfig(profile)
  const sso = new SSO({region})
  const response = await sso.getRoleCredentials({
    accountId: ssoConfig.sso_account_id,
    roleName: ssoConfig.sso_role_name,
    accessToken
  })

  if (response.roleCredentials) {
    const {accessKeyId, secretAccessKey, sessionToken} = response.roleCredentials
    console.log(`AWS_ACCESS_KEY_ID=${accessKeyId};`)
    console.log(`AWS_SECRET_ACCESS_KEY=${secretAccessKey};`)
    console.log(`AWS_SESSION_TOKEN=${sessionToken};`)
    console.log(`AWS_DEFAULT_REGION=${region};`)
    console.log(`export AWS_ACCESS_KEY_ID AWS_SECRET_ACCESS_KEY AWS_SESSION_TOKEN AWS_DEFAULT_REGION;`)
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
