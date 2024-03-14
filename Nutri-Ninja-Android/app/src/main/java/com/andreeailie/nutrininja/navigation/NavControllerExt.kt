package com.andreeailie.nutrininja.navigation

import androidx.navigation.NavController
import com.andreeailie.core.util.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route)
}