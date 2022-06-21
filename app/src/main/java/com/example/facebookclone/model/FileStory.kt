package com.example.facebookclone.model

import java.io.Serializable

data class FileStory(
    var idFile:String ="",
    var url:String="",
    var typeFile: TypeFile = TypeFile.IMAGE
):Serializable{

}
