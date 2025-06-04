package miao.kmirror.jianzhoucat.feature.screen.global.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import miao.kmirror.jianzhoucat.utils.ToastManager

@Composable
fun GlobalToastHost() {
    var message by remember { mutableStateOf<String?>(null) }
    var currentJob by remember { mutableStateOf<Job?>(null) }

    // 收集 Toast 消息
    LaunchedEffect(Unit) {
        ToastManager.toastMessage.collect { newMessage ->
            // 取消之前的计时
            currentJob?.cancel()
            // 更新消息
            message = newMessage
            // 启动新的计时
            currentJob = launch {
                delay(2000) // Toast 显示 2 秒
                message = null
            }
        }
    }

    // 显示自定义 Toast（例如：底部弹出）
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = message != null,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            message?.let {
                ToastCard(message = it)
            }
        }
    }
}


@Composable
fun ToastCard(message: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF323232))
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(text = message, color = Color.White, fontSize = 14.sp)
    }
}