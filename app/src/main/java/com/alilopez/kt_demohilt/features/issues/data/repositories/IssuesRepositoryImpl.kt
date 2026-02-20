package com.alilopez.kt_demohilt.features.issues.data.repositories

import com.alilopez.kt_demohilt.features.issues.data.datasources.remote.api.IssuesApi
import com.alilopez.kt_demohilt.features.issues.data.datasources.remote.mappers.toDomain
import com.alilopez.kt_demohilt.features.issues.data.datasources.remote.models.IssueRequest
import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue
import com.alilopez.kt_demohilt.features.issues.domain.repositories.IssuesRepository
import javax.inject.Inject
import kotlin.time.Duration

class IssuesRepositoryImpl @Inject constructor(
    private val issuesApi: IssuesApi
) : IssuesRepository {

    override suspend fun getIssues(): List<Issue> {
        return issuesApi.getIssues().map { it.toDomain() }
    }

    override suspend fun createIssue(
        name: String,
        description: String,
        status: String,
        timeToDo: Duration
    ): Issue {
        val request = IssueRequest(
            name = name,
            status = status,
            description = description,
            timeToDo = timeToDo.toIsoString(),
            currentTime = Duration.ZERO.toIsoString()
        )
        return issuesApi.createIssue(request).toDomain()
    }

    override suspend fun updateIssue(
        id: Long,
        name: String,
        description: String,
        status: String,
        timeToDo: Duration,
        currentTime: Duration
    ): Issue {
        val request = IssueRequest(
            name = name,
            status = status,
            description = description,
            timeToDo = timeToDo.toIsoString(),
            currentTime = currentTime.toIsoString()
        )
        return issuesApi.updateIssue(id, request).toDomain()
    }

    override suspend fun updateCurrentTime(id: Long, currentTime: Duration): Issue {
        return issuesApi.updateCurrentTime(id, currentTime.toIsoString()).toDomain()
    }

    override suspend fun deleteIssue(id: Long) {
        issuesApi.deleteIssue(id)
    }
}