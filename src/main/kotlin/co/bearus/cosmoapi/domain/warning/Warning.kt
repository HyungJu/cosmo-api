package co.bearus.cosmoapi.domain.warning

class Warning private constructor(val amount: Int) {
    companion object {
        fun of(value: Int): Warning = value
            .takeIf { it >= 0 }
            ?.let { Warning(it) }
            ?: throw IllegalArgumentException("Warning count must be greater than 0")
    }

    override fun toString(): String {
        return amount.toString()
    }
}