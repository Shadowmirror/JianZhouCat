package miao.kmirror.jianzhoucat.feature.state

sealed class LoadState {
    object Loading : LoadState()
    object Success : LoadState()
    object Error : LoadState()
}
