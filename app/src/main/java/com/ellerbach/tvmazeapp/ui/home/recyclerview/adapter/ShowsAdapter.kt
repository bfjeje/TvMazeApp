package com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.model.Show

class ShowsAdapter(private val context: Context?) :
    RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    private lateinit var listener: OnItemClickListener
    private var shows: ArrayList<Show?> = arrayListOf()

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val createdView: View =
            LayoutInflater.from(context).inflate(R.layout.show_item, parent, false)
        return ViewHolder(createdView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val show: Show? = shows[position]
        holder.bind(show)
    }

    override fun getItemCount(): Int {
        return shows.size
    }

    fun updateShows(listShows: List<Show?>?) {
        notifyItemRangeRemoved(0, this.shows.size)
        this.shows.clear()
        this.shows.addAll(ArrayList(listShows))
        this.notifyItemRangeInserted(0, this.shows.size)
    }


    inner class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

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
                context?.let { image ->
                    Glide.with(image)
                        .load(showData.image.medium)
                        .into(ivMain)
//                    Uses Cache, so no bandwidth consumed
                    Glide.with(image)
                        .load(showData.image.medium)
                        .into(ivBackground)
                }
            }
        }
    }
}