package com.alilopez.kt_demohilt.features.issues.domain.usecases

import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue
import com.alilopez.kt_demohilt.features.issues.domain.repositories.IssuesRepository
import javax.inject.Inject
import kotlin.time.Duration

class CreateIssueUseCase @Inject constructor(private val repository: IssuesRepository) {
    suspend operator fun invoke(name: String, description: String, status: String, timeToDo: Duration): Result<Issue> {
        return try {
            val issue = repository.createIssue(name, description, status, timeToDo)
            Result.success(issue)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}