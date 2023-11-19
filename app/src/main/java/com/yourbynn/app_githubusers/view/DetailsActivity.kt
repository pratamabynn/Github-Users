package com.yourbynn.app_githubusers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yourbynn.app_githubusers.R
import com.yourbynn.app_githubusers.adapter.SectionPagerAdapter
import com.yourbynn.app_githubusers.data.remote.ResponseUserDetails
import com.yourbynn.app_githubusers.databinding.ActivityDetailsBinding
import com.yourbynn.app_githubusers.model.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {

        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_AVATAR = "EXTRA_AVATAR"
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_HTML  = "EXTRA_HTML"

        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data dari intent
        val username = intent.getStringExtra(EXTRA_USERNAME) ?: ""
        val avatar = intent.getStringExtra(EXTRA_AVATAR).toString()
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val htmlUrl = intent.getStringExtra(EXTRA_HTML).toString()

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        bundle.putInt(EXTRA_ID, id)

        detailViewModel.findUser(username)
        detailViewModel.detailUser.observe(this) { item ->
            setUserData(item)
        }

        detailViewModel.isLoadingDetail.observe(this) {
            showLoading(it)
        }

        var _isCheck = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count!= null) {
                    if (count>0) {
                        binding.toggleFavorite.isChecked = true
                        _isCheck = true
                    } else {
                        binding.toggleFavorite.isChecked = false
                        _isCheck = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener{
            _isCheck = !_isCheck
            if (_isCheck) {
                detailViewModel.addFavorite(avatar, htmlUrl, id, username)
            } else {
                detailViewModel.deleteFavorite(id)
            }
            binding.toggleFavorite.isChecked = _isCheck
        }

        // Mengatur judul dan tombol kembali
        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Mengirimkan username ke adapter
        val sectionsPagerAdapter = SectionPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
        addFavorite()
    }

    private fun addFavorite() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            binding.progressBarDetail.visibility = View.INVISIBLE
        }
    }

    private fun setUserData(userItem: ResponseUserDetails) {
        binding.apply {
            Glide.with(applicationContext)
                .load(userItem.avatarUrl)
                .error(R.drawable.ic_baseline_person_24)
                .into(imgProfile)
            tvFullName.text = userItem.name
            tvFullName.text = userItem.login
            tvFollowersNumber.text = userItem.followers.toString()
            tvFollowingNumber.text = userItem.following.toString()
        }
    }

}
