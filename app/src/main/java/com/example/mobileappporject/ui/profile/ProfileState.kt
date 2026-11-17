package com.example.mobileappporject.ui.profile

import android.net.Uri

data class ProfileState(
    val name: String = "Learner",
    val email: String = "user@example.com",
    val avatarEmoji: String = "👨‍💻",
    val avatarUri: Uri? = null,
    val isEditingName: Boolean = false,
    val tempName: String = "",
    val notificationsEnabled: Boolean = true,
    val soundEffectsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val selectedLanguage: String = "English",
    val showAvatarPicker: Boolean = false,
    val showAccountSecurity: Boolean = true,
    val showChangeEmailDialog: Boolean = false,
    val showChangePasswordDialog: Boolean = false,
    val newEmailInput: String = "",
    val newPasswordInput: String = "",
    val feedbackMessage: String? = null,
    val isEmailVerified: Boolean = false
)