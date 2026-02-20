package com.alilopez.kt_demohilt.features.issues.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue
import com.alilopez.kt_demohilt.features.issues.domain.usecases.CreateIssueUseCase
import com.alilopez.kt_demohilt.features.issues.domain.usecases.DeleteIssueUseCase
import com.alilopez.kt_demohilt.features.issues.domain.usecases.GetIssuesUseCase
import com.alilopez.kt_demohilt.features.issues.domain.usecases.UpdateCurrentTimeUseCase
import com.alilopez.kt_demohilt.features.issues.domain.usecases.UpdateIssueUseCase
import com.alilopez.kt_demohilt.features.issues.presentation.screens.IssuesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class IssuesViewModel @Inject constructor(
    private val getIssuesUseCase: GetIssuesUseCase,
    private val createIssueUseCase: CreateIssueUseCase,
    private val updateIssueUseCase: UpdateIssueUseCase,
    private val updateCurrentTimeUseCase: UpdateCurrentTimeUseCase,
    private val deleteIssueUseCase: DeleteIssueUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(IssuesState())
    val uiState = _uiState.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _selectedStatus = MutableStateFlow("Por Hacer")
    val selectedStatus = _selectedStatus.asStateFlow()

    private val _isDropdownExpanded = MutableStateFlow(false)
    val isDropdownExpanded = _isDropdownExpanded.asStateFlow()

    private val _estimatedHours = MutableStateFlow("0")
    val estimatedHours = _estimatedHours.asStateFlow()

    private val _estimatedMinutes = MutableStateFlow("0")
    val estimatedMinutes = _estimatedMinutes.asStateFlow()

    private val timerJobs = mutableMapOf<Long, Job>()
    private val statusOrder = listOf("En Proceso", "Pospuesta", "Por Hacer")

    init {
        loadIssues()
    }

    fun onTitleChange(title: String) {
        _title.value = title
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onSelectedStatusChange(status: String) {
        _selectedStatus.value = status
        _isDropdownExpanded.value = false
    }

    fun onDropdownExpandedChange(isExpanded: Boolean) {
        _isDropdownExpanded.value = isExpanded
    }

    fun onEstimatedHoursChange(hours: String) {
        _estimatedHours.value = hours
    }

    fun onEstimatedMinutesChange(minutes: String) {
        _estimatedMinutes.value = minutes
    }

    fun loadIssues() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getIssuesUseCase().fold(
                onSuccess = { issues ->
                    val grouped = issues.groupBy { it.status }
                        .toSortedMap(compareBy { statusOrder.indexOf(it) })
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            issues = issues,
                            groupedIssues = grouped,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
            )
        }
    }

    fun onShowModal(issue: Issue? = null) {
        _title.value = issue?.name ?: ""
        _description.value = issue?.description ?: ""
        _selectedStatus.value = issue?.status ?: "Por Hacer"
        _estimatedHours.value = issue?.timeToDo?.inWholeHours?.toString() ?: "0"
        _estimatedMinutes.value = (issue?.timeToDo?.inWholeMinutes?.rem(60))?.toString() ?: "0"
        _uiState.update { it.copy(isShowModal = true, selectedIssue = issue) }
    }

    fun onDismissModal() {
        _uiState.update { it.copy(isShowModal = false, selectedIssue = null) }
    }

    fun createOrUpdateIssue(name: String, description: String, status: String, hours: Int, minutes: Int) {
        viewModelScope.launch {
            val timeToDo = hours.hours + minutes.minutes
            val selectedIssue = _uiState.value.selectedIssue

            val result = if (selectedIssue == null) {
                createIssueUseCase(name, description, status, timeToDo)
            } else {
                updateIssueUseCase(
                    id = selectedIssue.id,
                    name = name,
                    description = description,
                    status = status,
                    timeToDo = timeToDo,
                    currentTime = selectedIssue.currentTime
                )
            }

            result.fold(
                onSuccess = { onDismissModal(); loadIssues() },
                onFailure = { error -> _uiState.update { it.copy(error = error.message) } }
            )
        }
    }

    fun onPlay(issueId: Long) {
        timerJobs[issueId]?.cancel()
        _uiState.update { it.copy(activeTimers = it.activeTimers + issueId) }

        val job = viewModelScope.launch {
            val issue = _uiState.value.issues.find { it.id == issueId } ?: return@launch
            var currentTime = issue.currentTime

            while (currentTime < issue.timeToDo) {
                delay(1000)
                currentTime += 1.seconds
                updateIssueTimeInState(issueId, currentTime)

                if (currentTime >= issue.timeToDo) {
                    deleteIssue(issueId)
                    break
                }
            }
        }
        timerJobs[issueId] = job
    }

    fun onPause(issueId: Long) {
        timerJobs[issueId]?.cancel()
        timerJobs.remove(issueId)
        _uiState.update { it.copy(activeTimers = it.activeTimers - issueId) }

        viewModelScope.launch {
            val issue = _uiState.value.issues.find { it.id == issueId } ?: return@launch
            updateCurrentTimeUseCase(issueId, issue.currentTime)
        }
    }

    private fun updateIssueTimeInState(issueId: Long, newCurrentTime: Duration) {
        _uiState.update { currentState ->
            val updatedIssues = currentState.issues.map { issue ->
                if (issue.id == issueId) issue.copy(currentTime = newCurrentTime) else issue
            }
            val updatedGroupedIssues = updatedIssues.groupBy { it.status }
                .toSortedMap(compareBy { statusOrder.indexOf(it) })
            currentState.copy(issues = updatedIssues, groupedIssues = updatedGroupedIssues)
        }
    }

    fun deleteIssue(issueId: Long) {
        timerJobs[issueId]?.cancel()
        timerJobs.remove(issueId)
        _uiState.update { it.copy(activeTimers = it.activeTimers - issueId) }

        viewModelScope.launch {
            deleteIssueUseCase(issueId).fold(
                onSuccess = { loadIssues() },
                onFailure = { error -> _uiState.update { it.copy(error = error.message) } }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJobs.values.forEach { it.cancel() }
    }
}