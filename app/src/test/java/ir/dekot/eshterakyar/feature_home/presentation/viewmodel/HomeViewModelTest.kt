package ir.dekot.eshterakyar.feature_home.presentation.viewmodel

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetActiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetInactiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetNearingRenewalSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionStatsUseCase
import ir.dekot.eshterakyar.feature_home.domain.usecase.GetUserGreetingUseCase
import ir.dekot.eshterakyar.feature_home.presentation.mvi.HomeIntent
import ir.dekot.eshterakyar.feature_home.presentation.mvi.HomeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getActiveSubscriptionsUseCase: GetActiveSubscriptionsUseCase = mockk()
    private val getSubscriptionStatsUseCase: GetSubscriptionStatsUseCase = mockk()
    private val getInactiveSubscriptionsUseCase: GetInactiveSubscriptionsUseCase = mockk()
    private val getNearingRenewalSubscriptionsUseCase: GetNearingRenewalSubscriptionsUseCase = mockk()
    private val getUserGreetingUseCase: GetUserGreetingUseCase = mockk()

    private lateinit var viewModel: HomeViewModel
    
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        
        coEvery { getActiveSubscriptionsUseCase() } returns flowOf(emptyList())
        coEvery { getSubscriptionStatsUseCase() } returns ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.SubscriptionStats(0, 0.0, 0.0, 0.0)
        coEvery { getInactiveSubscriptionsUseCase() } returns 0
        coEvery { getNearingRenewalSubscriptionsUseCase() } returns 0
        coEvery { getUserGreetingUseCase.execute(any()) } returns ir.dekot.eshterakyar.feature_home.domain.model.UserGreeting("کاربر", ir.dekot.eshterakyar.feature_home.domain.model.TimeOfDay.MORNING)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initiated, state should load data`() = runTest {
        viewModel = HomeViewModel(
            getActiveSubscriptionsUseCase,
            getSubscriptionStatsUseCase,
            getInactiveSubscriptionsUseCase,
            getNearingRenewalSubscriptionsUseCase,
            getUserGreetingUseCase
        )
        
        // With UnconfinedTestDispatcher, the init block coroutine executes immediately.
        // So the state should ALREADY be updated.
        
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals(emptyList<Any>(), state.subscriptions)
    }
    
    @Test
    fun `onIntent Refresh should reload data`() = runTest {
        viewModel = HomeViewModel(
            getActiveSubscriptionsUseCase,
            getSubscriptionStatsUseCase,
            getInactiveSubscriptionsUseCase,
            getNearingRenewalSubscriptionsUseCase,
            getUserGreetingUseCase
        )

        // Reset verify count from init block
        io.mockk.clearMocks(getActiveSubscriptionsUseCase, answers = false, recordedCalls = true)

        viewModel.onIntent(HomeIntent.Refresh)
        
        // Should call again
        coVerify(exactly = 1) { getActiveSubscriptionsUseCase() }
    }
    
    @Test
    fun `onIntent OnAddSubscriptionClicked should emit NavigateToAddSubscription effect`() = runTest {
        viewModel = HomeViewModel(
            getActiveSubscriptionsUseCase,
            getSubscriptionStatsUseCase,
            getInactiveSubscriptionsUseCase,
            getNearingRenewalSubscriptionsUseCase,
            getUserGreetingUseCase
        )

        viewModel.effect.test {
            viewModel.onIntent(HomeIntent.OnAddSubscriptionClicked)
            val effect = awaitItem()
            assertEquals(ir.dekot.eshterakyar.feature_home.presentation.mvi.HomeEffect.NavigateToAddSubscription, effect)
        }
    }
}