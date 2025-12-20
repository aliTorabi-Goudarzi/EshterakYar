package ir.dekot.eshterakyar.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.core.database.GetUserUseCase
import ir.dekot.eshterakyar.core.database.User
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.navigation.Screens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val rootNavigator: RootNavigator
) : ViewModel() {


    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    fun navigateToSettings(){
        rootNavigator.navigateTo(destination = Screens.SettingsScreen)
    }

    fun navigateToPersonalInformation(){
        rootNavigator.navigateTo(destination = Screens.PersonalInformation)
    }

    private fun loadUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Create sample user if none exists and get current user
                val user = getUserUseCase.createSampleUserIfNeeded()
                _uiState.value = _uiState.value.copy(
                    user = user,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "خطا در بارگذاری اطلاعات کاربر: ${e.message}"
                )
            }
        }
    }

    fun refreshUser() {
        loadUser()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}