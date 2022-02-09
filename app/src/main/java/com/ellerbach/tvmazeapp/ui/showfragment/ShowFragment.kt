package com.ellerbach.tvmazeapp.ui.showfragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.databinding.ShowFragmentBinding
import com.ellerbach.tvmazeapp.model.Episode
import com.ellerbach.tvmazeapp.model.Show
import com.ellerbach.tvmazeapp.ui.mainactivity.MainActivityViewModel
import com.ellerbach.tvmazeapp.ui.showfragment.adapter.SeasonInterface
import com.ellerbach.tvmazeapp.ui.showfragment.adapter.SeasonsAdapter
import kotlinx.coroutines.launch

class ShowFragment : Fragment() {

    private lateinit var viewModel: ShowViewModel
    private val mainViewModel: MainActivityViewModel by activityViewModels()
    private var _binding: ShowFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var seasonsAdapter: SeasonsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        seasonsAdapter =
            SeasonsAdapter(requireContext(), listOf<Episode>(), object : SeasonInterface {
                override fun onEpisodeClick(episode: Episode) {
                }
            })
        _binding = ShowFragmentBinding.inflate(inflater, container, false)
        arguments?.get("repository")?.let { repo ->
            viewModel =
                ViewModelProvider(
                    this,
                    ShowViewModel.FACTORY(repo as ShowsRepository)
                )[ShowViewModel::class.java]
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.get("show")?.let { viewModel.setShow(it as Show) }

        viewModel.showData.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getEpisodes(it?.id.toString())
            }
        }

        viewModel.listGroup.observe(viewLifecycleOwner) {
            seasonsAdapter = SeasonsAdapter(
                requireContext(),
                it,
                object : SeasonInterface {
                    override fun onEpisodeClick(episode: Episode) {
                        val bundle = bundleOf(Pair("episode", episode))
                        this@ShowFragment.findNavController()
                            .navigate(R.id.action_showFragment_to_episodeFragment, bundle)
                    }
                }
            )
            bindData()
        }

        viewModel.showData.observe(viewLifecycleOwner) {
            it?.let { show ->
                show.image?.medium?.let { image ->
                    Glide.with(this).load(image)
                        .into(binding.ivBackgroundShow)
                }
                binding.tvShowName.text = show.name
                if (show.summary.isNullOrEmpty()) {
                    binding.tvSummary.visibility = View.GONE
                } else {
                    show.summary.let { html ->
                        val summaryString = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
                        binding.tvSummary.append(summaryString)
                    }
                }

                if (show.genres.isNullOrEmpty() && show.schedule.days.isNullOrEmpty() && show.schedule.time.isNullOrEmpty()) {
                    binding.llOtherInfo.visibility = View.GONE
                } else {
                    if (show.genres.isNullOrEmpty()) {
                        binding.tvGenres.visibility = View.GONE
                    } else {
                        show.genres.let { genresList ->
                            binding.tvGenres.text = getString(R.string.genres_twodots)
                            for (genre: String in genresList) {
                                if (genre == genresList.last()) {
                                    binding.tvGenres.append(genre)
                                } else {
                                    binding.tvGenres.append(" $genre ,")
                                }
                            }
                        }
                    }

                    if (show.schedule.days.isNullOrEmpty() && show.schedule.time.isNullOrEmpty()) {
                        binding.tvDays.visibility = View.GONE
                    } else {
                        binding.tvDays.text = getString(R.string.watch_space)
                        if (show.schedule.days.isNotEmpty()) {
                            binding.tvDays.append(" every ")
                            for (day: String? in show.schedule.days) {
                                if (day == show.schedule.days.last()) {
                                    binding.tvDays.append("$day ")
                                } else {
                                    binding.tvDays.append("$day, ")
                                }
                            }
                        }
                        show.schedule.time?.let { time ->
                            if (time.isNotBlank()) {
                                binding.tvDays.append("at $time")
                            }
                        }
                    }
                }
            }
        }

        mainViewModel.query.observe(viewLifecycleOwner) {
            if (!it.isNullOrBlank()) {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_showFragment_to_searchShowFragment)
            }
        }
    }

    private fun bindData() {
        binding.expandableListview.setAdapter(seasonsAdapter)
        binding.expandableListview.setOnGroupClickListener { parent, v, groupPosition, id ->
            setListViewHeight(parent, groupPosition)
            false
        }
    }

    private fun setListViewHeight(
        listView: ExpandableListView?,
        group: Int
    ) {
        val listAdapter = listView?.expandableListAdapter as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(
            listView.width,
            View.MeasureSpec.EXACTLY
        )
        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += groupItem.measuredHeight
            if (listView.isGroupExpanded(i) && i != group
                || !listView.isGroupExpanded(i) && i == group
            ) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem = listAdapter.getChildView(
                        i, j, false, null,
                        listView
                    )
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }
        val params = listView.layoutParams
        var height = (totalHeight
                + listView.dividerHeight * (listAdapter.groupCount - 1))
        if (height < 10) height = 200
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }
}