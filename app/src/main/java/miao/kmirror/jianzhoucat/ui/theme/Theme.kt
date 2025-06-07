package miao.kmirror.jianzhoucat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.materialkolor.rememberDynamicColorScheme

private val lightScheme = lightColorScheme(
    primary = primaryLight, // 主色：用于主按钮、FAB、进度条等
    onPrimary = onPrimaryLight, // 主色上的内容色：按钮文字、图标等
    primaryContainer = primaryContainerLight, // 主色容器：卡片背景、大按钮等
    onPrimaryContainer = onPrimaryContainerLight, // 主色容器上的内容色

    secondary = secondaryLight, // 辅助色：Chip、标签、次级按钮等
    onSecondary = onSecondaryLight, // 辅助色上的内容色
    secondaryContainer = secondaryContainerLight, // 辅助色容器
    onSecondaryContainer = onSecondaryContainerLight, // 辅助容器上的内容色

    tertiary = tertiaryLight, // 第三色：特色强调色、图表、边角装饰
    onTertiary = onTertiaryLight, // 第三色上的内容色
    tertiaryContainer = tertiaryContainerLight, // 第三色容器
    onTertiaryContainer = onTertiaryContainerLight, // 第三容器上的内容色

    error = errorLight, // 错误主色：警告文字、图标、边框
    onError = onErrorLight, // 错误色上的内容色
    errorContainer = errorContainerLight, // 错误背景色：Snackbar、输入框背景
    onErrorContainer = onErrorContainerLight, // 错误容器上的文字色

    background = backgroundLight, // 页面背景色
    onBackground = onBackgroundLight, // 页面背景上的内容色（常规文本）

    surface = surfaceLight, // 内容背景色：卡片、Dialog、底部栏等
    onSurface = onSurfaceLight, // surface 上的文字/图标色

    surfaceVariant = surfaceVariantLight, // 次级内容背景色（有层级差异）
    onSurfaceVariant = onSurfaceVariantLight, // surfaceVariant 上的内容色

    outline = outlineLight, // 边框/分割线颜色
    outlineVariant = outlineVariantLight, // 次级边框/轮廓线颜色

    scrim = scrimLight, // 遮罩颜色（例如弹窗背景遮罩）

    inverseSurface = inverseSurfaceLight, // Snackbar、底部浮层背景色
    inverseOnSurface = inverseOnSurfaceLight, // Snackbar 上的文字/图标色
    inversePrimary = inversePrimaryLight, // 用于强调的反色按钮等

    surfaceDim = surfaceDimLight, // 用于暗背景区域
    surfaceBright = surfaceBrightLight, // 高亮背景区域（如顶部应用栏）

    surfaceContainerLowest = surfaceContainerLowestLight, // 最底层容器（几乎等同白色）
    surfaceContainerLow = surfaceContainerLowLight, // 比背景稍高的容器背景
    surfaceContainer = surfaceContainerLight, // 常规容器背景
    surfaceContainerHigh = surfaceContainerHighLight, // 高层浮层容器背景（Dialog 等）
    surfaceContainerHighest = surfaceContainerHighestLight, // 最高层容器（Modal、菜单）
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)


@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun JianZhouCatTheme(
    themeColor: Color,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = rememberDynamicColorScheme(seedColor = themeColor, isDark = darkTheme)

//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> darkScheme
//        else -> lightScheme
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}