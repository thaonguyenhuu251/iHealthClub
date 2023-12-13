package com.htnguyen.ihealthclub.database

import androidx.annotation.WorkerThread
import com.htnguyen.ihealthclub.model.OptionsHome

class OptionsRepository(private val optionsDao: OptionsDao) {
    val allOptions: List<OptionsHome> = optionsDao.getOptions()

    @WorkerThread
    suspend fun insert(options: OptionsHome){
        optionsDao.insert(options)
    }
}