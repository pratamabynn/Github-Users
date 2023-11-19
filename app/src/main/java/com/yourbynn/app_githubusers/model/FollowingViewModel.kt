package com.yourbynn.app_githubusers.model

import retrofit2.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yourbynn.app_githubusers.config.ApiConfig
import com.yourbynn.app_githubusers.data.remote.User
import retrofit2.Response
import retrofit2.Callback

class FollowingViewModel : ViewModel() {

    companion object {
        private const val TAG = "ModelFollowingView"
    }

    private val _followingUser = MutableLiveData<List<User>>()
    val followingUser: LiveData<List<User>> = _followingUser

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    fun getFollowingUser(input: String?) {
        _isLoadingFollowing.value = true

        val client = ApiConfig.getApiService().getUserFollowings(input)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoadingFollowing.value = false
                if(response.isSuccessful && response.body() != null) {
                    _followingUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoadingFollowing.value = false
                t.message?.let { Log.d("gagal", it) }
            }
        })
    }
}
