package com.example.everyday_diary.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.everyday_diary.room.dao.TokenDao
import com.example.everyday_diary.room.model.Token

@Database(entities = [Token::class], version = 3, exportSchema = false)
abstract class TokenDatabase : RoomDatabase() {
    abstract fun tokenDao() : TokenDao

    companion object {
        private var INSTANCE : TokenDatabase? = null

        @Synchronized
        fun getInstance(context: Context): TokenDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    TokenDatabase::class.java, "token_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}