# Plan: Refactor Home Feature to Navigation 3

## Phase 1: MVI & ViewModel Refactoring
- [x] Task: Define `HomeState`, `HomeIntent`, and `HomeEffect` (MVI Contracts). d3d1000
    - [ ] Subtask: Create `HomeContract.kt` in `feature_home/presentation/mvi/`.
- [ ] Task: Refactor `HomeViewModel` to Implement MVI.
    - [ ] Subtask: Write Unit Tests for `HomeViewModel` (TDD - Red).
    - [ ] Subtask: Implement `HomeViewModel` using `StateFlow` and `onIntent`.
    - [ ] Subtask: Refactor Tests (TDD - Green/Refactor).
- [ ] Task: Conductor - User Manual Verification 'Phase 1: MVI & ViewModel Refactoring' (Protocol in workflow.md)

## Phase 2: Navigation 3 & UI Integration
- [ ] Task: Refactor `HomeScreen` to Stateless/Stateful Pattern.
    - [ ] Subtask: Extract logic to `HomeRoute` (Stateful).
    - [ ] Subtask: Ensure `HomeScreen` is pure UI (Stateless).
- [ ] Task: Integrate Navigation 3.
    - [ ] Subtask: Define `HomeDestination` serializable object.
    - [ ] Subtask: Update `RootGraph` / `NavDisplay` to include Home with `koinEntryProvider`.
- [ ] Task: Conductor - User Manual Verification 'Phase 2: Navigation 3 & UI Integration' (Protocol in workflow.md)

## Phase 3: Final Verification & Cleanup
- [ ] Task: Verify Navigation Flow and State.
    - [ ] Subtask: Manual test of Home screen interactions.
    - [ ] Subtask: Check for memory leaks or duplicate ViewModel instances.
- [ ] Task: Conductor - User Manual Verification 'Phase 3: Final Verification & Cleanup' (Protocol in workflow.md)
