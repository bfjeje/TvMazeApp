package com.ellerbach.tvmazeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ellerbach.tvmazeapp.R
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.databinding.FragmentHomeSeriesListBinding
import com.ellerbach.tvmazeapp.model.ShowDatabase
import com.ellerbach.tvmazeapp.model.getDatabase
import com.ellerbach.tvmazeapp.network.getNetworkService
import com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter.AllShowsAdapter
import com.ellerbach.tvmazeapp.ui.mainactivity.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeSeriesListFragment : Fragment() {

    private var _binding: FragmentHomeSeriesListBinding? = null

    private val binding get() = _binding!!

    private var allShowAdapter: AllShowsAdapter? = null
    private lateinit var homeViewModel: HomeSeriesListViewModel
    private lateinit var repository: ShowsRepository
    private lateinit var database: ShowDatabase
    private lateinit var recyclerView: RecyclerView

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeSeriesListBinding.inflate(inflater, container, false)
        database = getDatabase(requireContext())
        repository = ShowsRepository(getNetworkService(), database.showDAO)

        homeViewModel =
            ViewModelProvider(
                this,
                HomeSeriesListViewModel.FACTORY(repository)
            )[HomeSeriesListViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
        mainActivityViewModel.query.observe(viewLifecycleOwner) {
            if (!it.isNullOrBlank()) {
                findNavController(this)
                    .navigate(R.id.action_navigation_list_shows_to_searchShowFragment)
            }
        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.refreshShowList().collectLatest { shows ->
                allShowAdapter?.submitData(shows)
            }
        }
    }

    private fun initView() {
        allShowAdapter = AllShowsAdapter(repository)
        recyclerView = binding.rvShows.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recyclerView.adapter = allShowAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        allShowAdapter = null
        _binding = null
    }
}