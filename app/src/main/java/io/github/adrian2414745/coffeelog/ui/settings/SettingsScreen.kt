package io.github.adrian2414745.coffeelog.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.adrian2414745.coffeelog.ui.components.ScreenHeader
import io.github.adrian2414745.coffeelog.ui.theme.AppType
import io.github.adrian2414745.coffeelog.ui.theme.Emphasis
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory),
) {
    val context = LocalContext.current
    val darkPref by viewModel.darkTheme.collectAsStateWithLifecycle()
    val darkChecked = darkPref ?: isSystemInDarkTheme()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { snackbarHostState.showSnackbar(it) }
    }

    val exportLauncher = rememberLauncherForActivityResult(CreateDocument("application/json")) { uri ->
        if (uri != null) viewModel.export(uri, context.contentResolver)
    }
    val importLauncher = rememberLauncherForActivityResult(OpenDocument()) { uri ->
        if (uri != null) viewModel.import(uri, context.contentResolver)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            ScreenHeader(title = "Settings", onBack = onBack, subtitle = "DATA MANAGEMENT")
            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                SectionLabel("DATA")
                SettingCard(
                    icon = Icons.Outlined.Download,
                    title = "Import Data",
                    subtitle = "Restore from a JSON backup",
                    onClick = { importLauncher.launch(arrayOf("application/json")) },
                )
                SettingCard(
                    icon = Icons.Outlined.Upload,
                    title = "Export Data",
                    subtitle = "Download all brews as JSON",
                    onClick = { exportLauncher.launch("coffeelog-backup.json") },
                )
                SectionLabel("APPEARANCE")
                SettingCard(
                    icon = Icons.Outlined.DarkMode,
                    title = "Dark theme",
                    subtitle = "Graphite palette",
                    onClick = { viewModel.setDarkTheme(!darkChecked) },
                    trailing = {
                        Switch(
                            checked = darkChecked,
                            onCheckedChange = { viewModel.setDarkTheme(it) },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = MaterialTheme.colorScheme.tertiary,
                                checkedThumbColor = MaterialTheme.colorScheme.onTertiary,
                                checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                uncheckedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            ),
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = AppType.SectionLabel,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
        modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun SettingCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit = { Chevron() },
) {
    val shape = RoundedCornerShape(6.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(13.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(19.dp))
            Column {
                Text(
                    title,
                    style = AppType.SettingTitle,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    subtitle,
                    style = AppType.HelperSubtitle,
                    color = Emphasis.tertiary,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        trailing()
    }
}

@Composable
private fun Chevron() {
    Text(
        "›",
        style = AppType.Chevron,
        color = Emphasis.tertiary,
    )
}
