package ir.dekot.eshterakyar.feature_addSubscription.presentation.viewmodel

import io.mockk.mockk
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.InsertSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.presentation.intent.AddSubscriptionIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddSubscriptionViewModelTest {

    private val insertSubscriptionUseCase: InsertSubscriptionUseCase = mockk(relaxed = true)
    private lateinit var viewModel: AddSubscriptionViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AddSubscriptionViewModel(insertSubscriptionUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when OnNameChanged intent is received, state is updated`() = runTest {
        val name = "Netflix"
        viewModel.onIntent(AddSubscriptionIntent.OnNameChanged(name))
        
        assertEquals(name, viewModel.uiState.value.name)
    }

    @Test
    fun `when OnNextStep intent is received and step 1 is valid, step increments`() = runTest {
        // Setup valid step 1
        viewModel.onIntent(AddSubscriptionIntent.OnNameChanged("Netflix"))
        
        viewModel.onIntent(AddSubscriptionIntent.OnNextStep)
        
        assertEquals(2, viewModel.uiState.value.currentStep)
    }

    @Test
    fun `when OnNextStep intent is received and step 1 is invalid, step does not increment`() = runTest {
        // Invalid step 1 (empty name)
        viewModel.onIntent(AddSubscriptionIntent.OnNameChanged(""))
        
        viewModel.onIntent(AddSubscriptionIntent.OnNextStep)
        
        assertEquals(1, viewModel.uiState.value.currentStep)
        // Should also set error
        assertNotNull(viewModel.uiState.value.nameError)
    }

    @Test
    fun `when OnPreviousStep intent is received at step 2, step decrements`() = runTest {
        // Go to step 2 first
        viewModel.onIntent(AddSubscriptionIntent.OnNameChanged("Netflix"))
        viewModel.onIntent(AddSubscriptionIntent.OnNextStep)
        
        viewModel.onIntent(AddSubscriptionIntent.OnPreviousStep)
        
        assertEquals(1, viewModel.uiState.value.currentStep)
    }
}
