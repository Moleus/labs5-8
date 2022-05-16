package data

interface EntityProvider {
    fun getAll(): List<Flat>
    fun getById(id : Long): Flat
//
//    override fun get(id: Long): Entity =
//        entities.find { it.id == id } ?: throw IllegalArgumentException("$id not present in entities list")
//
//    override fun getFilteredCount(filters: List<Filter>): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun getPage(startIndex: Int, pageSize: Int, filters: List<Filter>, sort: Sort?): List<Entity> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getTotalCount(): Int = entities.size
//
//    override fun indexOf(id: Long, filters: List<Filter>): Int {
//        TODO("Not yet implemented")
//    }
}