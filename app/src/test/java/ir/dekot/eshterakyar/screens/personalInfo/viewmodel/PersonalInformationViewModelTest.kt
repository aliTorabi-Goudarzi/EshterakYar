package ir.dekot.eshterakyar.screens.personalInfo.viewmodel

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import ir.dekot.eshterakyar.core.database.GetUserUseCase
import ir.dekot.eshterakyar.core.database.User
import ir.dekot.eshterakyar.core.domain.usecase.UpdateUserUseCase
import ir.dekot.eshterakyar.screens.personalInfo.mvi.PersonalInformationIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class PersonalInformationViewModelTest {

    private val getUserUseCase: GetUserUseCase = mockk()
    private val updateUserUseCase: UpdateUserUseCase = mockk(relaxed = true)
    private lateinit var viewModel: PersonalInformationViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { getUserUseCase.createSampleUserIfNeeded() } returns User(1, "Old", "Name", "09123456789", null, Date())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load populates state`() = runTest {
        viewModel = PersonalInformationViewModel(getUserUseCase, updateUserUseCase)
        
        viewModel.onIntent(PersonalInformationIntent.LoadUser)

        val state = viewModel.uiState.value
        assertEquals("Old", state.name)
        assertEquals("Name", state.lastName)
        assertEquals("09123456789", state.phoneNumber)
    }

    @Test
    fun `UpdateName updates state`() = runTest {
        viewModel = PersonalInformationViewModel(getUserUseCase, updateUserUseCase)
        viewModel.onIntent(PersonalInformationIntent.UpdateName("New"))
        
        assertEquals("New", viewModel.uiState.value.name)
    }

    @Test
    fun `SaveUser with invalid name shows error`() = runTest {
        viewModel = PersonalInformationViewModel(getUserUseCase, updateUserUseCase)
        viewModel.onIntent(PersonalInformationIntent.UpdateName(""))
        viewModel.onIntent(PersonalInformationIntent.SaveUser)
        
        assert(viewModel.uiState.value.nameError != null)
        coVerify(exactly = 0) { updateUserUseCase(any()) }
    }

    @Test
    fun `SaveUser with valid data calls useCase`() = runTest {
        viewModel = PersonalInformationViewModel(getUserUseCase, updateUserUseCase)
        viewModel.onIntent(PersonalInformationIntent.LoadUser)
        
        viewModel.onIntent(PersonalInformationIntent.SaveUser)
        
        coVerify(exactly = 1) { updateUserUseCase(any()) }
    }
}
