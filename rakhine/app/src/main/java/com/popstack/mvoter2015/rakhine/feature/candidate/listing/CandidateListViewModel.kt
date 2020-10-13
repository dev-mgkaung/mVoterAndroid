package com.popstack.mvoter2015.rakhine.feature.candidate.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popstack.mvoter2015.domain.constituency.model.HouseType
import com.popstack.mvoter2015.domain.location.usecase.GetUserStateRegion
import com.popstack.mvoter2015.domain.location.usecase.GetUserWard
import com.popstack.mvoter2015.rakhine.helper.livedata.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class CandidateListViewModel @Inject constructor(
    private val houseViewItemMapper: CandidateListHouseViewItemMapper,
    private val getUserStateRegion: GetUserStateRegion,
    private val getUserWard: GetUserWard
) : ViewModel() {

  sealed class ViewEvent {
    object RequestUserLocation : ViewEvent()
  }

  val viewEventLiveData = SingleLiveEvent<ViewEvent>()

  val houseViewItemListLiveData =
    SingleLiveEvent<List<CandidateListHouseViewItem>>()

  fun loadHouses() {
    viewModelScope.launch {
      val houseTypes = HouseType.values()

      val userWard = getUserWard.execute(Unit) ?: run {
        viewEventLiveData.postValue(ViewEvent.RequestUserLocation)
        return@launch
      }
      val viewItems = houseTypes.map { houseType ->
        houseViewItemMapper.mapFromHouseType(houseType, userWard)
      }
      houseViewItemListLiveData.postValue(viewItems)
    }
  }
}