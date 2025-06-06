package miao.kmirror.jianzhoucat.feature.screen.main.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import miao.kmirror.jianzhoucat.feature.screen.main.viewmodel.MainViewModel
import miao.kmirror.jianzhoucat.feature.screen.setting.page.SettingScreen
import miao.kmirror.jianzhoucat.feature.screen.word.page.WordScreen

@Composable
fun MainNavScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Words") },
                    label = { Text("Words") },
                    selected = currentDestination?.hasRoute<WordNav>() == true,
                    onClick = {
                        navController.navigate(WordNav) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = currentDestination?.hasRoute<SettingNav>() == true,
                    onClick = {
                        navController.navigate(SettingNav) {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WordNav,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<WordNav> { WordScreen() }
            composable<SettingNav> { SettingScreen() }
        }
    }
}

@Serializable
object WordNav

@Serializable
object SettingNav