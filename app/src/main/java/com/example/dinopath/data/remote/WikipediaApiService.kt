package com.example.dinopath.data.remote

import com.example.dinopath.data.remote.dto.WikipediaPageSummaryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WikipediaApiService {

    @GET("page/summary/{title}")
    suspend fun getPageSummary(
        @Path("title")
        title: String,
    ): WikipediaPageSummaryDto
}