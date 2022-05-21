package ru.moleus.kollector.domain.collection

import collection.CollectionChange
import collection.CollectionChangelist
import model.data.Model

class ChangesApplier<T : Model?>(private val target: MutableSet<T>) {
    fun apply(changelist: CollectionChangelist<T>) {
        for (change in changelist.changelist.values) {
            applyChange(change)
        }
    }

    private fun applyChange(change: CollectionChange<T>) {
        target.removeAll(change.removed)
        target.addAll(change.added)
    }
}