package com.ellerbach.tvmazeapp.ui.showfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.model.*

class SeasonsAdapter(
    val context: Context,
    listEpisodes: List<Episode?>
) :
    BaseExpandableListAdapter() {

    var listOfSeasons: ArrayList<Long> = arrayListOf()
    var listOfEpisodesBySeasons: HashMap<Long, ArrayList<Episode>> = HashMap()

    val emptyEpisode: Episode = Episode(
        0,
        "",
        "",
        0,
        0,
        "",
        "",
        "",
        "",
        0,
        Rating(0.0),
        Image("", ""),
        "",
        Links(
            Self("")
        )
    )

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
        return listOfEpisodesBySeasons.get(listOfSeasons[season])?.size ?: 0
    }

    override fun getGroup(season: Int): Any {
        return listOfSeasons[season]
    }

    override fun getChild(season: Int, episodeOfSeason: Int): Any {
        return listOfEpisodesBySeasons.get(listOfSeasons[season])?.get(episodeOfSeason)
            ?: emptyEpisode
    }

    override fun getGroupId(season: Int): Long {
        return listOfSeasons.elementAt(season)
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
        val group: String = getGroup(season).toString()
        val convert: View = convertView
            ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.list_seasons,
                parent, false
            )
        val textView: TextView? = convert.findViewById(R.id.list_parent)
        textView?.text = group
        return convert
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val child: Episode = getChild(groupPosition, childPosition) as Episode
        val convert: View = convertView
            ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.list_episodes,
                parent
            )
        val textView: TextView? = convert.findViewById(R.id.list_child)
        textView?.text = "${child.number} - ${child.name}"
        return convert
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

}