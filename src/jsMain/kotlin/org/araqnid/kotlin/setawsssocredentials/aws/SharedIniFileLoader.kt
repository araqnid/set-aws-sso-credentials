@file:JsModule("@aws-sdk/shared-ini-file-loader")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws

import kotlin.js.Promise

external interface SharedConfigInit {
    /**
     * The path at which to locate the ini credentials file. Defaults to the
     * value of the `AWS_SHARED_CREDENTIALS_FILE` environment variable (if
     * defined) or `~/.aws/credentials` otherwise.
     */
    var filepath: String?

    /**
     * The path at which to locate the ini config file. Defaults to the value of
     * the `AWS_CONFIG_FILE` environment variable (if defined) or
     * `~/.aws/config` otherwise.
     */
    var configFilepath: String?
}

external interface SharedConfigFiles {
    val credentialsFile: ParsedIniData
    val configFile: ParsedIniData
}

external fun loadSharedConfigFiles(init: SharedConfigInit = definedExternally): Promise<SharedConfigFiles>
