package com.yourbynn.app_githubusers.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call.Details
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourbynn.app_githubusers.R
import com.yourbynn.app_githubusers.adapter.ListAdapter
import com.yourbynn.app_githubusers.data.local.FavoriteEntity
import com.yourbynn.app_githubusers.data.remote.User
import com.yourbynn.app_githubusers.databinding.ActivityFavoriteBinding
import com.yourbynn.app_githubusers.model.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var listAdapter: ListAdapter
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listAdapter = ListAdapter()
        listAdapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@FavoriteActivity, DetailsActivity::class.java).also {
                    it.putExtra(DetailsActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailsActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailsActivity.EXTRA_HTML, data.htmlUrl)
                    startActivity(it)
                }
            }
        })

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            hasFixedSize()
            adapter = listAdapter
        }

        favoriteViewModel.getFavoriteUser()?.observe(this@FavoriteActivity) {
            if (it!=null) {
                val list = newList(it)
                listAdapter.updateList(list)
            }
        }
    }

    private fun newList(it: List<FavoriteEntity>): List<User> {
        val listUser = ArrayList<User>()
        for (user in it) {
            val userMapped = User (
                user.id,
                user.avatarUrl,
                user.htmlUrl,
                user.login
            )
            listUser.add(userMapped)
        }
        return listUser
    }
}