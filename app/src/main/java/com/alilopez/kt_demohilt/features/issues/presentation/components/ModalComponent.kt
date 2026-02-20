package com.alilopez.kt_demohilt.features.issues.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.alilopez.kt_demohilt.features.issues.domain.entities.Issue


@Composable
fun ModalComponent(
    issue: Issue?,
    title: String,
    description: String,
    selectedStatus: String,
    isDropdownExpanded: Boolean,
    estimatedHours: String,
    estimatedMinutes: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSelectedStatusChange: (String) -> Unit,
    onDropdownExpandedChange: (Boolean) -> Unit,
    onEstimatedHoursChange: (String) -> Unit,
    onEstimatedMinutesChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Int, Int) -> Unit
) {
    val statuses = listOf("Por Hacer", "En Proceso", "Pospuesta")

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (issue == null) "Nueva Tarea" else "Editar Tarea",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Título", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                TextField(
                    value = title,
                    onValueChange = onTitleChange,
                    placeholder = { Text("Nombre de la tarea") },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Descripción", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                TextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    placeholder = { Text("Detalles de la tarea") },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Estado", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart)) {
                    Text(selectedStatus, modifier = Modifier.fillMaxWidth().clickable(onClick = { onDropdownExpandedChange(true) }).padding(vertical = 8.dp), color = Color.Black)
                    DropdownMenu(expanded = isDropdownExpanded, onDismissRequest = { onDropdownExpandedChange(false) }) {
                        statuses.forEach { status ->
                            DropdownMenuItem(text = { Text(status) }, onClick = { onSelectedStatusChange(status) })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Tiempo estimado", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = estimatedHours,
                        onValueChange = onEstimatedHoursChange,
                        placeholder = { Text("0") },
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text("h", modifier = Modifier.padding(horizontal = 8.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = estimatedMinutes,
                        onValueChange = onEstimatedMinutesChange,
                        placeholder = { Text("0") },
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text("m", modifier = Modifier.padding(horizontal = 8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Button(onClick = { onSave(title, description, selectedStatus, estimatedHours.toIntOrNull() ?: 0, estimatedMinutes.toIntOrNull() ?: 0) }) {
                        Text(if (issue == null) "Crear" else "Guardar")
                    }
                }
            }
        }
    }
}
