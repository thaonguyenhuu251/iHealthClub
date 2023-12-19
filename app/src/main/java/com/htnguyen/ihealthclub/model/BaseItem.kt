package com.htnguyen.ihealthclub.model

import androidx.databinding.BaseObservable

abstract class BaseItem : BaseObservable() {
    private var objectTitle: String?

    get() = objectTitle
    set(value) {
        objectTitle = value
    }

    abstract val layoutResourceId: Int

    open var dataType: Int = 0

}