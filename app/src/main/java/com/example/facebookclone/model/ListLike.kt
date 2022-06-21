package com.example.facebookclone.model

import java.io.Serializable

data class ListLike(
    var idUser: String = "",
    var srcLike: TypeLike = TypeLike.LIKE
):Serializable{}

