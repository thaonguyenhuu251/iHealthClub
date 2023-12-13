package com.htnguyen.ihealthclub.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_table")
data class UserSaved(
    @PrimaryKey var phoneNumber: String = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var password: String? = "",
    var photoUrl: String? = "",
): Serializable {

}