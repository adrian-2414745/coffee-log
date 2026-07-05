package io.github.adrian2414745.coffeelog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
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

/** Back `‹` + title (+ optional mono subtitle) header used on the non-dashboard screens. */
@Composable
fun ScreenHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 12.dp, top = 16.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "‹",
            style = AppType.Back,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clickable(onClick = onBack)
                .padding(end = 13.dp),
        )
        Column {
            Text(
                text = title,
                style = AppType.ScreenTitle,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = AppType.HeaderSubtitle,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        Spacer(Modifier.weight(1f))
        actions()
    }
}

/** Fixed bottom bar with a wide amber labelled button, e.g. "Add coffee" / "Add brew". */
@Composable
fun AddBar(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(6.dp)
    Box(
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(shape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                style = AppType.PrimaryButton,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
