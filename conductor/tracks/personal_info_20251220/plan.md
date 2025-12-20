# Plan: Implement Personal Information Screen

## Phase 1: Data Layer & Domain Logic
- [x] Task: Create/Update `UserDao` to support updating user details. 64ad50d
    - [ ] Subtask: Ensure `updateUser(user: User)` exists and is tested.
- [x] Task: Create `UpdateUserUseCase`. cf27614
    - [ ] Subtask: Write Unit Tests (TDD - Red).
    - [ ] Subtask: Implement `UpdateUserUseCase`.
    - [ ] Subtask: Refactor (TDD - Green).
- [ ] Task: Conductor - User Manual Verification 'Phase 1: Data Layer & Domain Logic' (Protocol in workflow.md)

## Phase 2: Presentation Logic (MVI)
- [ ] Task: Define `PersonalInformationState`, `Intent`, and `Effect`.
- [ ] Task: Create `PersonalInformationViewModel`.
    - [ ] Subtask: Write Unit Tests for initial load and validation logic (TDD - Red).
    - [ ] Subtask: Implement ViewModel with MVI (Load User, Validate, Save).
    - [ ] Subtask: Refactor Tests (TDD - Green).
- [ ] Task: Conductor - User Manual Verification 'Phase 2: Presentation Logic (MVI)' (Protocol in workflow.md)

## Phase 3: UI Implementation
- [ ] Task: Create `PersonalInformationScreen` (Stateless) and `PersonalInformationRoute` (Stateful).
    - [ ] Subtask: Implement UI with `SquircleShape` inputs and FAB.
    - [ ] Subtask: Connect to ViewModel.
- [ ] Task: Integrate Navigation.
    - [ ] Subtask: Add `PersonalInformation` destination to `Screens`.
    - [ ] Subtask: Update `ProfileScreen` to navigate to new screen.
    - [ ] Subtask: Update `NestedNavigationModule` / `RootGraph`.
- [ ] Task: Conductor - User Manual Verification 'Phase 3: UI Implementation' (Protocol in workflow.md)

## Phase 4: Final Verification
- [ ] Task: Verify Data Persistence and Validation.
- [ ] Task: Conductor - User Manual Verification 'Phase 4: Final Verification' (Protocol in workflow.md)
