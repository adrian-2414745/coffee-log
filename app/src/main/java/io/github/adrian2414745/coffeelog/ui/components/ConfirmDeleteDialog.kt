package io.github.adrian2414745.coffeelog.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/** Shared "Are you sure you want to delete?" confirm dialog with Yes / Cancel. */
@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Are you sure you want to delete?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", color = MaterialTheme.colorScheme.tertiary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    )
}
