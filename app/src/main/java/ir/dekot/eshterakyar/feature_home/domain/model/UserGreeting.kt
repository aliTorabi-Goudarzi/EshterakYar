package ir.dekot.eshterakyar.feature_home.domain.model

data class UserGreeting(
    val userName: String,
    val timeOfDay: TimeOfDay,
)

enum class TimeOfDay(val persianName: String) {
    MORNING("صبح"),
    NOON("ظهر"),
    AFTERNOON("بعد از ظهر"),
    EVENING("عصر"),
    NIGHT("شب")
}
