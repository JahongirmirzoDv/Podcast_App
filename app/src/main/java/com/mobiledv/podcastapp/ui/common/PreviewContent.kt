package com.mobiledv.podcastapp.ui.common

import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets
import com.mobiledv.podcastapp.ui.navigation.ProvideNavHostController
import com.mobiledv.podcastapp.ui.theme.PodcastAppTheme

@Composable
fun PreviewContent(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    PodcastAppTheme(darkTheme = darkTheme) {
        ProvideWindowInsets {
            ProvideNavHostController {
                content()
            }
        }
    }
}