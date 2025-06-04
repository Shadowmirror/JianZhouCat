package miao.kmirror.jianzhoucat.feature.screen.word.page

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import miao.kmirror.jianzhoucat.domin.model.WordModel
import miao.kmirror.jianzhoucat.feature.screen.word.viewmodel.WordViewModel
import miao.kmirror.jianzhoucat.feature.state.LoadState

@Composable
fun WordScreen(
    wordViewModel: WordViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        wordViewModel.initData()
    }

    val uiState by wordViewModel.uiState.collectAsState()

    when (uiState) {
        LoadState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("数据加载有误请重试")
            }
        }

        LoadState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("数据加载中请稍后")
            }
        }

        LoadState.Success -> {
            val currentWordModel by wordViewModel.currentWordModel.collectAsState()

            // 用 currentWordModel 作为 key，实现切换动画
            AnimatedContent(
                targetState = currentWordModel,
                transitionSpec = {
                    (slideInHorizontally { fullWidth -> fullWidth } + fadeIn()).togetherWith(slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut())
                },
                label = "WordCardSwitch"
            ) { word ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    WordTitle(
                        wordModel = word,
                        modifier = Modifier.weight(1f)
                    )
                    WordChoice(
                        onChoiceClick = {
                            wordViewModel.nextWord()
                        }
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}


@Composable
fun WordTitle(
    wordModel: WordModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = wordModel.word,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = wordModel.phoneticSymbol,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = wordModel.translate,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = wordModel.exampleSentence,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun WordChoice(
    onChoiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        val buttonModifier = Modifier
            .fillMaxWidth(320 / 360f)
            .aspectRatio(320 / 50f)

        Box(
            contentAlignment = Alignment.Center,
            modifier = buttonModifier
                .background(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                .clickable { onChoiceClick() }
        ) {
            Text("认识", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primaryContainer)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = buttonModifier
                .background(color = MaterialTheme.colorScheme.secondary, shape = MaterialTheme.shapes.medium)
                .clickable { onChoiceClick() }
        ) {
            Text("模糊", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondaryContainer)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = buttonModifier
                .background(color = MaterialTheme.colorScheme.error, shape = MaterialTheme.shapes.medium)
                .clickable { onChoiceClick() }
        ) {
            Text("忘记", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.errorContainer)
        }
    }
}
