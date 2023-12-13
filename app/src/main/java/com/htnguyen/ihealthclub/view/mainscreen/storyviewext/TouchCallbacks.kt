package com.htnguyen.ihealthclub.view.mainscreen.storyviewext

interface TouchCallbacks {
    fun touchPull()

    fun touchDown(xValue: Float, yValue: Float)

    fun touchUp()
}