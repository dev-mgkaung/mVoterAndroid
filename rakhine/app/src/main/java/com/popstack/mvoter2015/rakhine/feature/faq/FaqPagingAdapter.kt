package com.popstack.mvoter2015.rakhine.feature.faq

import android.text.TextUtils
import android.text.util.Linkify
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.util.LinkifyCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.popstack.mvoter2015.rakhine.databinding.ItemFaqBallotExampleBinding
import com.popstack.mvoter2015.rakhine.databinding.ItemFaqBinding
import com.popstack.mvoter2015.rakhine.databinding.ItemFaqCheckVoterListBinding
import com.popstack.mvoter2015.rakhine.databinding.ItemFaqLawsAndUnfairPracticeBinding
import com.popstack.mvoter2015.rakhine.databinding.ItemFaqPollingStationProhibitionBinding
import com.popstack.mvoter2015.domain.faq.model.FaqId
import com.popstack.mvoter2015.rakhine.helper.diff.diffCallBackWith
import com.popstack.mvoter2015.rakhine.helper.extensions.inflater
import com.popstack.mvoter2015.rakhine.helper.extensions.withSafeAdapterPosition

class FaqPagingAdapter(
  private val ballotExampleClick: () -> Unit,
  private val checkVoterListClick: () -> Unit,
  private val lawsAndUnfairPractice: () -> Unit,
  private val share: (FaqId, @ParameterName("position") Int) -> Unit
) :
  PagingDataAdapter<FaqViewItem, FaqPagingAdapter.InfoViewHolder>(
    diffCallBackWith(
      areItemTheSame = { item1, item2 ->
        if (item1 is FaqViewItem.QuestionAndAnswer && item2 is FaqViewItem.QuestionAndAnswer) {
          item1.faqId == item2.faqId
        } else {
          item1 == item2
        }
      },
      areContentsTheSame = { item1, item2 ->
        item1 == item2
      }
    )
  ) {

  /**
   * Set that remembers which faq has been expanded by user
   * This is used in [onBindViewHolder] to determine whether a view has been expanded or not
   */
  private val expandedFaqSet = mutableSetOf<FaqId>()

  companion object {
    const val VIEW_TYPE_BALLOT_EXAMPLE = 1
    const val VIEW_TYPE_PROHIBITION = 2
    const val VIEW_TYPE_CHECK_VOTER_LIST = 3
    const val VIEW_TYPE_LAWS_AND_UNFAIR_PRACTICE = 4
    const val VIEW_TYPE_FAQ = 5
  }

  override fun getItemViewType(position: Int): Int {
    //Sometimes crashing with Index Out of Bounds
    if (position >= itemCount) {
      return super.getItemViewType(position)
    }
    getItem(position)?.let { itemAtIndex ->
      return when (itemAtIndex) {
        FaqViewItem.BallotExample -> VIEW_TYPE_BALLOT_EXAMPLE
        FaqViewItem.PollingStationProhibition -> VIEW_TYPE_PROHIBITION
        FaqViewItem.CheckVoterList -> VIEW_TYPE_CHECK_VOTER_LIST
        is FaqViewItem.LawAndUnfairPractices -> VIEW_TYPE_LAWS_AND_UNFAIR_PRACTICE
        is FaqViewItem.QuestionAndAnswer -> VIEW_TYPE_FAQ
      }
    }
    return super.getItemViewType(position)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
    when (viewType) {
      VIEW_TYPE_BALLOT_EXAMPLE -> {
        val binding = ItemFaqBallotExampleBinding.inflate(parent.inflater(), parent, false)
        return InfoViewHolder.BallotExampleViewHolder(binding).also { holder ->
          holder.itemView.setOnClickListener {
            ballotExampleClick.invoke()
          }
        }
      }
      VIEW_TYPE_PROHIBITION -> {
        val binding =
          ItemFaqPollingStationProhibitionBinding.inflate(parent.inflater(), parent, false)
        return InfoViewHolder.PollingStationProhibitionViewHolder(binding)
      }
      VIEW_TYPE_CHECK_VOTER_LIST -> {
        val binding =
          ItemFaqCheckVoterListBinding.inflate(parent.inflater(), parent, false)
        return InfoViewHolder.CheckVoterListViewHolder(binding).also {
          it.itemView.setOnClickListener {
            checkVoterListClick.invoke()
          }
        }
      }
      VIEW_TYPE_LAWS_AND_UNFAIR_PRACTICE -> {
        val binding =
          ItemFaqLawsAndUnfairPracticeBinding.inflate(parent.inflater(), parent, false)
        return InfoViewHolder.LawsAndUnfairPractices(binding).also { holder ->
          holder.itemView.setOnClickListener {
            lawsAndUnfairPractice.invoke()
          }
        }
      }
      VIEW_TYPE_FAQ -> {
        val binding = ItemFaqBinding.inflate(parent.inflater(), parent, false)
        return InfoViewHolder.FaqViewHolder(binding).also { holder ->
          holder.itemView.setOnClickListener {
            holder.withSafeAdapterPosition { position ->
              val faqId = (getItem(position) as FaqViewItem.QuestionAndAnswer).faqId
              if (expandedFaqSet.contains(faqId)) {
                expandedFaqSet.remove(faqId)
              } else {
                expandedFaqSet.add(faqId)
              }
              notifyItemChanged(position)
            }
          }

          holder.binding.ivShare.setOnClickListener {
            holder.withSafeAdapterPosition { position ->
              val itemAtIndex = getItem(position) as FaqViewItem.QuestionAndAnswer
              share(itemAtIndex.faqId, position)
            }
          }

        }
      }
    }
    throw IllegalStateException()
  }

  override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {

    if (holder is InfoViewHolder.FaqViewHolder) {
      getItem(position)?.let { itemAtIndex ->
        val faqViewItem = itemAtIndex as FaqViewItem.QuestionAndAnswer
        holder.binding.apply {
          tvQuestion.text = faqViewItem.question
          tvAnswer.text = HtmlCompat.fromHtml(faqViewItem.answer, HtmlCompat.FROM_HTML_MODE_COMPACT).trim()
          tvSource.isVisible = faqViewItem.source != null
          tvSource.text = faqViewItem.source ?: ""

          if (expandedFaqSet.contains(faqViewItem.faqId)) {
            tvAnswer.maxLines = Int.MAX_VALUE
            tvAnswer.ellipsize = null
            ivShare.isVisible = true
            val isAdded = LinkifyCompat.addLinks(tvAnswer, Linkify.WEB_URLS)
            tvAnswer.isClickable = isAdded
          } else {
            tvAnswer.maxLines = 2
            tvAnswer.ellipsize = TextUtils.TruncateAt.END
            ivShare.isVisible = false
            tvAnswer.isClickable = false
          }
        }
      }
    }
  }

  sealed class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class BallotExampleViewHolder(binding: ItemFaqBallotExampleBinding) :
      InfoViewHolder(binding.root)

    class PollingStationProhibitionViewHolder(binding: ItemFaqPollingStationProhibitionBinding) :
      InfoViewHolder(binding.root)

    class CheckVoterListViewHolder(binding: ItemFaqCheckVoterListBinding) :
      InfoViewHolder(binding.root)

    class LawsAndUnfairPractices(binding: ItemFaqLawsAndUnfairPracticeBinding) :
      InfoViewHolder(binding.root)

    class FaqViewHolder(val binding: ItemFaqBinding) : InfoViewHolder(binding.root)

  }

}