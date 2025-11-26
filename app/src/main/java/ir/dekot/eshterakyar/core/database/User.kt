package ir.dekot.eshterakyar.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val lastName: String,
    val phoneNumber: String, // Iran number format: +98 9XX XXX XXXX or 09XX XXX XXXX
    val profilePicture: String? = null, // URL or path to profile image
    val accountCreationDate: Date
)