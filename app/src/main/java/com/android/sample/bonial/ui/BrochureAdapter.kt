package com.android.sample.bonial.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.sample.bonial.databinding.BrochureItemBinding
import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.extention.layoutInflater

class BrochureAdapter : ListAdapter<Brochure, BrochureAdapter.BrochureViewHolder>(DiffCallback) {

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BrochureViewHolder.from(parent)

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: BrochureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun isPremium(position: Int): Boolean {
        return getItem(position).isPremium
    }

    /**
     * ViewHolder for Brochure items. All work is done by data binding.
     */
    class BrochureViewHolder(private val binding: BrochureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brochure: Brochure) {
            with(binding) {
                this.brochure = brochure
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): BrochureViewHolder {
                val binding = BrochureItemBinding.inflate(
                    parent.context.layoutInflater,
                    parent,
                    false
                )
                return BrochureViewHolder(binding)
            }
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Brochure]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Brochure>() {
        override fun areItemsTheSame(oldItem: Brochure, newItem: Brochure): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Brochure, newItem: Brochure): Boolean {
            return oldItem == newItem
        }
    }
}