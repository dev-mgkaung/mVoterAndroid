package com.popstack.mvoter2015.rakhine.feature.browser

import com.popstack.mvoter2015.rakhine.feature.settings.AppSettings
import javax.inject.Inject

class OpenBrowserDelegate @Inject constructor(
  private val appSettings: AppSettings
) {

  suspend fun browserHandler(): OpenBrowserHandler {
    return if (appSettings.getUseExternalBrowser()) {
      ExternalBrowserHandler()
    } else {
      InAppBrowserHandler()
    }
  }
}