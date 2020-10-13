package com.popstack.mvoter2015.rakhine.di.component

import android.app.Application
import com.popstack.mvoter2015.rakhine.MVoterApp
import com.popstack.mvoter2015.rakhine.di.module.AppModule
import com.popstack.mvoter2015.rakhine.feature.about.AboutFeatureModule
import com.popstack.mvoter2015.rakhine.feature.candidate.CandidateFeatureModule
import com.popstack.mvoter2015.rakhine.feature.faq.FaqFeatureModule
import com.popstack.mvoter2015.rakhine.feature.location.LocationUpdateFeatureModule
import com.popstack.mvoter2015.rakhine.feature.news.NewsFeatureModule
import com.popstack.mvoter2015.rakhine.feature.party.PartyFeatureModule
import com.popstack.mvoter2015.rakhine.feature.settings.SettingsFeatureModule
import com.popstack.mvoter2015.rakhine.feature.voterlist.VoterListFeatureModule
import com.popstack.mvoter2015.rakhine.feature.votingguide.VotingGuideFeatureModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AppModule::class,
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    CandidateFeatureModule::class,
    PartyFeatureModule::class,
    LocationUpdateFeatureModule::class,
    FaqFeatureModule::class,
    VotingGuideFeatureModule::class,
    NewsFeatureModule::class,
    SettingsFeatureModule::class,
    AboutFeatureModule::class,
    VoterListFeatureModule::class
  ]
)
interface AppComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }

  fun inject(application: MVoterApp)

}