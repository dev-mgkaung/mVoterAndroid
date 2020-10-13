package com.popstack.mvoter2015.rakhine.feature.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelinelabs.conductor.RouterTransaction
import com.popstack.mvoter2015.rakhine.R
import com.popstack.mvoter2015.rakhine.core.mvp.MvvmController
import com.popstack.mvoter2015.rakhine.databinding.ControllerNewsBinding
import com.popstack.mvoter2015.domain.news.model.NewsId
import com.popstack.mvoter2015.rakhine.exception.GlobalExceptionHandler

import com.popstack.mvoter2015.rakhine.feature.browser.OpenBrowserDelegate
import com.popstack.mvoter2015.rakhine.feature.home.BottomNavigationHostViewModelStore
import com.popstack.mvoter2015.rakhine.feature.news.search.NewsSearchController
import com.popstack.mvoter2015.rakhine.helper.RecyclerViewMarginDecoration
import com.popstack.mvoter2015.rakhine.helper.conductor.requireActivity
import com.popstack.mvoter2015.rakhine.helper.conductor.requireContext
import com.popstack.mvoter2015.rakhine.helper.conductor.setSupportActionBar
import com.popstack.mvoter2015.rakhine.helper.conductor.supportActionBar
import com.popstack.mvoter2015.rakhine.helper.extensions.isLandScape
import com.popstack.mvoter2015.rakhine.helper.extensions.isTablet

import com.popstack.mvoter2015.rakhine.paging.CommonLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsController : MvvmController<ControllerNewsBinding>() {





  override val bindingInflater: (LayoutInflater) -> ControllerNewsBinding =
    ControllerNewsBinding::inflate

  private val viewModel: NewsViewModel by viewModels(
    store = BottomNavigationHostViewModelStore.viewModelStore ?: viewModelStore
  )

  private val newsPagingAdapter by lazy {
    NewsRecyclerViewAdapter(this::onNewsItemClick)
  }

  private val globalExceptionHandler by lazy {
    GlobalExceptionHandler(requireContext())
  }

  @Inject
  lateinit var openBrowserDelegate: OpenBrowserDelegate

  override fun onBindView(savedViewState: Bundle?) {
    super.onBindView(savedViewState)
    setSupportActionBar(binding.toolBar)
    supportActionBar()?.title = requireContext().getString(R.string.navigation_news)

    setHasOptionsMenu(R.menu.menu_news, this::handleMenuItemClick)

    binding.rvNews.apply {
      adapter = newsPagingAdapter.withLoadStateFooter(
        footer = CommonLoadStateAdapter(newsPagingAdapter::retry)
      )
      layoutManager = if (requireContext().isTablet() && requireContext().isLandScape()) {
        GridLayoutManager(requireContext(), 2)
      } else {
        LinearLayoutManager(requireContext())
      }
      val dimen =
        context.resources.getDimensionPixelSize(R.dimen.recycler_view_item_margin)
      if (requireContext().isTablet() && requireContext().isLandScape()) {
        addItemDecoration(RecyclerViewMarginDecoration(dimen, 2))
      } else {
        addItemDecoration(RecyclerViewMarginDecoration(dimen, 0))
      }
    }

    binding.btnRetry.setOnClickListener {
      newsPagingAdapter.retry()
    }

    newsPagingAdapter.addLoadStateListener { loadStates ->
      val refreshLoadState = loadStates.refresh
      binding.rvNews.isVisible = refreshLoadState is LoadState.NotLoading
      if (refreshLoadState is LoadState.Loading) binding.progressIndicator.show()
      else binding.progressIndicator.hide()
      binding.tvErrorMessage.isVisible = refreshLoadState is LoadState.Error
      binding.btnRetry.isVisible = refreshLoadState is LoadState.Error

      if (refreshLoadState is LoadState.Error) {
        binding.tvErrorMessage.text =
          globalExceptionHandler.getMessageForUser(refreshLoadState.error)
      }
    }

    lifecycleScope.launch {
      viewModel.getNewsPagingFlow()
        .collectLatest { pagingData ->
          newsPagingAdapter.submitData(lifecycle, pagingData)
        }
    }
  }

  private fun onNewsItemClick(
    id: NewsId,
    url: String
  ) {
    lifecycleScope.launch {
      openBrowserDelegate.browserHandler()
        .launchInBrowser(requireActivity(), url)
    }
  }

  private fun handleMenuItemClick(menuItem: MenuItem): Boolean {
    return when (menuItem.itemId) {
      R.id.action_search -> {
        router.pushController(RouterTransaction.with(NewsSearchController()))
        true
      }
      else -> false
    }
  }

}