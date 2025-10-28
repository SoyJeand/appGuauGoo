package com.example.appguaugo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.example.appguaugo.presentation.login.LoginScreen
import com.example.appguaugo.presentation.home.HomeScreen
import com.example.appguaugo.viewmodel.MainViewModel
import com.example.appguaugo.viewmodel.LoginViewModel

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                loginViewModel = loginViewModel,
                mainViewModel = mainViewModel
            )
        }
        composable("home") {
            HomeScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
    }
}
