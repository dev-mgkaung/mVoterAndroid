package com.popstack.mvoter2015.rakhine.feature.location

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.popstack.mvoter2015.rakhine.core.mvp.MvvmController
import com.popstack.mvoter2015.rakhine.databinding.ControllerTownshipChooserBinding
import com.popstack.mvoter2015.rakhine.feature.HasRouter
import com.popstack.mvoter2015.rakhine.helper.asyncviewstate.AsyncViewState
import com.popstack.mvoter2015.rakhine.helper.conductor.requireActivity


class TownshipChooserController : MvvmController<ControllerTownshipChooserBinding>() {



  private val viewModel: TownshipChooserViewModel by viewModels()

  override val bindingInflater: (LayoutInflater) -> ControllerTownshipChooserBinding =
    ControllerTownshipChooserBinding::inflate

  private lateinit var stateRegionTownshipAdapter: StateRegionTownshipRecyclerViewAdapter

  override fun onBindView(savedViewState: Bundle?) {
    super.onBindView(savedViewState)

    stateRegionTownshipAdapter = viewModel.run {
      StateRegionTownshipRecyclerViewAdapter(
        onStateRegionClicked,
        onTownshipClicked,
        onTownshipRetryClicked
      )
    }

    binding.rvStatRegionTownship.apply {
      adapter = stateRegionTownshipAdapter
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    binding.ivClose.setOnClickListener {
      if (requireActivity() is HasRouter) {
        (requireActivity() as HasRouter).router()
          .popCurrentController()
      }
    }

    binding.btnRetry.setOnClickListener {
      viewModel.loadStateRegions()
    }

    viewModel.onTownshipChosenEvent.observe(
      this,
      {
        if (requireActivity() is HasRouter) {
          (requireActivity() as HasRouter).router()
            .popCurrentController()
          (targetController as? OnTownshipChosenListener)?.onTownshipChosen(it.first, it.second)
        }
      }
    )
    viewModel.onStateRegionChosen.observe(
      this,
      {
        binding.rvStatRegionTownship.postDelayed(
          {
            (binding.rvStatRegionTownship.layoutManager as LinearLayoutManager).scrollToPosition(it)
          },
          200
        )
      }
    )
    viewModel.viewItemLiveData.observe(this, Observer(::observeViewItem))

    if (savedViewState == null) {
      viewModel.loadStateRegions()
    }
  }

  private fun changeComponentVisibility(
    rvIsVisible: Boolean = false,
    errorIsVisible: Boolean = false,
    progressIsVisible: Boolean = false
  ) = with(binding) {
    progressBar.isVisible = progressIsVisible
    groupErrorComponent.isVisible = errorIsVisible
    rvStatRegionTownship.isVisible = rvIsVisible
  }

  private fun observeViewItem(viewState: AsyncViewState<List<StateRegionTownshipViewItem>>) {
    when (viewState) {
      is AsyncViewState.Loading -> {
        changeComponentVisibility(progressIsVisible = true)
      }
      is AsyncViewState.Success -> {
        stateRegionTownshipAdapter.submitList(ArrayList(viewState.value))
        changeComponentVisibility(rvIsVisible = true)
      }
      is AsyncViewState.Error -> {
        val error = viewState.errorMessage
        binding.tvErrorMessage.text = error
        changeComponentVisibility(errorIsVisible = true)
      }
    }
  }

}