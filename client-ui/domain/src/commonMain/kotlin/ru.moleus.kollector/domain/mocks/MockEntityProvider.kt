package ru.moleus.kollector.domain.mocks

import common.context.EntityProvider
import model.data.Flat

class MockEntityProvider : EntityProvider {
    private val entitiesList: List<Flat> = listOf(
//        Flat().apply {
//            setArea(1)
//            setCoordinates(Coordinates(1.0, 2))
//        },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
        Flat().apply { setArea(12) },
//        Flat(id = 0, name = "Alice0"),
//        Flat(id = 1, name = "Alice1"),
//        Flat(id = 2, name = "Alice2"),
//        Flat(id = 3, name = "Alice3"),
//        Flat(id = 4, name = "Alice4"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
//        Flat(id = 5, name = "Alice5"),
    )

    override fun getAll(): List<Flat> = entitiesList
    override fun getById(id: Long): Flat = entitiesList.first { it.id == id }
}