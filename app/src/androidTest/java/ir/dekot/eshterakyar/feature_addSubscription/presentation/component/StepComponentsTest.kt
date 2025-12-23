package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import ir.dekot.eshterakyar.feature_addSubscription.domain.model.SubscriptionCategory
import ir.dekot.eshterakyar.feature_addSubscription.presentation.state.AddSubscriptionUiState
import org.junit.Rule
import org.junit.Test

class StepComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun basicInfoStep_showsCorrectFields() {
        val state = AddSubscriptionUiState(name = "Test Sub", category = SubscriptionCategory.ENTERTAINMENT)
        
        composeTestRule.setContent {
            BasicInfoStep(
                uiState = state,
                onNameChange = {},
                onCategoryChange = {}
            )
        }

        composeTestRule.onNodeWithText("Test Sub").assertIsDisplayed()
        composeTestRule.onNodeWithText("سرگرمی").assertIsDisplayed()
    }
}
