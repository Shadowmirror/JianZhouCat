package miao.kmirror.jianzhoucat.feature.component

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun HsvColorPicker(
    modifier: Modifier = Modifier,
    initialColor: Color = Color.Red,
    onColorChanged: (Color) -> Unit,
) {
    val context = LocalContext.current
    val bitmapSize = 720
    val cacheFile = remember { File(context.cacheDir, "hsv_color_wheel_$bitmapSize.png") }
    var isBitmapReady by remember { mutableStateOf(cacheFile.exists()) }

    LaunchedEffect(cacheFile.exists()) {
        if (!cacheFile.exists()) {
            withContext(Dispatchers.IO) {
                createColorWheelBitmap(bitmapSize, cacheFile)
            }
            isBitmapReady = true
        }
    }

    if (!isBitmapReady) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val initialHsv = remember(initialColor) {
        FloatArray(3).apply {
            android.graphics.Color.colorToHSV(initialColor.toArgb(), this)
        }
    }

    val hue = remember { mutableFloatStateOf(initialHsv[0].coerceIn(0f, 360f)) }
    val saturation = remember { mutableFloatStateOf(initialHsv[1].coerceIn(0f, 1f)) }
    val radius = remember { mutableStateOf(0f) }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectTapAndDragGestures { offset ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val dx = offset.x - center.x
                    val dy = offset.y - center.y
                    val distance = hypot(dx, dy)
                    val maxRadius = radius.value

                    val sat = (distance / maxRadius).coerceIn(0f, 1f)
                    val angle = (atan2(dy, dx) * 180f / PI).toFloat()
                    val hueValue = (angle + 360f) % 360f

                    hue.floatValue = hueValue
                    saturation.floatValue = sat

                    onColorChanged(Color.hsv(hueValue, sat, 1f))
                }
            }
            .onSizeChanged {
                radius.value = it.width / 2f
            }
    ) {
        // 背景色轮使用 Image 显示
        AsyncImage(
            model = cacheFile,
            contentDescription = "HSV Color Wheel",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        // 指示器使用 Canvas 绘制
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val outerRadius = size.width / 2f

            val angleRad = Math.toRadians(hue.floatValue.toDouble())
            val selectorRadius = saturation.floatValue * outerRadius
            val selectorX = center.x + selectorRadius * cos(angleRad).toFloat()
            val selectorY = center.y + selectorRadius * sin(angleRad).toFloat()
            val indicatorRadius = outerRadius * 0.08f

            drawCircle(
                color = Color.hsv(hue.floatValue, saturation.floatValue, 1f),
                center = Offset(selectorX, selectorY),
                radius = indicatorRadius,
            )

            drawCircle(
                color = Color.White,
                center = Offset(selectorX, selectorY),
                radius = indicatorRadius,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}


private suspend fun createColorWheelBitmap(size: Int, cacheFile: File): Boolean {
    // 创建 bitmap
    val bmp = createBitmap(size, size)
    val center = size / 2f
    val radius = center

    for (y in 0 until size) {
        for (x in 0 until size) {
            val dx = x - center
            val dy = y - center
            val dist = hypot(dx, dy)

            val color = if (dist <= radius) {
                val saturation = (dist / radius).coerceIn(0f, 1f)
                val angle = ((atan2(dy, dx) * 180 / Math.PI) + 360) % 360
                android.graphics.Color.HSVToColor(
                    floatArrayOf(angle.toFloat(), saturation, 1f)
                )
            } else {
                Color.Transparent.toArgb()
            }

            bmp.setPixel(x, y, color)
        }
    }

    // 缓存为 PNG 文件
    try {
        FileOutputStream(cacheFile).use { out ->
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return cacheFile.exists()
}


@Composable
fun HsvColorPickerSquare(
    modifier: Modifier = Modifier,
    initialColor: Color = Color.Red,
    onColorChanged: (Color) -> Unit
) {
    val context = LocalContext.current
    val bitmapSize = 720
    val cacheFile = remember { File(context.cacheDir, "hsv_color_square_$bitmapSize.png") }
    var isBitmapReady by remember { mutableStateOf(cacheFile.exists()) }

    LaunchedEffect(cacheFile.exists()) {
        if (!cacheFile.exists()) {
            withContext(Dispatchers.IO) {
                createSquareColorMap(bitmapSize, cacheFile)
            }
            isBitmapReady = true
        }
    }

    if (!isBitmapReady) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val initialHsv = remember(initialColor) {
        FloatArray(3).apply {
            android.graphics.Color.colorToHSV(initialColor.toArgb(), this)
        }
    }

    val hue = remember { mutableFloatStateOf(initialHsv[0].coerceIn(0f, 360f)) }
    val saturation = remember { mutableFloatStateOf(initialHsv[1].coerceIn(0f, 1f)) }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectTapAndDragGestures { offset ->
                    val hueValue = (offset.x / size.width * 360f).coerceIn(0f, 360f)
                    val satValue = (offset.y / size.height).coerceIn(0f, 1f)

                    hue.floatValue = hueValue
                    saturation.floatValue = satValue

                    onColorChanged(Color.hsv(hueValue, satValue, 1f))
                }
            }
    ) {
        AsyncImage(
            model = cacheFile,
            contentDescription = "HSV Square Picker",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val selectorX = (hue.floatValue / 360f) * size.width
            val selectorY = saturation.floatValue * size.height
            val indicatorRadius = size.minDimension * 0.04f

            drawCircle(
                color = Color.hsv(hue.floatValue, saturation.floatValue, 1f),
                center = Offset(selectorX, selectorY),
                radius = indicatorRadius
            )
            drawCircle(
                color = Color.White,
                center = Offset(selectorX, selectorY),
                radius = indicatorRadius,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

private suspend fun createSquareColorMap(size: Int, cacheFile: File): Boolean {
    val bmp = createBitmap(size, size)

    for (y in 0 until size) {
        val sat = y.toFloat() / size
        for (x in 0 until size) {
            val hue = x.toFloat() / size * 360f
            val color = android.graphics.Color.HSVToColor(floatArrayOf(hue, sat, 1f))
            bmp.setPixel(x, y, color)
        }
    }

    return try {
        FileOutputStream(cacheFile).use { out ->
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        cacheFile.exists()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * 用于监听单次点击 + 拖动手势，并传出手指在画布上的位置
 * 传入 onInput 回调函数会在手势开始和每次移动时被调用
 */
private suspend fun PointerInputScope.detectTapAndDragGestures(onInput: (Offset) -> Unit) {
    // 每个手势循环，确保不会丢事件
    awaitEachGesture {
        // 等待首次按下，记录触点位置
        val down = awaitFirstDown()
        onInput(down.position)

        // 拖动时持续监听位置更新
        drag(down.id) { change ->
            onInput(change.position)
            change.consume() // 消费事件，防止冲突
        }
    }
}
