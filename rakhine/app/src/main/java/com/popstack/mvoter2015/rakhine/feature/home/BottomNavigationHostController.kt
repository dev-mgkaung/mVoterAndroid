package com.popstack.mvoter2015.rakhine.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.RouterTransaction
import com.popstack.mvoter2015.rakhine.R
import com.popstack.mvoter2015.rakhine.core.LifeCycleAwareController
import com.popstack.mvoter2015.rakhine.databinding.ControllerBottomNavHostBinding
import com.popstack.mvoter2015.rakhine.di.Injectable
import com.popstack.mvoter2015.rakhine.feature.candidate.listing.CandidateListController
import com.popstack.mvoter2015.rakhine.feature.faq.FaqController
import com.popstack.mvoter2015.rakhine.feature.news.NewsController
import com.popstack.mvoter2015.rakhine.feature.party.listing.PartyListController
import com.popstack.mvoter2015.rakhine.feature.votingguide.VotingGuideController
import com.popstack.mvoter2015.rakhine.helper.conductor.BNVRouterPagerAdapter

class BottomNavigationHostController : LifeCycleAwareController<ControllerBottomNavHostBinding>(), Injectable {

  companion object {
    const val TAG = "BottomNavigationHostController"
  }

  private val viewModelStore = ViewModelStore()

  override val bindingInflater: (LayoutInflater) -> ControllerBottomNavHostBinding =
    ControllerBottomNavHostBinding::inflate

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup,
    savedViewState: Bundle?
  ): View {
    BottomNavigationHostViewModelStore.viewModelStore = viewModelStore
    val view = super.onCreateView(inflater, container, savedViewState)

    if (savedViewState == null) {
      binding.bottomNavigationView.selectedItemId = R.id.navigation_candidate
    }
    return view
  }

  override fun onBindView(savedViewState: Bundle?) {

    val bottomNavRouterPagerAdapter = BNVRouterPagerAdapter(
      this,
      binding.bottomNavigationView,
      binding.homeViewPager,
      mapOf(
        R.id.navigation_candidate to { RouterTransaction.with(CandidateListController()).tag(CandidateListController.CONTROLLER_TAG) },
        R.id.navigation_party to {
          RouterTransaction.with(
            PartyListController()
          )
        },
        R.id.navigation_how_to_vote to {
          RouterTransaction.with(VotingGuideController())
        },
        R.id.navigation_info to {
          RouterTransaction.with(FaqController())
        },
        R.id.navigation_news to {
          RouterTransaction.with(NewsController())
        }
      )
    )
    binding.homeViewPager.adapter = bottomNavRouterPagerAdapter

    binding.bottomNavigationView.setOnNavigationItemReselectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.navigation_candidate -> {
          bottomNavRouterPagerAdapter.getRouter(0)?.popToTag(CandidateListController.CONTROLLER_TAG)
        }
        R.id.navigation_party -> {
          kotlin.runCatching {
            view?.findViewById<RecyclerView>(R.id.rvParty)?.smoothScrollToPosition(0)
          }
        }
        R.id.navigation_info -> {
          kotlin.runCatching {
            view?.findViewById<RecyclerView>(R.id.rvFaq)?.smoothScrollToPosition(0)
          }
        }
        R.id.navigation_news -> {
          kotlin.runCatching {
            view?.findViewById<RecyclerView>(R.id.rvNews)?.smoothScrollToPosition(0)
          }
        }
      }
    }
  }

  override fun onDestroyView(view: View) {
    viewModelStore.clear()
    BottomNavigationHostViewModelStore.viewModelStore = null
    super.onDestroyView(view)
  }

}