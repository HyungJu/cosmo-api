package co.bearus.cosmoapi.domain

import java.util.*

interface UserRepository {
    fun findByUUID(uuid: UUID): User
    fun findByUsername(username: String): User
    fun findAll(): List<User>
    fun save(user: User)
}