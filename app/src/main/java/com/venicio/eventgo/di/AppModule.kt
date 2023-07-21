package com.venicio.eventgo.di

import com.venicio.eventgo.viewmodel.EventGoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EventGoViewModel() }

}