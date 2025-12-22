# Plan: Implement Personal Information Screen

## Phase 1: Data Layer & Domain Logic
- [x] Task: Create/Update `UserDao` to support updating user details. 64ad50d
    - [ ] Subtask: Ensure `updateUser(user: User)` exists and is tested.
- [x] Task: Create `UpdateUserUseCase`. cf27614
    - [ ] Subtask: Write Unit Tests (TDD - Red).
    - [ ] Subtask: Implement `UpdateUserUseCase`.
    - [ ] Subtask: Refactor (TDD - Green).
- [x] Task: Conductor - User Manual Verification 'Phase 1: Data Layer & Domain Logic' (Protocol in workflow.md) [checkpoint: ab9d669]

## Phase 2: Presentation Logic (MVI)
- [x] Task: Define `PersonalInformationState`, `Intent`, and `Effect`. 31f8f36
- [x] Task: Create `PersonalInformationViewModel`. 4396ede
    - [ ] Subtask: Write Unit Tests for initial load and validation logic (TDD - Red).
    - [ ] Subtask: Implement ViewModel with MVI (Load User, Validate, Save).
    - [ ] Subtask: Refactor Tests (TDD - Green).
- [x] Task: Conductor - User Manual Verification 'Phase 2: Presentation Logic (MVI)' (Protocol in workflow.md) [checkpoint: a1edb28]

## Phase 3: UI Implementation
- [x] Task: Create `PersonalInformationScreen` (Stateless) and `PersonalInformationRoute` (Stateful). 45aea9c
    - [ ] Subtask: Implement UI with `SquircleShape` inputs and FAB.
    - [ ] Subtask: Connect to ViewModel.
- [x] Task: Integrate Navigation. b86e4e6
    - [ ] Subtask: Add `PersonalInformation` destination to `Screens`.
    - [ ] Subtask: Update `ProfileScreen` to navigate to new screen.
    - [ ] Subtask: Update `NestedNavigationModule` / `RootGraph`.
- [x] Task: Conductor - User Manual Verification 'Phase 3: UI Implementation' (Protocol in workflow.md) [checkpoint: 47a2232]

## Phase 4: Final Verification
- [x] Task: Verify Data Persistence and Validation. 626caee
- [x] Task: Conductor - User Manual Verification 'Phase 4: Final Verification' (Protocol in workflow.md) [checkpoint: 2e1e3d9]
