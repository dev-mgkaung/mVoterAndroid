package com.popstack.mvoter2015.rakhine.feature.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.ControllerLifecycleOwner
import com.popstack.mvoter2015.rakhine.config.AppFirstTimeConfig
import com.popstack.mvoter2015.rakhine.feature.home.BottomNavigationHostController
import com.popstack.mvoter2015.rakhine.feature.location.LocationUpdateController
import com.popstack.mvoter2015.rakhine.helper.conductor.requireContext

import kotlinx.coroutines.launch

class SplashController : Controller(), LifecycleOwner {

  private val lifecycleOwner by lazy {
    ControllerLifecycleOwner(this)
  }

  override fun getLifecycle(): Lifecycle {
    return lifecycleOwner.lifecycle
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup,
    savedViewState: Bundle?
  ): View {
    val firstTimeConfig = AppFirstTimeConfig(requireContext())
    lifecycleScope.launch {
      if (firstTimeConfig.isFirstTime()) {
        router.setRoot(RouterTransaction.with(LocationUpdateController()))
      } else {
        router.setRoot(
          RouterTransaction.with(BottomNavigationHostController())
            .tag(BottomNavigationHostController.TAG)
        )
      }
    }
    return View(requireContext())
  }

}