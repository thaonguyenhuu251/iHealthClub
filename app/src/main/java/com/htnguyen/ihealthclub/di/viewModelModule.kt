package com.htnguyen.ihealthclub.di

import com.htnguyen.ihealthclub.view.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RegisterViewModel() }
}

