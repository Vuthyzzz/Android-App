package com.example.mobileappporject.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Course : Screen("course", "Course")
    object Grammar : Screen("grammar", "Grammar")
    object Practice : Screen("practice", "Practice")
    object Progress : Screen("progress", "Progress")
    object Profile : Screen("profile", "Profile")
}
