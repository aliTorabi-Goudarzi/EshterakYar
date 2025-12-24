package ir.dekot.eshterakyar.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.core.domain.model.JalaliDate
import ir.dekot.eshterakyar.core.domain.utils.DateConverter
import ir.dekot.eshterakyar.core.utils.LocalTheme
import java.time.LocalDate

@Stable
class JalaliDatePickerState(
        initialDate: JalaliDate? = null,
        initialViewDate: JalaliDate = initialDate ?: DateConverter.toJalali(LocalDate.now())
) {
    var selectedDate by mutableStateOf(initialDate)
    var viewYear by mutableIntStateOf(initialViewDate.year)
    var viewMonth by mutableIntStateOf(initialViewDate.month)
    var isYearSelectionMode by mutableStateOf(false)

    fun onDateSelected(date: JalaliDate) {
        selectedDate = date
    }

    fun onNextMonth() {
        if (viewMonth == 12) {
            viewMonth = 1
            viewYear++
        } else {
            viewMonth++
        }
    }

    fun onPreviousMonth() {
        if (viewMonth == 1) {
            viewMonth = 12
            viewYear--
        } else {
            viewMonth--
        }
    }

    fun onYearSelected(year: Int) {
        viewYear = year
        isYearSelectionMode = false
    }

    fun toggleYearSelection() {
        isYearSelectionMode = !isYearSelectionMode
    }

    fun getFullMonthName(): String {
        return JalaliDate(viewYear, viewMonth, 1).monthName()
    }
}

@Composable
fun rememberJalaliDatePickerState(initialDate: JalaliDate? = null): JalaliDatePickerState {
    return remember { JalaliDatePickerState(initialDate) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JalaliDatePickerDialog(
        onDismissRequest: () -> Unit,
        onDateChange: (JalaliDate) -> Unit,
        initialDate: JalaliDate? = null,
        title: String = "انتخاب تاریخ"
) {
    val state = rememberJalaliDatePickerState(initialDate)
    val theme = LocalTheme.current

    DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                Button(
                        onClick = {
                            state.selectedDate?.let { onDateChange(it) }
                            onDismissRequest()
                        },
                        enabled = state.selectedDate != null
                ) { Text("تایید") }
            },
            dismissButton = { TextButton(onClick = onDismissRequest) { Text("لغو") } },
            colors =
                    androidx.compose.material3.DatePickerDefaults.colors(
                            containerColor = theme.surface,
                    )
    ) { JalaliDatePicker(state = state, title = title) }
}

@Composable
fun JalaliDatePicker(state: JalaliDatePickerState, title: String) {
    val theme = LocalTheme.current

    Column(modifier = Modifier.padding(16.dp).background(theme.surface)) {
        // Header
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = theme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            val headerText =
                    state.selectedDate?.let { "${it.day} ${it.monthName()} ${it.year}" }
                            ?: "تاریخی انتخاب نشده"

            Text(
                    text = headerText,
                    style = MaterialTheme.typography.headlineLarge,
                    color = theme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { state.toggleYearSelection() }
            )
        }

        HorizontalDivider(color = theme.onSurfaceVariant.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))

        if (state.isYearSelectionMode) {
            YearSelector(currentYear = state.viewYear, onYearSelected = state::onYearSelected)
        } else {
            CalendarContent(state)
        }
    }
}

@Composable
private fun CalendarContent(state: JalaliDatePickerState) {
    val theme = LocalTheme.current

    Column {
        // Month Navigation
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = state::onPreviousMonth) {
                Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "ماه قبل",
                        tint = theme.onSurface
                )
            }

            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { state.toggleYearSelection() }
            ) {
                Text(
                        text = "${state.getFullMonthName()} ${state.viewYear}",
                        style = MaterialTheme.typography.titleMedium,
                        color = theme.onSurface,
                        fontWeight = FontWeight.Bold
                )
                Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "تغییر سال",
                        modifier = Modifier.size(16.dp).padding(start = 4.dp),
                        tint = theme.onSurfaceVariant
                )
            }

            IconButton(onClick = state::onNextMonth) {
                Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "ماه بعد",
                        tint = theme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Week Days Header
        Row(modifier = Modifier.fillMaxWidth()) {
            val weekDays = listOf("ش", "ی", "د", "س", "چ", "پ", "ج")
            weekDays.forEach { day ->
                Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Days Grid
        // Calculate start day of week
        val firstDayOfMonth =
                remember(state.viewYear, state.viewMonth) {
                    val jalaliDate = JalaliDate(state.viewYear, state.viewMonth, 1)
                    val gregorian = DateConverter.toGregorian(jalaliDate)
                    val dayOfWeek = gregorian.dayOfWeek.value // 1 (Mon) to 7 (Sun)
                    // Mapping to Persian: Sat=0, Sun=1, ... Fri=6
                    // Mon(1) -> 2
                    // Tue(2) -> 3
                    // ...
                    // Sat(6) -> 0
                    // Sun(7) -> 1
                    (dayOfWeek + 1) % 7
                }

        val daysInMonth =
                remember(state.viewYear, state.viewMonth) {
                    JalaliDate.monthLength(state.viewYear, state.viewMonth)
                }

        LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(240.dp),
                userScrollEnabled = false
        ) {
            // Empty cells for offset
            items(firstDayOfMonth) { Spacer(modifier = Modifier.aspectRatio(1f)) }

            items(daysInMonth) { dayIndex ->
                val day = dayIndex + 1
                val date = JalaliDate(state.viewYear, state.viewMonth, day)
                val isSelected = state.selectedDate == date
                val isToday = date == DateConverter.toJalali(LocalDate.now())

                Box(
                        modifier =
                                Modifier.aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(
                                                when {
                                                    isSelected -> theme.primary
                                                    isToday -> theme.primary.copy(alpha = 0.1f)
                                                    else -> Color.Transparent
                                                }
                                        )
                                        .clickable { state.onDateSelected(date) },
                        contentAlignment = Alignment.Center
                ) {
                    Text(
                            text = day.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) theme.onPrimary else theme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun YearSelector(currentYear: Int, onYearSelected: (Int) -> Unit) {
    val theme = LocalTheme.current
    val years = remember { (1350..1450).toList() }
    val listState =
            rememberLazyListState(
                    initialFirstVisibleItemIndex = years.indexOf(currentYear).coerceAtLeast(0)
            )

    LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth().height(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(years) { year ->
            val isSelected = year == currentYear
            TextButton(onClick = { onYearSelected(year) }, modifier = Modifier.fillMaxWidth()) {
                Text(
                        text = year.toString(),
                        style =
                                if (isSelected) MaterialTheme.typography.headlineMedium
                                else MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) theme.primary else theme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
