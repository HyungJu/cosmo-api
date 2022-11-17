package co.bearus.cosmoapi.infrastructure

import co.bearus.cosmoapi.core.TrackableList
import co.bearus.cosmoapi.domain.User
import co.bearus.cosmoapi.domain.UserRepository
import co.bearus.cosmoapi.domain.cash.Cash
import co.bearus.cosmoapi.domain.warning.Warning
import co.bearus.cosmoapi.infrastructure.cash.CashData
import co.bearus.cosmoapi.infrastructure.cash.CashDataDao
import co.bearus.cosmoapi.infrastructure.cash.CashLogData
import co.bearus.cosmoapi.infrastructure.warning.WarningData
import co.bearus.cosmoapi.infrastructure.warning.WarningDataDao
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepositoryImpl(
    private val warningDataDao: WarningDataDao,
    private val cashDataDao: CashDataDao
) : UserRepository {

    override fun findByUsername(username: String): User {
        val uuid = warningDataDao.getUUIDbyUsername(username)

        return findByUUID(uuid)
    }

    override fun findByUUID(uuid: UUID): User {
        val warningData = warningDataDao.getUserByUUID(uuid)
        val cashData = cashDataDao.getCashDataByUUID(uuid)
        val cashLogs = cashDataDao.getCashLogByUUID(uuid)

        return User(
            uuid = warningData.uuid,
            username = warningData.nickname,
            warning = Warning.of(warningData.warning),
            cash = Cash.of(
                value = cashData.value,
                cultureLandFailureCount = cashData.cultureLandFailureCount,
                chargeLog = cashLogs.mapTo(TrackableList(), CashLogData::toDomain)
            )
        )
    }

    override fun findAll(): List<User> {
        val warningData = warningDataDao.getAllUser()
        val cashLogs = cashDataDao.getAllCashData()
        val cashData = cashDataDao.getAllCash()

        return warningData.map {
            User(
                uuid = it.key,
                username = it.value.nickname,
                warning = Warning.of(it.value.warning),
                cash = Cash.of(
                    value = cashData[it.key]?.value ?: 0,
                    cultureLandFailureCount = cashData[it.key]?.cultureLandFailureCount ?: 0,
                    chargeLog = cashLogs[it.key]?.mapTo(TrackableList(), CashLogData::toDomain) ?: TrackableList()
                )
            )
        }
    }

    override fun save(user: User) {
        try {
            warningDataDao.updateUser(WarningData(user.uuid, user.warning.amount, user.username))
            cashDataDao.updateCacheData(CashData(user.uuid, user.cash.value, user.cash.cultureLandFailureCount))
            user.cash.chargeLog.forEach { it ->
                cashDataDao.insertCashLog(
                    CashLogData(
                        user.uuid,
                        it.type,
                        it.value,
                        it.chargedAt
                    )
                )
            }
        } catch (exception: Exception) {

        }
    }
}