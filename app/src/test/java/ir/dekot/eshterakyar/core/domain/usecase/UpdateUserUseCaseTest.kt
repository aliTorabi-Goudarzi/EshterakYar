package ir.dekot.eshterakyar.core.domain.usecase

import io.mockk.coVerify
import io.mockk.mockk
import ir.dekot.eshterakyar.core.database.User
import ir.dekot.eshterakyar.core.database.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.Date

class UpdateUserUseCaseTest {

    private val repository: UserRepository = mockk(relaxed = true)
    private val useCase = UpdateUserUseCase(repository)

    @Test
    fun `invoke calls updateUser on repository`() = runTest {
        val user = User(
            id = 1,
            name = "Test",
            lastName = "User",
            phoneNumber = "09123456789",
            accountCreationDate = Date()
        )

        useCase(user)

        coVerify { repository.updateUser(user) }
    }
}
