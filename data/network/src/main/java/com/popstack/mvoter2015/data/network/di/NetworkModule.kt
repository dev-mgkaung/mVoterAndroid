package com.popstack.mvoter2015.data.network.di

import com.popstack.mvoter2015.data.common.appupdate.AppUpdateNetworkSource
import com.popstack.mvoter2015.data.common.candidate.CandidateNetworkSource
import com.popstack.mvoter2015.data.common.faq.FaqNetworkSource
import com.popstack.mvoter2015.data.common.location.LocationNetworkSource
import com.popstack.mvoter2015.data.common.news.NewsNetworkSource
import com.popstack.mvoter2015.data.common.party.PartyNetworkSource
import com.popstack.mvoter2015.data.network.appupdate.AppUpdateNetworkSourceImpl
import com.popstack.mvoter2015.data.network.source.CandidateNetworkSourceImpl
import com.popstack.mvoter2015.data.network.source.FaqNetworkSourceImpl
import com.popstack.mvoter2015.data.network.source.LocationNetworkSourceImpl
import com.popstack.mvoter2015.data.network.source.NewsNetworkSourceImpl
import com.popstack.mvoter2015.data.network.source.PartyNetworkSourceImpl
import dagger.Binds
import dagger.Module

@Module(includes = [ServiceModule::class])
abstract class NetworkModule {

  @Binds
  abstract fun appUpdateNetworkSource(appUpdateNetworkSource: AppUpdateNetworkSourceImpl): AppUpdateNetworkSource

  @Binds
  abstract fun partyNetworkSource(partyNetworkSource: PartyNetworkSourceImpl): PartyNetworkSource

  @Binds
  abstract fun faqNetworkSource(faqNetworkSource: FaqNetworkSourceImpl): FaqNetworkSource

  @Binds
  abstract fun newsNetworkSource(newsNetworkSource: NewsNetworkSourceImpl): NewsNetworkSource

  @Binds
  abstract fun candidateNetworkSource(candidateNetworkSourceImpl: CandidateNetworkSourceImpl): CandidateNetworkSource

  @Binds
  abstract fun locationNetworkSource(locationNetworkSource: LocationNetworkSourceImpl): LocationNetworkSource
}