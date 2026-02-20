package com.alilopez.kt_demohilt.features.issues.domain.usecases

import com.alilopez.kt_demohilt.features.issues.domain.repositories.IssuesRepository
import javax.inject.Inject

class DeleteIssueUseCase @Inject constructor(private val repository: IssuesRepository) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return try {
            repository.deleteIssue(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}