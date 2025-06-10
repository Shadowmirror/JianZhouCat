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
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.AndroidEntryPoint
import miao.kmirror.jianzhoucat.feature.screen.AppNavigation
import miao.kmirror.jianzhoucat.feature.screen.global.component.GlobalToastHost
import miao.kmirror.jianzhoucat.feature.screen.main.viewmodel.MainAtyViewModel
import miao.kmirror.jianzhoucat.ui.theme.JianZhouCatTheme
import miao.kmirror.jianzhoucat.utils.TTSHelper
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainAtyViewModel by viewModels<MainAtyViewModel>()

    @Inject
    lateinit var ttsHelper: TTSHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeColor by mainAtyViewModel.themeColor.collectAsState()

            JianZhouCatTheme(
                themeColor = Color(themeColor)
            ) {
                Box(modifier = Modifier.Companion.fillMaxSize()) {
                    AppNavigation()
                    GlobalToastHost()
                }
            }
        }
    }

    override fun onDestroy() {
        ttsHelper.shutdown()
        super.onDestroy()
    }


}