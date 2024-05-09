package com.andreeailie.core.navigation

import com.andreeailie.core.R

enum class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
    Home("Home", R.drawable.ic_home, Route.TRACKER_OVERVIEW),
    Groceries("Groceries", R.drawable.ic_grocery, Route.AGE),
    Profile("Profile", R.drawable.ic_profile, Route.HEIGHT)
}