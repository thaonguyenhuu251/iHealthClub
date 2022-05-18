package com.example.facebookclone.database

import androidx.annotation.WorkerThread
import com.example.facebookclone.model.OptionsHome

class OptionsRepository(private val optionsDao: OptionsDao) {
    val allOptions: List<OptionsHome> = optionsDao.getOptions()

    @WorkerThread
    suspend fun insert(options: OptionsHome){
        optionsDao.insert(options)
    }
}