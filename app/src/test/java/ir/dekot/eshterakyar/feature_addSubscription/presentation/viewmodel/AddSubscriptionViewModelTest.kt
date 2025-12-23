package ir.dekot.eshterakyar.feature_addSubscription.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.Subscription
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.DeleteSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionsSortedByCreationUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.InsertSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.presentation.intent.AddSubscriptionIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddSubscriptionViewModelTest {

    private lateinit var viewModel: AddSubscriptionViewModel
    private val insertSubscriptionUseCase: InsertSubscriptionUseCase = mockk(relaxed = true)
    private val getSubscriptionsSortedByCreationUseCase: GetSubscriptionsSortedByCreationUseCase = mockk()
    private val deleteSubscriptionUseCase: DeleteSubscriptionUseCase = mockk(relaxed = true)
    
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mocking the flow before ViewModel init
        coEvery { getSubscriptionsSortedByCreationUseCase() } returns flowOf(emptyList())
        
        // Note: This constructor call expects the updated signature
        viewModel = AddSubscriptionViewModel(
            insertSubscriptionUseCase,
            getSubscriptionsSortedByCreationUseCase,
            deleteSubscriptionUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load subscriptions`() = runTest {
        // Given
        val subs = listOf<Subscription>(mockk(), mockk())
        coEvery { getSubscriptionsSortedByCreationUseCase() } returns flowOf(subs)
        
        // Re-init viewModel to trigger init block
        viewModel = AddSubscriptionViewModel(
            insertSubscriptionUseCase,
            getSubscriptionsSortedByCreationUseCase,
            deleteSubscriptionUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(subs, viewModel.uiState.value.subscriptions)
    }

    @Test
    fun `OnSubscriptionClicked should open bottom sheet and set selection`() {
        val sub: Subscription = mockk()
        viewModel.onIntent(AddSubscriptionIntent.OnSubscriptionClicked(sub))
        
        assertEquals(sub, viewModel.uiState.value.selectedSubscription)
        assertTrue(viewModel.uiState.value.isBottomSheetOpen)
    }
    
    @Test
    fun `OnBottomSheetDismissed should close sheet and clear selection`() {
        viewModel.onIntent(AddSubscriptionIntent.OnBottomSheetDismissed)
        
        assertFalse(viewModel.uiState.value.isBottomSheetOpen)
        assertEquals(null, viewModel.uiState.value.selectedSubscription)
    }

    @Test
    fun `OnDeleteClicked should show dialog`() {
        viewModel.onIntent(AddSubscriptionIntent.OnDeleteClicked)
        assertTrue(viewModel.uiState.value.isDeleteDialogVisible)
    }
    
    @Test
    fun `OnDeleteCancelled should hide dialog`() {
        viewModel.onIntent(AddSubscriptionIntent.OnDeleteCancelled)
        assertFalse(viewModel.uiState.value.isDeleteDialogVisible)
    }

    @Test
    fun `OnDeleteConfirmed should delete subscription and close sheet`() = runTest {
        val sub: Subscription = mockk()
        viewModel.onIntent(AddSubscriptionIntent.OnSubscriptionClicked(sub))
        viewModel.onIntent(AddSubscriptionIntent.OnDeleteConfirmed)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { deleteSubscriptionUseCase(sub) }
        assertFalse(viewModel.uiState.value.isDeleteDialogVisible)
        assertFalse(viewModel.uiState.value.isBottomSheetOpen)
    }

    @Test
    fun `OnSuccessDismissed should reset form but keep subscriptions`() = runTest {
        // Given
        val subs = listOf<Subscription>(mockk(), mockk())
        coEvery { getSubscriptionsSortedByCreationUseCase() } returns flowOf(subs)
        
        viewModel = AddSubscriptionViewModel(
            insertSubscriptionUseCase,
            getSubscriptionsSortedByCreationUseCase,
            deleteSubscriptionUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()
        
        // When
        viewModel.onIntent(AddSubscriptionIntent.OnSuccessDismissed)
        
        // Then
        assertEquals(subs, viewModel.uiState.value.subscriptions)
        assertFalse(viewModel.uiState.value.saveSuccess)
        assertEquals(1, viewModel.uiState.value.currentStep)
    }
}