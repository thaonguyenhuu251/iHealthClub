package com.htnguyen.ihealthclub.utils

import com.htnguyen.ihealthclub.FacebookApp

object Event {
    const val EVENT_SEARCH_EMOJI = "SEARCH_EMOJI"

    fun searchEmoji(searchText: String) {
        FacebookApp.eventBus.onNext(hashMapOf(EVENT_SEARCH_EMOJI to searchText))
    }

}