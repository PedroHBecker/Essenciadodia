package com.seugrupo.essenciadodia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.seugrupo.essenciadodia.ui.AppNavigation

import com.seugrupo.essenciadodia.ui.theme.EssenciadodiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            EssenciadodiaTheme {
                AppNavigation()
            }
        }
    }
}