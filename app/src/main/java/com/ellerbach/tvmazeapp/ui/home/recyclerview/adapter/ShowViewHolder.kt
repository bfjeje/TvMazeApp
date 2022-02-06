package com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.model.SearchSpecificShow
import com.ellerbach.tvmazeapp.model.Show

class ShowViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {

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
            showData.image.medium.let {
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

    fun bind(showData: SearchSpecificShow?) {
        showData?.let { showDataNotEmpty ->
            this.show = showDataNotEmpty.show
            tvName.text = this.show.name
            itemView.let { item ->
                this.show.image.medium.let {
                    Glide.with(item)
                        .load(this.show.image.medium)
                        .into(ivMain)
                    //                    Uses Cache, so no bandwidth consumed
                    Glide.with(item)
                        .load(this.show.image.medium)
                        .into(ivBackground)
                }
            }
        }
    }
}