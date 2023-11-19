package com.yourbynn.app_githubusers.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import retrofit2.Call
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yourbynn.app_githubusers.config.ApiConfig
import com.yourbynn.app_githubusers.data.local.FavoriteDao
import com.yourbynn.app_githubusers.data.local.FavoriteEntity
import com.yourbynn.app_githubusers.data.local.UserDb
import com.yourbynn.app_githubusers.data.remote.ResponseUserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Callback

class DetailViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        private const val TAG = "ModelDetailView"
    }

    private val _detailUser = MutableLiveData<ResponseUserDetails>()
    val detailUser: LiveData<ResponseUserDetails> = _detailUser

    private val _isLoadingDetail = MutableLiveData<Boolean>()
    val isLoadingDetail: LiveData<Boolean> = _isLoadingDetail

    private var dao: FavoriteDao?
    private var database: UserDb?

    init {
        database = UserDb.getInstance(application)
        dao = database?.favoriteDao()
    }

    fun findUser(input: String) {
        _isLoadingDetail.value = true
        val client = ApiConfig.getApiService().getUserDetails(input)
        client.enqueue(object : Callback<ResponseUserDetails> {
            override fun onResponse(
                call: Call<ResponseUserDetails>,
                response: Response<ResponseUserDetails>
            ) {
                _isLoadingDetail.value = false
                if(response.isSuccessful && response.body() != null) {
                    _detailUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResponseUserDetails>, t: Throwable) {
                _isLoadingDetail.value = false
                t.message?.let { Log.d("gagal", it) }
            }

        })
    }

    fun addFavorite(avatarUrl: String, htmlUrl: String, id: Int, username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteEntity(
                id,
                avatarUrl,
                htmlUrl,
                username
            )
            dao?.addFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = dao?.checkUser(id)

    fun deleteFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dao?.deleteFavorite(id)
        }
    }
}