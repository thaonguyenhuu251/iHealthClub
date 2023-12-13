package com.htnguyen.ihealthclub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.htnguyen.ihealthclub.model.UserSaved
@Dao
interface UserDao {
    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * FROM user_table")
    fun getUsers(): List<UserSaved>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: UserSaved)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}
