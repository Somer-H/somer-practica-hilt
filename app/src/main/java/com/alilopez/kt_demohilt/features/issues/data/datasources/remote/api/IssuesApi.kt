package com.alilopez.kt_demohilt.features.issues.data.datasources.remote.api

import com.alilopez.kt_demohilt.features.issues.data.datasources.remote.models.IssueRequest
import com.alilopez.kt_demohilt.features.issues.data.datasources.remote.models.IssueResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IssuesApi {
    @GET("issues")
    suspend fun getIssues(): List<IssueResponse>

    @POST("issues")
    suspend fun createIssue(@Body issue: IssueRequest): IssueResponse

    @PUT("issues/{id}")
    suspend fun updateIssue(@Path("id") id: Long, @Body issue: IssueRequest): IssueResponse

    @PATCH("issues/{id}")
    suspend fun updateCurrentTime(@Path("id") id: Long, @Query("currentTime") currentTime: String): IssueResponse

    @DELETE("issues/{id}")
    suspend fun deleteIssue(@Path("id") id: Long)
}