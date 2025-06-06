package miao.kmirror.jianzhoucat.feature.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import miao.kmirror.jianzhoucat.feature.screen.main.page.MainNavScreen
import miao.kmirror.jianzhoucat.feature.screen.splash.page.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = SplashNav) {
        composable<SplashNav> {
            SplashScreen {
                navController.navigate(MainNav) {
                    popUpTo(SplashNav) { inclusive = true }
                }
            }
        }
        composable<MainNav> {
//            MainScreen()
            MainNavScreen()
        }
    }
}

@Serializable
object SplashNav

@Serializable
object MainNav