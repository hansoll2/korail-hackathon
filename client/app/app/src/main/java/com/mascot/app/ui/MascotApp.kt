package com.mascot.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mascot.app.ui.home.HomeScreen
import com.mascot.app.ui.quest.QuestScreen
import com.mascot.app.ui.ar.ARScreen
import com.mascot.app.ui.encyclopedia.EncyclopediaScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "홈", Icons.Default.Home)
    object Quest : Screen("quest", "퀘스트", Icons.Default.Star)
    object AR : Screen("ar", "AR모드", Icons.Default.Search)
    object Encyclopedia : Screen("encyclopedia", "도감", Icons.Default.List)
    object Detail : Screen("quest_detail/{questId}", "상세정보", Icons.Default.Star)
}

@Composable
fun MascotApp() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Home,
        Screen.Quest,
        Screen.AR,
        Screen.Encyclopedia
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // 탭 클릭 시 스택 쌓임 방지 (백버튼 누르면 홈으로)
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Quest.route) { QuestScreen(navController) }
            composable(Screen.AR.route) { ARScreen(navController) }
            composable(Screen.Encyclopedia.route) { EncyclopediaScreen(navController) }

            composable(
                route = Screen.Detail.route, // "quest_detail/{questId}"
                arguments = listOf(
                    androidx.navigation.navArgument("questId") { type = androidx.navigation.NavType.IntType }
                )
            ) { backStackEntry ->
                val questId = backStackEntry.arguments?.getInt("questId") ?: 0

                com.mascot.app.ui.quest.QuestDetailScreen(navController, questId)
            }
        }
    }
}