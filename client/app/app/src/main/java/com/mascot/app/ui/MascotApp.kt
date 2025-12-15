package com.mascot.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.mascot.app.ui.home.HomeScreen
import com.mascot.app.ui.quest.QuestScreen
import com.mascot.app.ui.quest.QuestDetailScreen
import com.mascot.app.ui.quest.CompletedQuestScreen
import com.mascot.app.ui.ar.ARScreen
import com.mascot.app.ui.encyclopedia.EncyclopediaScreen
import com.mascot.app.ui.tutorial.TutorialStartScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "홈", Icons.Default.Home)
    object Quest : Screen("quest", "퀘스트", Icons.Default.Star)
    object AR : Screen("ar", "AR", Icons.Default.Search)
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

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. 메인 화면 영역
        NavHost(
            navController = navController,
            startDestination = "tutorial_start",
            modifier = Modifier.fillMaxSize()
        ) {

            // ✨✨ [핵심 수정] 홈 화면이 인자를 받을 수 있도록 설정 변경 ✨✨
            composable(
                // 1. 경로 뒤에 ?new_mascot={new_mascot} 를 붙여줍니다.
                route = "${Screen.Home.route}?new_mascot={new_mascot}",

                // 2. 어떤 인자를 받을지 정의합니다.
                arguments = listOf(
                    navArgument("new_mascot") {
                        type = NavType.BoolType
                        defaultValue = false // 평소에는 false
                    }
                )
            ) {
                HomeScreen(navController)
            }

            composable(Screen.Quest.route) {
                Box(modifier = Modifier.padding(bottom = 80.dp)) {
                    QuestScreen(navController)
                }
            }

            composable(Screen.AR.route) {
                Box(modifier = Modifier.padding(bottom = 80.dp)) {
                    ARScreen(navController)
                }
            }

            composable(Screen.Encyclopedia.route) {
                Box(modifier = Modifier.padding(bottom = 80.dp)) {
                    EncyclopediaScreen()
                }
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("questId") { type = NavType.StringType })
            ) { backStackEntry ->
                val questId = backStackEntry.arguments?.getString("questId") ?: "0"

                Box(modifier = Modifier.padding(bottom = 80.dp)) {
                    QuestDetailScreen(
                        navController = navController,
                        questId = questId
                    )
                }
            }

            composable("tutorial_start") {
                TutorialStartScreen(navController)
            }

            composable("completed_quests") {
                CompletedQuestScreen(navController = navController)
            }
        }

        // 2. 하단 네비게이션 바
        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            containerColor = NavigationBarDefaults.containerColor,
            tonalElevation = NavigationBarDefaults.Elevation
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            items.forEach { screen ->
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                    label = { Text(screen.title) },
                    selected = currentDestination
                        ?.hierarchy
                        // Route 비교 시 쿼리 파라미터를 고려해야 할 수도 있지만,
                        // hierarchy 체크는 보통 기본 경로를 포함하면 true를 반환하므로 작동합니다.
                        ?.any { it.route?.startsWith(screen.route) == true } == true,

                    onClick = {
                        when (screen) {
                            Screen.Home -> {
                                navController.navigate(Screen.Home.route) { // 기본 "home"으로 이동 (false)
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }

                            Screen.AR -> {
                                navController.navigate(Screen.AR.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }

                            else -> {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFFFD260).copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}