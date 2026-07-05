package io.github.adrian2414745.coffeelog.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.adrian2414745.coffeelog.ui.theme.AppType

/** Fixed bottom action bar: a wide amber SAVE beside a quarter-width outlined CANCEL. */
@Composable
fun SaveCancelBar(
    saveLabel: String,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    saveEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        val shape = RoundedCornerShape(6.dp)
        // SAVE
        Box(
            Modifier
                .weight(3f)
                .height(46.dp)
                .clip(shape)
                .background(
                    if (saveEnabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                    },
                )
                .clickable(enabled = saveEnabled, onClick = onSave),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = saveLabel,
                style = AppType.PrimaryButton,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        // CANCEL
        Box(
            Modifier
                .weight(1f)
                .height(46.dp)
                .clip(shape)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.22f)),
                    shape,
                )
                .clickable(onClick = onCancel),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "CANCEL",
                style = AppType.CancelButton,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}
