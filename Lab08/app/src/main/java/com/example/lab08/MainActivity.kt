package com.example.lab08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab08.ui.screens.SettingsScreen
import com.example.lab08.ui.theme.Lab08Theme
import com.example.lab08.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TouristGuideApp()
        }
    }
}

@Composable
fun TouristGuideApp() {
    val viewModel: ThemeViewModel = viewModel()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    Lab08Theme(darkTheme = isDarkTheme) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = { viewModel.toggleTheme() },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}