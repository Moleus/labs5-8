//package overview.table.model.swing
//
//import javax.swing.table.AbstractTableModel
//
//class FlatTableModel : AbstractTableModel() {
//    // TableModel's column names
//    private val columnNames = arrayOf(
//        "CLUB", "MP", "W", "D", "L", "GF", "GA", "GD", "PTS"
//    )
//
//    // TableModel's data
//    private val data = arrayOf(
//        arrayOf<Any>("Chelsea", 8, 6, 1, 1, 16, 3, 13, 19),
//        arrayOf<Any>("Liverpool", 8, 5, 3, 0, 22, 6, 16, 18),
//        arrayOf<Any>("Manchester City", 8, 5, 2, 1, 16, 3, 13, 17),
//        arrayOf<Any>("Brighton", 8, 4, 3, 1, 8, 5, 3, 15),
//        arrayOf<Any>("Tottenham", 8, 5, 0, 3, 9, 12, -3, 15)
//    )
//
//    /**
//     * Returns the number of rows in the table model.
//     */
//    override fun getRowCount(): Int {
//        return data.size
//    }
//
//    /**
//     * Returns the number of columns in the table model.
//     */
//    override fun getColumnCount(): Int {
//        return columnNames.size
//    }
//
//    /**
//     * Returns the column name for the column index.
//     */
//    override fun getColumnName(column: Int): String {
//        return columnNames[column]
//    }
//
//    /**
//     * Returns data type of the column specified by its index.
//     */
//    override fun getColumnClass(columnIndex: Int): Class<*> {
//        return getValueAt(0, columnIndex).javaClass
//    }
//
//    /**
//     * Returns the value of a table model at the specified
//     * row index and column index.
//     */
//    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
//        return data[rowIndex][columnIndex]
//    }
//}