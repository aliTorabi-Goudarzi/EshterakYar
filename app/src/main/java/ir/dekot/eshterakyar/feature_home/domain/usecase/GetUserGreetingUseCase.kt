package ir.dekot.eshterakyar.feature_home.domain.usecase

import ir.dekot.eshterakyar.feature_home.domain.model.TimeOfDay
import ir.dekot.eshterakyar.feature_home.domain.model.UserGreeting
import java.text.SimpleDateFormat
import java.util.*

class GetUserGreetingUseCase {

    fun execute(userName: String): UserGreeting {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val timeOfDay = when (hour) {
            in 4..11 -> TimeOfDay.MORNING
            in 11..16 -> TimeOfDay.NOON
            in 16..19 -> TimeOfDay.AFTERNOON
            in 19 .. 21 -> TimeOfDay.EVENING
            else -> TimeOfDay.NIGHT
        }

        return UserGreeting(
            userName = userName,
            timeOfDay = timeOfDay,
        )
    }
}
