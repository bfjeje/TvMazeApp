package com.ellerbach.tvmazeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ellerbach.tvmazeapp.databinding.ShowFragmentBinding
import com.ellerbach.tvmazeapp.model.Show

class ShowFragment : Fragment() {

    companion object {
        fun newInstance() = ShowFragment()
    }

    private lateinit var viewModel: ShowViewModel
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
        viewModel = ViewModelProvider(this).get(ShowViewModel::class.java)
        viewModel.setShow(arguments?.get("show") as Show)

        // TODO: Use the ViewModel
    }

}