package com.mobiledv.podcastapp.ui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabirt.podcastapp.domain.model.PodcastSearch
import com.mobiledv.podcastapp.domain.model.Episode
import com.mobiledv.podcastapp.domain.repository.PodcastRepository
import com.mobiledv.podcastapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastSearchViewModel @Inject constructor(
    private val repository: dagger.Lazy<PodcastRepository>
) : ViewModel() {

    val repositoryGet = repository.get()
    var podcastSearch by mutableStateOf<Resource<PodcastSearch>>(Resource.Loading)
        private set

    init {
        searchPodcasts()
    }

    fun getPodcastDetail(id: String): Episode? {
        return when (podcastSearch) {
            is Resource.Error -> null
            Resource.Loading -> null
            is Resource.Success -> (podcastSearch as Resource.Success<PodcastSearch>).data.results.find { it.id == id }
        }
    }

    fun searchPodcasts() {
        viewModelScope.launch(Dispatchers.IO) {
            podcastSearch = Resource.Loading
            val result = repositoryGet.searchPodcasts("fiction", "episode")
            result.fold(
                { failure ->
                    podcastSearch = Resource.Error(failure)
                },
                { data ->
                    podcastSearch = Resource.Success(data)
                }
            )
        }
    }
}