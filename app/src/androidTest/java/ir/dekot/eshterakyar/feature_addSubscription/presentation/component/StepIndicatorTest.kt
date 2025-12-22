package ir.dekot.eshterakyar.feature_addSubscription.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import ir.dekot.eshterakyar.R
import org.junit.Rule
import org.junit.Test
import androidx.test.platform.app.InstrumentationRegistry

class StepIndicatorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun stepIndicator_showsCorrectStepText() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Note: The context needs to be the one where resources are available.
        // Usually targetContext is correct for instrumented tests accessing app resources.
        val expectedText = context.getString(R.string.step_counter, 2, 4)

        composeTestRule.setContent {
            StepIndicator(
                currentStep = 2,
                totalSteps = 4
            )
        }

        composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
    }
}
