package com.matedroid.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matedroid.data.local.AppSettings
import com.matedroid.data.local.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val serverUrl: String = "",
    val apiToken: String = "",
    val isLoading: Boolean = true,
    val isTesting: Boolean = false,
    val isSaving: Boolean = false,
    val testResult: TestResult? = null,
    val error: String? = null
)

sealed class TestResult {
    data object Success : TestResult()
    data class Failure(val message: String) : TestResult()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = settingsDataStore.settings.first()
            _uiState.value = _uiState.value.copy(
                serverUrl = settings.serverUrl,
                apiToken = settings.apiToken,
                isLoading = false
            )
        }
    }

    fun updateServerUrl(url: String) {
        _uiState.value = _uiState.value.copy(
            serverUrl = url,
            testResult = null,
            error = null
        )
    }

    fun updateApiToken(token: String) {
        _uiState.value = _uiState.value.copy(
            apiToken = token,
            testResult = null,
            error = null
        )
    }

    fun testConnection() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isTesting = true, testResult = null, error = null)

            try {
                val url = _uiState.value.serverUrl.trimEnd('/')
                if (url.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isTesting = false,
                        testResult = TestResult.Failure("Server URL is required")
                    )
                    return@launch
                }

                // TODO: Implement actual API ping test in Phase 2
                // For now, just validate URL format
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    _uiState.value = _uiState.value.copy(
                        isTesting = false,
                        testResult = TestResult.Failure("URL must start with http:// or https://")
                    )
                    return@launch
                }

                // Simulate successful test for now
                _uiState.value = _uiState.value.copy(
                    isTesting = false,
                    testResult = TestResult.Success
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isTesting = false,
                    testResult = TestResult.Failure(e.message ?: "Unknown error")
                )
            }
        }
    }

    fun saveSettings(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)

            try {
                val url = _uiState.value.serverUrl.trimEnd('/')
                if (url.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = "Server URL is required"
                    )
                    return@launch
                }

                settingsDataStore.saveSettings(
                    serverUrl = url,
                    apiToken = _uiState.value.apiToken
                )

                _uiState.value = _uiState.value.copy(isSaving = false)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Failed to save settings"
                )
            }
        }
    }

    fun clearTestResult() {
        _uiState.value = _uiState.value.copy(testResult = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
