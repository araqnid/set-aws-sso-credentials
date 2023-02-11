@file:JsModule("@aws-sdk/shared-ini-file-loader")

package org.araqnid.kotlin.setawsssocredentials.aws

import node.Dict
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

// typealias IniSection = JsRecord<String?>
// typealias ParsedIniData = JsRecord<IniSection>

external interface SharedConfigFiles {
    val credentialsFile: /* ParsedIniData */ Dict<Dict<String>>
    val configFile: /* ParsedIniData */ Dict<Dict<String>>
}

external fun loadSharedConfigFiles(init: SharedConfigInit = definedExternally): Promise<SharedConfigFiles>
