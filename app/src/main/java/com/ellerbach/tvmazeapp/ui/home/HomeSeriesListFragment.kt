package com.ellerbach.tvmazeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.databinding.FragmentHomeSeriesListBinding
import com.ellerbach.tvmazeapp.model.ShowDatabase
import com.ellerbach.tvmazeapp.model.getDatabase
import com.ellerbach.tvmazeapp.network.getNetworkService
import com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter.ShowsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeSeriesListFragment : Fragment() {

    private var _binding: FragmentHomeSeriesListBinding? = null

    private val binding get() = _binding!!

    private var showAdapter: ShowsAdapter? = null
    private lateinit var homeViewModel: HomeSeriesListViewModel
    private lateinit var repository: ShowsRepository
    private lateinit var database: ShowDatabase
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeSeriesListBinding.inflate(inflater, container, false)
        database = getDatabase(requireContext())
        repository = ShowsRepository(getNetworkService(), database.showDAO)

        homeViewModel =
            ViewModelProviders
                .of(this, HomeSeriesListViewModel.FACTORY(repository))
                .get(HomeSeriesListViewModel::class.java)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.refreshShowList().collectLatest { shows ->
                showAdapter?.setOnItemClickListener(object : ShowsAdapter.OnItemClickListener {
                    override fun onItemClick(itemView: View?, position: Int) {
                        Toast.makeText(requireContext(), "$position clicked", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
                showAdapter?.submitData(shows)
            }
        }
    }

    private fun initView() {
        showAdapter = ShowsAdapter()
        recyclerView = binding.rvShows.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recyclerView.adapter = showAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showAdapter = null
        _binding = null
    }
}