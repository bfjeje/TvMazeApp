package com.ellerbach.tvmazeapp

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ellerbach.tvmazeapp.databinding.ShowFragmentBinding
import com.ellerbach.tvmazeapp.model.Show

class ShowFragment : Fragment() {

    companion object {
        fun newInstance() = ShowFragment()
    }

    private val viewModel by viewModels<ShowViewModel>()
    private var _binding: ShowFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ShowFragmentBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.get("show")?.let { viewModel.setShow(it as Show) }

        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

}