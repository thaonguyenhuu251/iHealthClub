package com.htnguyen.ihealthclub.model

import androidx.room.Entity
import java.io.Serializable


@Entity(tableName = "options_table")
data class OptionsHome(
    var optionName: String = "",
    var srcImage: Int,
    var backgroundColor: Int,
    var textColor: Int
):Serializable{

}
