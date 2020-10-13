package com.popstack.mvoter2015.rakhine.feature.votingguide.viewholders

import com.popstack.mvoter2015.rakhine.databinding.ItemHowToVoteSectionTitleBinding
import com.popstack.mvoter2015.rakhine.feature.votingguide.SectionTitle
import com.popstack.mvoter2015.rakhine.feature.votingguide.VotingGuideViewItem

class SectionTitleViewHolder(private val binding: ItemHowToVoteSectionTitleBinding) :
  VotingGuideViewHolder(binding.root) {

  override fun bind(viewItem: VotingGuideViewItem) {
    val sectionTitle = viewItem as? SectionTitle ?: return
    binding.tvSectionTitle.text = sectionTitle.text
  }

}