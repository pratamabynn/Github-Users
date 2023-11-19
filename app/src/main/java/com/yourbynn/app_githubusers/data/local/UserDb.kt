package com.yourbynn.app_githubusers.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class],
    version = 1
)
abstract class UserDb: RoomDatabase() {
    companion object {
        private var INSTANCE: UserDb? = null

        fun getInstance(context: Context): UserDb? {
            if (INSTANCE==null){
                synchronized(UserDb::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDb::class.java,
                        "user.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun favoriteDao(): FavoriteDao
}