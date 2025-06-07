package miao.kmirror.jianzhoucat.feature.screen.setting.page

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import miao.kmirror.jianzhoucat.feature.component.HsvColorPicker
import miao.kmirror.jianzhoucat.feature.screen.main.page.MainActivity
import miao.kmirror.jianzhoucat.feature.screen.main.viewmodel.MainAtyViewModel

@SuppressLint("ContextCastToActivity")
@Preview(showBackground = true)
@Composable
fun SettingScreen() {
    val activity = LocalContext.current as MainActivity
    val mainAtyViewModel: MainAtyViewModel = hiltViewModel(activity)

    var isShowChangeThemeColorDialog by remember { mutableStateOf(false) }
    if (isShowChangeThemeColorDialog) {
        ChangeThemeColorDialog(
            onDismiss = {},
            dismissDialog = { isShowChangeThemeColorDialog = false },
            initColor = mainAtyViewModel.getThemeColor(),
            saveColor = {
                mainAtyViewModel.setThemeColor(it)
            },
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "设置页",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                CardItem(text = "更改主题色") {
                    isShowChangeThemeColorDialog = true
                }
                Spacer(modifier = Modifier.height(20.dp))
                CardItem(text = "更改当前用户") {
                    mainAtyViewModel.changeCurrentUser()
                }
            }
        }
    }
}

@Preview()
@Composable
private fun CardItem(
    text: String = "设置页 Item",
    onClick: () -> Unit = {}
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun ChangeThemeColorDialog(
    initColor: Color = Color.Red,
    dismissDialog: () -> Unit = {},
    onDismiss: () -> Unit = {},
    saveColor: (Color) -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true
        )
    ) {

        var rememberColor by remember { mutableStateOf(initColor) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.large)
        ) {
            Row(

                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(10.dp)
                        .clickable {
                            dismissDialog()
                        }
                )
            }
            HsvColorPicker(
                initialColor = rememberColor,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                rememberColor = it
            }

            Spacer(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = rememberColor, shape = MaterialTheme.shapes.large)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    saveColor(rememberColor)
                },
            ) {
                Text("更改颜色")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HsvColorPickerPreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .aspectRatio(1f)
        ) { color ->
            Log.i("KmirrorTag", "HsvColorPickerPreview: color alpha = ${color.alpha}, red = ${color.red}, green = ${color.green}, blue = ${color.blue}")
        }
    }
}