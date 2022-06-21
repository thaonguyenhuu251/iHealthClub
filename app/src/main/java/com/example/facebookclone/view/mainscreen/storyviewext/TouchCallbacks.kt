package com.example.facebookclone.view.mainscreen.storyviewext

interface TouchCallbacks {
    fun touchPull()

    fun touchDown(xValue: Float, yValue: Float)

    fun touchUp()
}