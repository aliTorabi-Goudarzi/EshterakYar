package ir.dekot.eshterakyar.screens.personalInfo.mvi

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
data class PersonalInformationState(
    val name: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val profilePicture: String? = null,
    val accountCreationDate: Date? = null,
    val nameError: String? = null,
    val lastNameError: String? = null,
    val phoneNumberError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface PersonalInformationIntent {
    data object LoadUser : PersonalInformationIntent
    data class UpdateName(val name: String) : PersonalInformationIntent
    data class UpdateLastName(val lastName: String) : PersonalInformationIntent
    data class UpdatePhoneNumber(val phoneNumber: String) : PersonalInformationIntent
    data class UpdateProfilePicture(val path: String) : PersonalInformationIntent
    data object SaveUser : PersonalInformationIntent
    data object ClearError : PersonalInformationIntent
    data object NavigateBack : PersonalInformationIntent
}

sealed interface PersonalInformationEffect {
    data object NavigateBack : PersonalInformationEffect
    data class ShowMessage(val message: String) : PersonalInformationEffect
}
