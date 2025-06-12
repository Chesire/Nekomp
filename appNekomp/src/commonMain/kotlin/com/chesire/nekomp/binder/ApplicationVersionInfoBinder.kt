package com.chesire.nekomp.binder

import com.chesire.nekomp.BuildKonfig
import com.chesire.nekomp.feature.settings.data.ApplicationVersionInfo

/**
 * Binds information to an instance of [ApplicationVersionInfo].
 */
class ApplicationVersionInfoBinder : ApplicationVersionInfo {

    override val versionName: String = BuildKonfig.VERSION_NAME
    override val versionCode: Int = BuildKonfig.VERSION_CODE
}
