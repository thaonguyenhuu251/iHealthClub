package com.htnguyen.ihealthclub.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htnguyen.ihealthclub.model.AreaCode
import java.io.InputStream

fun readJSONFromAsset(context: Context, fileName: String) : String {
    var json: String? = null
    val  inputStream: InputStream = context.assets.open(fileName)
    json = inputStream.bufferedReader().use{it.readText()}
    return json
}

fun parseJsonToListAreaCode(json: String): Array<AreaCode> {
    val gson = Gson()
    val arrayTutorialType = object : TypeToken<Array<AreaCode>>() {}.type
    return gson.fromJson(json, arrayTutorialType)
}