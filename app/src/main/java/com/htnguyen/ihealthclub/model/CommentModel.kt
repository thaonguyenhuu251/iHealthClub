package com.htnguyen.ihealthclub.model

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "comment_table")
data class CommentModel(
    var idComment: Long = 0L,
    var idPost: String ="",
    var idUser: String = "",
    var urlAvatar: String="",
    var createAt: Long = 0L,
    var userAction: MutableList<UserAction> = mutableListOf(),
    var contentComment: String=""
): Serializable {

}
