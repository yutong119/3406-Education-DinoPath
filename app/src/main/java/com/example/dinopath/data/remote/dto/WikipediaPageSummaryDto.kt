package com.example.dinopath.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WikipediaPageSummaryDto(

    @Json(name = "title")
    val title: String,

    @Json(name = "extract")
    val extract: String?,

    @Json(name = "thumbnail")
    val thumbnail: WikipediaThumbnailDto?,
)

@JsonClass(generateAdapter = true)
data class WikipediaThumbnailDto(

    @Json(name = "source")
    val source: String?,

    @Json(name = "width")
    val width: Int?,

    @Json(name = "height")
    val height: Int?,
)