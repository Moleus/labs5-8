package overview.ui.table.swing

//            Surface(
//                border = BorderStroke(1.dp, Color.LightGray),
//                contentColor = Color.Transparent,
//                modifier = Modifier.width(cellWidth(columnIndex))
//            ) {

//    val tableModel = FlatTableModel()
//    val sortKeys: List<RowSorter.SortKey> = listOf(
//        RowSorter.SortKey(4, SortOrder.ASCENDING),
//        RowSorter.SortKey(0, SortOrder.ASCENDING)
//    )
//    val rowSorter = TableRowSorter(tableModel)
//    rowSorter.sortKeys = sortKeys;
//
//    val entitiesTable = remember(entities) {
//        swingTable(
//            entities = entities,
//            model = FlatTableModel(),
//            rowSorter = rowSorter,
//            component::onEntityClicked
//        )
//    }


//    SwingPanel(
//        background = Color.White,
//        modifier = modifier.fillMaxSize().background(Color.Blue),
//        factory = {
//            entitiesTable
//            }
//    )
