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
    listEpisodes: List<Episode?>
) :
    BaseExpandableListAdapter() {

    var listOfSeasons: ArrayList<Long> = arrayListOf()
    var listOfEpisodesBySeasons: HashMap<Long, ArrayList<Episode>> = HashMap()

    init {
        for (episode: Episode? in listEpisodes) {
            episode?.let {
                if (!listOfSeasons.contains(it.season)) {
                    listOfSeasons.add(it.season)
                }
                listOfEpisodesBySeasons.let { listOfEpisodes ->
                    if (listOfEpisodes.containsKey(it.season)) {
                        listOfEpisodes.get(it.season)?.add(it)
                    } else {
                        listOfEpisodes.put(it.season, arrayListOf(it))
                    }
                }
            }
        }
    }

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
            view = inflater.inflate(R.layout.list_seasons, null)
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
        val episodeTitle = "${getChild(groupPosition, childPosition).number} - ${
            getChild(
                groupPosition,
                childPosition
            ).name
        }"

        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_episodes, null)
        }

        val episodeTv = view!!.findViewById<TextView>(R.id.list_child)
        episodeTv.text = episodeTitle

        return view
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}