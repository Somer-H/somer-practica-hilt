package com.alilopez.kt_demohilt.features.issues.presentation.screens

import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue

data class IssuesState(
    val isLoading: Boolean = false,
    val issues: List<Issue> = emptyList(),
    val error: String? = null,
    val isShowModal: Boolean = false,
    val selectedIssue: Issue? = null,
    val groupedIssues: Map<String, List<Issue>> = emptyMap(),
    val activeTimers: Set<Long> = emptySet()
)
