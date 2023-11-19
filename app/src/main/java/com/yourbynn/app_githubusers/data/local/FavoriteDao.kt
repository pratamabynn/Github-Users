package com.yourbynn.app_githubusers.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addFavorite(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite_entity")
    fun getFavoriteUser(): LiveData<List<FavoriteEntity>>

    @Query("SELECT count(*) FROM favorite_entity WHERE favorite_entity.id = :id")
    suspend fun checkUser(id: Int) : Int

    @Query("DELETE FROM favorite_entity WHERE favorite_entity.id = :id")
    suspend fun deleteFavorite(id: Int) : Int
}