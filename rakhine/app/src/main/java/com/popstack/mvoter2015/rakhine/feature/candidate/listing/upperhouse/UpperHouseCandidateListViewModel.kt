package com.popstack.mvoter2015.rakhine.feature.candidate.listing.upperhouse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popstack.mvoter2015.domain.candidate.usecase.GetMyUpperHouseCandidateList
import com.popstack.mvoter2015.domain.constituency.usecase.GetMyUpperHouseConstituency
import com.popstack.mvoter2015.rakhine.exception.GlobalExceptionHandler
import com.popstack.mvoter2015.rakhine.feature.candidate.listing.CandidateListResult
import com.popstack.mvoter2015.rakhine.feature.candidate.listing.toSmallCandidateViewItem
import com.popstack.mvoter2015.rakhine.helper.asyncviewstate.AsyncViewStateLiveData
import com.popstack.mvoter2015.rakhine.helper.livedata.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UpperHouseCandidateListViewModel @Inject constructor(
  private val getMyUpperHouseCandidateList: GetMyUpperHouseCandidateList,
  private val getMyUpperHouseConstituency: GetMyUpperHouseConstituency,
  private val globalExceptionHandler: GlobalExceptionHandler
) : ViewModel() {

  inner class Data {
    var constituencyName: String = ""
  }

  sealed class ViewEvent {
    data class ShowConstituencyName(val constituencyName: String) : ViewEvent()
  }

  val viewItemLiveData = AsyncViewStateLiveData<CandidateListResult>()
  val viewEventLiveData = SingleLiveEvent<ViewEvent>()
  val data = Data()

  fun loadCandidates() {
    viewModelScope.launch {
      viewItemLiveData.postLoading()
      kotlin.runCatching {
        val constituency = getMyUpperHouseConstituency.execute(Unit)
        data.constituencyName = constituency.name
        viewEventLiveData.setValue(ViewEvent.ShowConstituencyName(data.constituencyName))
        if (constituency.remark != null) {
          viewItemLiveData.postSuccess(CandidateListResult.Remark(constituency.remark!!))
          return@launch
        }

        val candidateList = getMyUpperHouseCandidateList.execute(Unit)
        val smallCandidateList = candidateList
          .sortedBy {
            it.sortingBallotOrder
          }
          .map {
            it.toSmallCandidateViewItem()
          }

        viewItemLiveData.postSuccess(CandidateListResult.CandidateListViewItem(smallCandidateList))
      }
        .exceptionOrNull()
        ?.let { exception ->
          Timber.e(exception)
          viewItemLiveData.postError(exception, globalExceptionHandler.getMessageForUser(exception))
        }
    }
  }

}