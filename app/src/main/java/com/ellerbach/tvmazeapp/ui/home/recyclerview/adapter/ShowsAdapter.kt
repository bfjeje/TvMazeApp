package com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.model.Show

class ShowsAdapter : PagingDataAdapter<Show, ShowsAdapter.ViewHolder>(ShowDiffCallBack()) {

    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    private lateinit var listener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val createdView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.show_item, parent, false)
        return ViewHolder(createdView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var show: Show
        private var tvName: TextView = itemView.findViewById(R.id.tv_show_name)
        private var ivBackground: ImageView = itemView.findViewById(R.id.iv_show_background)
        private var ivMain: ImageView = itemView.findViewById(R.id.iv_show_main)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position)
                }
            }
        }

        fun bind(show: Show?) {
            show?.let { showData ->
                this.show = showData
                tvName.text = showData.name
                itemView.let { item ->
                    showData.image.medium.let {
                        Glide.with(item)
                            .load(showData.image.medium)
                            .into(ivMain)
                        //                    Uses Cache, so no bandwidth consumed
                        Glide.with(item)
                            .load(showData.image.medium)
                            .into(ivBackground)
                    }
                }
            }
        }
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