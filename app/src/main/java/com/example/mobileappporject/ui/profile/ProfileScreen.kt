package com.example.mobileappporject.ui.profile

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileappporject.ui.auth.AuthAccent
import com.example.mobileappporject.ui.auth.AuthCanvas
import com.example.mobileappporject.ui.auth.AuthInk
import com.example.mobileappporject.ui.auth.AuthMutedInk

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    // Gallery Image Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.onEvent(ProfileEvent.CustomImageSelected(uri))
        }
    }

    val imageBitmap = remember(state.avatarUri) {
        state.avatarUri?.let { uri ->
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                }
            } catch (_: Exception) {
                null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthCanvas)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Feedback message notice if present
        state.feedbackMessage?.let { msg ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF0FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(msg, color = AuthAccent, fontSize = 13.sp, modifier = Modifier.weight(1f))
                    TextButton(onClick = { viewModel.onEvent(ProfileEvent.ClearFeedbackMessage) }) {
                        Text("Dismiss", color = AuthInk, fontSize = 12.sp)
                    }
                }
            }
        }

        // Profile Avatar Display (Photo or Emoji)
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(AuthAccent, CircleShape)
                .clickable { viewModel.onEvent(ProfileEvent.ToggleAvatarPicker) },
            contentAlignment = Alignment.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Profile Photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(state.avatarEmoji, fontSize = 42.sp)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Upload Photo", color = AuthAccent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            TextButton(onClick = { viewModel.onEvent(ProfileEvent.ToggleAvatarPicker) }) {
                Text("Choose Emoji", color = AuthMutedInk, fontSize = 13.sp)
            }
        }

        // Emoji Avatar Picker if open
        if (state.showAvatarPicker) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Select Emoji Avatar", color = AuthInk, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        listOf("👨‍💻", "👩‍💻", "🦊", "🐼", "🚀", "⭐").forEach { emoji ->
                            Text(
                                emoji,
                                fontSize = 28.sp,
                                modifier = Modifier
                                    .clickable { viewModel.onEvent(ProfileEvent.SelectAvatar(emoji)) }
                                    .padding(6.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Name & Edit Section
        if (state.isEditingName) {
            OutlinedTextField(
                value = state.tempName,
                onValueChange = { viewModel.onEvent(ProfileEvent.TempNameChanged(it)) },
                label = { Text("Full Name") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuthAccent,
                    unfocusedBorderColor = Color(0xFFE2E7F0),
                    focusedLabelColor = AuthAccent
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.onEvent(ProfileEvent.SaveName) },
                    colors = ButtonDefaults.buttonColors(containerColor = AuthAccent),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save", fontSize = 13.sp)
                }
                Button(
                    onClick = { viewModel.onEvent(ProfileEvent.ToggleEditName) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E7F0)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancel", color = AuthInk, fontSize = 13.sp)
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(state.name, color = AuthInk, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = { viewModel.onEvent(ProfileEvent.ToggleEditName) }) {
                    Text("Edit", color = AuthAccent, fontSize = 13.sp)
                }
            }
        }

        Text(state.email, color = AuthMutedInk, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(20.dp))

        // Account & Security Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Account & Security", color = AuthInk, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onEvent(ProfileEvent.ToggleChangeEmailDialog) }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Change Email / Gmail", color = AuthInk, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(state.email, color = AuthMutedInk, fontSize = 12.sp)
                    }
                    Text("✏️", fontSize = 16.sp)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onEvent(ProfileEvent.ToggleChangePasswordDialog) }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Change Password", color = AuthInk, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Update your Firebase password", color = AuthMutedInk, fontSize = 12.sp)
                    }
                    Text("🔒", fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Preferences Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("App Preferences", color = AuthInk, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                // Notifications Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Push Notifications", color = AuthInk, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Daily reminders & updates", color = AuthMutedInk, fontSize = 12.sp)
                    }
                    Switch(
                        checked = state.notificationsEnabled,
                        onCheckedChange = { viewModel.onEvent(ProfileEvent.ToggleNotifications(it)) },
                        colors = SwitchDefaults.colors(checkedThumbColor = AuthAccent, checkedTrackColor = Color(0xFFEEF0FF))
                    )
                }

                // Sound Effects Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sound Effects", color = AuthInk, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Audio feedback on exercises", color = AuthMutedInk, fontSize = 12.sp)
                    }
                    Switch(
                        checked = state.soundEffectsEnabled,
                        onCheckedChange = { viewModel.onEvent(ProfileEvent.ToggleSoundEffects(it)) },
                        colors = SwitchDefaults.colors(checkedThumbColor = AuthAccent, checkedTrackColor = Color(0xFFEEF0FF))
                    )
                }

                // Dark Theme Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dark Theme", color = AuthInk, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Adjust app appearance", color = AuthMutedInk, fontSize = 12.sp)
                    }
                    Switch(
                        checked = state.darkModeEnabled,
                        onCheckedChange = { viewModel.onEvent(ProfileEvent.ToggleDarkMode(it)) },
                        colors = SwitchDefaults.colors(checkedThumbColor = AuthAccent, checkedTrackColor = Color(0xFFEEF0FF))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sign Out Button
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53E3E))
        ) {
            Text("Sign out", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }

    // Change Email Dialog
    if (state.showChangeEmailDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(ProfileEvent.ToggleChangeEmailDialog) },
            title = { Text("Change Email Address") },
            text = {
                Column {
                    Text("Enter your new email address below:", fontSize = 13.sp, color = AuthMutedInk)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.newEmailInput,
                        onValueChange = { viewModel.onEvent(ProfileEvent.NewEmailInputChanged(it)) },
                        label = { Text("New Email") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.onEvent(ProfileEvent.SubmitChangeEmail) },
                    colors = ButtonDefaults.buttonColors(containerColor = AuthAccent)
                ) {
                    Text("Update Email")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(ProfileEvent.ToggleChangeEmailDialog) }) {
                    Text("Cancel", color = AuthMutedInk)
                }
            }
        )
    }

    // Change Password Dialog
    if (state.showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(ProfileEvent.ToggleChangePasswordDialog) },
            title = { Text("Change Password") },
            text = {
                Column {
                    Text("Enter your new password below:", fontSize = 13.sp, color = AuthMutedInk)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.newPasswordInput,
                        onValueChange = { viewModel.onEvent(ProfileEvent.NewPasswordInputChanged(it)) },
                        label = { Text("New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.onEvent(ProfileEvent.SubmitChangePassword) },
                    colors = ButtonDefaults.buttonColors(containerColor = AuthAccent)
                ) {
                    Text("Update Password")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(ProfileEvent.ToggleChangePasswordDialog) }) {
                    Text("Cancel", color = AuthMutedInk)
                }
            }
        )
    }
}
