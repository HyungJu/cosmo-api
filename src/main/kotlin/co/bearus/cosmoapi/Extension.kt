package co.bearus.cosmoapi

fun <T> List<T>.push(value: T): List<T> {
    val clonedList = ArrayList(this)
    clonedList.add(value)
    return clonedList
}