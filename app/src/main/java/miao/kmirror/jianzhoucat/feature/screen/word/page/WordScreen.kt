package miao.kmirror.jianzhoucat.feature.screen.word.page

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
                    modifier = Modifier.fillMaxSize(),
                ) {
                    WordTitle(
                        wordModel = word,
                        modifier = Modifier.fillMaxWidth(),
                        speakWord = { wordViewModel.speakWord() }
                    )
                    WordSpaceLine()
                    WordContent(
                        wordModel = word, modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    WordSpaceLine()
                    WordChoice(
                        onChoiceClick = {
                            wordViewModel.nextWord()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun WordScreenContentPreview() {
    val wordModel = WordModel.getMock()
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        WordTitle(
            wordModel = wordModel,
            modifier = Modifier.fillMaxWidth()
        )
        WordSpaceLine()
        WordContent(
            wordModel = wordModel, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        WordSpaceLine()
        WordChoice(
            onChoiceClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@Composable
fun WordTitle(
    wordModel: WordModel = WordModel.getMock(),
    speakWord: () -> Unit = {},
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = wordModel.word,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(11.dp))
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                wordModel.phoneticSymbol?.let {
                    Text(
                        text = wordModel.phoneticSymbol,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(12.dp)
                        .clickable {
                            speakWord()
                        }
                )
            }
            Text(
                text = wordModel.translate,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 30.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WordTitlePreview() {
    Box(modifier = Modifier.fillMaxWidth()) {
        WordTitle(modifier = Modifier.fillMaxWidth())
    }
}


@Composable
private fun WordContent(
    wordModel: WordModel = WordModel.getMock(),
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(20.dp)
    ) {
        if (wordModel.exampleSentence.isNullOrEmpty()) {
            item {
                Text(
                    text = "没有例句哦",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            itemsIndexed(wordModel.exampleSentence) { index, item ->
                Text(
                    text = item.english,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = item.chinese,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordContentPreview() {
    Column(modifier = Modifier.fillMaxWidth()) {
        WordContent(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@Composable
private fun WordChoice(
    onChoiceClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 51.dp)
                .background(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                .clickable { onChoiceClick() }
        ) {
            Text("认识", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primaryContainer)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .heightIn(min = 51.dp)
                    .weight(1f)
                    .background(color = MaterialTheme.colorScheme.secondary, shape = MaterialTheme.shapes.medium)
                    .clickable { onChoiceClick() }
            ) {
                Text("模糊", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondaryContainer)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .heightIn(min = 51.dp)
                    .weight(1f)
                    .background(color = MaterialTheme.colorScheme.error, shape = MaterialTheme.shapes.medium)
                    .clickable { onChoiceClick() }
            ) {
                Text("忘记", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.errorContainer)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WordChoicePreview() {
    Box(modifier = Modifier.fillMaxWidth()) {
        WordChoice(
            onChoiceClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun WordSpaceLine() {
    Spacer(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline)
    )
}


@Preview(showBackground = true)
@Composable
private fun WordSpaceLinePreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        WordSpaceLine()
    }
}

