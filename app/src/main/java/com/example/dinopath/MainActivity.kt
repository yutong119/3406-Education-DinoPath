package com.example.dinopath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dinopath.ui.DinoPathApp
import com.example.dinopath.ui.theme.DinoPathTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DinoPathTheme {
                DinoPathApp()
            }
        }
    }
}