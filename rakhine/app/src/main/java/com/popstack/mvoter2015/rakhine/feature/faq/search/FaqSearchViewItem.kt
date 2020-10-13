package com.popstack.mvoter2015.rakhine.feature.faq.search

import com.popstack.mvoter2015.domain.faq.model.FaqId

data class FaqSearchViewItem(
  val faqId: FaqId,
  val question: String,
  val answer: String,
  val source: String?
)