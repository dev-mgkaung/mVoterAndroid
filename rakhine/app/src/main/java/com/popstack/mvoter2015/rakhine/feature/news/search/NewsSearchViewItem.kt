package com.popstack.mvoter2015.rakhine.feature.news.search

import com.popstack.mvoter2015.domain.news.model.NewsId

data class NewsSearchViewItem(
  val id: NewsId,
  val title: String,
  val summary: String,
  val imageUrl: String?,
  val publishedDate: String,
  val url: String
)