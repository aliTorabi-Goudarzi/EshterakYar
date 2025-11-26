package ir.dekot.eshterakyar.core.database

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: User): Long
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun getUserById(userId: Long): User?
    suspend fun getCurrentUser(): User?
    fun getCurrentUserFlow(): Flow<User?>
    suspend fun getAllUsers(): List<User>
    suspend fun getUserCount(): Int
}

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    override suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    override suspend fun getCurrentUser(): User? {
        return userDao.getCurrentUser()
    }

    override fun getCurrentUserFlow(): Flow<User?> {
        return userDao.getCurrentUserFlow()
    }

    override suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    override suspend fun getUserCount(): Int {
        return userDao.getUserCount()
    }
}