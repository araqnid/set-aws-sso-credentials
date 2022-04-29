# AWS SSO Credentials Helper

Integrates AWS SSO accounts with command-line usage, avoiding having to go to the start page in a web browser every time
you need to fetch or refresh the environment variables for an SSO session.

You need to configure profiles in your `$HOME/.aws/config` file like so:

```
[profile profilename]
sso_start_url = https://d-example.awsapps.com/start
sso_region = eu-west-1
sso_account_id = 12345678
sso_role_name = AdministratorAccess
region = eu-west-1
```

You can then use the standard AWS SDK tool to do `aws --profile profilename sso login` and it will drive your web
browser to do an SSO login and produce intermediate credentials. However in order to use AWS services,
you need to use the SSO service to get temporary (even more temporary) credentials in a specific account and role. The
start page typically supports doing that: this tool will look in the cached credentials and call
SSO for you, and emit the shell fragment necessary to set the environment variables directly.

## Tool invocation

Syntax: `set-sso-credentials [profile-name]`

If you run the tool with no arguments, it will simply list the profiles that have an `sso_start_url` associated with
them.

If you provide a profile name, it will get the SSO attributes for that profile and call SSO to get role credentials, and
then
write a shell fragment to set the role credentials in the environment.

In this directory, run `yarn build` to compile the Typescript and bundle with the libraries into the `dist` directory,
so that
can be invoked without yarn.

## Convenience aliases

The `init_aliases.sh` script is intended to be run from `.zshrc` or `.bashrc` and will provide convenient access to the
profiles. It should be sourced with the path to the built tool as an argument:

```sh
. $HOME/src/set-aws-sso-credentials/init_aliases.sh $HOME/src/set-aws-sso-credentials/dist
```

This will provide a `aws_sso_profile` command that can take a profile name as an argument, and will automatically
produce
aliases for each profile found in your config. So with the example config above, you can run

```sh
aws_sso_profile profilename
```

Or simply use the convenience alias:

```sh
profilename
```

After setting the SSO credentials, the `aws_sso_profile` wrapper will run `aws sts get-caller-identity` to confirm the
credentials that have been installed.

