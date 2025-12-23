package ir.dekot.eshterakyar.feature_addSubscription.domain.usecase

import io.mockk.every
import io.mockk.mockk
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetSubscriptionsSortedByCreationUseCaseTest {

    private val repository: SubscriptionRepository = mockk()
    // This class doesn't exist yet, causing compilation error (Red phase)
    private val useCase = GetSubscriptionsSortedByCreationUseCase(repository)

    @Test
    fun `invoke should return subscriptions sorted by creation from repository`() = runTest {
        // Given
        val subscriptions = listOf<Subscription>(mockk(), mockk())
        every { repository.getAllSubscriptionsSortedByCreation() } returns flowOf(subscriptions)

        // When
        val result = useCase().first()

        // Then
        assertEquals(subscriptions, result)
    }
}
