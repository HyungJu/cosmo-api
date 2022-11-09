package co.bearus.cosmoapi.core

class TrackableList<T> : ArrayList<T>() {
    private val addedElements = mutableListOf<T>()
    private val removedElements = mutableListOf<T>()

    override fun add(element: T): Boolean {
        addedElements.add(element)
        return super.add(element)
    }

    override fun remove(element: T): Boolean {
        removedElements.add(element)
        return super.remove(element)
    }

    fun getAddedElements(): List<T> {
        return addedElements
    }

    fun getRemovedElements(): List<T> {
        return removedElements
    }

    operator fun plusAssign(element: T) {
        add(element)
    }

    operator fun plus(element: T): TrackableList<T> {
        val newList = TrackableList<T>()
        newList.addAll(this)
        newList.add(element)

        return newList
    }
}