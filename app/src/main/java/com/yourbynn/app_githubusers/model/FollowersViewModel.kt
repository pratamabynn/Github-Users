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

class FollowersViewModel : ViewModel() {

    companion object {
        private const val TAG = "ModelFollowersView"
    }

    private val _followersUser = MutableLiveData<List<User>>()
    val followersUser: LiveData<List<User>> = _followersUser

    private val _isLoadingFollower = MutableLiveData<Boolean>()
    val isLoadingFollower: LiveData<Boolean> = _isLoadingFollower

    fun getFollowersUser(input: String?) {
        _isLoadingFollower.value = true

        val client = ApiConfig.getApiService().getUserFollowers(input)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(panggilan: Call<List<User>>, respons: Response<List<User>>) {
                _isLoadingFollower.value = false
                if(respons.isSuccessful && respons.body() != null) {
                    _followersUser.value = respons.body()
                }
            }

            override fun onFailure(panggilan: Call<List<User>>, t: Throwable) {
                _isLoadingFollower.value = false
                t.message?.let { Log.d("gagal", it) }
            }
        })
    }
}
