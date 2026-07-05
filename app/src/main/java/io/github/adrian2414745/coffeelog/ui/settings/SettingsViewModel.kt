package io.github.adrian2414745.coffeelog.ui.settings

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.adrian2414745.coffeelog.data.ThemeRepository
import io.github.adrian2414745.coffeelog.data.transfer.DataTransferManager
import io.github.adrian2414745.coffeelog.di.appContainer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themeRepository: ThemeRepository,
    private val transfer: DataTransferManager,
) : ViewModel() {

    val darkTheme: StateFlow<Boolean?> = themeRepository.darkTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages: Flow<String> = _messages.receiveAsFlow()

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { themeRepository.setDarkTheme(enabled) }
    }

    fun export(uri: Uri, resolver: ContentResolver) {
        viewModelScope.launch {
            try {
                val text = transfer.encode(transfer.buildExport())
                resolver.openOutputStream(uri)?.use { it.write(text.toByteArray()) }
                    ?: error("Couldn't open the file for writing.")
                _messages.send("Data exported.")
            } catch (e: Exception) {
                _messages.send("Export failed: ${e.message}")
            }
        }
    }

    fun import(uri: Uri, resolver: ContentResolver) {
        viewModelScope.launch {
            try {
                val text = resolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                    ?: error("Couldn't read the file.")
                val root = transfer.decode(text) // validates before touching the DB
                transfer.applyImport(root)
                _messages.send("Data imported.")
            } catch (e: Exception) {
                _messages.send("Import failed: ${e.message}")
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val c = appContainer()
                SettingsViewModel(c.themeRepository, c.dataTransferManager)
            }
        }
    }
}
