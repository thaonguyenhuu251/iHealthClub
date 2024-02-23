package com.htnguyen.ihealthclub.model

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "comment_table")
data class CommentModel(
    var idComment: String? = "",
    var idUser: String = "",
    var timeAction: Long? = 0,
    var typeAction: TypeAction = TypeAction.NO,
    var contentAction: String = "",
    var feedbackTo: String = "",
    var userAction: MutableList<UserAction> = mutableListOf(),
    var listComment: MutableList<CommentModel> = mutableListOf()
): Serializable {

}
