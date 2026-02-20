package com.alilopez.kt_demohilt.features.issues.domain.entities

import kotlin.time.Duration

data class Issue (
    val id: Long,
    val name: String,
    val status: String,
    val description: String,
    val timeToDo: Duration,
    val currentTime: Duration,
    val leftTime: Duration
)