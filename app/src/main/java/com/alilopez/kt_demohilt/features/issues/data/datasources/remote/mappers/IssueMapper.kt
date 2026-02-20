package com.alilopez.kt_demohilt.features.issues.data.datasources.remote.mappers

import com.alilopez.kt_demohilt.features.issues.data.datasources.remote.models.IssueResponse
import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue
import kotlin.time.Duration

fun IssueResponse.toDomain(): Issue {
    return Issue(
        id = this.id,
        name = this.name,
        status = this.status,
        description = this.description,
        timeToDo = Duration.parse(this.timeToDo),
        currentTime = Duration.parse(this.currentTime),
        leftTime = Duration.parse(this.leftTime)
    )
}