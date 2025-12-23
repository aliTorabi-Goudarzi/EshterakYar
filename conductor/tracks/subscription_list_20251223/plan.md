# Plan: Implement Subscription List & Management

## Phase 1: Data Layer & Domain
- [x] Task: Update `SubscriptionDao`
    - [x] Sub-Task: Add `getAllSubscriptions()` query ordered by ID/Date DESC.
    - [x] Sub-Task: Add `deleteSubscriptionById()` (if not exists).
- [x] Task: Update `SubscriptionRepository`
    - [x] Sub-Task: Add `getAllSubscriptions` function signature and implementation.
    - [x] Sub-Task: Add `deleteSubscription` function signature and implementation.
- [x] Task: Create Use Cases
    - [x] Sub-Task: Create `GetAllSubscriptionsUseCase`.
    - [x] Sub-Task: Create `DeleteSubscriptionUseCase`.
- [x] Task: TDD - Domain Logic
    - [x] Sub-Task: Write tests for UseCases.
    - [x] Sub-Task: Implement UseCases.

## Phase 2: Presentation Layer (ViewModel & State)
- [x] Task: Update `AddSubscriptionUiState`
    - [x] Sub-Task: Add `subscriptions: List<Subscription>` field.
    - [x] Sub-Task: Add `selectedSubscription: Subscription?` (for BottomSheet).
    - [x] Sub-Task: Add `isBottomSheetOpen: Boolean`.
    - [x] Sub-Task: Add `isDeleteDialogVisible: Boolean`.
- [x] Task: Update `AddSubscriptionIntent`
    - [x] Sub-Task: Add `OnSubscriptionClicked(subscription)`.
    - [x] Sub-Task: Add `OnBottomSheetDismissed`.
    - [x] Sub-Task: Add `OnDeleteClicked`.
    - [x] Sub-Task: Add `OnDeleteConfirmed`.
    - [x] Sub-Task: Add `OnDeleteCancelled`.
    - [x] Sub-Task: Add `OnEditClicked(subscription)`.
- [x] Task: Update `AddSubscriptionViewModel`
    - [x] Sub-Task: Load subscriptions on init (observe Flow).
    - [x] Sub-Task: Handle new Intents.
- [x] Task: TDD - ViewModel
    - [x] Sub-Task: Write unit tests for loading list and handling selection/deletion intents.
    - [x] Sub-Task: Implement ViewModel logic.

## Phase 3: UI Implementation
- [x] Task: Create `SubscriptionItem` Composable
    - [x] Sub-Task: Design the card for the LazyColumn item (Name, Cost, Date, Icon).
- [x] Task: Create `SubscriptionDetailBottomSheet` Composable
    - [x] Sub-Task: Design the layout (Info fields + Edit/Delete buttons).
- [x] Task: Update `AddSubscriptionScreen`
    - [x] Sub-Task: Integrate `LazyColumn` below the existing form.
    - [x] Sub-Task: Add `ModalBottomSheet` integration.
    - [x] Sub-Task: Add `AlertDialog` for delete confirmation.
- [ ] Task: Conductor - User Manual Verification 'Phase 3' (Protocol in workflow.md)
