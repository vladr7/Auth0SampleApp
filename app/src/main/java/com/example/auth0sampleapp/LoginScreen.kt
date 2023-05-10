package com.example.auth0sampleapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = onLoginClicked, modifier = Modifier.padding(24.dp)) {
            Text(text = "Login", modifier = Modifier.padding(12.dp))
        }
        Button(onClick = onLogoutClicked, modifier = Modifier.padding(24.dp)) {
            Text(text = "Logout", modifier = Modifier.padding(12.dp))
        }
    }
}