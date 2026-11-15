package com.example.mobileappporject.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobileappporject.ui.auth.login.isValidEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileViewModel : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        loadUser()
    }

    private fun loadUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email ?: "user@example.com"
            val userName = currentUser.displayName?.ifBlank { null }
                ?: userEmail.substringBefore("@").replace(".", " ").capitalizeWords()
            state = state.copy(
                name = userName,
                email = userEmail,
                tempName = userName,
                isEmailVerified = currentUser.isEmailVerified
            )
        }
    }

    fun onEvent(event: ProfileEvent) {
        state = when (event) {
            is ProfileEvent.TempNameChanged -> state.copy(tempName = event.value)
            ProfileEvent.ToggleEditName -> state.copy(
                isEditingName = !state.isEditingName,
                tempName = state.name
            )
            ProfileEvent.SaveName -> {
                val newName = if (state.tempName.isNotBlank()) state.tempName else state.name
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build()
                    currentUser.updateProfile(profileUpdates)
                }
                state.copy(name = newName, isEditingName = false)
            }
            is ProfileEvent.ToggleNotifications -> state.copy(notificationsEnabled = event.enabled)
            is ProfileEvent.ToggleSoundEffects -> state.copy(soundEffectsEnabled = event.enabled)
            is ProfileEvent.ToggleDarkMode -> state.copy(darkModeEnabled = event.enabled)
            is ProfileEvent.LanguageChanged -> state.copy(selectedLanguage = event.language)
            is ProfileEvent.SelectAvatar -> state.copy(avatarEmoji = event.emoji, avatarUri = null, showAvatarPicker = false)
            is ProfileEvent.CustomImageSelected -> state.copy(avatarUri = event.uri, showAvatarPicker = false)
            ProfileEvent.ToggleAvatarPicker -> state.copy(showAvatarPicker = !state.showAvatarPicker)
            ProfileEvent.ToggleChangeEmailDialog -> state.copy(showChangeEmailDialog = !state.showChangeEmailDialog, newEmailInput = "")
            is ProfileEvent.NewEmailInputChanged -> state.copy(newEmailInput = event.value)
            ProfileEvent.SubmitChangeEmail -> {
                if (!state.newEmailInput.isValidEmail()) {
                    state.copy(feedbackMessage = "Please enter a valid email address.")
                } else {
                    val currentUser = auth.currentUser
                    currentUser?.updateEmail(state.newEmailInput)
                        ?.addOnSuccessListener {
                            state = state.copy(
                                email = state.newEmailInput,
                                showChangeEmailDialog = false,
                                feedbackMessage = "Email updated successfully!"
                            )
                        }
                        ?.addOnFailureListener { e ->
                            state = state.copy(feedbackMessage = e.localizedMessage ?: "Failed to update email.")
                        }
                    state
                }
            }
            ProfileEvent.ToggleChangePasswordDialog -> state.copy(showChangePasswordDialog = !state.showChangePasswordDialog, newPasswordInput = "")
            is ProfileEvent.NewPasswordInputChanged -> state.copy(newPasswordInput = event.value)
            ProfileEvent.SubmitChangePassword -> {
                if (state.newPasswordInput.length < 8) {
                    state.copy(feedbackMessage = "Password must be at least 8 characters.")
                } else {
                    val currentUser = auth.currentUser
                    currentUser?.updatePassword(state.newPasswordInput)
                        ?.addOnSuccessListener {
                            state = state.copy(
                                showChangePasswordDialog = false,
                                feedbackMessage = "Password updated successfully!"
                            )
                        }
                        ?.addOnFailureListener { e ->
                            state = state.copy(feedbackMessage = e.localizedMessage ?: "Failed to update password.")
                        }
                    state
                }
            }
            ProfileEvent.ClearFeedbackMessage -> state.copy(feedbackMessage = null)
        }
    }
}

private fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { word -> word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
