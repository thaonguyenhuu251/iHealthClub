package com.htnguyen.ihealthclub.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_table")
data class UserSaved(
    @PrimaryKey var account: String = "",
    var userName: String? = "",
    var password: String? = "",
    var photoUrl: String? = "",
): Serializable {

}