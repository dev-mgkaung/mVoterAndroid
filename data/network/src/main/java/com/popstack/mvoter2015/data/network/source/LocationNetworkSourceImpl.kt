package com.popstack.mvoter2015.data.network.source

import com.popstack.mvoter2015.data.common.location.LocationNetworkSource
import com.popstack.mvoter2015.data.network.api.MvoterApi
import com.popstack.mvoter2015.data.network.helper.executeOrThrow
import com.popstack.mvoter2015.domain.location.model.Township
import com.popstack.mvoter2015.domain.location.model.TownshipPCode
import com.popstack.mvoter2015.domain.location.model.Ward
import javax.inject.Inject

class LocationNetworkSourceImpl @Inject constructor(
  private val mvoterApi: MvoterApi
) : LocationNetworkSource {
  override fun getStateRegionList(): List<String> =
    mvoterApi.getStateRegionList().executeOrThrow().data

  // TODO: Replace Pcode with unique identifier
  override fun getTownshipsListForStateRegion(stateRegionIdentifier: String): List<Township> =
    mvoterApi.getTownshipsForStateRegion(stateRegionIdentifier).executeOrThrow().data.map { Township(TownshipPCode(""), it) }

  override fun getWardsForTownship(stateRegion: String, township: String): List<String> =
    mvoterApi.getWardsForTownship(stateRegion, township).executeOrThrow().data

  override fun getWardDetails(stateRegion: String, township: String, wardName: String): Ward =
    mvoterApi.getWardDetails(stateRegion, township, wardName).executeOrThrow().data.wardAttributes.toWard()
}