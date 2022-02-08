package com.ellerbach.tvmazeapp.ui.episode

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ellerbach.tvmazeapp.databinding.EpisodeFragmentBinding
import com.ellerbach.tvmazeapp.model.Episode

class EpisodeFragment : Fragment() {

    companion object {
        fun newInstance() = EpisodeFragment()
    }

    private var _binding: EpisodeFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EpisodeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = EpisodeFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.get("episode")?.let { episode ->
            viewModel.setEpisode(episode as Episode)
        }

        viewModel.episode.observe(viewLifecycleOwner) { episode ->
            if (episode != null) {
                episode.image?.let { image ->
                    Glide.with(view)
                        .load(image.original)
                        .into(binding.ivBackgroundEpisode)
                }
                binding.tvEpisodeName.text = "Episode ${episode.number}:\n${episode.name}"
                binding.tvSeasonNumber.text = "Season ${episode.season}"
                episode.summary.let { htmlSummary ->
                    val summaryString = Html.fromHtml(htmlSummary, Html.FROM_HTML_MODE_COMPACT)
                    binding.tvSummary.text = "Summary:\n${summaryString}"
                }
            }
        }
    }
}