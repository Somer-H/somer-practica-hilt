package com.alilopez.kt_demohilt.features.issues.domain.usecases

import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue
import com.alilopez.kt_demohilt.features.issues.domain.repositories.IssuesRepository
import javax.inject.Inject

class GetIssuesUseCase @Inject constructor(private val repository: IssuesRepository) {
    suspend operator fun invoke(): Result<List<Issue>> {
        return try {
            val issues = repository.getIssues()
            Result.success(issues)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}