package com.ellerbach.tvmazeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.databinding.SearchShowFragmentBinding
import com.ellerbach.tvmazeapp.model.ShowDatabase
import com.ellerbach.tvmazeapp.model.getDatabase
import com.ellerbach.tvmazeapp.network.getNetworkService
import com.ellerbach.tvmazeapp.ui.mainactivity.MainActivityViewModel
import com.ellerbach.tvmazeapp.ui.searchfragment.OnSearchViewItemClickListener
import com.ellerbach.tvmazeapp.ui.searchfragment.SearchShowViewModel
import com.ellerbach.tvmazeapp.ui.searchfragment.adapter.SearchSpecificShowAdapter
import kotlinx.coroutines.launch

class SearchShowFragment : Fragment() {

    private var _binding: SearchShowFragmentBinding? = null

    private val binding get() = _binding!!

    private var specificShowAdapter: SearchSpecificShowAdapter? = null
    private lateinit var searchViewModel: SearchShowViewModel
    private lateinit var repository: ShowsRepository
    private lateinit var database: ShowDatabase
    private lateinit var recyclerView: RecyclerView

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SearchShowFragmentBinding.inflate(inflater, container, false)
        database = getDatabase(requireContext())
        repository = ShowsRepository(getNetworkService(), database.showDAO)

        searchViewModel =
            ViewModelProvider(
                this,
                SearchShowViewModel.FACTORY(repository)
            )[SearchShowViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeSearchView()
    }

    private fun initView() {
        specificShowAdapter =
            SearchSpecificShowAdapter(repository, object : OnSearchViewItemClickListener {
                override fun onShowClickListener() {
                    mainActivityViewModel.query.value = ""
                    mainActivityViewModel.clearSearchView(true)
                }
            })
        recyclerView = binding.rvShows.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recyclerView.adapter = specificShowAdapter
    }

    private fun observeSearchView() {
        mainActivityViewModel.query.observe(viewLifecycleOwner) {
            if (hasSomethingToSearch(it)) {
                getQueryResults(it)
            }
        }
    }

    private fun getQueryResults(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.updateShows(query)
        }
        searchViewModel.listGroup.observe(viewLifecycleOwner) {
            specificShowAdapter?.updateShows(it)
        }
    }

    private fun hasSomethingToSearch(it: String?) = !it.isNullOrBlank()

    override fun onDestroyView() {
        super.onDestroyView()
        specificShowAdapter = null
        _binding = null
    }
}