package com.example.facebookclone.model

import java.io.Serializable

data class User(
  var phoneNumber: String = "",
  var firstName: String? = "",
  var lastName: String? = "",
  var birthday: String? = "",
  var gender: Int? = 0,
  var email: String? = "",
  var password: String? = "",
  var photoUrl: String? = "",

  ) : Serializable {

}
