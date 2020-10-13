package com.popstack.mvoter2015.rakhine.feature.about

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AboutFeatureModule {

  @ContributesAndroidInjector
  abstract fun aboutController(): AboutController

}