package com.example.mobileappporject.ui.profile

import android.net.Uri

sealed interface ProfileEvent {
    data class TempNameChanged(val value: String) : ProfileEvent
    object ToggleEditName : ProfileEvent
    object SaveName : ProfileEvent
    data class ToggleNotifications(val enabled: Boolean) : ProfileEvent
    data class ToggleSoundEffects(val enabled: Boolean) : ProfileEvent
    data class ToggleDarkMode(val enabled: Boolean) : ProfileEvent
    data class LanguageChanged(val language: String) : ProfileEvent
    data class SelectAvatar(val emoji: String) : ProfileEvent
    data class CustomImageSelected(val uri: Uri?) : ProfileEvent
    object ToggleAvatarPicker : ProfileEvent
    object ToggleAccountSecurity : ProfileEvent
    object ToggleChangeEmailDialog : ProfileEvent
    data class NewEmailInputChanged(val value: String) : ProfileEvent
    object SubmitChangeEmail : ProfileEvent
    object ToggleChangePasswordDialog : ProfileEvent
    data class NewPasswordInputChanged(val value: String) : ProfileEvent
    object SubmitChangePassword : ProfileEvent
    object ClearFeedbackMessage : ProfileEvent
}