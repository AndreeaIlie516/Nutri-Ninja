package com.andreeailie.nutrininja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.andreeailie.nutrininja.ui.theme.NutriNinjaTheme
import com.andreeailie.onboarding_presentation.welcome.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriNinjaTheme {
                WelcomeScreen()
            }
        }
    }
}