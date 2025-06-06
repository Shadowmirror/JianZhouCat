package miao.kmirror.jianzhoucat.feature.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin


/**
 * HSV 色轮颜色选择器
 *
 * @param initialColor 初始颜色
 * @param onColorChanged 每次用户点击或拖动选中颜色时触发
 */
@Composable
fun HsvColorPicker(
    modifier: Modifier = Modifier,
    initialColor: Color = Color.Red,
    onColorChanged: (Color) -> Unit
) {

    val bitmapSize = 1024

    // 缓存色盘图像，生成圆形的 HSV 色轮 Bitmap
    val bitmap = remember(bitmapSize) {
        val bmp = createBitmap(bitmapSize, bitmapSize)
        val center = bitmapSize / 2f
        val radius = center

        // 遍历像素生成色轮：圆形区域内的每个像素根据极坐标计算其颜色
        for (y in 0 until bitmapSize) {
            for (x in 0 until bitmapSize) {
                val dx = x - center
                val dy = y - center
                val dist = hypot(dx, dy)

                if (dist <= radius) {
                    // 饱和度 = 距离 / 半径（越远越鲜艳）
                    val saturation = (dist / radius).coerceIn(0f, 1f)
                    // 色相角度 = atan2(dy, dx) 转成角度制再转为 [0, 360)
                    val angle = ((atan2(dy, dx) * 180 / Math.PI) + 360) % 360
                    val color = android.graphics.Color.HSVToColor(
                        floatArrayOf(angle.toFloat(), saturation, 1f)
                    )
                    bmp[x, y] = color
                } else {
                    // 超出圆外的部分设为透明
                    bmp[x, y] = Color.Transparent.toArgb()
                }
            }
        }

        // 转为 Compose 使用的 ImageBitmap
        bmp.asImageBitmap()
    }

    // 将初始颜色转换为 HSV 数组
    val initialHsv = remember(initialColor) {
        FloatArray(3).apply {
            android.graphics.Color.colorToHSV(initialColor.toArgb(), this)
        }
    }

    // 色相（Hue），范围：0~360
    val hue = remember { mutableFloatStateOf(initialHsv[0].coerceIn(0f, 360f)) }
    // 饱和度（Saturation），范围：0~1
    val saturation = remember { mutableFloatStateOf(initialHsv[1].coerceIn(0f, 1f)) }
    // 当前圆盘的半径（从 onSizeChanged 设置）
    val radius = remember { mutableStateOf(0f) }

    // 包裹整个色轮和指示器的容器
    Box(
        modifier = modifier
            .aspectRatio(1f) // 保持宽高比为 1，确保圆形
            .pointerInput(Unit) {
                // 自定义手势识别，处理点击和拖动
                detectTapAndDragGestures { offset ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val dx = offset.x - center.x
                    val dy = offset.y - center.y
                    val distance = hypot(dx, dy)
                    val maxRadius = radius.value

                    // 饱和度由距离决定，归一化为 0~1
                    val sat = (distance / maxRadius).coerceIn(0f, 1f)
                    // 色相角度，由触点与中心形成的角度计算得出
                    val angle = (atan2(dy, dx) * 180f / PI).toFloat()
                    val hueValue = (angle + 360f) % 360f

                    hue.floatValue = hueValue
                    saturation.floatValue = sat

                    // 构造颜色并回调
                    onColorChanged(Color.hsv(hueValue, sat, 1f))
                }
            }
            .onSizeChanged {
                // 组件尺寸变更时记录半径
                radius.value = it.width / 2f
            }
    ) {
        // 画布绘制色盘 + 拾色器指示器
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val outerRadius = size.width / 2f

            // 绘制 HSV 色轮图片
            drawImage(
                image = bitmap,
                srcSize = IntSize(bitmap.width, bitmap.height),
                dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                dstOffset = IntOffset(0, 0),
                filterQuality = FilterQuality.High
            )

            // 计算当前选择的颜色点位置（极坐标转直角坐标）
            val angleRad = Math.toRadians(hue.floatValue.toDouble())
            val selectorRadius = saturation.floatValue * outerRadius
            val selectorX = center.x + selectorRadius * cos(angleRad).toFloat()
            val selectorY = center.y + selectorRadius * sin(angleRad).toFloat()
            val indicatorRadius = outerRadius * 0.08f

            // 内部圆：当前颜色的预览
            drawCircle(
                color = Color.hsv(hue.floatValue, saturation.floatValue, 1f),
                center = Offset(selectorX, selectorY),
                radius = indicatorRadius,
            )

            // 外边框：白色圆圈框住当前颜色
            drawCircle(
                color = Color.White,
                center = Offset(selectorX, selectorY),
                radius = indicatorRadius,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}


/**
 * 用于监听单次点击 + 拖动手势，并传出手指在画布上的位置
 * 传入 onInput 回调函数会在手势开始和每次移动时被调用
 */
suspend fun PointerInputScope.detectTapAndDragGestures(onInput: (Offset) -> Unit) {
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