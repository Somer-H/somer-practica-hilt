package com.alilopez.kt_demohilt.features.issues.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alilopez.kt_demohilt.features.issues.presentation.components.IssueCard
import com.alilopez.kt_demohilt.features.issues.presentation.components.ModalComponent
import com.alilopez.kt_demohilt.features.issues.presentation.viewmodel.IssuesViewModel


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun IssuesScreen(viewModel: IssuesViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val selectedStatus by viewModel.selectedStatus.collectAsStateWithLifecycle()
    val isDropdownExpanded by viewModel.isDropdownExpanded.collectAsStateWithLifecycle()
    val estimatedHours by viewModel.estimatedHours.collectAsStateWithLifecycle()
    val estimatedMinutes by viewModel.estimatedMinutes.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onShowModal() }) {
                Icon(Icons.Default.Add, contentDescription = "Create Issue")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                Text(text = "Error: ${state.error}", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    item {
                        Text(
                            text = "Mis Tareas",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${state.issues.size} tareas totales",
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    state.groupedIssues.forEach { (status, issues) ->
                        item {
                            Text(
                                text = "$status (${issues.size})",
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(issues, key = { it.id }) { issue ->
                            IssueCard(
                                issue = issue,
                                onDelete = viewModel::deleteIssue,
                                onEdit = { viewModel.onShowModal(it) },
                                onPlay = viewModel::onPlay,
                                onPause = viewModel::onPause,
                                isTimerActive = state.activeTimers.contains(issue.id)
                            )
                        }
                    }
                }
            }

            if (state.isShowModal) {
                ModalComponent(
                    issue = state.selectedIssue,
                    title = title,
                    description = description,
                    selectedStatus = selectedStatus,
                    isDropdownExpanded = isDropdownExpanded,
                    estimatedHours = estimatedHours,
                    estimatedMinutes = estimatedMinutes,
                    onTitleChange = viewModel::onTitleChange,
                    onDescriptionChange = viewModel::onDescriptionChange,
                    onSelectedStatusChange = viewModel::onSelectedStatusChange,
                    onDropdownExpandedChange = viewModel::onDropdownExpandedChange,
                    onEstimatedHoursChange = viewModel::onEstimatedHoursChange,
                    onEstimatedMinutesChange = viewModel::onEstimatedMinutesChange,
                    onDismiss = { viewModel.onDismissModal() },
                    onSave = { name, description, status, hours, minutes ->
                        viewModel.createOrUpdateIssue(name, description, status, hours, minutes)
                    }
                )
            }
        }
    }
}
