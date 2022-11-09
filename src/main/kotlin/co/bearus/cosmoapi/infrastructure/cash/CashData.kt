package co.bearus.cosmoapi.infrastructure.cash

import java.util.*

data class CashData(
    val uuid: UUID,
    val value: Int,
    val cultureLandFailureCount: Int
)
