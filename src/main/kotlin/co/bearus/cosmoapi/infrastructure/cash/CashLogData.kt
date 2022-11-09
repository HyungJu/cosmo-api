package co.bearus.cosmoapi.infrastructure.cash

import co.bearus.cosmoapi.domain.cash.ChargeLog
import co.bearus.cosmoapi.domain.cash.ChargeType
import java.time.Instant
import java.util.*

data class CashLogData(
    val uuid: UUID,
    val chargeType: ChargeType,
    val value: Int,
    val time: Instant
) {
    fun toDomain(): ChargeLog {
        return ChargeLog.of(
            type = chargeType,
            value = value,
            chargedAt = time
        )
    }
}
