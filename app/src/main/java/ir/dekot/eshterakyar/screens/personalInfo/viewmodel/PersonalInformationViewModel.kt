package ir.dekot.eshterakyar.screens.personalInfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.dekot.eshterakyar.core.database.GetUserUseCase
import ir.dekot.eshterakyar.core.database.User
import ir.dekot.eshterakyar.core.domain.usecase.UpdateUserUseCase
import ir.dekot.eshterakyar.screens.personalInfo.mvi.PersonalInformationEffect
import ir.dekot.eshterakyar.screens.personalInfo.mvi.PersonalInformationIntent
import ir.dekot.eshterakyar.screens.personalInfo.mvi.PersonalInformationState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PersonalInformationViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalInformationState())
    val uiState: StateFlow<PersonalInformationState> = _uiState.asStateFlow()

    private val _effect = Channel<PersonalInformationEffect>()
    val effect = _effect.receiveAsFlow()

    private var currentUser: User? = null

    init {
        onIntent(PersonalInformationIntent.LoadUser)
    }

    fun onIntent(intent: PersonalInformationIntent) {
        when (intent) {
            PersonalInformationIntent.LoadUser -> loadUser()
            is PersonalInformationIntent.UpdateName -> {
                _uiState.value = _uiState.value.copy(name = intent.name, nameError = null)
            }
            is PersonalInformationIntent.UpdateLastName -> {
                _uiState.value = _uiState.value.copy(lastName = intent.lastName, lastNameError = null)
            }
            is PersonalInformationIntent.UpdatePhoneNumber -> {
                _uiState.value = _uiState.value.copy(phoneNumber = intent.phoneNumber, phoneNumberError = null)
            }
            is PersonalInformationIntent.UpdateProfilePicture -> {
                _uiState.value = _uiState.value.copy(profilePicture = intent.path)
            }
            PersonalInformationIntent.SaveUser -> saveUser()
            PersonalInformationIntent.ClearError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
            PersonalInformationIntent.NavigateBack -> {
                sendEffect(PersonalInformationEffect.NavigateBack)
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val user = getUserUseCase.createSampleUserIfNeeded()
                currentUser = user
                
                if (user != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        name = user.name,
                        lastName = user.lastName,
                        phoneNumber = user.phoneNumber,
                        profilePicture = user.profilePicture,
                        accountCreationDate = user.accountCreationDate
                    )
                } else {
                     _uiState.value = _uiState.value.copy(isLoading = false, error = "کاربری یافت نشد")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun saveUser() {
        val currentState = _uiState.value
        val name = currentState.name
        val lastName = currentState.lastName
        val phoneNumber = currentState.phoneNumber

        var isValid = true
        var nameError: String? = null
        var lastNameError: String? = null
        var phoneNumberError: String? = null

        if (name.isBlank()) {
            nameError = "نام نمی‌تواند خالی باشد"
            isValid = false
        }
        if (lastName.isBlank()) {
            lastNameError = "نام خانوادگی نمی‌تواند خالی باشد"
            isValid = false
        }
        
        val phoneRegex = Regex("^(?:\\+98|0)?9\\d{9}$") 
        val cleanedPhone = phoneNumber.replace(" ", "")
        if (!cleanedPhone.matches(phoneRegex)) {
             phoneNumberError = "شماره تلفن معتبر نیست"
             isValid = false
        }

        if (!isValid) {
            _uiState.value = _uiState.value.copy(
                nameError = nameError,
                lastNameError = lastNameError,
                phoneNumberError = phoneNumberError
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val updatedUser = currentUser?.copy(
                    name = name,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    profilePicture = currentState.profilePicture
                ) ?: User(
                    name = name,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    profilePicture = currentState.profilePicture,
                    accountCreationDate = java.util.Date()
                )

                updateUserUseCase(updatedUser)
                
                currentUser = updatedUser
                
                _uiState.value = _uiState.value.copy(isLoading = false)
                sendEffect(PersonalInformationEffect.ShowMessage("اطلاعات با موفقیت ذخیره شد"))
                sendEffect(PersonalInformationEffect.NavigateBack)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun sendEffect(effect: PersonalInformationEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
