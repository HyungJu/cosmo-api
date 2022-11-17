package co.bearus.cosmoapi.domain.cash

class Cash private constructor(
    val value: Int,
    val cultureLandFailureCount: Int,
    val chargeLog: MutableList<ChargeLog>,
) {
    companion object {
        fun of(value: Int, cultureLandFailureCount: Int, chargeLog: MutableList<ChargeLog>): Cash {
            if (cultureLandFailureCount < 0) {
                throw IllegalArgumentException("CultureLandFailedCount must be greater than or equal to 0")
            }
            return Cash(value, cultureLandFailureCount, chargeLog)
        }
    }

    fun addCash(amount: Int, type: ChargeType): Cash {
        val chargeLog = ChargeLog.of(type, amount)
        this.chargeLog.add(chargeLog)

        return of(
            this.value + amount,
            this.cultureLandFailureCount,
            this.chargeLog
        )
    }

    fun resetCultureLandFailureCount(): Cash {
        return of(
            this.value,
            0,
            this.chargeLog
        )
    }

    override fun toString(): String {
        return "Cash(value=$value, cultureLandFailureCount=$cultureLandFailureCount, chargeLog=$chargeLog)"
    }
}