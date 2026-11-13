package com.example.mobileappporject.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthAccent
import com.example.mobileappporject.ui.auth.AuthMutedInk
import com.example.mobileappporject.ui.course.CourseScreen
import com.example.mobileappporject.ui.home.HomeScreen
import com.example.mobileappporject.ui.practice.PracticeScreen
import com.example.mobileappporject.ui.profile.ProfileScreen
import com.example.mobileappporject.ui.progress.ProgressScreen

private enum class MainTab(val title: String, val iconLabel: String) {
    HOME("Home", "🏠"),
    COURSE("Course", "📚"),
    PRACTICE("Practice", "✍️"),
    PROGRESS("Progress", "📊"),
    PROFILE("Profile", "👤")
}

@Composable
fun MainAppScreen(onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf(MainTab.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Text(tab.iconLabel, fontSize = 20.sp) },
                        label = { Text(tab.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AuthAccent,
                            selectedTextColor = AuthAccent,
                            unselectedIconColor = AuthMutedInk,
                            unselectedTextColor = AuthMutedInk,
                            indicatorColor = Color(0xFFEEF0FF)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (selectedTab) {
                MainTab.HOME -> HomeScreen()
                MainTab.COURSE -> CourseScreen()
                MainTab.PRACTICE -> PracticeScreen()
                MainTab.PROGRESS -> ProgressScreen()
                MainTab.PROFILE -> ProfileScreen(onLogout = onLogout)
            }
        }
    }
}
