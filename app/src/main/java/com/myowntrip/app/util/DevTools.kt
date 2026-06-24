package com.myowntrip.app.util

import com.myowntrip.app.BuildConfig

/**
 * Herramientas de QA (p. ej. borrado total). Solo variant debug; nunca en release.
 */
object DevTools {
  val allowClearAllUserData: Boolean
    get() = BuildConfig.ENABLE_DEV_WIPE && BuildConfig.BUILD_TYPE == "debug"
}
