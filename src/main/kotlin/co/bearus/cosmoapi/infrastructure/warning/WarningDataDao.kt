package co.bearus.cosmoapi.infrastructure.warning

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.sql.DriverManager
import java.util.*

@Component
class WarningDataDao(
    @Value("\${cosmo.jdbc.url}/bungeecordbridge")
    val jdbcUrl: String
) {
    private val connection = DriverManager.getConnection(jdbcUrl)

    fun getUUIDbyUsername(username: String): UUID {
        val statement = connection.prepareStatement("SELECT uuid FROM warning_data WHERE nickname = ?")
        statement.setString(1, username)
        val resultSet = statement.executeQuery()
        if (!resultSet.next()) throw RuntimeException("User not found")

        return UUID.fromString(resultSet.getString("uuid"))
    }

    fun getUserByUUID(uuid: UUID): WarningData {
        val statement = connection.prepareStatement("SELECT * FROM warning_data WHERE uuid = ?")
        statement.setString(1, uuid.toString())

        statement.executeQuery().use { resultSet ->
            if (!resultSet.next()) throw RuntimeException("User not found")

            return WarningData(
                UUID.fromString(resultSet.getString("uuid")),
                resultSet.getInt("warning"),
                resultSet.getString("nickname")
            )
        }
    }

    fun getAllUser(): Map<UUID, WarningData> {
        val map = mutableMapOf<UUID, WarningData>()

        val statement = connection.prepareStatement("SELECT * FROM warning_data")

        statement.executeQuery().use { resultSet ->
            while (resultSet.next()) {
                val uuid = UUID.fromString(resultSet.getString("uuid"))
                map[uuid] = WarningData(
                    uuid,
                    resultSet.getInt("warning"),
                    resultSet.getString("nickname")
                )
            }
        }

        return map
    }

    fun updateUser(warningData: WarningData) {
        val statement = connection.prepareStatement(
            "UPDATE warning_data SET warning = ?, nickname = ? WHERE uuid = ?"
        )
        statement.setInt(1, warningData.warning)
        statement.setString(2, warningData.nickname)
        statement.setString(3, warningData.uuid.toString())

        statement.executeUpdate()
    }
}