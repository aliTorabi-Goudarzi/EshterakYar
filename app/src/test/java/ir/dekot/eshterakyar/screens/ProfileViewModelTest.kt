package ir.dekot.eshterakyar.screens

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import ir.dekot.eshterakyar.core.database.GetUserUseCase
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val getUserUseCase: GetUserUseCase = mockk()
    private val rootNavigator: RootNavigator = mockk(relaxed = true)
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { getUserUseCase.createSampleUserIfNeeded() } returns null
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `navigateToPersonalInformation calls navigateTo on rootNavigator`() = runTest {
        viewModel = ProfileViewModel(getUserUseCase, rootNavigator)
        viewModel.navigateToPersonalInformation()
        verify { rootNavigator.navigateTo(Screens.PersonalInformation) }
    }
}
