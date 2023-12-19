package com.htnguyen.ihealthclub.utils

import com.htnguyen.ihealthclub.ClubApp

object Event {
    const val EVENT_SEARCH_EMOJI = "SEARCH_EMOJI"

    fun searchEmoji(searchText: String) {
        ClubApp.eventBus.onNext(hashMapOf(EVENT_SEARCH_EMOJI to searchText))
    }

}