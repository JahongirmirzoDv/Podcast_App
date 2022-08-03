package com.mobiledv.podcastapp.ui.vm

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.mobiledv.podcastapp.constant.K
import com.mobiledv.podcastapp.data.service.MediaPlayerService
import com.mobiledv.podcastapp.data.service.MediaPlayerServiceConnection
import com.mobiledv.podcastapp.domain.model.Episode
import com.mobiledv.podcastapp.util.currentPosition
import com.mobiledv.podcastapp.util.isPlayEnabled
import com.mobiledv.podcastapp.util.isPlaying
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PodcastPlayerViewModel @Inject constructor(
    private val serviceConnection: dagger.Lazy<MediaPlayerServiceConnection>
) : ViewModel() {

    var serviceConnectionGet = serviceConnection.get()
    val currentPlayingEpisode = serviceConnectionGet.currentPlayingEpisode

    var showPlayerFullScreen by mutableStateOf(false)

    var currentPlaybackPosition by mutableStateOf(0L)

    val podcastIsPlaying: Boolean
        get() = playbackState.value?.isPlaying == true

    val currentEpisodeProgress: Float
        get() {
            if (currentEpisodeDuration > 0) {
                return currentPlaybackPosition.toFloat() / currentEpisodeDuration
            }
            return 0f
        }

    val currentPlaybackFormattedPosition: String
        get() = formatLong(currentPlaybackPosition)

    val currentEpisodeFormattedDuration: String
        get() = formatLong(currentEpisodeDuration)

    private val playbackState = serviceConnectionGet.playbackState

    private val currentEpisodeDuration: Long
        get() = MediaPlayerService.currentDuration

    fun playPodcast(episodes: List<Episode>, currentEpisode: Episode) {
        serviceConnectionGet.playPodcast(episodes)
        if (currentEpisode.id == currentPlayingEpisode.value?.id) {
            if (podcastIsPlaying) {
                serviceConnectionGet.transportControls.pause()
            } else {
                serviceConnectionGet.transportControls.play()
            }
        } else {
            serviceConnectionGet.transportControls.playFromMediaId(currentEpisode.id, null)
        }
    }

    fun tooglePlaybackState() {
        when {
            playbackState.value?.isPlaying == true -> {
                serviceConnectionGet.transportControls.pause()
            }

            playbackState.value?.isPlayEnabled == true -> {
                serviceConnectionGet.transportControls.play()
            }
        }
    }

    fun stopPlayback() {
        serviceConnectionGet.transportControls.stop()
    }

    fun calculateColorPalette(drawable: Drawable, onFinised: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { palette ->
            palette?.darkVibrantSwatch?.rgb?.let { colorValue ->
                onFinised(Color(colorValue))
            }
        }
    }

    fun fastForward() {
        serviceConnectionGet.fastForward()
    }

    fun rewind() {
        serviceConnectionGet.rewind()
    }

    /**
     * @param value 0.0 to 1.0
     */
    fun seekToFraction(value: Float) {
        serviceConnectionGet.transportControls.seekTo(
            (currentEpisodeDuration * value).toLong()
        )
    }

    suspend fun updateCurrentPlaybackPosition() {
        val currentPosition = playbackState.value?.currentPosition
        if (currentPosition != null && currentPosition != currentPlaybackPosition) {
            currentPlaybackPosition = currentPosition
        }
        delay(K.PLAYBACK_POSITION_UPDATE_INTERVAL)
        updateCurrentPlaybackPosition()
    }

    private fun formatLong(value: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return dateFormat.format(value)
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnectionGet.unsubscribe(
            K.MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}