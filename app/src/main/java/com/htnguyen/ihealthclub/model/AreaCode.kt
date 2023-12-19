package com.htnguyen.ihealthclub.model

import com.google.gson.annotations.SerializedName
import com.htnguyen.ihealthclub.R
import java.io.Serializable

data class AreaCode (
    @SerializedName("name") val name : String,
    @SerializedName("region") val region : String,
    @SerializedName("phone") val phone: List<String>?,
    @SerializedName("image") val image : String,
    @SerializedName("emoji") val emoji : String,
    //@SerializedName("phoneLength") val phoneLength : Int,
) : BaseItem(), Serializable {
    override val layoutResourceId: Int
        get() = R.layout.item_area_code

}