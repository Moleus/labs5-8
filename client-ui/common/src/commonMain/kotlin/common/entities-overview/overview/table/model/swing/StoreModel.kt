package common.`entities-overview`.overview.table.model.swing
//
//import java.awt.event.MouseAdapter
//import java.awt.event.MouseEvent
//import javax.swing.JTable
//import javax.swing.table.TableModel
//import javax.swing.table.TableRowSorter

//import demo.bigLazyTable.newDemo.data.database.SuperstoreDatabase
//import demo.bigLazyTable.newDemo.data.service.Superstore


//class SuperstoreModel(superstore: Superstore) : BaseModel<FlatStoreLabels>(FlatStoreLabels.TITLE) {
//    override val id = IntegerAttribute(
//        model = this,
//        label = FlatStoreLabels.ID,
//        value = superstore.RowID,
//        databaseField = SuperstoreDatabase.RowID
//    )
//
//    private val name = StringAttribute(
//        model = this,
//        label = FlatStoreLabels.NAME,
//        value = superstore.Category,
//        databaseField = SuperstoreDatabase.Category
//    )
//
//    override val displayedAttributesInTable = listOf(
//        id,
//        name,
//    )
//}
//fun swingTable(
//    entities: List<Entity>,
//    model: TableModel,
//    rowSorter: TableRowSorter<FlatTableModel>,
//    onRowClick: (id: Long) -> Unit
//) =
//    JTable().apply {
//        this.model = model
//        this.rowSorter = rowSorter
//        this.addMouseListener(object : MouseAdapter() {
//            override fun mouseClicked(e: MouseEvent?) {
//                e?.let {
//                    val row = this@apply.rowAtPoint(it.point)
//                    val id = entities[row].id
//                    onRowClick(id)
//                }
//            }
//        })
//    }
//
