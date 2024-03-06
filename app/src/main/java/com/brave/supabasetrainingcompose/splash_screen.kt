package com.brave.supabasetrainingcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        val session = supabase.auth.currentSessionOrNull()
        delay(2000)
        if (session == null) {
            navController.navigate("LoginScreen") {
                popUpTo("SplashScreen") {
                    inclusive = true
                }
            }

        } else {
            navController.navigate("MainScreen") {
                popUpTo("SplashScreen") {
                    inclusive = true
                }
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        // You can replace this with your own splash screen content
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}
