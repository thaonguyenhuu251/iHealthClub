package com.htnguyen.ihealthclub.view.mainscreen.storyviewext

interface StoryCallbacks {
    fun startStories()

    fun pauseStories()

    fun nextStory()

    fun onDescriptionClickListener(position: Int)
}