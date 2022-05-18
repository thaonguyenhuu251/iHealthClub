package com.example.facebookclone.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.facebookclone.model.OptionsHome
import com.example.facebookclone.model.UserSaved

interface OptionsDao {
    @Query("SELECT * FROM options_table")
    fun getOptions(): List<OptionsHome>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: OptionsHome)

    @Query("DELETE FROM options_table")
    suspend fun deleteAll()
}