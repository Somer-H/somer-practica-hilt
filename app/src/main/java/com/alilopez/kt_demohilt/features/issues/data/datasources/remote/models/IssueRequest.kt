package com.alilopez.kt_demohilt.features.issues.data.datasources.remote.models

data class IssueRequest(
    val name: String,
    val status: String,
    val description: String,
    val timeToDo: String,
    val currentTime: String
)
