package com.example.mytradingsimulatorlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mytradingsimulatorlite.ui.navigation.AppNav
import com.example.mytradingsimulatorlite.ui.theme.MyTradingSimulatorLiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTradingSimulatorLiteTheme {
                AppNav()
            }
        }
    }
}
