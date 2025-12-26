package ir.dekot.eshterakyar.feature_category.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.utils.LocalTheme
import ir.dekot.eshterakyar.feature_category.domain.model.CategoryItem
import ir.dekot.eshterakyar.feature_category.presentation.intent.CategoryManagementIntent
import ir.dekot.eshterakyar.feature_category.presentation.viewmodel.CategoryManagementViewModel
import org.koin.androidx.compose.koinViewModel
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

/** صفحه مدیریت دسته‌بندی‌ها */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
        rootNavigator: RootNavigator,
        viewModel: CategoryManagementViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme = LocalTheme.current

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    text = "مدیریت دسته‌بندی‌ها",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = theme.onSurface
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { rootNavigator.goBack() }) {
                                Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "بازگشت",
                                        tint = theme.onSurface
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = theme.surface)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                        onClick = {
                            viewModel.processIntent(CategoryManagementIntent.OnShowAddDialog)
                        },
                        containerColor = theme.primary
                ) {
                    Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "افزودن دسته",
                            tint = theme.onPrimary
                    )
                }
            },
            containerColor = theme.backgroundColor
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = theme.primary
                )
            } else {
                LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // دسته‌های پیش‌فرض
                    item {
                        Text(
                                text = "دسته‌های پیش‌فرض",
                                style = MaterialTheme.typography.titleMedium,
                                color = theme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(items = uiState.categories.filter { it.isDefault }, key = { it.id }) {
                            category ->
                        CategoryItemCard(category = category, onEdit = {}, onDelete = {})
                    }

                    // دسته‌های سفارشی
                    val customCategories = uiState.categories.filter { !it.isDefault }
                    if (customCategories.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                    text = "دسته‌های سفارشی",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = theme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(items = customCategories, key = { it.id }) { category ->
                            CategoryItemCard(
                                    category = category,
                                    onEdit = {
                                        viewModel.processIntent(
                                                CategoryManagementIntent.OnEditCategory(category)
                                        )
                                    },
                                    onDelete = {
                                        viewModel.processIntent(
                                                CategoryManagementIntent.OnDeleteCategory(
                                                        category.id
                                                )
                                        )
                                    }
                            )
                        }
                    }
                }
            }

            // نمایش خطا
            uiState.errorMessage?.let { error ->
                Snackbar(
                        modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                        action = {
                            TextButton(
                                    onClick = {
                                        viewModel.processIntent(
                                                CategoryManagementIntent.OnClearError
                                        )
                                    }
                            ) { Text("باشه", color = theme.onPrimary) }
                        }
                ) { Text(error) }
            }
        }

        // دیالوگ افزودن/ویرایش
        if (uiState.showAddDialog) {
            AddCategoryDialog(
                    isEditing = uiState.editingCategory != null,
                    name = uiState.newCategoryName,
                    selectedColor = uiState.selectedColorCode,
                    onNameChange = {
                        viewModel.processIntent(CategoryManagementIntent.OnNameChanged(it))
                    },
                    onColorChange = {
                        viewModel.processIntent(CategoryManagementIntent.OnColorChanged(it))
                    },
                    onDismiss = {
                        viewModel.processIntent(CategoryManagementIntent.OnDismissDialog)
                    },
                    onSave = { viewModel.processIntent(CategoryManagementIntent.OnSaveCategory) }
            )
        }
    }
}

/** کارت نمایش یک دسته‌بندی */
@Composable
private fun CategoryItemCard(category: CategoryItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    val theme = LocalTheme.current
    val categoryColor =
            try {
                Color(android.graphics.Color.parseColor(category.colorCode))
            } catch (e: Exception) {
                Color.Gray
            }

    Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = theme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            // آیکون با رنگ
            Box(
                    modifier =
                            Modifier.size(40.dp)
                                    .clip(SquircleShape(10, smoothing = CornerSmoothing.Medium))
                                    .background(categoryColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = categoryColor,
                        modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // نام
            Column(modifier = Modifier.weight(1f)) {
                Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = theme.onSurface
                )
                if (category.isDefault) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = theme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                                text = "پیش‌فرض",
                                style = MaterialTheme.typography.bodySmall,
                                color = theme.onSurfaceVariant
                        )
                    }
                }
            }

            // دکمه‌های عملیات
            if (!category.isDefault) {
                IconButton(onClick = onEdit) {
                    Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "ویرایش",
                            tint = theme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "حذف",
                            tint = theme.error
                    )
                }
            }
        }
    }
}

/** دیالوگ افزودن/ویرایش دسته */
@Composable
private fun AddCategoryDialog(
        isEditing: Boolean,
        name: String,
        selectedColor: String,
        onNameChange: (String) -> Unit,
        onColorChange: (String) -> Unit,
        onDismiss: () -> Unit,
        onSave: () -> Unit
) {
    val theme = LocalTheme.current
    val colorOptions =
            listOf(
                    "#E91E63",
                    "#2196F3",
                    "#4CAF50",
                    "#FF5722",
                    "#9C27B0",
                    "#00BCD4",
                    "#F44336",
                    "#3F51B5",
                    "#607D8B"
            )

    AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                        text = if (isEditing) "ویرایش دسته" else "افزودن دسته جدید",
                        style = MaterialTheme.typography.titleLarge,
                        color = theme.onSurface
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                            value = name,
                            onValueChange = onNameChange,
                            label = { Text("نام دسته") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                            text = "انتخاب رنگ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = theme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        colorOptions.forEach { colorCode ->
                            val color = Color(android.graphics.Color.parseColor(colorCode))
                            Box(
                                    modifier =
                                            Modifier.size(32.dp)
                                                    .clip(
                                                            SquircleShape(
                                                                    8,
                                                                    smoothing =
                                                                            CornerSmoothing.Medium
                                                            )
                                                    )
                                                    .background(color)
                                                    .clickable { onColorChange(colorCode) },
                                    contentAlignment = Alignment.Center
                            ) {
                                if (colorCode == selectedColor) {
                                    Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onSave) { Text(if (isEditing) "ذخیره" else "افزودن") }
            },
            dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } },
            containerColor = theme.surface
    )
}
