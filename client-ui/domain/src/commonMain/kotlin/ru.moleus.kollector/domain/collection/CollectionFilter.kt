package ru.moleus.kollector.domain.collection

import collection.CollectionChangelist
import collection.CollectionWrapper
import model.data.Flat
import model.data.Model
import java.time.LocalDateTime

class CollectionFilter(collectionWrapper: CollectionWrapper<Flat>) {
    private lateinit var objectsCollection: MutableSet<Flat>
    private var collectionVersion: Long = 0

    /**
     * Returns collection creation time
     */
    lateinit var creationDateTime: LocalDateTime
    private val changesApplier: ChangesApplier<Flat>

    init {
        loadFullCollection(collectionWrapper)
        changesApplier = ChangesApplier(objectsCollection)
    }

    private fun loadFullCollection(collectionWrapper: CollectionWrapper<Flat>) {
        objectsCollection = HashSet(collectionWrapper.collection)
        creationDateTime = collectionWrapper.creationDateTime
        collectionVersion = collectionWrapper.version
    }

    fun applyChangelist(changelist: CollectionChangelist<Flat>) {
        collectionVersion = changelist.latestVersion
        changesApplier.apply(changelist)
    }

    /**
     * Returns collection version. Used in logic clock syncronization.
     */
    fun getCollectionVersion(): Long {
        return collectionVersion
    }

    /**
     * Returns collection size.
     *
     * @see LinkedHashSet.size
     */
    val size: Int
        get() = objectsCollection.size

    /**
     * Returns array of [Flat] which name contains passed string.
     *
     * @param filter string to lookup in names.
     */
    fun filterContainsName(filter: String): Array<Model> =
        objectsCollection.filter { flat -> flat.name.contains(filter) }.toTypedArray()

    /**
     * Returns a [Set] of unique [Long] values got by [Flat.getNumberOfRooms] from each entry.
     */
    val uniqueNumberOfRooms: Set<Long>
        get() = objectsCollection.map<Flat, Long> { it.numberOfRooms }.toSet()

    /**
     * Returns an array of [Boolean] values in descending order got by [Flat.getNewness] from each entry.
     */
    val fieldDescendingNew: Array<Boolean>
        get() = objectsCollection.reversed().map<Flat, Boolean> { it.newness }.toTypedArray()
}