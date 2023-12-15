package com.htnguyen.ihealthclub.model

import java.io.Serializable

data class UserLogin(
    var idUser: String? = null,
    var account: String? = "",
    var password: String? = "",
) :  Serializable {

}