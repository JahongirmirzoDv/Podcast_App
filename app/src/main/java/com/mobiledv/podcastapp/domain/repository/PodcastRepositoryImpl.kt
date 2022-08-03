package com.mobiledv.podcastapp.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.fabirt.podcastapp.domain.model.PodcastSearch
import com.mobiledv.podcastapp.data.datastore.PodcastDataStore
import com.mobiledv.podcastapp.data.network.service.PodcastService
import com.mobiledv.podcastapp.error.Failure
import com.mobiledv.podcastapp.util.Either
import com.mobiledv.podcastapp.util.left
import com.mobiledv.podcastapp.util.right

class PodcastRepositoryImpl(
    private val service: PodcastService,
    private val dataStore: PodcastDataStore
) : PodcastRepository {

    companion object {
        private const val TAG = "PodcastRepository"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun searchPodcasts(
        query: String,
        type: String
    ): Either<Failure, PodcastSearch> {
        return try {
            val canFetchAPI = dataStore.canFetchAPI()
            if (canFetchAPI) {
                val result = service.searchPodcasts(query, type).asDomainModel()
                dataStore.storePodcastSearchResult(result)
                right(result)
                left(Failure.UnexpectedFailure)
            } else {
                right(dataStore.readLastPodcastSearchResult())
            }
        } catch (e: Exception) {
            left(Failure.UnexpectedFailure)
        }
    }
}