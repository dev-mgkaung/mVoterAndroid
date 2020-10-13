package com.popstack.mvoter2015.rakhine.feature.candidate.search

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.popstack.mvoter2015.rakhine.R
import com.popstack.mvoter2015.rakhine.core.recyclerview.ViewBindingViewHolder
import com.popstack.mvoter2015.rakhine.databinding.ItemCandidateBinding
import com.popstack.mvoter2015.domain.candidate.model.CandidateId
import com.popstack.mvoter2015.rakhine.feature.candidate.listing.SmallCandidateViewItem
import com.popstack.mvoter2015.rakhine.helper.diff.diffCallBackWith
import com.popstack.mvoter2015.rakhine.helper.extensions.inflater
import com.popstack.mvoter2015.rakhine.helper.extensions.withSafeAdapterPosition

internal class CandidateSearchPagingAdapter constructor(
  private val itemClick: (CandidateId) -> Unit
) :
  PagingDataAdapter<SmallCandidateViewItem, CandidateSearchPagingAdapter.CandidateSearchResultViewItemViewHolder>(
    diffCallBackWith(
      areItemTheSame = { item1, item2 ->
        item1.id == item2.id
      },
      areContentsTheSame = { item1, item2 ->
        item1 == item2
      }
    )
  ) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): CandidateSearchResultViewItemViewHolder {
    val binding = ItemCandidateBinding.inflate(parent.inflater(), parent, false)
    val viewHolder = CandidateSearchResultViewItemViewHolder(binding)
    viewHolder.apply {
      itemView.setOnClickListener {
        withSafeAdapterPosition { position ->
          getItem(position)?.let { itemAtIndex ->
            itemClick.invoke(CandidateId(itemAtIndex.id))
          }
        }
      }
    }
    return viewHolder
  }

  override fun onBindViewHolder(
      holder: CandidateSearchResultViewItemViewHolder,
      position: Int
  ) {
    getItem(position)?.let { itemAtIndex ->
      holder.binding.apply {
        ivCandidate.load(itemAtIndex.photoUrl) {
          placeholder(R.drawable.placeholder_oval)
          error(R.drawable.placeholder_oval)
          transformations(CircleCropTransformation())
          crossfade(true)
        }

        tvCandidateName.text = itemAtIndex.name
        tvCandidatePartyName.text = itemAtIndex.partyName

        ivCandidatePartySeal.load(itemAtIndex.partySealImageUrl) {
          placeholder(R.drawable.placeholder_rect)
          error(R.drawable.placeholder_rect)
          crossfade(true)
        }
      }
    }
  }

  class CandidateSearchResultViewItemViewHolder(
    binding: ItemCandidateBinding
  ) : ViewBindingViewHolder<ItemCandidateBinding>(binding)
}