package ru.moleus.kollector.domain.collection

import com.arkivanov.decompose.value.ValueObserver
import common.context.EntityProvider
import model.data.Flat

class FlatProvider(
    private val collectionFilter: CollectionFilter
) : EntityProvider {
    override fun getAll(): List<Flat> = collectionFilter.collection.toList()

    override fun subscribe(onUpdate: ValueObserver<Set<Flat>>) {
        collectionFilter.subscribeOnUpdate(onUpdate)
    }

    override fun getById(id: Long): Flat {
        return collectionFilter.collection.find { it.id == id } ?: throw NoSuchElementException("No flat with id: $id")
    }
}