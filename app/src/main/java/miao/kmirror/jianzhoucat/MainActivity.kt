package miao.kmirror.jianzhoucat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import miao.kmirror.jianzhoucat.feature.screen.AppNavigation
import miao.kmirror.jianzhoucat.feature.screen.global.component.GlobalToastHost
import miao.kmirror.jianzhoucat.ui.theme.JianZhouCatTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JianZhouCatTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                    GlobalToastHost()
                }
            }
        }
    }
}
