package com.popstack.mvoter2015.rakhine.feature.faq

import com.popstack.mvoter2015.domain.faq.model.FaqId

sealed class FaqViewItem {

  object BallotExample : FaqViewItem()

  object PollingStationProhibition : FaqViewItem()

  object CheckVoterList : FaqViewItem()

  object LawAndUnfairPractices : FaqViewItem()

  data class QuestionAndAnswer(
    val faqId: FaqId,
    val question: String,
    val answer: String,
    val source: String?
  ) : FaqViewItem()
}