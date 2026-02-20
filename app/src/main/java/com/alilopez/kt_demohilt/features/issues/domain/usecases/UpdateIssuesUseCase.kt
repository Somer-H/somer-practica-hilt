package com.alilopez.kt_demohilt.features.issues.domain.usecases

import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue
import com.alilopez.kt_demohilt.features.issues.domain.repositories.IssuesRepository
import javax.inject.Inject
import kotlin.time.Duration

class UpdateIssueUseCase @Inject constructor(private val repository: IssuesRepository) {
    suspend operator fun invoke(id: Long, name: String, description: String, status: String, timeToDo: Duration, currentTime: Duration): Result<Issue> {
        return try {
            val issue = repository.updateIssue(id, name, description, status, timeToDo, currentTime)
            Result.success(issue)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}