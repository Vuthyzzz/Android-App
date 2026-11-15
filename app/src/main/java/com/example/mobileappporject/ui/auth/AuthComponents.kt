package com.example.mobileappporject.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val AuthInk = Color(0xFF172B4D)
internal val AuthMutedInk = Color(0xFF718096)
internal val AuthCanvas = Color(0xFFF7F8FC)
internal val AuthAccent = Color(0xFF5C67E8)
private val AuthBorder = Color(0xFFE2E7F0)

@Composable
internal fun AuthBrandHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp),
            contentAlignment = Alignment.Center
        ) {

        }
        Text(
            "",
            modifier = Modifier.padding(start = 11.dp),
            color = AuthInk,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Surface(color = Color(0xFFEEF0FF), shape = CircleShape) {
            Text(
                "",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = AuthAccent,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
internal fun AuthPage(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthCanvas)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthBrandHeader()
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .size(68.dp)
                .background(Color(0xFFE9EBFF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("✦", color = AuthAccent, fontSize = 30.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, color = AuthInk, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            subtitle,
            modifier = Modifier.fillMaxWidth(0.94f),
            color = AuthMutedInk,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                content()
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            "By continuing, you agree to our Terms of Service and Privacy Policy.",
            modifier = Modifier.fillMaxWidth(0.94f),
            color = AuthMutedInk,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
internal fun AuthInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    password: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder, color = Color(0xFFA0A9B8)) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (password && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (password && onTogglePassword != null) {
            {
                TextButton(onClick = onTogglePassword) {
                    Text(if (passwordVisible) "Hide" else "Show", color = AuthAccent)
                }
            }
        } else {
            null
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AuthAccent,
            unfocusedBorderColor = AuthBorder,
            focusedLabelColor = AuthAccent,
            unfocusedLabelColor = AuthMutedInk,
            focusedTextColor = AuthInk,
            unfocusedTextColor = AuthInk,
            cursorColor = AuthAccent
        )
    )
}

@Composable
internal fun AuthPrimaryButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AuthAccent)
    ) {
        Text(label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
internal fun AuthGoogleButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, AuthBorder),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = AuthInk
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Stylized Google 'G' Icon badge
            Surface(
                shape = CircleShape,
                color = Color(0xFFF1F5F9),
                modifier = Modifier.size(26.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("G", color = Color(0xFF4285F4), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text("Continue with Google", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = AuthInk)
        }
    }
}

@Composable
internal fun AuthDivider(text: String = "OR") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = AuthBorder)
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp),
            color = AuthMutedInk,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = AuthBorder)
    }
}

@Composable
internal fun AuthFeedback(message: String?) {
    if (message != null) {
        Text(
            message,
            modifier = Modifier.padding(vertical = 10.dp),
            color = if (message.startsWith("Please") || message.contains("failed") || message.contains("invalid")) Color(0xFFB54747) else AuthAccent,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
internal fun AuthNavigationPrompt(
    text: String,
    action: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = AuthMutedInk, fontSize = 14.sp)
        TextButton(onClick = onClick) {
            Text(action, color = AuthAccent, fontWeight = FontWeight.SemiBold)
        }
    }
}
