package sae.iot

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import sae.iot.ui.IOTApp
import sae.iot.ui.theme.IOTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val sharedPreferences =
                context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

            val dark = sharedPreferences.getBoolean("dark_mode", false)

            IOTTheme(darkDefault = dark) {
                IOTApp()
            }
        }
    }
}