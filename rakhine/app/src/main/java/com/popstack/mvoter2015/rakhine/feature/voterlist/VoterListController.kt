package com.popstack.mvoter2015.rakhine.feature.voterlist

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.popstack.mvoter2015.rakhine.R
import com.popstack.mvoter2015.rakhine.core.LifeCycleAwareController
import com.popstack.mvoter2015.rakhine.databinding.ControllerVoterListBinding
import com.popstack.mvoter2015.rakhine.di.Injectable
import com.popstack.mvoter2015.rakhine.feature.browser.OpenBrowserDelegate
import com.popstack.mvoter2015.rakhine.helper.RecyclerViewMarginDecoration
import com.popstack.mvoter2015.rakhine.helper.conductor.requireActivity
import com.popstack.mvoter2015.rakhine.helper.conductor.requireContext
import com.popstack.mvoter2015.rakhine.helper.conductor.setSupportActionBar
import com.popstack.mvoter2015.rakhine.helper.conductor.supportActionBar
import kotlinx.coroutines.launch
import javax.inject.Inject

class VoterListController : LifeCycleAwareController<ControllerVoterListBinding>(), Injectable {

  override val bindingInflater: (LayoutInflater) -> ControllerVoterListBinding = ControllerVoterListBinding::inflate

  @Inject
  lateinit var openBrowserDelegate: OpenBrowserDelegate

  private val voterListAdapter = VoterListLinkRecyclerViewAdapter(
    itemClick = { item ->
      val link = item.second
      lifecycleScope.launch {
        openBrowserDelegate.browserHandler().launchInBrowser(requireActivity(), link)
      }
    }
  )

  override fun onBindView(savedViewState: Bundle?) {
    super.onBindView(savedViewState)

    setSupportActionBar(binding.toolBar)
    supportActionBar()?.setDisplayHomeAsUpEnabled(true)

    binding.rvVoterList.apply {
      adapter = voterListAdapter
      layoutManager = LinearLayoutManager(requireContext())
      val dimen = context.resources.getDimensionPixelSize(R.dimen.recycler_view_item_margin)
      addItemDecoration(RecyclerViewMarginDecoration(dimen, 0))
    }
  }

}