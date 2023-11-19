package com.yourbynn.app_githubusers.model

import retrofit2.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import retrofit2.Response
import com.yourbynn.app_githubusers.config.ApiConfig
import com.yourbynn.app_githubusers.data.remote.ResponseGithubDetails
import com.yourbynn.app_githubusers.data.remote.User
import com.yourbynn.app_githubusers.pref.SettingsTheme
import retrofit2.Callback

class MainViewModel(private val pref : SettingsTheme) : ViewModel() {

    companion object {
        private const val TAG = "ModelMainView"
        private const val NAMA_PENGGUNA = "Arbian"
    }

    private val _listUser = MutableLiveData<List<User>>()
    val listUser: LiveData<List<User>> = _listUser

    val searchQuery = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findUsers(NAMA_PENGGUNA)
    }

    fun findUsers(input: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(input)
        client.enqueue(object : Callback<ResponseGithubDetails> {
            override fun onResponse(
                call: Call<ResponseGithubDetails>,
                response: Response<ResponseGithubDetails>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listUser.value = response.body()?.users as List<User>?
                }
            }

            override fun onFailure(panggilan: Call<ResponseGithubDetails>, t: Throwable) {
                _isLoading.value = false
                t.message?.let { Log.d("gagal", it) }
            }
        })
    }

    fun getDarkMode(): LiveData<Boolean> {
        return pref.getTheme().asLiveData()
    }
}
