package com.htnguyen.ihealthclub.model

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "emoji_table")
data class EmojiHome(
    var emojiName: String = "",
    var srcImage: String = ""
):Serializable{

}

