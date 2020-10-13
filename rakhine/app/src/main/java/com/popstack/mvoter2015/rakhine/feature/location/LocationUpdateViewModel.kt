package com.popstack.mvoter2015.rakhine.feature.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popstack.mvoter2015.domain.location.model.StateRegionTownship
import com.popstack.mvoter2015.domain.location.model.Ward
import com.popstack.mvoter2015.domain.location.usecase.GetWardDetails
import com.popstack.mvoter2015.domain.location.usecase.SaveUserStateRegionTownship
import com.popstack.mvoter2015.domain.location.usecase.SaveUserWard
import com.popstack.mvoter2015.rakhine.exception.GlobalExceptionHandler
import com.popstack.mvoter2015.rakhine.helper.LocalityUtils
import com.popstack.mvoter2015.rakhine.helper.livedata.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LocationUpdateViewModel @Inject constructor(
  private val getWardDetails: GetWardDetails,
  private val saveUserWard: SaveUserWard,
  private val saveUserStateRegionTownship: SaveUserStateRegionTownship,
  private val globalExceptionHandler: GlobalExceptionHandler
) : ViewModel() {

  sealed class ViewEvent {
    object EnableDoneButton : ViewEvent()
    object NavigateToHomePage : ViewEvent()
    object ShowConstituencyLoading : ViewEvent()
    object HideWardField : ViewEvent()
    object ShowWardField : ViewEvent()
    data class ShowErrorMessage(val error: String) : ViewEvent()
  }

  inner class Data {
    var chosenTownship: String? = null
    var chosenStateRegion: String? = null
    var chosenWard: String? = null
    var wardDetails: Ward? = null
    var isTownshipFromNPT: Boolean = false
  }

  val data = Data()
  val viewEventLiveData = SingleLiveEvent<ViewEvent>()

  fun onTownshipChosen(
    chosenStateRegion: String,
    chosenTownship: String
  ) {
    data.chosenStateRegion = chosenStateRegion
    data.chosenTownship = chosenTownship
    data.isTownshipFromNPT = LocalityUtils.isTownshipFromNPT(chosenTownship)
    if (data.isTownshipFromNPT) {
      data.chosenWard = chosenTownship
      hideWardField()
      onWardChosen(chosenTownship)
    } else {
      data.chosenWard = null
      showWardField()
    }

    data.wardDetails = null
  }

  private fun hideWardField() {
    viewEventLiveData.setValue(ViewEvent.HideWardField)
  }

  private fun showWardField() {
    viewEventLiveData.setValue(ViewEvent.ShowWardField)
  }

  fun onWardChosen(ward: String) {
    data.chosenWard = ward
    viewModelScope.launch {
      kotlin.runCatching {
        viewEventLiveData.setValue(ViewEvent.ShowConstituencyLoading)
        data.wardDetails = fetchWardDetails()
        viewEventLiveData.setValue(ViewEvent.EnableDoneButton)
      }.exceptionOrNull()?.let { exception ->
        data.wardDetails = null
        viewEventLiveData.setValue(ViewEvent.ShowErrorMessage(globalExceptionHandler.getMessageForUser(exception)))
        Timber.e(exception)
      }
    }
  }

  private suspend fun fetchWardDetails() = getWardDetails.execute(
    GetWardDetails.Params(
      stateRegion = data.chosenStateRegion!!,
      township = data.chosenTownship!!,
      ward = data.chosenWard!!
    )
  )

  fun onDoneClicked() {
    viewModelScope.launch {
      kotlin.runCatching {
        viewEventLiveData.setValue(ViewEvent.ShowConstituencyLoading)
        if (data.wardDetails == null) {
          data.wardDetails = fetchWardDetails()
        }

        saveUserWard.execute(SaveUserWard.Params(data.wardDetails!!))

        saveUserStateRegionTownship.execute(
          SaveUserStateRegionTownship.Params(
            StateRegionTownship(
              data.chosenStateRegion!!,
              data.chosenTownship!!
            )
          )
        )

        viewEventLiveData.setValue(ViewEvent.NavigateToHomePage)
      }.exceptionOrNull()?.let { exception ->
        viewEventLiveData.setValue(ViewEvent.ShowErrorMessage(globalExceptionHandler.getMessageForUser(exception)))
        Timber.e(exception)
      }
    }
  }

}