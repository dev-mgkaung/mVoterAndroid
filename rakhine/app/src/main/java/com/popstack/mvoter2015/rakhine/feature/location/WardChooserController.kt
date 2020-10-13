package com.popstack.mvoter2015.rakhine.feature.location

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.popstack.mvoter2015.rakhine.core.mvp.MvvmController
import com.popstack.mvoter2015.rakhine.databinding.ControllerWardChooserBinding
import com.popstack.mvoter2015.rakhine.feature.HasRouter
import com.popstack.mvoter2015.rakhine.helper.asyncviewstate.AsyncViewState
import com.popstack.mvoter2015.rakhine.helper.conductor.requireActivity


class WardChooserController(bundle: Bundle) : MvvmController<ControllerWardChooserBinding>(bundle) {

  companion object {
    const val ARG_STATE_REGION = "candidate_upper_house"
    const val ARG_TOWNSHIP = "township"

    fun newInstance(stateRegion: String, township: String) = WardChooserController(
      bundleOf(
        ARG_STATE_REGION to stateRegion,
        ARG_TOWNSHIP to township
      )
    )
  }



  private val stateRegion by lazy {
    args.getString(ARG_STATE_REGION)
  }

  private val township by lazy {
    args.getString(ARG_TOWNSHIP)
  }

  private val viewModel: WardChooserViewModel by viewModels()

  override val bindingInflater: (LayoutInflater) -> ControllerWardChooserBinding =
    ControllerWardChooserBinding::inflate

  private lateinit var wardRecyclerViewAdapter: WardRecyclerViewAdapter

  override fun onBindView(savedViewState: Bundle?) {
    super.onBindView(savedViewState)

    wardRecyclerViewAdapter = WardRecyclerViewAdapter(
      viewModel.onWardClicked
    )

    binding.rvWard.apply {
      adapter = wardRecyclerViewAdapter
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    binding.tvTownship.text = township

    binding.ivClose.setOnClickListener {
      if (requireActivity() is HasRouter) {
        (requireActivity() as HasRouter).router()
          .popCurrentController()
      }
    }

    binding.btnRetry.setOnClickListener {
      viewModel.loadWards(stateRegion!!, township!!)
    }

    viewModel.onWardChosenEvent.observe(
      this,
      { ward ->
        if (requireActivity() is HasRouter) {
          (requireActivity() as HasRouter).router()
            .popCurrentController()
          (targetController as? OnWardChosenListener)?.onWardChosen(ward)
        }
      }
    )
    viewModel.viewItemLiveData.observe(this, Observer(::observeViewItem))

    if (savedViewState == null) {
      viewModel.loadWards(stateRegion!!, township!!)
    }
  }

  private fun changeComponentVisibility(
    progressBarIsVisible: Boolean = false,
    wardListIsVisible: Boolean = false,
    errorComponentIsVisible: Boolean = false
  ) = with(binding) {
    progressBar.isVisible = progressBarIsVisible
    rvWard.isVisible = wardListIsVisible
    groupErrorComponent.isVisible = errorComponentIsVisible
  }

  private fun observeViewItem(viewState: AsyncViewState<List<String>>) {
    when (viewState) {
      is AsyncViewState.Loading -> {
        changeComponentVisibility(progressBarIsVisible = true)
      }
      is AsyncViewState.Success -> {
        wardRecyclerViewAdapter.submitList(ArrayList(viewState.value))
        changeComponentVisibility(wardListIsVisible = true)
      }
      is AsyncViewState.Error -> {
        binding.tvErrorMessage.text = viewState.errorMessage
        changeComponentVisibility(errorComponentIsVisible = true)
      }
    }
  }

}