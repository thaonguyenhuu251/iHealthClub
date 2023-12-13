package com.htnguyen.ihealthclub.database

import androidx.annotation.WorkerThread
import com.htnguyen.ihealthclub.model.UserSaved

class UserRepository(private val userDao: UserDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allUsers: List<UserSaved> = userDao.getUsers()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    suspend fun insert(user: UserSaved) {
        userDao.insert(user)
    }
}