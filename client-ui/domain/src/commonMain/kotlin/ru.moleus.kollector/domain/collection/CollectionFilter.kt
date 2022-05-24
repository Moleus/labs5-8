package ru.moleus.kollector.domain.collection

import collection.CollectionChangelist
import collection.CollectionWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.ValueObserver
import com.arkivanov.decompose.value.reduce
import model.data.Flat
import model.data.Model
import java.time.LocalDateTime

class CollectionFilter(collectionWrapper: CollectionWrapper<Flat>) {
    private val objectsCollection: MutableSet<Flat> = HashSet(collectionWrapper.collection)
    private val collectionValue = MutableValue(objectsCollection)

    val collection: Set<Flat> = objectsCollection
    private var collectionVersion = collectionWrapper.version

    /**
     * Returns collection creation time
     */
    val creationDateTime: LocalDateTime = collectionWrapper.creationDateTime
    private val changesApplier: ChangesApplier<Flat> = ChangesApplier(target = objectsCollection)

    fun subscribeOnUpdate(onUpdate: ValueObserver<Set<Flat>>) {
        collectionValue.subscribe { collection -> onUpdate(collection) }
    }

    fun applyChangelist(changelist: CollectionChangelist<Flat>) {
        collectionVersion = changelist.latestVersion
        changesApplier.apply(changelist = changelist)
        collectionValue.reduce { objectsCollection }
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