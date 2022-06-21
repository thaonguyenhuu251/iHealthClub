package com.example.facebookclone.model

import androidx.room.Entity
import java.io.Serializable
import java.util.*

@Entity(tableName = "story_table")
data class ObjectStory (
    var idStory: String = "",
    var idUser: String ="",
    var urlAvatar: String="",
    var createBy: String ="",
    var createAt: Long = 0L,
    var listFile : MutableList<FileStory> = mutableListOf()
): Serializable {

}