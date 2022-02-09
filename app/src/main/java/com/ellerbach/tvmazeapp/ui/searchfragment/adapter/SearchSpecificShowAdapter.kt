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

    private var shows: ArrayList<SearchSpecificShow?> = arrayListOf()

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val createdView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.show_item, parent, false)
        return ShowViewHolder(createdView)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val show: SearchSpecificShow? = shows[position]
        holder.bind(show, repository, onClickListener)
    }

    override fun getItemCount(): Int {
        return shows.size
    }

    fun updateShows(listShows: List<SearchSpecificShow?>?) {
        notifyItemRangeRemoved(0, this.shows.size)
        this.shows.clear()
        this.shows.addAll(ArrayList(listShows))
        this.notifyItemRangeInserted(0, this.shows.size)
    }
}