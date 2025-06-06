package miao.kmirror.jianzhoucat.feature.screen.main.page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import miao.kmirror.jianzhoucat.feature.screen.AppNavigation
import miao.kmirror.jianzhoucat.feature.screen.global.component.GlobalToastHost
import miao.kmirror.jianzhoucat.feature.screen.main.viewmodel.MainAtyViewModel
import miao.kmirror.jianzhoucat.ui.theme.JianZhouCatTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainAtyViewModel by viewModels<MainAtyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val seedColor by mainAtyViewModel.seedColor.collectAsState()

            JianZhouCatTheme(
                seedColor = seedColor
            ) {
                Box(modifier = Modifier.Companion.fillMaxSize()) {
                    AppNavigation()
                    GlobalToastHost()
                }
            }
        }
    }
}