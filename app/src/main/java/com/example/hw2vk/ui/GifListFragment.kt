package com.example.hw2vk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import com.example.hw2vk.R
import androidx.lifecycle.ViewModelProvider

class GifListFragment : Fragment() {

    private val viewModel: GifViewModel by viewModels {
        GifViewModelFactory(requireContext().applicationContext as android.app.Application)
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
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        GifListScreen(
                            viewModel = viewModel,
                            onGifClick = { gif ->
                                val fragment = FullScreenGifFragment.newInstance(gif)
                                parentFragmentManager.commit {
                                    add(R.id.fragment_container, fragment)
                                    addToBackStack(null)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// Фабрика для ViewModel
class GifViewModelFactory(
    private val application: android.app.Application
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GifViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GifViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}