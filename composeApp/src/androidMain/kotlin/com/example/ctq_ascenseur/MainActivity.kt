package com.example.ctq_ascenseur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ctq_ascenseur.data.appContext
import com.example.ctq_ascenseur.di.initKoin
import com.example.ctq_ascenseur.ui.App
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        appContext = applicationContext
        
        // Initialize Koin if not already initialized
        try {
            initKoin {
                androidContext(this@MainActivity)
            }
        } catch (e: Exception) {
            // Already initialized
        }

        setContent {
            App()
        }
    }
}
