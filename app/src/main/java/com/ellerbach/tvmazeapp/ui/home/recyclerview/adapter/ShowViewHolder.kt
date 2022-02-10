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

    fun bindMainScreenShows(showData: Show?, repository: ShowsRepository) {
        showData?.let { show ->
            tvName.text = show.name
            itemView.let { item ->
                setImages(show, item)
                item.setOnClickListener {
                    navigateToShowFragment(
                        show,
                        repository,
                        R.id.action_navigation_shows_to_showFragment
                    )
                }
            }
        }
    }

    fun bindSearchScreenShows(
        showData: SearchSpecificShow?,
        repository: ShowsRepository,
        onClickListener: OnSearchViewItemClickListener
    ) {
        showData?.show?.let { show ->
            tvName.text = show.name
            itemView.let { item ->
                setImages(show, item)
                item.setOnClickListener {
                    onClickListener.onShowClickListener()
                    navigateToShowFragment(
                        show,
                        repository,
                        R.id.action_searchShowFragment_to_showFragment
                    )
                }
            }
        }
    }

    private fun navigateToShowFragment(
        show: Show,
        repository: ShowsRepository,
        navigation: Int

    ) {
        val bundle = bundleOf(Pair("show", show), Pair("repository", repository))
        itemView.findNavController()
            .navigate(navigation, bundle)
    }

    private fun setImages(showData: Show, item: View) {
        showData.image?.medium?.let { medium ->
            setImageInImageView(item, medium, ivMain)
            //                    Uses Cache, so no bandwidth consumed
            setImageInImageView(item, medium, ivBackground)
        }
    }

    private fun setImageInImageView(item: View, medium: String, imageView: ImageView) {
        Glide.with(item)
            .load(medium)
            .into(imageView)
    }
}