package com.example.ctq_ascenseur.di

import com.example.ctq_ascenseur.data.AppDatabase
import com.example.ctq_ascenseur.data.getDatabase
import com.example.ctq_ascenseur.data.getDatabaseBuilder
import com.example.ctq_ascenseur.ui.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { getDatabase(getDatabaseBuilder()) }
    single { get<AppDatabase>().appDao() }
    
    viewModelOf(::MainViewModel)
}
