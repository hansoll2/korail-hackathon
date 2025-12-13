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

    // ✨ 구조 변경: Scaffold 대신 Box를 사용하여 겹쳐 그리기(Overlay) 구현
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. 화면 내용 (가장 뒤에 배치)
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize() // 화면 전체를 꽉 채움
        ) {
            // [홈 화면] 패딩 없이 꽉 채움 (배경이 네비게이션 바 뒤까지 내려감)
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }

            // [다른 화면들] 내용이 네비게이션 바에 가려지지 않도록 하단 패딩 추가
            // (네비게이션 바 높이가 보통 80dp 정도 됩니다)
            composable(Screen.Quest.route) {
                Box(modifier = Modifier.padding(bottom = 80.dp)) {
                    QuestScreen(navController)
                }
            }
            composable("tutorial_start") {
                TutorialStartScreen(navController)
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
            composable("completed_quests") {
                CompletedQuestScreen(navController = navController)
            }

        }

            // 2. 네비게이션 바 (화면 위에 덮어씌움)
        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter), // 화면 바닥에 고정
            containerColor = NavigationBarDefaults.containerColor, // 원래 배경색 유지 (투명 X)
            tonalElevation = NavigationBarDefaults.Elevation // 원래 그림자 유지
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            items.forEach { screen ->
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                    label = { Text(screen.title) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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