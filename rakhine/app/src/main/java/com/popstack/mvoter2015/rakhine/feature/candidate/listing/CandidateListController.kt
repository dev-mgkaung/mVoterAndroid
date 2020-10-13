package com.popstack.mvoter2015.rakhine.feature.candidate.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.bluelinelabs.conductor.RouterTransaction
import com.google.android.material.tabs.TabLayout
import com.popstack.mvoter2015.rakhine.R
import com.popstack.mvoter2015.rakhine.core.mvp.MvvmController
import com.popstack.mvoter2015.rakhine.databinding.ControllerCandidateListBinding
import com.popstack.mvoter2015.rakhine.feature.HasRouter

import com.popstack.mvoter2015.rakhine.feature.candidate.search.CandidateSearchController
import com.popstack.mvoter2015.rakhine.feature.location.LocationUpdateController
import com.popstack.mvoter2015.rakhine.helper.ConstituencyTab
import com.popstack.mvoter2015.rakhine.helper.conductor.requireActivity
import com.popstack.mvoter2015.rakhine.helper.conductor.requireContext
import com.popstack.mvoter2015.rakhine.helper.conductor.setSupportActionBar
import com.popstack.mvoter2015.rakhine.helper.conductor.supportActionBar


class CandidateListController :
  MvvmController<ControllerCandidateListBinding>() {

  companion object {
    const val CONTROLLER_TAG = "CandidateListController"
    const val VIEW_STATE_SELECTED_TAB = "view_selected_tab"
  }

  private val viewModel: CandidateListViewModel by viewModels(
    store = viewModelStore
  )

  private val pagerAdapter by lazy {
    CandidateListHousePagerAdapter(this)
  }

  override val bindingInflater: (LayoutInflater) -> ControllerCandidateListBinding =
    ControllerCandidateListBinding::inflate

  private var selectedTab: Int? = null

  override fun onBindView(savedViewState: Bundle?) {
    super.onBindView(savedViewState)

    selectedTab = savedViewState?.getInt(VIEW_STATE_SELECTED_TAB)

    viewModel.viewEventLiveData.observe(this, Observer(::observeViewEvent))

    setSupportActionBar(binding.toolBar)
    supportActionBar()?.title = requireContext().getString(R.string.title_candidates)

    setHasOptionsMenu(R.menu.menu_candidate) { menuItem ->
      when (menuItem.itemId) {
        R.id.action_change_location -> {
          if (requireActivity() is HasRouter) {
            (requireActivity() as HasRouter).router()
              .pushController(RouterTransaction.with(LocationUpdateController()))
          }
          true
        }
        R.id.action_search -> {
          router.pushController(RouterTransaction.with(CandidateSearchController()))
          true
        }
      }
      false
    }

    /**
     * In case we need to change to user's selected tab,
     * we need to hide the list before changing the tab
     * to avoid glitching experience
     */
    hideCandidateList()

    binding.viewPager.offscreenPageLimit = 3
    binding.viewPager.adapter = pagerAdapter
    binding.tabLayout.setupWithViewPager(binding.viewPager)

    binding.btnChoose.setOnClickListener {
      if (requireActivity() is HasRouter) {
        (requireActivity() as HasRouter).router()
          .pushController(RouterTransaction.with(LocationUpdateController()))
      }
    }

    setupTabLayout()

    CandidateListPagerParentRouter.setParentRouter(router)

    viewModel.houseViewItemListLiveData.observe(lifecycleOwner, Observer(::observeHouseViewItem))
    viewModel.loadHouses()
  }

  private fun setupTabLayout() {
    binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabReselected(tab: TabLayout.Tab?) {}

      override fun onTabUnselected(tab: TabLayout.Tab?) {
        (tab?.customView as? ConstituencyTab)?.setUnselected()
      }

      override fun onTabSelected(tab: TabLayout.Tab?) {
        (tab?.customView as? ConstituencyTab)?.setSelected()
      }
    })
  }

  private fun showCandidateList() {
    binding.tabLayout.isVisible = true
    binding.viewPager.isVisible = true
  }

  private fun hideCandidateList() {
    binding.tabLayout.isVisible = false
    binding.viewPager.isVisible = false
  }

  private fun observeViewEvent(viewEvent: CandidateListViewModel.ViewEvent) {
    if (viewEvent is CandidateListViewModel.ViewEvent.RequestUserLocation) {
      binding.tabLayout.isVisible = false
      binding.groupChooseCandidateComponent.isVisible = true
    }
  }

  private fun observeHouseViewItem(houseViewItemList: List<CandidateListHouseViewItem>) {
    binding.groupChooseCandidateComponent.isVisible = false
    pagerAdapter.setItems(houseViewItemList)
    binding.tabLayout.removeAllTabs()
    houseViewItemList.forEach {
      binding.tabLayout.addTab(
        binding.tabLayout.newTab().setCustomView(
          ConstituencyTab(requireActivity()).apply {
            setText(it.houseName)
          }
        )
      )
    }
    changeSelectedTabIfNeeded()
  }

  private fun changeSelectedTabIfNeeded() {
    selectedTab?.let {
      binding.viewPager.post {
        binding.viewPager.setCurrentItem(it, false)
        showCandidateList()
      }
    } ?: showCandidateList()
  }

  override fun onSaveViewState(view: View, outState: Bundle) {
    outState.putInt(VIEW_STATE_SELECTED_TAB, binding.viewPager.currentItem)
    super.onSaveViewState(view, outState)
  }

  override fun onDestroyView(view: View) {
    binding.viewPager.adapter = null
    binding.tabLayout.setupWithViewPager(null)
    super.onDestroyView(view)
  }

  override fun onDestroy() {
    CandidateListPagerParentRouter.destroy()
    super.onDestroy()
  }

}