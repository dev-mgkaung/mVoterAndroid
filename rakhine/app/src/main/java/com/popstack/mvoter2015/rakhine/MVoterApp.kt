package com.popstack.mvoter2015.rakhine

import android.app.Application
import android.os.Build
import com.popstack.mvoter2015.rakhine.di.AppInjector
import com.popstack.mvoter2015.rakhine.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class MVoterApp : Application(), HasAndroidInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

  override fun onCreate() {
    super.onCreate()

    DaggerAppComponent.builder()
      .application(this)
      .build()
      .inject(this)

    if (BuildConfig.DEBUG) {
      //Setup Debug Configs
      Timber.plant(DebugTree())
    } else {
      //Setup Non-Debug Configs
    }

    AppInjector.initAutoInjection(this)
  }

  private fun isRoboUnitTest(): Boolean {
    return "robolectric" == Build.FINGERPRINT
  }

  override fun androidInjector(): AndroidInjector<Any> {
    return dispatchingAndroidInjector
  }

}