package com.mobiledv.podcastapp.data.network.model

data class PodcastSearchDto(
    val count: Long,
    val total: Long,
    val results: List<EpisodeDto>
) {
    fun asDomainModel() = PodcastSearchDto(
        count,
        total,
        results.map { it.asDomainModel() }
    )
}