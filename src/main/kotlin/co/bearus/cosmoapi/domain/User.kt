package co.bearus.cosmoapi.domain

import co.bearus.cosmoapi.domain.cash.Cash
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
        this.cash = this.cash.addCash(amount, type)
    }

    fun resetCultureLandFailureCount() {
        this.cash = this.cash.resetCultureLandFailureCount()
    }

    fun addWarning(amount: Int) {
        this.warning = this.warning.add(amount)
    }

    override fun toString(): String {
        return "User(uuid=$uuid, username='$username', cash=$cash, warning=$warning)"
    }
}
