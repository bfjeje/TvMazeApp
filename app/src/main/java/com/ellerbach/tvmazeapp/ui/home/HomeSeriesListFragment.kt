package com.ellerbach.tvmazeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.databinding.FragmentHomeSeriesListBinding
import com.ellerbach.tvmazeapp.model.ShowDatabase
import com.ellerbach.tvmazeapp.model.getDatabase
import com.ellerbach.tvmazeapp.network.getNetworkService
import com.ellerbach.tvmazeapp.ui.home.recyclerview.adapter.ShowsAdapter

class HomeSeriesListFragment : Fragment() {

    private var _binding: FragmentHomeSeriesListBinding? = null

    private val binding get() = _binding!!

    private lateinit var showAdapter: ShowsAdapter
    private lateinit var homeViewModel: HomeSeriesListViewModel
    private lateinit var repository: ShowsRepository
    private lateinit var database: ShowDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        database = getDatabase(requireContext())
        repository = ShowsRepository(getNetworkService(), database.showDAO)
        showAdapter = ShowsAdapter(context)

        homeViewModel =
            ViewModelProviders
                .of(this, HomeSeriesListViewModel.FACTORY(repository))
                .get(HomeSeriesListViewModel::class.java)

        _binding = FragmentHomeSeriesListBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val recyclerView: RecyclerView = binding.rvShows
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        homeViewModel.listOfShows.observe(viewLifecycleOwner) {
            showAdapter.setOnItemClickListener(object : ShowsAdapter.OnItemClickListener {
                override fun onItemClick(itemView: View?, position: Int) {
                    Toast.makeText(requireContext(), "$position clicked", Toast.LENGTH_SHORT).show()
                }
            })
            recyclerView.adapter = showAdapter
        }

        homeViewModel.refreshShowList()

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.listOfShows.observe(viewLifecycleOwner) { listShows ->
            showAdapter.updateShows(listShows)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}