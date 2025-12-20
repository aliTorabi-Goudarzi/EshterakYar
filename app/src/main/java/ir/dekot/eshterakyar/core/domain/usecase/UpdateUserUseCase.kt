package ir.dekot.eshterakyar.core.domain.usecase

import ir.dekot.eshterakyar.core.database.User
import ir.dekot.eshterakyar.core.database.UserRepository

class UpdateUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.updateUser(user)
    }
}
