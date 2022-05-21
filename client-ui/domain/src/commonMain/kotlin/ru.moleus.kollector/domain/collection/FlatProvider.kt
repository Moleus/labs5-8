package ru.moleus.kollector.domain.collection

import common.context.EntityProvider
import model.data.Flat
import ru.moleus.kollector.domain.collection.CollectionFilter

class FlatProvider(
    collectionFilter: CollectionFilter
) : EntityProvider {
    override fun getAll(): List<Flat> {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Flat {
        TODO("Not yet implemented")
    }
}