package com.example.facebookclone.model

import java.io.Serializable

data class FilePost(
    var idFile:String ="",
    var status: String ="",
    var emojiStatus: String="",
): Serializable {

}