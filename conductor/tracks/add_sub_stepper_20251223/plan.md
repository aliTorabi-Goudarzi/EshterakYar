# Implementation Plan - Multi-step Form Refactor

## Phase 1: Setup and Resource Extraction [checkpoint: bc104b8]
- [x] Task: Extract Strings 446b440
    - Extract all hardcoded strings from `AddSubscriptionScreen.kt` to `strings.xml`.
    - Add new strings required for the stepper (e.g., "Next", "Previous", "Review", "Step X of Y").
- [x] Task: Create UI Components 84699b6
    - Create `StepIndicator` composable for showing progress.
    - Create `StepContainer` composable to handle animations (slide/fade) between steps.
- [ ] Task: Conductor - User Manual Verification 'Setup and Resource Extraction' (Protocol in workflow.md)

## Phase 2: MVI & State Management Updates [checkpoint: 48fa24b]
- [x] Task: Update Domain/State Models 6baf6d4
    - Update `AddSubscriptionState` to include `currentStep` (Int or Enum).
    - Ensure all form fields are present in the state (Name, Category, Price, Currency, BillingCycle, Date, Active).
- [x] Task: Update ViewModel (TDD) fbe24fc
    - [x] Create/Update Unit Tests for `AddSubscriptionViewModel`.
        - Test `OnNextStep` validation logic (prevent step increment if invalid).
        - Test `OnPreviousStep` logic.
        - Test individual field updates.
    - [x] Implement `OnNextStep` and `OnPreviousStep` intents in ViewModel.
    - [x] Implement validation logic for each step in the ViewModel.
- [ ] Task: Conductor - User Manual Verification 'MVI & State Management Updates' (Protocol in workflow.md)

## Phase 3: Step Components Implementation
- [x] Task: Implement Step 1 - Basic Info fdc62f8
    - Create `BasicInfoStep.kt` composable.
    - Implement inputs for Name and Category.
    - Connect to ViewModel state and intents.
- [x] Task: Implement Step 2 - Payment Details d3879e6
    - Create `PaymentStep.kt` composable.
    - Implement inputs for Price, Currency, and Billing Cycle.
    - Connect to ViewModel state and intents.
- [ ] Task: Implement Step 3 - Schedule
    - Create `ScheduleStep.kt` composable.
    - Implement Date Picker Dialog integration.
    - Implement Switch for Active status.
    - Connect to ViewModel state and intents.
- [ ] Task: Implement Step 4 - Review
    - Create `ReviewStep.kt` composable.
    - Display a summary of all collected data.
    - Implement "Confirm" button to trigger the final save intent.
- [ ] Task: Conductor - User Manual Verification 'Step Components Implementation' (Protocol in workflow.md)

## Phase 4: Integration and Polish
- [ ] Task: Integrate Stepper into Main Screen
    - Refactor `AddSubscriptionScreen.kt` to use the new `StepContainer` and individual step composables.
    - Wire up the `StepIndicator` with the ViewModel's `currentStep` state.
    - Wire up Next/Back/Cancel buttons.
- [ ] Task: UI/UX Polish
    - Verify and fine-tune animations.
    - Ensure keyboard handling (IME actions) is correct between fields.
    - Verify dark mode support.
- [ ] Task: Conductor - User Manual Verification 'Integration and Polish' (Protocol in workflow.md)
