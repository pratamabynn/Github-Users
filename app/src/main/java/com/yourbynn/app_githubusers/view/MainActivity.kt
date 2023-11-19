package com.yourbynn.app_githubusers.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourbynn.app_githubusers.R
import com.yourbynn.app_githubusers.adapter.ListAdapter
import com.yourbynn.app_githubusers.data.local.FavoriteEntity
import com.yourbynn.app_githubusers.data.remote.User
import com.yourbynn.app_githubusers.databinding.ActivityMainBinding
import com.yourbynn.app_githubusers.model.MainViewModel
import com.yourbynn.app_githubusers.pref.SettingViewModelFactory
import com.yourbynn.app_githubusers.pref.SettingsTheme
import com.yourbynn.app_githubusers.pref.SettingsViewModel
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var listAdapter: ListAdapter
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "setting_theme")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingsTheme.getInstance(this.dataStore)
        mainViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[MainViewModel::class.java]

        listAdapter = ListAdapter()
        listAdapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailsActivity::class.java).also {
                    it.putExtra(DetailsActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailsActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailsActivity.EXTRA_HTML, data.htmlUrl)
                    it.putExtra(DetailsActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            hasFixedSize()
            adapter = listAdapter
        }

        mainViewModel.isLoading.observe(this) {
            toggleLoadingView(it)
        }

        mainViewModel.listUser.observe(this) {
            if (it != null) {
                listAdapter.updateList(it)
                toggleLoadingView(false)
            }
        }

        mainViewModel.getDarkMode().observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(androidx.appcompat.R.string.abc_search_hint)

        mainViewModel.searchQuery.observe(this) { query ->
            searchView.setQuery(query.toString(), false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mainViewModel.findUsers(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                mainViewModel.searchQuery.postValue(query)
                return false
            }
        })


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite -> {
                val action = Intent(this, FavoriteActivity::class.java)
                startActivity(action)
            }
            R.id.setting -> {
                val action = Intent(this, SettingActivity::class.java)
                startActivity(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun toggleLoadingView(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}
