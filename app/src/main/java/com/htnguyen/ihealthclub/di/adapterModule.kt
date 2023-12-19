package com.htnguyen.ihealthclub.di


import com.htnguyen.ihealthclub.base.BaseRecyclerViewAdapter
import com.htnguyen.ihealthclub.model.BaseItem
import org.koin.dsl.module

val adapterModule = module {
    single { BaseRecyclerViewAdapter<BaseItem>() }
}