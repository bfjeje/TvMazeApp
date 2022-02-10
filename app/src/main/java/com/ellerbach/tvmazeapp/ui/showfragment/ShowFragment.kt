package com.ellerbach.tvmazeapp.ui.showfragment

import android.os.Bundle
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

        seasonsAdapter = SeasonsAdapter(requireContext(), listOf<Episode>())
        _binding = ShowFragmentBinding.inflate(inflater, container, false)
        arguments?.get("repository")?.let { repo ->
            viewModel =
                getShowViewModel(repo)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.get("show")?.let { viewModel.setShow(it as Show) }

        setupObservers()
    }

    private fun setupObservers() {
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

        viewModel.showData.observe(viewLifecycleOwner) { show ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getEpisodes(show?.id.toString())
            }
            viewModel.showImage.observe(viewLifecycleOwner) { image ->
                Glide.with(this).load(image)
                    .into(binding.ivBackgroundShow)
            }
            viewModel.isSummaryPresent.observe(viewLifecycleOwner) { summaryPresent ->
                when (summaryPresent) {
                    true -> setSummary()
                    false -> binding.tvSummaryShow.visibility = View.GONE
                }
            }
            viewModel.name.observe(viewLifecycleOwner) {
                binding.tvShowName.text = it
            }
            viewModel.showTimeAndGenres.observe(viewLifecycleOwner) {
                when (it) {
                    true -> {
                        binding.llOtherInfo.visibility = View.VISIBLE
                        shouldShowGenres()
                        shouldShowSchedule()
                    }
                    false -> binding.llOtherInfo.visibility = View.GONE
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

    private fun shouldShowSchedule() {
        viewModel.isShowSchedule.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.llOtherInfo.visibility = View.VISIBLE
                    setSchedule()
                }
                else -> {
                    binding.llOtherInfo.visibility = View.GONE
                }
            }
        }
    }

    private fun setSchedule() {
        binding.tvDays.text = getString(R.string.watch_space)
        viewModel.days.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.tvDays.append(" every ")
                for (day: String? in it) {
                    if (day == it.last()) {
                        binding.tvDays.append("$day ")
                    } else {
                        binding.tvDays.append("$day, ")
                    }
                }
            }
        }
        viewModel.time.observe(viewLifecycleOwner) {
            if (!it.isNullOrBlank()) {
                binding.tvDays.append("at $it")
            }
        }
    }

    private fun shouldShowGenres() {
        viewModel.isShowGenres.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.tvGenres.visibility = View.VISIBLE
                    setGenres()
                }
                else -> {
                    binding.tvGenres.visibility = View.GONE
                }
            }
        }
    }

    private fun setGenres() {
        viewModel.genres.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.tvGenres.text = getString(R.string.genres_twodots)
                for (genre: String? in it) {
                    if (genre == it.last()) {
                        binding.tvGenres.append(" $genre")
                    } else {
                        binding.tvGenres.append(" $genre,")
                    }
                }
            }
        }
    }

    private fun setSummary() {
        binding.tvSummaryShow.apply {
            visibility = View.VISIBLE
            viewModel.showSummary.observe(viewLifecycleOwner) { summaryHtml ->
                this.text = summaryHtml
            }
        }
    }

    private fun getShowViewModel(repo: Any) = ViewModelProvider(
        this,
        ShowViewModel.FACTORY(repo as ShowsRepository)
    )[ShowViewModel::class.java]

    private fun bindData() {
        binding.expandableListview.setAdapter(seasonsAdapter)
        binding.expandableListview.setOnGroupClickListener { parent, _, groupPosition, _ ->
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