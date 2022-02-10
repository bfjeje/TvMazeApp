package com.ellerbach.tvmazeapp.ui.showfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.model.Episode

class SeasonsAdapter internal constructor(
    private val context: Context,
    listEpisodes: List<Episode?>,
    private val listener: SeasonInterface? = null
) :
    BaseExpandableListAdapter() {

    private var listOfSeasons: ArrayList<Long> = arrayListOf()
    private var listOfEpisodesBySeasons: HashMap<Long, ArrayList<Episode>> = HashMap()

    init {
        populateVariables(listEpisodes)
    }

    private fun populateVariables(listEpisodes: List<Episode?>) {
        for (episode: Episode? in listEpisodes) {
            episode?.let {
                if (isNewSeason(it)) {
                    listOfSeasons.add(it.season)
                }
                listOfEpisodesBySeasons.let { listOfEpisodes ->
                    if (seasonExistsInEpisode(listOfEpisodes, it)) {
                        listOfEpisodes[it.season]?.add(it)
                    } else {
                        listOfEpisodes.put(it.season, arrayListOf(it))
                    }
                }
            }
        }
    }

    private fun seasonExistsInEpisode(
        listOfEpisodes: HashMap<Long, ArrayList<Episode>>,
        it: Episode
    ) = listOfEpisodes.containsKey(it.season)

    private fun isNewSeason(it: Episode) =
        !listOfSeasons.contains(it.season)

    override fun getGroupCount(): Int {
        return listOfSeasons.size
    }

    override fun getChildrenCount(season: Int): Int {
        return this.listOfEpisodesBySeasons[listOfSeasons[season]]?.size ?: 0
    }

    override fun getGroup(season: Int): Any {
        return listOfSeasons[season]
    }

    override fun getChild(season: Int, episodeOfSeason: Int): Episode {
        return listOfEpisodesBySeasons[listOfSeasons[season]]!![episodeOfSeason]
    }

    override fun getGroupId(season: Int): Long {
        return season.toLong()
    }

    override fun getChildId(season: Int, episodeOfSeason: Int): Long {
        return episodeOfSeason.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        season: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        val seasonTitle: String = "" + getGroup(season)

        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_seasons, parent, false)
        }

        val seasonTv = view!!.findViewById<TextView>(R.id.list_parent)
        seasonTv.text = seasonTitle

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        val episode = getChild(groupPosition, childPosition)
        val episodeTitle = "${episode.number} - ${
            episode.name
        }"

        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_episodes, parent, false)
        }

        val episodeTv = view!!.findViewById<TextView>(R.id.list_child)
        episodeTv.text = episodeTitle

        listener?.let { clickListener ->
            view.setOnClickListener {
                clickListener.onEpisodeClick(getChild(groupPosition, childPosition))
            }
        }

        return view
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}

interface SeasonInterface {
    fun onEpisodeClick(episode: Episode)
}