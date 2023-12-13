package com.htnguyen.ihealthclub.model

import java.io.Serializable

data class ListLike(
    var idUser: String = "",
    var srcLike: TypeLike = TypeLike.LIKE
):Serializable{}

