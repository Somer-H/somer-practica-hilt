package com.alilopez.kt_demohilt.features.issues.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alilopez.kt_demohilt.R
import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue

@Composable
fun IssueCard(
    issue: Issue,
    onDelete: (Long) -> Unit,
    onEdit: (Issue) -> Unit,
    onPlay: (Long) -> Unit,
    onPause: (Long) -> Unit,
    isTimerActive: Boolean
) {
    val statusColor: Color
    val statusIcon: androidx.compose.ui.graphics.vector.ImageVector

    when (issue.status) {
        "Pospuesta" -> {
            statusColor = Color(0xFFFFA726)
            statusIcon = Icons.Outlined.Info
        }
        "Por Hacer" -> {
            statusColor = Color.Gray
            statusIcon = Icons.Outlined.Schedule
        }
        "En Proceso" -> {
            statusColor = Color(0xFF42A5F5)
            statusIcon = Icons.Outlined.PlayCircleOutline
        }
        else -> {
            statusColor = Color.Black
            statusIcon = Icons.Outlined.Info
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = "Status",
                        tint = statusColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = issue.status,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(
                    text = issue.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = issue.description,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Tiempo: ${issue.currentTime.toComponents { h, m, s, _ -> "%02d:%02d:%02d".format(h, m, s) }} / ${issue.timeToDo.toComponents { h, m, s, _ -> "%02d:%02d:%02d".format(h, m, s) }}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isTimerActive) {
                    IconButton(onClick = { onPause(issue.id) }) {
                        Icon(painter = painterResource(id = R.drawable.pause), contentDescription = "Pause")
                    }
                } else {
                    IconButton(onClick = { onPlay(issue.id) }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                    }
                }
                IconButton(onClick = { onEdit(issue) }) {
                    Icon(painter = painterResource(id = R.drawable.edit), contentDescription = "Edit", tint = Color.Gray)
                }
                IconButton(onClick = { onDelete(issue.id) }) {
                    Icon(painter = painterResource(id = R.drawable.delete), contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}