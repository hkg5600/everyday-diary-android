package com.example.everyday_diary.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.everyday_diary.room.model.Token
import com.example.travelercommunityapp.base.BaseDao
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TokenDao : BaseDao<Token> {

    @Query("Select * From TokenTable")
    fun getToken() : Single<Token>

    @Query("Delete From TokenTable")
    fun deleteToken() : Completable
}