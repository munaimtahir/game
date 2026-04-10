package com.vexel.offlinearcade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vexel.offlinearcade.core.data.ArcadeRepository
import com.vexel.offlinearcade.core.model.ArcadeSnapshot
import com.vexel.offlinearcade.core.model.RunResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArcadeViewModel(
    private val repository: ArcadeRepository,
) : ViewModel() {
    private val _snapshot = MutableStateFlow(ArcadeSnapshot())
    val snapshot: StateFlow<ArcadeSnapshot> = _snapshot.asStateFlow()

    init {
        viewModelScope.launch {
            repository.snapshot.collectLatest { _snapshot.value = it }
        }
    }

    fun toggleSound(enabled: Boolean) = updateSettings { it.copy(soundEnabled = enabled) }

    fun toggleMusic(enabled: Boolean) = updateSettings { it.copy(musicEnabled = enabled) }

    fun toggleVibration(enabled: Boolean) = updateSettings { it.copy(vibrationEnabled = enabled) }

    fun unlockTheme(themeId: String) {
        viewModelScope.launch { repository.purchaseTheme(themeId) }
    }

    fun selectTheme(themeId: String) {
        viewModelScope.launch { repository.selectTheme(themeId) }
    }

    fun recordRun(result: RunResult) {
        viewModelScope.launch { repository.recordRun(result) }
    }

    fun setPremiumUnlocked(unlocked: Boolean) {
        viewModelScope.launch { repository.setPremiumUnlocked(unlocked) }
    }

    private fun updateSettings(transform: (com.vexel.offlinearcade.core.model.SettingsState) -> com.vexel.offlinearcade.core.model.SettingsState) {
        viewModelScope.launch { repository.updateSettings(transform) }
    }

    companion object {
        fun factory(repository: ArcadeRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = ArcadeViewModel(repository) as T
        }
    }
}
