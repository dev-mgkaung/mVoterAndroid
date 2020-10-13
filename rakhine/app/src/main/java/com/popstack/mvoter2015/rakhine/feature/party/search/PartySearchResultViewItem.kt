package com.popstack.mvoter2015.rakhine.feature.party.search

import com.popstack.mvoter2015.domain.party.model.PartyId

data class PartySearchResultViewItem(
  val partyId: PartyId,
  val flagImageUrl: String,
  val name: String,
  val region: String
)