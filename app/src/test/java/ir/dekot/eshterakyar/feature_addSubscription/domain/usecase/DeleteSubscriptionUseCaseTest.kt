package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteSubscriptionUseCaseTest {

    private val repository: SubscriptionRepository = mockk(relaxed = true)
    private val useCase = DeleteSubscriptionUseCase(repository)

    @Test
    fun `invoke should call deleteSubscription on repository`() = runTest {
        // Given
        val subscription: Subscription = mockk()
        
        // When
        useCase(subscription)

        // Then
        coVerify(exactly = 1) { repository.deleteSubscription(subscription) }
    }
}
