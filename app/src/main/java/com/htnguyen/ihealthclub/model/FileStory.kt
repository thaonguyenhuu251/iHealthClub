package com.htnguyen.ihealthclub.model

import java.io.Serializable

data class FileStory(
    var idFile: String = "",
    var url: String = "",
    var typeFile: TypeFile = TypeFile.IMAGE
) : Serializable {

}
