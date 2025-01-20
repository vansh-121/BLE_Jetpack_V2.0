package com.example.ble_jetpackcompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash_screen") {

        composable("splash_screen") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("first_screen") {
                        popUpTo("splash_screen") { inclusive = true }
                    }
                }
            )
        }

//        composable("home_screen") {
//            SplashScreen(
//                onNavigatefromLogin = {
//                    navController.navigate("home_screen") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                },
//                onNavigatefromRegister = {
//                    navController.navigate("home_screen") {
//                        popUpTo("register") { inclusive = true }
//                    }
//                }
//            )
//        }


        composable("first_screen") {
            AnimatedFirstScreen(
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onNavigateToSignup = {
                    navController.navigate("register")
                }
            )
        }


        composable("login") {
            LoginScreen(
                onNavigateToLogin = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToRegister = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
    }
}