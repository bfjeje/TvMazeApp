package com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.model.Show

class AllShowsAdapter : PagingDataAdapter<Show, ShowViewHolder>(ShowDiffCallBack()) {

    private lateinit var listener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val createdView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.show_item, parent, false)
        return ShowViewHolder(createdView, listener)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ShowDiffCallBack : DiffUtil.ItemCallback<Show>() {
        override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem == newItem
        }
    }
}