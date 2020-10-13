package com.popstack.mvoter2015.rakhine.feature.faq

import androidx.lifecycle.ViewModel
import com.popstack.mvoter2015.rakhine.di.viewmodel.ViewModelKey
import com.popstack.mvoter2015.rakhine.feature.faq.ballot.BallotExampleController
import com.popstack.mvoter2015.rakhine.feature.faq.ballot.BallotExampleViewModel
import com.popstack.mvoter2015.rakhine.feature.faq.search.FaqSearchController
import com.popstack.mvoter2015.rakhine.feature.faq.search.FaqSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FaqFeatureModule {

  @ContributesAndroidInjector
  abstract fun faqController(): FaqController

  @Binds
  @IntoMap
  @ViewModelKey(FaqViewModel::class)
  abstract fun faqViewModel(viewModel: FaqViewModel): ViewModel

  @ContributesAndroidInjector
  abstract fun faqSearchController(): FaqSearchController

  @Binds
  @IntoMap
  @ViewModelKey(FaqSearchViewModel::class)
  abstract fun faqSearchViewModel(viewModel: FaqSearchViewModel): ViewModel

  @ContributesAndroidInjector
  abstract fun ballotExampleController(): BallotExampleController

  @Binds
  @IntoMap
  @ViewModelKey(BallotExampleViewModel::class)
  abstract fun ballotExampleViewModel(viewModel: BallotExampleViewModel): ViewModel

}