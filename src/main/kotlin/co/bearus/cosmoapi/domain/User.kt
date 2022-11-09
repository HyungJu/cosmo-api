package co.bearus.cosmoapi.domain

import co.bearus.cosmoapi.domain.cash.Cash
import co.bearus.cosmoapi.domain.cash.ChargeLog
import co.bearus.cosmoapi.domain.cash.ChargeType
import co.bearus.cosmoapi.domain.warning.Warning
import java.util.*

class User(
    val uuid: UUID,
    val username: String,
    var cash: Cash,
    var warning: Warning
) {
    fun addCash(amount: Int, type: ChargeType) {
        this.cash = Cash.of(
            amount + this.cash.value,
            this.cash.cultureLandFailureCount,
            this.cash.chargeLog + ChargeLog.of(type, amount)
        )
    }

    fun resetCultureLandFailureCount() {
        this.cash = Cash.of(
            this.cash.value,
            0,
            this.cash.chargeLog
        )
    }

    fun addWarning(amount: Int) {
        this.warning = Warning.of(this.warning.amount + amount)
    }

    override fun toString(): String {
        return "User(uuid=$uuid, username='$username', cash=$cash, warning=$warning)"
    }
}
