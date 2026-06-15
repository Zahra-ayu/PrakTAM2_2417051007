package com.example.praktam2_2417051007

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.praktam2_2417051007.ui.navigation.AppNavigation
import com.example.praktam2_2417051007.ui.theme.PrakTAM2_2417051007Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrakTAM2_2417051007Theme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}