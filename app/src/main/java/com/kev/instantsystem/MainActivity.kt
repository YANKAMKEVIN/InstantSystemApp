package com.kev.instantsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kev.instantsystem.ui.InstantSystemApp
import com.kev.instantsystem.ui.theme.InstantSystemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstantSystemTheme(darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InstantSystemNews(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun InstantSystemNews(
    modifier: Modifier = Modifier
) {
    InstantSystemApp()
}
