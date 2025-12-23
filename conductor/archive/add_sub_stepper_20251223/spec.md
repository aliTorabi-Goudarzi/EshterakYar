# Track Specification: Refactor AddSubscriptionScreen to Multi-step Form

## 1. Overview
The current `AddSubscriptionScreen` contains a long, single-page form that can be overwhelming for users. The goal of this track is to refactor this screen into a professional Multi-step Form (Stepper) to improve usability, reduce cognitive load, and enhance the visual appeal. This refactoring will adhere to the project's Clean Architecture and MVI principles.

## 2. Functional Requirements

### 2.1. Multi-step Structure
The form will be divided into the following 4 steps:
1.  **Step 1: Basic Info**
    *   Input: Subscription Name (Text)
    *   Input: Category (Dropdown)
2.  **Step 2: Payment Details**
    *   Input: Price (Number)
    *   Input: Currency (Dropdown - IRT/USD)
    *   Input: Billing Cycle (Dropdown)
3.  **Step 3: Schedule**
    *   Input: Next Renewal Date (Date Picker)
    *   Input: Active Status (Switch)
4.  **Step 4: Review**
    *   Read-only summary of all entered information.
    *   "Confirm/Save" button.

### 2.2. Navigation & Interactions
*   **Next Button:** Validates current step's data. If valid, proceeds to the next step.
*   **Back Button:** Returns to the previous step (disabled on Step 1).
*   **Cancel Button:** Exits the flow and returns to the previous screen (available on all steps).
*   **Date Selection:** Clicking the date field in Step 3 opens a standard Material 3 Date Picker Dialog.

### 2.3. Validation
*   Users cannot proceed to the next step until all required fields in the current step are valid.
*   Validation errors must be displayed inline (e.g., "Subscription name is required").

## 3. UI/UX Requirements

### 3.1. Visual Components
*   **Progress Indicator:** A visual step indicator (e.g., "Step 1 of 4") or a progress bar at the top of the screen.
*   **Step Transitions:** Smooth slide or fade animations when moving between steps.
*   **Feedback:** Immediate validation feedback on inputs.

### 3.2. Architecture & Code Style
*   **Component Extraction:** Each step must be implemented as a separate Composable function (e.g., `BasicInfoStep`, `PaymentStep`, `ScheduleStep`, `ReviewStep`) to maintain readability.
*   **Resource Management:** All UI strings must be extracted to `strings.xml`. No hardcoded strings in Composable files.
*   **Persian Comments:** All code comments must be in Persian (Farsi) as per project mandates.
*   **MVI Pattern:** The `AddSubscriptionViewModel` must handle the state for the current step and form data using a unified `State` object and `Intent` actions (e.g., `OnNextStep`, `OnPreviousStep`, `OnFieldChanged`).

## 4. Acceptance Criteria
*   The `AddSubscriptionScreen` is replaced by a functional Stepper UI.
*   The form is split into Basic Info, Payment, Schedule, and Review steps.
*   The Material 3 Date Picker functions correctly for date selection.
*   Form validation prevents progress on invalid data.
*   Animations work smoothly between steps.
*   All hardcoded strings are moved to `strings.xml`.
*   Code follows the "Persian Comments Only" rule.
*   The user can successfully add a subscription via the new flow.

## 5. Out of Scope
*   Changes to the underlying Database or Repository logic (unless required for data retrieval).
*   Adding new fields to the Subscription entity (refactoring existing fields only).
