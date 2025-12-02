package com.example.hw2vk.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.hw2vk.R
import com.example.hw2vk.data.GifItem
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifListScreen(
    viewModel: GifViewModel = viewModel(),
    onGifClick: (GifItem) -> Unit = {}
) {
    val gifs by viewModel.gifs
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (gifs.isEmpty()) viewModel.loadGifs()
    }

    when {
        error != null && gifs.isEmpty() ->
            ErrorScreen(error = error!!, onRetry = { viewModel.retry() })

        gifs.isEmpty() ->
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

        else ->
            MasonryGifGrid(
                items = gifs,
                onLoadMore = {
                    if (!isLoading && error == null) viewModel.loadMore()
                },
                itemContent = { index, gif ->
                    GifCard(
                        gif = gif,
                        onClick = {
                            Toast.makeText(
                                context,
                                context.getString(R.string.gif_number, index + 1),
                                Toast.LENGTH_SHORT
                            ).show()
                            onGifClick(gif)
                        }
                    )
                }
            )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> MasonryGifGrid(
    items: List<T>,
    onLoadMore: () -> Unit,
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    val gridState = rememberLazyStaggeredGridState()
    LaunchedEffect(gridState) {
        snapshotFlow {
            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.distinctUntilChanged()
            .collect { last ->
                if (last != null && last >= items.size - 3) {
                    onLoadMore()
                }
            }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(integerResource(id = R.integer.grid_column_count)),
        state = gridState,
        verticalItemSpacing = dimensionResource(id = R.dimen.grid_spacing),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.grid_spacing)),
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.grid_padding)),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items.size) { index ->
            itemContent(index, items[index])
        }
    }
}

@Composable
fun GifCard(
    gif: GifItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(dimensionResource(id = R.dimen.card_elevation)),
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
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ErrorScreen(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_xxlarge)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.error),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_large)))

        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_xxlarge)))

        Button(
            onClick = onRetry,
            modifier = Modifier.height(dimensionResource(id = R.dimen.button_height))
        ) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}