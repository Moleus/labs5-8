package root.app

import data.EntityProvider
import data.Flat

class MockEntityProvider : EntityProvider {
    private val entitiesList: List<Flat> = listOf(
        Flat(id = 0, name = "Alice0"),
        Flat(id = 1, name = "Alice1"),
        Flat(id = 2, name = "Alice2"),
        Flat(id = 3, name = "Alice3"),
        Flat(id = 4, name = "Alice4"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
        Flat(id = 5, name = "Alice5"),
    )

    override fun getAll(): List<Flat> = entitiesList
    override fun getById(id: Long): Flat = entitiesList.first { it.id == id }
}