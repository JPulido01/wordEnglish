package com.example.wordenglish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wordenglish.ui.detail.WordDetailScreen
import com.example.wordenglish.ui.favorites.FavoritesScreen
import com.example.wordenglish.ui.history.HistoryScreen
import com.example.wordenglish.ui.home.HomeScreen
import com.example.wordenglish.ui.review.ReviewScreen
import com.example.wordenglish.ui.settings.SettingsScreen
import com.example.wordenglish.ui.theme.WordEnglishTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordEnglishTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
private fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToWordDetail = { navController.navigate("word_detail") },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToHistory = { navController.navigate("history") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable("word_detail") {
            WordDetailScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = "word_detail/{favoriteId}",
            arguments = listOf(navArgument("favoriteId") { type = NavType.IntType })
        ) {
            WordDetailScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = "word_detail/history/{historyId}",
            arguments = listOf(navArgument("historyId") { type = NavType.IntType })
        ) {
            WordDetailScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable("favorites") {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToWordDetail = { id -> navController.navigate("word_detail/$id") },
                onNavigateToReview = { navController.navigate("review") }
            )
        }
        composable("history") {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { id -> navController.navigate("word_detail/history/$id") }
            )
        }
        composable("review") {
            ReviewScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable("settings") {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
