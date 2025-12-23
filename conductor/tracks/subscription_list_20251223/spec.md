# Specification: Subscription List & Management

## 1. Overview
This feature enhances the `AddSubscriptionScreen` by introducing a list of user-added subscriptions displayed below the "Add Subscription" button. It enables users to view their subscriptions, and manage them (View Details, Edit, Delete) via a BottomSheet interface.

## 2. Functional Requirements

### 2.1. Subscription List (`LazyColumn`)
- **Location:** Positioned vertically below the "Add Subscription" button/form container.
- **Behavior:**
  - When the "Add Subscription" form expands, the list is pushed down.
  - **Sorting:** Orders subscriptions by **Creation Date DESC** (Newest added subscription at the top).
- **Item Content:**
  - Subscription Name.
  - Cost and Currency.
  - Renewal Date / Days Remaining.
  - Category Icon/Color.

### 2.2. Subscription Details (BottomSheet)
- **Trigger:** Tapping any subscription item in the list opens a Modal BottomSheet.
- **Content:**
  - Full Subscription Name & Category.
  - Exact Next Renewal Date.
  - Billing Cycle (Monthly/Yearly).
  - Status (Active/Inactive).
- **Actions:**
  - **Edit:** Navigates to the existing `EditSubscription` screen.
  - **Delete:** Triggers a confirmation dialog.

### 2.3. Delete Confirmation
- **UI:** A standard Alert Dialog.
- **Message:** "Are you sure you want to delete this subscription?"
- **Actions:**
  - "Cancel": Closes dialog, keeps subscription.
  - "Delete": Removes subscription from DB, closes dialog and BottomSheet, updates the list.

## 3. Non-Functional Requirements
- **Performance:** `LazyColumn` must handle lists efficiently.
- **UI/UX:** Animations for list updates (insertions/deletions) should be smooth. BottomSheet follows Material Design 3.
- **Architecture:** Clean Architecture + MVI.

## 4. Acceptance Criteria
- [ ] User sees a list of subscriptions under the "Add" button, sorted newest first.
- [ ] Clicking "Add Subscription" expands the form and pushes the list down.
- [ ] Clicking a list item opens a BottomSheet with correct details.
- [ ] Clicking "Delete" in BottomSheet shows a confirmation dialog; confirming removes the item.
- [ ] Clicking "Edit" navigates to the `EditSubscription` route.
