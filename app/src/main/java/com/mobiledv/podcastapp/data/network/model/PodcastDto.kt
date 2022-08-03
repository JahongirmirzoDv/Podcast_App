package com.mobiledv.podcastapp.data.network.model

import com.google.gson.annotations.SerializedName

data class PodcastDto(
    val id: String,
    val image: String,
    val thumbnail: String,
    @SerializedName("title_original")
    val titleOriginal: String,
    @SerializedName("listennotes_url")
    val listennotesURL: String,
    @SerializedName("publisher_original")
    val publisherOriginal: String
){
    fun asDomainModel() = PodcastDto(
        id,
        image,
        thumbnail,
        titleOriginal,
        listennotesURL,
        publisherOriginal
    )
}
