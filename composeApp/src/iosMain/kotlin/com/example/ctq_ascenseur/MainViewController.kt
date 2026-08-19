package com.example.ctq_ascenseur

import androidx.compose.ui.window.ComposeUIViewController
import com.example.ctq_ascenseur.di.initKoin
import com.example.ctq_ascenseur.ui.App

fun MainViewController() = ComposeUIViewController {
    App()
}

fun initKoinIos() {
    initKoin()
}
