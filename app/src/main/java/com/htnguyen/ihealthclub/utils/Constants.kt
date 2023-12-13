package com.htnguyen.ihealthclub.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.htnguyen.ihealthclub.model.TypeLike

const val KEY_USER = "KEY_USER"
const val KEY_VERIFIED_ID = "KEY_VERIFIED_ID"
const val OTP_TIME_OUT = 120L

const val SHARED_PREFERENCES_KEY = "Facebookclone"
const val USER_ID = "USER_ID"
const val USER_NAME = "USER_NAME"
const val URL_PHOTO = "URL_PHOTO"

const val COLLECTION_PATH_USER = "users"

const val ICON_GET = "ICON_GET"
const val ICON_NAME = "ICON_NAME"

const val KEY_PATH_IMAGE_POST = "KEY_PATH_IMAGE_POST"

const val KEY_PATH_IMAGE_STORY="KEY_PATH_IMAGE_STORY"
const val COLLECTION_PATH_STORY = "story"

const val KEY_ID = "KEY_ID"
const val KEY_SEARCH_EMOJI = "KEY_SEARCH_EMOJI"

 fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
        val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        cursor?.getString(columnIndex!!)
    } finally {
        cursor?.close()
    }
}

fun typeReaction(reaction: String) :TypeLike{
    when(reaction){
        "Like" -> return TypeLike.LIKE
        "Love" -> return TypeLike.LOVE
        "Smile"->return TypeLike.SMILE
        "Wow" -> return TypeLike.WOW
        "Sad"-> return TypeLike.SAD
        "Angry"-> return TypeLike.ANGRY
    }
    return TypeLike.NO
}
