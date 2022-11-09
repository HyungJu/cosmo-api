package co.bearus.cosmoapi.infrastructure.warning

import java.util.*

data class WarningData(
    val uuid: UUID,
    val warning: Int,
    val nickname: String
)
