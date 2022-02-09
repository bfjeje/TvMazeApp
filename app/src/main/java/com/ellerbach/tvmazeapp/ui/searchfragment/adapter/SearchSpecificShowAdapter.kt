package com.ellerbach.tvmazeapp.ui.searchfragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.model.SearchSpecificShow
import com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter.ShowViewHolder
import com.ellerbach.tvmazeapp.ui.searchfragment.OnSearchViewItemClickListener

class SearchSpecificShowAdapter(
    val repository: ShowsRepository,
    val onClickListener: OnSearchViewItemClickListener
) :
    RecyclerView.Adapter<ShowViewHolder>() {

    private var specificShows: ArrayList<SearchSpecificShow?> = arrayListOf()

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val createdView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.show_item, parent, false)
        return ShowViewHolder(createdView)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val specificShow: SearchSpecificShow? = specificShows[position]
        holder.bindSearchScreenShows(specificShow, repository, onClickListener)
    }

    override fun getItemCount(): Int {
        return specificShows.size
    }

    fun updateShows(listShows: List<SearchSpecificShow?>?) {
        notifyItemRangeRemoved(0, this.specificShows.size)
        this.specificShows.clear()
        this.specificShows.addAll(ArrayList(listShows))
        this.notifyItemRangeInserted(0, this.specificShows.size)
    }
}