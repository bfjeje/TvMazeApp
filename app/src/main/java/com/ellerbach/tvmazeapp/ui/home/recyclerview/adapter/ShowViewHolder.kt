package com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.model.SearchSpecificShow
import com.ellerbach.tvmazeapp.model.Show
import com.ellerbach.tvmazeapp.ui.searchfragment.OnSearchViewItemClickListener

class ShowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var tvName: TextView = itemView.findViewById(R.id.tv_show_name)
    private var ivBackground: ImageView = itemView.findViewById(R.id.iv_show_background)
    private var ivMain: ImageView = itemView.findViewById(R.id.iv_show_main)

    fun bind(show: Show?, repository: ShowsRepository) {
        show?.let { showData ->
            tvName.text = showData.name
            showData.image?.medium?.let { medium ->
                itemView.let { item ->
                    Glide.with(item)
                        .load(medium)
                        .into(ivMain)
//                    Uses Cache, so no bandwidth consumed
                    Glide.with(item)
                        .load(medium)
                        .into(ivBackground)
                }
            }
            itemView.setOnClickListener {
                val bundle = bundleOf(Pair("show", showData), Pair("repository", repository))
                itemView.findNavController()
                    .navigate(R.id.action_navigation_shows_to_showFragment, bundle)
            }
        }
    }

    fun bind(
        showData: SearchSpecificShow?,
        repository: ShowsRepository,
        onClickListener: OnSearchViewItemClickListener
    ) {
        showData?.let {
            tvName.text = showData.show.name
            itemView.let { item ->
                showData.show.image?.medium.let { medium ->
                    Glide.with(item)
                        .load(medium)
                        .into(ivMain)
//                    Uses Cache, so no bandwidth consumed
                    Glide.with(item)
                        .load(medium)
                        .into(ivBackground)
                }
            }
            itemView.setOnClickListener {
                val bundle = bundleOf(Pair("show", showData.show), Pair("repository", repository))
                onClickListener.onShowClickListener()
                itemView.findNavController()
                    .navigate(R.id.action_searchShowFragment_to_showFragment, bundle)
            }
        }
    }
}