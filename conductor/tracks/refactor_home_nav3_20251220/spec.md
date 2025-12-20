# Specification: Refactor Home Feature to Navigation 3

## Goal
Refactor the existing `feature_home` module to strictly adhere to the "Supreme Android Agent Constitution," focusing on Navigation 3, Koin 4, and MVI architecture.

## Scope
- **Architecture:** Enforce Clean Architecture + MVI (Model-View-Intent).
- **Navigation:** Migrate from standard Jetpack Compose Navigation to **Navigation 3**.
- **Dependency Injection:** Update to **Koin 4** using `viewModelOf` and `koinEntryProvider`.
- **UI:** Split screens into Stateful (Route) and Stateless (Screen) components.

## Key Changes
1.  **ViewModel:**
    -   Expose only `StateFlow<State>` and `fun onIntent(Intent)`.
    -   Remove any direct navigation calls or public methods other than `onIntent`.
2.  **Screen:**
    -   Create `HomeRoute` (Stateful): Collects state, handles side effects, dispatches intents.
    -   Create `HomeScreen` (Stateless): Renders UI based on state.
3.  **Navigation:**
    -   Register `HomeScreen` using `navigation<T>` in the `NavDisplay`.
    -   Use `koinEntryProvider` for scoped ViewModel injection.

## Verification
-   `HomeViewModel` must have 100% unit test coverage.
-   Navigation between Home and other screens must work seamlessly with Navigation 3.
-   Configuration changes (rotation) must preserve state.
