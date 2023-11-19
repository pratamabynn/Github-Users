package com.yourbynn.app_githubusers.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yourbynn.app_githubusers.data.local.FavoriteDao
import com.yourbynn.app_githubusers.data.local.FavoriteEntity
import com.yourbynn.app_githubusers.data.local.UserDb

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var dao: FavoriteDao?
    private var database: UserDb?

    init {
        database = UserDb.getInstance(application)
        dao = database?.favoriteDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteEntity>>? {
        return dao?.getFavoriteUser()
    }
}