# Specification: Implement Personal Information Screen

## Overview
Implement a "Personal Information" screen accessible from the Profile Screen. This screen allows users to view and edit their personal details stored locally in the Room database.

## Functional Requirements
- **Navigation:**
    -   Clicking "Personal Information" item in `ProfileScreen` navigates to `PersonalInformationScreen`.
- **View/Edit:**
    -   Display current user information: First Name, Last Name, Phone Number, Profile Picture, Account Creation Date.
    -   Allow editing of: First Name, Last Name, Phone Number, Profile Picture.
    -   Account Creation Date is **View Only**.
- **Validation:**
    -   **Real-time & Strict:** Validate fields as the user types and before saving.
        -   Name/Last Name: Non-empty.
        -   Phone Number: Valid Iran format (+98 9XX... or 09XX...).
- **Persistence:**
    -   Save updated information to the local `User` entity in Room database upon clicking "Save".

## UI/UX Requirements
-   **Style:** Match application's custom design language.
    -   Use `SquircleShape` for input text fields.
-   **Interaction:**
    -   Use a **Floating Action Button (FAB)** for the "Save" action.
-   **Feedback:** Show success message (Snackbar) upon saving.

## Data Model
-   Utilize existing `ir.dekot.eshterakyar.core.database.User` entity.
-   Fields: `name`, `lastName`, `phoneNumber`, `profilePicture` (String path/URL), `accountCreationDate`.

## Acceptance Criteria
-   User can navigate to Personal Information screen.
-   Current data loads correctly.
-   Validation errors appear for invalid inputs.
-   Valid data is saved to the database and reflects in the Profile Screen upon return.
