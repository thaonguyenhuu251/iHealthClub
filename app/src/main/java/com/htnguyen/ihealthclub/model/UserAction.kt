package com.htnguyen.ihealthclub.model

import com.google.firebase.database.Exclude

data class UserAction(
    var idUser: String? = "",
    var typeAction: TypeAction = TypeAction.NO,
    var timeAction: Long? = 0,
    var contentAction: String = ""
)  {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idUser" to idUser,
            "typeAction" to typeAction,
            "timeAction" to timeAction,
            "contentAction" to contentAction
        )
    }
}

