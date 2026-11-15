package com.example.mobileappporject.ui.home

data class HomeState(
    val userName: String = "Learner",
    val streakDays: Int = 5,
    val dailyGoalMins: Int = 20,
    val completedMins: Int = 15
)
