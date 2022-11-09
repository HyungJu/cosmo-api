package co.bearus.cosmoapi.infrastructure.cash

import co.bearus.cosmoapi.core.TrackableList
import co.bearus.cosmoapi.domain.cash.ChargeType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.sql.DriverManager
import java.util.*

@Component
class CashDataDao(
    @Value("\${cosmo.jdbc.url}/sponsor")
    val jdbcUrl: String
) {
    private val connection = DriverManager.getConnection(jdbcUrl)

    fun getCashLogByUUID(uuid: UUID): TrackableList<CashLogData> {
        val cashLogList: TrackableList<CashLogData> = TrackableList()
        val statement = connection.prepareStatement("SELECT * from logs WHERE uuid = ?")

        statement.setString(1, uuid.toString())
        statement.executeQuery().use { resultSet ->
            while (resultSet.next()) {
                val sign = resultSet.getString("sign")
                val value = resultSet.getInt("value")
                cashLogList.add(
                    CashLogData(
                        uuid = UUID.fromString(resultSet.getString("uuid")),
                        chargeType = if (resultSet.getInt("charge") == 0) ChargeType.CULTURELAND else ChargeType.SYSTEM,
                        value = if (sign == "-") -value else +value,
                        time = resultSet.getTimestamp("time").toInstant()
                    )
                )
            }
        }

        return cashLogList
    }

    fun getCashDataByUUID(uuid: UUID): CashData {
        val statement = connection.prepareStatement(
            "SELECT uuid, value, failed as cultureLandFailureCount FROM cash WHERE uuid = ?"
        )
        statement.setString(1, uuid.toString())

        statement.executeQuery().use { resultSet ->
            if (!resultSet.next()) throw RuntimeException("User not found")

            return CashData(
                uuid = UUID.fromString(resultSet.getString("uuid")),
                value = resultSet.getInt("value"),
                cultureLandFailureCount = resultSet.getInt("cultureLandFailureCount")
            )
        }
    }

    fun getAllCashData(): Map<UUID, TrackableList<CashLogData>> {
        val map = mutableMapOf<UUID, TrackableList<CashLogData>>()

        val statement =
            connection.prepareStatement(
                "SELECT * from logs"
            )

        statement.executeQuery().use { resultSet ->
            while (resultSet.next()) {
                val sign = resultSet.getString("sign")
                val value = resultSet.getInt("value")
                val uuid = UUID.fromString(resultSet.getString("uuid"))

                map.computeIfAbsent(uuid) { TrackableList() }.add(
                    CashLogData(
                        uuid = uuid,
                        chargeType = if (resultSet.getInt("charge") == 0) ChargeType.CULTURELAND else ChargeType.SYSTEM,
                        value = if (sign == "-") -value else +value,
                        time = resultSet.getTimestamp("time").toInstant()
                    )
                )
            }
        }

        return map
    }

    fun getAllCash(): Map<UUID, CashData> {
        val map = mutableMapOf<UUID, CashData>()

        val statement = connection.prepareStatement("SELECT uuid, value, failed as cultureLandFailureCount FROM cash")


        statement.executeQuery().use { resultSet ->
            while (resultSet.next()) {
                val uuid = UUID.fromString(resultSet.getString("uuid"))
                map[uuid] = CashData(
                    uuid,
                    value = resultSet.getInt("value"),
                    cultureLandFailureCount = resultSet.getInt("cultureLandFailureCount")
                )
            }
        }

        return map
    }

    fun insertCashLog(cashLogData: CashLogData) {
        val statement =
            connection.prepareStatement("INSERT INTO logs (uuid, sign, value, charge, time) VALUES (?, ?, ?, ?, ?)")
        statement.setString(1, cashLogData.uuid.toString())
        statement.setString(2, if (cashLogData.value < 0) "-" else "+")
        statement.setInt(3, Math.abs(cashLogData.value))
        statement.setInt(4, if (cashLogData.chargeType == ChargeType.CULTURELAND) 0 else 1)
        statement.setTimestamp(5, java.sql.Timestamp.from(cashLogData.time))

        statement.executeUpdate()
    }

    fun updateCacheData(cacheData: CashData) {
        val statement = connection.prepareStatement("UPDATE cash SET value = ?, failed = ? WHERE uuid = ?")
        statement.setInt(1, cacheData.value)
        statement.setInt(2, cacheData.cultureLandFailureCount)
        statement.setString(3, cacheData.uuid.toString())

        statement.executeUpdate()
    }
}