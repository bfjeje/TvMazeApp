package com.ellerbach.tvmazeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.databinding.FragmentHomeSeriesListBinding
import com.ellerbach.tvmazeapp.model.SearchSpecificShow
import com.ellerbach.tvmazeapp.model.ShowDatabase
import com.ellerbach.tvmazeapp.model.getDatabase
import com.ellerbach.tvmazeapp.network.getNetworkService
import com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter.AllShowsAdapter
import com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter.SearchSpecificShowAdapter
import com.ellerbach.tvmazeapp.ui.mainactivity.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeSeriesListFragment : Fragment() {

    private var _binding: FragmentHomeSeriesListBinding? = null

    private val binding get() = _binding!!

    private var allShowAdapter: AllShowsAdapter? = null
    private var specificShowAdapter: SearchSpecificShowAdapter? = null
    private lateinit var homeViewModel: HomeSeriesListViewModel
    private lateinit var repository: ShowsRepository
    private lateinit var database: ShowDatabase
    private lateinit var recyclerView: RecyclerView


    private lateinit var mainActivityViewModel: MainActivityViewModel

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

        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        return binding.root
    }

    private fun getQueryResults(query: String) {
        binding.rvShows.swapAdapter(specificShowAdapter, false)
        viewLifecycleOwner.lifecycleScope.launch {
            val listOfShows: List<SearchSpecificShow?> = homeViewModel.searchSpecificShow(query)
            specificShowAdapter?.updateShows(listOfShows)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        lifecycle.addObserver(mainActivityViewModel)
        collectUiState()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.refreshShowList().collectLatest { shows ->
                allShowAdapter?.submitData(shows)
            }

            mainActivityViewModel.query.observe(viewLifecycleOwner) {
                if (!it.isNullOrBlank()) {
                    binding.rvShows.swapAdapter(specificShowAdapter, false)
                    getQueryResults(it)
                } else {
                    binding.rvShows.swapAdapter(allShowAdapter, false)
                }
            }
        }
    }

    private fun initView() {
        allShowAdapter = AllShowsAdapter(repository)
        specificShowAdapter = SearchSpecificShowAdapter()
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