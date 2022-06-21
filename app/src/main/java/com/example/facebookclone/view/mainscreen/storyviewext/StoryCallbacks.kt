package com.example.facebookclone.view.mainscreen.storyviewext

interface StoryCallbacks {
    fun startStories()

    fun pauseStories()

    fun nextStory()

    fun onDescriptionClickListener(position: Int)
}