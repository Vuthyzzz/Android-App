package com.example.mobileappporject.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.mobileappporject.ui.auth.AuthAccent
import com.example.mobileappporject.ui.auth.AuthMutedInk

enum class BottomTab(val route: String, val title: String, val iconLabel: String) {
    HOME("home", "Home", "🏠"),
    COURSE("course", "Course", "📚"),
    PRACTICE("practice", "Practice", "✍️"),
    PROGRESS("progress", "Progress", "📊"),
    PROFILE("profile", "Profile", "👤")
}

@Composable
fun AppBottomBar(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        BottomTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
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
