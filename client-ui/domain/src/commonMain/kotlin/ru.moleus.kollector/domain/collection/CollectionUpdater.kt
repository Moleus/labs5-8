package ru.moleus.kollector.domain.collection

import collection.CollectionChangelist
import common.context.Exchanger
import model.data.Flat

class CollectionUpdater(
    private val exchanger: Exchanger,
    private val target: CollectionFilter
) : Runnable {

    override fun run() {
        while (true) {
            try {
                val changelist: CollectionChangelist<Flat> =
                    exchanger.requestCollectionChanges(target.getCollectionVersion())
                target.applyChangelist(changelist)
            } catch (_: Exception) {
            }
            Thread.sleep(5000)
        }
    }
}