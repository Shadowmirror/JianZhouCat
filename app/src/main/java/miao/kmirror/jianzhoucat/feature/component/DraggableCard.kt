package miao.kmirror.jianzhoucat.feature.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class SwipeResult {
    ACCEPTED, REJECTED
}

@Composable
fun DraggableCard(
    item: Any,
    modifier: Modifier = Modifier,
    onSwiped: (SwipeResult, Any) -> Unit,
    content: @Composable () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val swipeThreshold = with(LocalDensity.current) { (screenWidth * 0.25f).toPx() }

    val swipeX = remember { Animatable(0f) }
    val swipeY = remember { Animatable(0f) }

    val coroutineScope = rememberCoroutineScope()

    var hasSwiped by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (abs(swipeX.value) > swipeThreshold && !hasSwiped) {
                            val direction = if (swipeX.value > 0) SwipeResult.ACCEPTED else SwipeResult.REJECTED
                            hasSwiped = true
                            coroutineScope.launch {
                                swipeX.animateTo(if (swipeX.value > 0) screenWidth.toPx() else -screenWidth.toPx())
                                onSwiped(direction, item)
                                swipeX.snapTo(0f)
                                swipeY.snapTo(0f)
                                hasSwiped = false
                            }
                        } else {
                            coroutineScope.launch {
                                swipeX.animateTo(0f, tween(300))
                                swipeY.animateTo(0f, tween(300))
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            swipeX.snapTo(swipeX.value + dragAmount.x)
                            swipeY.snapTo(swipeY.value + dragAmount.y)
                        }
                    }
                )
            }
            .graphicsLayer {
                translationX = swipeX.value
                translationY = swipeY.value
                rotationZ = (swipeX.value / 60).coerceIn(-40f, 40f)
            }
            .clip(RoundedCornerShape(16.dp))
    ) {
        content()
    }
}
