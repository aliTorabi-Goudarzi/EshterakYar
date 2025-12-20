package ir.dekot.eshterakyar.screens.personalInfo.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.screens.personalInfo.mvi.PersonalInformationEffect
import ir.dekot.eshterakyar.screens.personalInfo.mvi.PersonalInformationIntent
import ir.dekot.eshterakyar.screens.personalInfo.mvi.PersonalInformationState
import ir.dekot.eshterakyar.screens.personalInfo.viewmodel.PersonalInformationViewModel
import org.koin.androidx.compose.koinViewModel
import sv.lib.squircleshape.SquircleShape
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PersonalInformationRoute(
    viewModel: PersonalInformationViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PersonalInformationEffect.NavigateBack -> onNavigateBack()
                is PersonalInformationEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    PersonalInformationScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInformationScreen(
    uiState: PersonalInformationState,
    onIntent: (PersonalInformationIntent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val theme = LocalTheme.current
    val dateFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale("fa", "IR")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("اطلاعات شخصی") },
                navigationIcon = {
                    IconButton(onClick = { onIntent(PersonalInformationIntent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = theme.surface,
                    titleContentColor = theme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onIntent(PersonalInformationIntent.SaveUser) },
                containerColor = theme.primary,
                contentColor = theme.onPrimary,
                shape = SquircleShape()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = theme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = "ذخیره")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = theme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { onIntent(PersonalInformationIntent.UpdateName(it)) },
                label = { Text("نام") },
                modifier = Modifier.fillMaxWidth(),
                shape = SquircleShape(),
                isError = uiState.nameError != null,
                supportingText = uiState.nameError?.let { { Text(it) } },
                singleLine = true
            )

            // Last Name
            OutlinedTextField(
                value = uiState.lastName,
                onValueChange = { onIntent(PersonalInformationIntent.UpdateLastName(it)) },
                label = { Text("نام خانوادگی") },
                modifier = Modifier.fillMaxWidth(),
                shape = SquircleShape(),
                isError = uiState.lastNameError != null,
                supportingText = uiState.lastNameError?.let { { Text(it) } },
                singleLine = true
            )

            // Phone Number
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = { onIntent(PersonalInformationIntent.UpdatePhoneNumber(it)) },
                label = { Text("شماره تلفن") },
                modifier = Modifier.fillMaxWidth(),
                shape = SquircleShape(),
                isError = uiState.phoneNumberError != null,
                supportingText = uiState.phoneNumberError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            // Account Creation Date (View Only)
            uiState.accountCreationDate?.let { date ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = SquircleShape(),
                    colors = CardDefaults.cardColors(containerColor = theme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "تاریخ ایجاد حساب",
                            style = MaterialTheme.typography.labelMedium,
                            color = theme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dateFormatter.format(date),
                            style = MaterialTheme.typography.bodyLarge,
                            color = theme.onSurface
                        )
                    }
                }
            }
            
            if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PersonalInformationScreenPreview() {
    PersonalInformationScreen(
        uiState = PersonalInformationState(
            name = "علی",
            lastName = "ترابی",
            phoneNumber = "09123456789",
            accountCreationDate = java.util.Date()
        ),
        onIntent = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
}
