package com.popstack.mvoter2015.rakhine.feature.votingguide.viewholders

import androidx.core.view.isVisible
import com.popstack.mvoter2015.rakhine.databinding.ItemHowToVoteStepBinding
import com.popstack.mvoter2015.rakhine.feature.votingguide.Step
import com.popstack.mvoter2015.rakhine.feature.votingguide.VotingGuideViewItem

class StepViewHolder(private val binding: ItemHowToVoteStepBinding) :
  VotingGuideViewHolder(binding.root) {

  override fun bind(viewItem: VotingGuideViewItem) {
    (viewItem as? Step)?.run {
      binding.tvStep.text = text
      binding.lineTop.isVisible = shouldShowUpperLine
      binding.lineBottom.isVisible = shouldShowLowerLine
    } ?: return
  }

}