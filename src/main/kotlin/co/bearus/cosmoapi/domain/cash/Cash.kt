package co.bearus.cosmoapi.domain.cash

import co.bearus.cosmoapi.core.TrackableList

class Cash private constructor(
    val value: Int,
    val cultureLandFailureCount: Int,
    val chargeLog: TrackableList<ChargeLog>,
) {
    companion object {
        fun of(value: Int, cultureLandFailureCount: Int, chargeLog: TrackableList<ChargeLog>): Cash {
            if (cultureLandFailureCount < 0) {
                throw IllegalArgumentException("CultureLandFailedCount must be greater than or equal to 0")
            }
            return Cash(value, cultureLandFailureCount, chargeLog)
        }
    }

    override fun toString(): String {
        return "Cash(value=$value, cultureLandFailureCount=$cultureLandFailureCount, chargeLog=$chargeLog)"
    }
}