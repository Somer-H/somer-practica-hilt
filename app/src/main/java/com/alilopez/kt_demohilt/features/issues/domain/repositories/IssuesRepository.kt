package com.alilopez.kt_demohilt.features.issues.domain.repositories

import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue
import kotlin.time.Duration

interface IssuesRepository {
    suspend fun getIssues(): List<Issue>
    suspend fun createIssue(name: String, description: String, status: String, timeToDo: Duration): Issue
    suspend fun updateIssue(id: Long, name: String, description: String, status: String, timeToDo: Duration, currentTime: Duration): Issue
    suspend fun updateCurrentTime(id: Long, currentTime: Duration): Issue
    suspend fun deleteIssue(id: Long)
}