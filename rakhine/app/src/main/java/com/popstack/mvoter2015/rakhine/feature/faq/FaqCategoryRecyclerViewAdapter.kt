package com.popstack.mvoter2015.rakhine.feature.faq

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.popstack.mvoter2015.rakhine.R
import com.popstack.mvoter2015.rakhine.databinding.ItemFaqCategoryBinding
import com.popstack.mvoter2015.domain.faq.model.FaqCategory
import com.popstack.mvoter2015.rakhine.feature.faq.ballot.displayString
import com.popstack.mvoter2015.rakhine.helper.extensions.inflater
import com.popstack.mvoter2015.rakhine.helper.extensions.withSafeAdapterPosition

class FaqCategoryRecyclerViewAdapter(
  val onClickCategory: (FaqCategory) -> Unit
) : RecyclerView.Adapter<FaqCategoryRecyclerViewAdapter.FaqCategoryViewHolder>() {

  private var selectedCategory: FaqCategory? = null

  private val categoryList = listOf(
    FaqCategory.VOTER_LIST,
    FaqCategory.DIPLOMATIC,
    FaqCategory.INTERNATIONAL_OBSERVER,
    FaqCategory.CANDIDATE,
    FaqCategory.CONFLICT_RESOLUTION,
    FaqCategory.MEDIATION_COMMITTEES
  )

  class FaqCategoryViewHolder(val binding: ItemFaqCategoryBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqCategoryViewHolder {

    return FaqCategoryViewHolder(
      ItemFaqCategoryBinding.inflate(
        parent.inflater(),
        parent,
        false
      )
    ).also { holder ->
      holder.itemView.setOnClickListener {
        holder.withSafeAdapterPosition { position ->
          onClickCategory.invoke(categoryList[position])
        }
      }
    }
  }

  fun setSelectedCategory(faqCategory: FaqCategory) {
    selectedCategory = faqCategory
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int {
    return categoryList.size
  }

  override fun onBindViewHolder(holder: FaqCategoryViewHolder, position: Int) {
    val category = categoryList[position]
    holder.binding.apply {

      tvCategory.setTextColor(
        if (category == selectedCategory) {
          viewSelectedIndicator.visibility = View.VISIBLE
          ContextCompat.getColor(holder.itemView.context, R.color.accent)
        } else {
          viewSelectedIndicator.visibility = View.INVISIBLE
          ContextCompat.getColor(holder.itemView.context, R.color.text_primary)
        }
      )

      tvCategory.text = category.displayString(holder.itemView.context)
    }
  }
}