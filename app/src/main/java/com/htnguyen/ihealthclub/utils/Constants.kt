package com.htnguyen.ihealthclub.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.htnguyen.ihealthclub.model.TypeAction
import java.util.regex.Pattern

const val KEY_USER = "KEY_USER"
const val KEY_VERIFIED_ID = "KEY_VERIFIED_ID"
const val OTP_TIME_OUT = 120L

const val SHARED_PREFERENCES_KEY = "IHealthSport"
const val USER_ID = "USER_ID"
const val USER_NAME = "USER_NAME"
const val URL_PHOTO = "URL_PHOTO"
const val USER_EMAIL = "USER_EMAIL"
const val USER_PHONE = "USER_PHONE"

const val COLLECTION_PATH_USER = "UserLogin"

const val ICON_GET = "ICON_GET"
const val ICON_NAME = "ICON_NAME"

const val KEY_PATH_IMAGE_POST = "KEY_PATH_IMAGE_POST"
const val KEY_PATH_IMAGE = "KEY_PATH_IMAGE"
const val KEY_PATH_IMAGE_STORY = "KEY_PATH_IMAGE_STORY"
const val COLLECTION_PATH_STORY = "story"
const val TYPE_REGISTER = "TYPE_REGISTER"

const val KEY_ID = "KEY_ID"
const val KEY_SEARCH_EMOJI = "KEY_SEARCH_EMOJI"

const val PERSON_TYPE = "PERSON_TYPE"

const val KEY_EMOJI_PUT = "KEY_EMOJI_PUT"
const val KEY_SPORT_PUT = "KEY_SPORT_PUT"

val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

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

fun typeReaction(reaction: String): TypeAction {
    when (reaction) {
        "Like" -> return TypeAction.LIKE
        "Love" -> return TypeAction.LOVE
        "Smile" -> return TypeAction.SMILE
        "Wow" -> return TypeAction.WOW
        "Sad" -> return TypeAction.SAD
        "Angry" -> return TypeAction.ANGRY
    }
    return TypeAction.NO
}

fun checkEmail(email: String): Boolean {
    return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
}
