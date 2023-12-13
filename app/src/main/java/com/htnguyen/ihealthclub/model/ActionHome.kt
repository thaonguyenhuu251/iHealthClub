package com.htnguyen.ihealthclub.model


import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "action_table")
data class ActionHome (
    var actionName: String = "",
    var srcImage: Int
):Serializable{

}