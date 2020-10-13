package com.popstack.mvoter2015.rakhine.feature.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFeatureModule {

  @ContributesAndroidInjector
  abstract fun settingsController(): SettingsController

}