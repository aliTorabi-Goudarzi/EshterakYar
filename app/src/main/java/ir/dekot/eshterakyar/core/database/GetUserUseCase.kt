package ir.dekot.eshterakyar.core.database

import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val userRepository: UserRepository
) {

    suspend fun getCurrentUser(): User? {
        return try {
            userRepository.getCurrentUser()
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentUserFlow(): Flow<User?> {
        return userRepository.getCurrentUserFlow()
    }

    suspend fun createSampleUserIfNeeded(): User? {
        return try {
            val currentUser = userRepository.getCurrentUser()
            if (currentUser == null) {
                // Create a sample user for testing
                val sampleUser = User(
                    name = "کاربر",
                    lastName = "نمونه",
                    phoneNumber = "+98 912 345 6789",
                    profilePicture = null,
                    accountCreationDate = java.util.Date()
                )
                val userId = userRepository.insertUser(sampleUser)
                userRepository.getUserById(userId)
            } else {
                currentUser
            }
        } catch (e: Exception) {
            null
        }
    }
}