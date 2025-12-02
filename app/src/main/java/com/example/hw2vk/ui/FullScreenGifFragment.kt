package com.example.hw2vk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.hw2vk.data.GifItem
import com.google.gson.Gson

class FullScreenGifFragment : Fragment() {

    companion object {
        private const val ARG_GIF = "gif"

        fun newInstance(gif: GifItem): FullScreenGifFragment {
            return FullScreenGifFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_GIF, Gson().toJson(gif))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MaterialTheme {
                    val gifJson = arguments?.getString(ARG_GIF)
                    val gif = Gson().fromJson(gifJson, GifItem::class.java)

                    FullScreenGifScreen(
                        gif = gif,
                        onBackPressed = {
                            parentFragmentManager.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FullScreenGifScreen(
    gif: GifItem?,
    onBackPressed: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(onClick = onBackPressed),
        contentAlignment = Alignment.Center
    ) {
        gif?.let {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it.images.original.url)
                    .decoderFactory(GifDecoder.Factory())
                    .build(),
                contentDescription = "GIF",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .statusBarsPadding()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Назад",
                tint = Color.White
            )
        }
    }
}