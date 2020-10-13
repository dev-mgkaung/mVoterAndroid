package com.popstack.mvoter2015.rakhine.feature.browser

import android.app.Activity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.popstack.mvoter2015.rakhine.R
import com.popstack.mvoter2015.rakhine.helper.intent.Intents

interface OpenBrowserHandler {

  fun launchInBrowser(activity: Activity, url: String)

}

class InAppBrowserHandler() : OpenBrowserHandler {

  override fun launchInBrowser(activity: Activity, url: String) {
    CustomTabsIntent.Builder()
      .setToolbarColor(ContextCompat.getColor(activity, R.color.accent))
      .build()
      .launchUrl(activity, url.toUri())
  }

}

class ExternalBrowserHandler() : OpenBrowserHandler {

  override fun launchInBrowser(activity: Activity, url: String) {
    val intent = Intents.viewUrl(url)
    activity.startActivity(intent)
  }

}