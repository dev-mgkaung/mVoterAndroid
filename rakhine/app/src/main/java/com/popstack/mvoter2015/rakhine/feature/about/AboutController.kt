package com.popstack.mvoter2015.rakhine.feature.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.bluelinelabs.conductor.RouterTransaction
import com.popstack.mvoter2015.rakhine.BuildConfig
import com.popstack.mvoter2015.rakhine.R
import com.popstack.mvoter2015.rakhine.core.LifeCycleAwareController
import com.popstack.mvoter2015.rakhine.databinding.ControllerAboutBinding
import com.popstack.mvoter2015.rakhine.di.Injectable
import com.popstack.mvoter2015.rakhine.feature.browser.OpenBrowserDelegate
import com.popstack.mvoter2015.rakhine.helper.conductor.requireActivity
import com.popstack.mvoter2015.rakhine.helper.conductor.requireContext
import com.popstack.mvoter2015.rakhine.helper.conductor.setSupportActionBar
import com.popstack.mvoter2015.rakhine.helper.conductor.supportActionBar

import kotlinx.coroutines.launch
import javax.inject.Inject

class AboutController : LifeCycleAwareController<ControllerAboutBinding>(), Injectable {

  @Inject
  lateinit var openBrowserDelegate: OpenBrowserDelegate

  override val bindingInflater: (LayoutInflater) -> ControllerAboutBinding =
    ControllerAboutBinding::inflate

  override fun onBindView(savedViewState: Bundle?) {
    super.onBindView(savedViewState)

    setSupportActionBar(binding.toolBar)
    supportActionBar()?.title = ""
    supportActionBar()?.setDisplayHomeAsUpEnabled(true)

    binding.viewTermOfUse.setOnClickListener {
      runCatching {
        lifecycleScope.launch {
          openBrowserDelegate.browserHandler()
            .launchInBrowser(requireActivity(), "https://mvoterapp.com/terms")
        }
      }
    }

    binding.viewPrivacyPolicy.setOnClickListener {
      runCatching {
        lifecycleScope.launch {
          openBrowserDelegate.browserHandler()
            .launchInBrowser(requireActivity(), "https://mvoterapp.com/privacy")
        }
      }
    }

    binding.viewLicense.setOnClickListener {
      router.pushController(RouterTransaction.with(LicenseController()))
    }

    binding.ivContactFacebook.setOnClickListener {
      openFacebookPage()
    }

    binding.ivContactMail.setOnClickListener {
      openEmail()
    }

    binding.ivContactWebsite.setOnClickListener {
      openAppWebsite()
    }

    binding.tvVersion.text = requireContext().getString(R.string.version, BuildConfig.VERSION_NAME)
  }

  /**
   * Copied from https://stackoverflow.com/a/15022153/3125020
   */
  private fun openEmail() {
    runCatching {
      val emailIntent = Intent(
        Intent.ACTION_SENDTO,
        Uri.fromParts("mailto", "popstackhack@gmail.com", null)
      )
      emailIntent.putExtra(
        Intent.EXTRA_EMAIL, arrayOf("popstackhack@gmail.com")
      ) //To make it work on 4.3 in case we support older version
      startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }
  }

  private fun openAppWebsite() {
    runCatching {
      lifecycleScope.launch {
        openBrowserDelegate.browserHandler()
          .launchInBrowser(requireActivity(), "https://mvoterapp.com/")
      }
    }
  }

  private fun openFacebookPage() {
    try {
      requireContext().packageManager.getPackageInfo("com.facebook.katana", 0)
      startActivity(
        Intent(
          Intent.ACTION_VIEW,
          Uri.parse("fb://page/1731925863693956")
        )
      )
    } catch (e: Exception) {
      runCatching {
        lifecycleScope.launch {
          openBrowserDelegate.browserHandler()
            .launchInBrowser(requireActivity(), "https://www.facebook.com/mvoter2015")
        }
      }
    }

  }

}