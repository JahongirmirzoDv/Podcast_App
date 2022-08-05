package com.mobiledv.podcastapp.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.google.accompanist.insets.ProvideWindowInsets
import com.mobiledv.podcastapp.constant.K
import com.mobiledv.podcastapp.ui.common.ProvideMultiViewModel
import com.mobiledv.podcastapp.ui.entry.EntryScreen
import com.mobiledv.podcastapp.ui.home.HomeScreen
import com.mobiledv.podcastapp.ui.navigation.Destination
import com.mobiledv.podcastapp.ui.navigation.Navigator
import com.mobiledv.podcastapp.ui.navigation.ProvideNavHostController
import com.mobiledv.podcastapp.ui.podcast.PodcastBottomBar
import com.mobiledv.podcastapp.ui.podcast.PodcastDetailScreen
import com.mobiledv.podcastapp.ui.podcast.PodcastPlayerScreen
import com.mobiledv.podcastapp.ui.theme.PodcastAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        var startDestination = Destination.welcome
        if (intent?.action == K.ACTION_PODCAST_NOTIFICATION_CLICK) {
            startDestination = Destination.home
        }

        setContent {
            PodcastApp(
                startDestination = startDestination,
                backDispatcher = onBackPressedDispatcher
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PodcastApp(
    startDestination: String = Destination.welcome,
    backDispatcher: OnBackPressedDispatcher
) {
    PodcastAppTheme {
        ProvideWindowInsets {
            ProvideMultiViewModel {
                ProvideNavHostController {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        NavHost(Navigator.current, startDestination) {
                            composable(Destination.welcome) { EntryScreen() }

                            composable(Destination.home) {
                                HomeScreen()
                            }

                            composable(
                                Destination.podcast,
                                deepLinks = listOf(navDeepLink { uriPattern = "https://www.listennotes.com/e/{id}" })
                            ) { backStackEntry ->
                                PodcastDetailScreen(
                                    podcastId = backStackEntry.arguments?.getString("id")!!,
                                )
                            }
                        }
                        PodcastBottomBar(
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                        PodcastPlayerScreen(backDispatcher)
                    }
                }
            }
        }
    }
}
