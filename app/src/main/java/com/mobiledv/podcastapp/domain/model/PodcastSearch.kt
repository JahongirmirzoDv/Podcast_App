package com.fabirt.podcastapp.domain.model

import com.mobiledv.podcastapp.domain.model.Episode

data class PodcastSearch(
    val count: Long,
    val total: Long,
    val results: List<Episode>
)