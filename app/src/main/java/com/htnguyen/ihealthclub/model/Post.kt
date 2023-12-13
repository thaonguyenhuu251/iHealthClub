package com.htnguyen.ihealthclub.model

import com.google.firebase.database.Exclude

data class Post(
    var idPost: Long = 0L,
    var idUser: String = "",
    var urlAvatar: String = "",
    var status: String = "",
    var emojiStatus: String = "",
    var listFile: MutableList<String> = mutableListOf(),
    var listLike: MutableList<ListLike> = mutableListOf(),
    var typeFile: TypeFile = TypeFile.IMAGE,
    var likeTotal: Int = 0,
    var commentTotal: Int = 0,
    var shareTotal: Int = 0,
    var createAt: Long = 0L,
    var createBy: String = ""
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idPost" to idPost,
            "idUser" to idUser,
            "urlAvatar" to urlAvatar,
            "status" to status,
            "emojiStatus" to emojiStatus,
            "listFile" to listFile,

            "typeFile" to typeFile,
            "likeTotal" to likeTotal,
            "commentTotal" to commentTotal,
            "shareTotal" to shareTotal,
            "createAt" to createAt,
            "createBy" to createBy
        )
    }
}
