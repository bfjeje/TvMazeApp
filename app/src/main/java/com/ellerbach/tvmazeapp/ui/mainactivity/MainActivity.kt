package com.ellerbach.tvmazeapp.ui.mainactivity

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ellerbach.tvmazeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configSearchView()
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    private fun observeViewModel() {
        mainViewModel.clearSearchView.observe(this) {
            if (it) {
                binding.searchView.setQuery("", false)
                binding.searchView.isIconified = true
                mainViewModel.clearSearchView(false)
            }
        }
    }

    private fun configSearchView() {
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    mainViewModel.query.value = newText
                    return true
                }
            })
    }
}