package co.bearus.cosmoapi.domain.cash

import java.time.Instant

class ChargeLog private constructor(
    val type: ChargeType,
    val value: Int,
    val chargedAt: Instant
) {
    companion object {
        fun of(type: ChargeType, value: Int, chargedAt: Instant = Instant.now()) = ChargeLog(type, value, chargedAt)
    }

    override fun toString(): String {
        return "ChargeLog(type=$type, value=$value, chargedAt=$chargedAt)"
    }
}