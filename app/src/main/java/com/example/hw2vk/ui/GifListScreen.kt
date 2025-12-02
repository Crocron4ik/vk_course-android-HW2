package com.example.hw2vk.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.hw2vk.data.GifItem
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun GifListScreen(viewModel: GifViewModel = viewModel()) {
    val gifs by viewModel.gifs
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.distinctUntilChanged().collect { index ->
            if (index != null && index >= gifs.size - 3 && !isLoading && error == null) {
                viewModel.loadMore()
            }
        }
    }

    LaunchedEffect(Unit) {
        if (gifs.isEmpty()) {
            viewModel.loadGifs()
        }
    }

    Box(Modifier.fillMaxSize()) {
        when {
            error != null && gifs.isEmpty() ->
                ErrorScreen(error = error!!, onRetry = { viewModel.retry() })

            gifs.isEmpty() ->
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }

            else -> LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(gifs) { index, gif ->
                    GifCard(
                        gif = gif,
                        index = index,
                        onClick = {
                            Toast.makeText(
                                context,
                                "GIF №${index + 1}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }

                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GifCard(gif: GifItem, index: Int, onClick: () -> Unit) {
    val aspectRatio = remember(gif.id) {
        val width = gif.images.fixed_height.width.toFloatOrNull() ?: 200f
        val height = gif.images.fixed_height.height.toFloatOrNull() ?: 200f
        if (width > 0) width / height else 1f // width/height для aspectRatio
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(gif.images.fixed_height.url)
                .decoderFactory(GifDecoder.Factory())
                .crossfade(true)
                .build(),
            contentDescription = gif.title,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ErrorScreen(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ошибка",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}