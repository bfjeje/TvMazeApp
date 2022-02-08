package com.ellerbach.tvmazeapp.ui.showfragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.databinding.ShowFragmentBinding
import com.ellerbach.tvmazeapp.model.Show
import com.ellerbach.tvmazeapp.ui.showfragment.adapter.SeasonsAdapter
import kotlinx.coroutines.launch

class ShowFragment : Fragment() {

    companion object {
        fun newInstance() = ShowFragment()
    }

    private lateinit var viewModel: ShowViewModel
    private var _binding: ShowFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var seasonsAdapter: SeasonsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ShowFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.get("repository")?.let { repo ->
            viewModel =
                ViewModelProvider(this, ShowViewModel.FACTORY(repo as ShowsRepository))
                    .get(ShowViewModel::class.java)
        }
        arguments?.get("show")?.let { viewModel.setShow(it as Show) }

        viewModel.showData.observe(viewLifecycleOwner, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getEpisodes(it?.id.toString())
            }
        })

        viewModel.listGroup.observe(viewLifecycleOwner, Observer {
            seasonsAdapter = SeasonsAdapter(
                requireContext(),
                it
            )
            bindData()
        })

        viewModel.showData.observe(viewLifecycleOwner, Observer {
            it?.let { show ->
                show.image.medium?.let {
                    Glide.with(this).load(it)
                        .into(binding.ivBackgroundShow)
                }
                binding.tvShowName.text = show.name
                if (show.summary.isNullOrEmpty()) {
                    binding.llSummary.visibility = View.GONE
                } else {
                    show.summary.let { html ->
                        val summaryString = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
                        binding.tvSummary.text = "Summary: \n${summaryString}"
                    }
                }

                if (show.genres.isNullOrEmpty() && show.schedule.days.isNullOrEmpty() && show.schedule.time.isNullOrEmpty()) {
                    binding.llOtherInfo.visibility = View.GONE
                } else {
                    if (show.genres.isNullOrEmpty()) {
                        binding.tvGenres.visibility = View.GONE
                    } else {
                        show.genres.let { genresList ->
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
                        binding.tvDays.text = "Watch "
                        if (show.schedule.days.isNotEmpty()) {
                            binding.tvDays.append("every ")
                            for (day: String? in show.schedule.days) {
                                if (day == show.schedule.days.last()) {
                                    binding.tvDays.append("$day ")
                                } else {
                                    binding.tvDays.append("$day, ")
                                }
                            }
                        }
                        show.schedule.time?.let {
                            if (it.isNotBlank()) {
                                show.schedule.time.let { time ->
                                    binding.tvDays.append("at $time")
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    private fun bindData() {
        binding.expandableListview.setAdapter(seasonsAdapter)
    }

    private fun getQueryResults(query: String) {
        //binding.expandableListview.setAdapter(seasonsAdapter)
        //viewLifecycleOwner.lifecycleScope.launch {
        //  val listOfShows: List<SearchSpecificShow?> = homeViewModel.searchSpecificShow(query)
        //  specificShowAdapter?.updateShows(listOfShows)
        //}
    }

}