package io.github.adrian2414745.coffeelog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.adrian2414745.coffeelog.ui.navigation.CoffeeLogNavGraph
import io.github.adrian2414745.coffeelog.ui.theme.CoffeeLogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val container = (application as CoffeeLogApp).container
        setContent {
            val darkPref by container.themeRepository.darkTheme.collectAsState(initial = null)
            val darkTheme = darkPref ?: isSystemInDarkTheme()
            CoffeeLogTheme(darkTheme = darkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CoffeeLogNavGraph()
                }
            }
        }
    }
}
