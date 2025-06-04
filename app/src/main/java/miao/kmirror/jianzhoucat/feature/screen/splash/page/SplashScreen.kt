package miao.kmirror.jianzhoucat.feature.screen.splash.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import miao.kmirror.jianzhoucat.feature.screen.splash.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    onTimeout: () -> Unit
) {

    val timeout by splashViewModel.timeOut.collectAsState()
    val dataInitialized by splashViewModel.dataInitialized.collectAsState()

    // 启动初始化
    LaunchedEffect(Unit) {
        splashViewModel.initData()
    }

    // 监听跳转时机
    LaunchedEffect(dataInitialized) {
        if (dataInitialized) {
            onTimeout()
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("欢迎页面 - Splash Screen - $timeout")
    }
}