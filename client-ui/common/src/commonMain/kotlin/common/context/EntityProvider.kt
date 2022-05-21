package common.context

import model.data.Flat

interface EntityProvider {
    fun getAll(): List<Flat>
    fun getById(id : Long): Flat
}