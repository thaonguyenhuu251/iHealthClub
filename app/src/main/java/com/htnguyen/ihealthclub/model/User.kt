package com.htnguyen.ihealthclub.model

import java.io.Serializable

data class User(
    var idUser: String? = null,
    var email: String? = "",
    var phoneNumber: String? = "",
    var birthDay: Long? = 0L,
    var gender: Boolean? = null,
    var photoUrl: String? = "",
    var height: Float? = 0f,
    var weight: Float? = 0f,
    var name: String? = ""
) : Serializable {

}
