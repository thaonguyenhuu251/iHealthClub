package com.htnguyen.ihealthclub.model

import com.google.firebase.database.Exclude

data class Post(
    var idPost: String = "",
    var idUser: String = "",
    var emojiStatus: String = "",
    var bodyStatus: String = "",
    var createAt: Long = 0L,
    var typePost: TypeFile = TypeFile.IMAGE,
    var likeTotal: Int = 0,
    var commentTotal: Int = 0,
    var shareTotal: Int = 0,
    var listFile: List<String> = listOf(),
    var listLike: List<UserAction> = listOf()
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idPost" to idPost,
            "idUser" to idUser,
            "bodyStatus" to bodyStatus,
            "emojiStatus" to emojiStatus,
            "listFile" to listFile,
            "typeFile" to typePost,
            "likeTotal" to likeTotal,
            "commentTotal" to commentTotal,
            "shareTotal" to shareTotal,
            "createAt" to createAt,
        )
    }
}
