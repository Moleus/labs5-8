package common.`entities-overview`.overview.ui.table

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bigLazyTable.view.table.TableContainer
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import overview.table.EntitiesTable


@Composable
fun TableUi(component: EntitiesTable, modifier: Modifier = Modifier) {
    BigLazyTableUI(component)
}

@Composable
fun BigLazyTableUI(component: EntitiesTable) {
    Row(modifier = Modifier.fillMaxSize()) {
        TableContainer(
            weight = 2f,
            component = component,
        )
    }
}

//
//@Composable
//private fun CellText(text: String, modifier: Modifier = Modifier) {
//    Text(
//        text = text,
//        fontSize = 20.sp,
//        textAlign = TextAlign.Center,
//        modifier = modifier.padding(16.dp),
//        maxLines = 1,
//        overflow = TextOverflow.Ellipsis,
//    )
//}
//
//@Composable
//private fun TableHeader(rowIndex: Int) {
//    if (rowIndex != 0) return
//    Row() {
//        CellText("Header 1")
//    }
//}
//
//@Composable
//private fun selectionColor(): Color =
//    MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
//
//private fun actionButton(
//    text: String,
//    action: () -> Unit
//): JButton {
//    val button = JButton(text)
//    button.alignmentX = Component.CENTER_ALIGNMENT
//    button.addActionListener { action() }
//
//    return button
//}
//
//@Composable
//fun Table(
//    modifier: Modifier = Modifier,
//    rowModifier: Modifier = Modifier,
//    verticalLazyListState: LazyListState = rememberLazyListState(),
//    horizontalScrollState: ScrollState = rememberScrollState(),
//    columnCount: Int,
//    rowCount: Int,
//    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
//    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
//    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> Unit
//) {
//    val columnWidths = remember { mutableStateMapOf<Int, Int>() }
//
//    Box(modifier = modifier.then(Modifier.horizontalScroll(horizontalScrollState))) {
//        LazyColumn(state = verticalLazyListState) {
//            items(rowCount) { rowIndex ->
//                Column {
//                    beforeRow?.invoke(rowIndex)
//
//                    Row(modifier = rowModifier) {
//                        (0 until columnCount).forEach { columnIndex ->
//                            Box(modifier = Modifier.layout { measurable, constraints ->
//                                val placeable = measurable.measure(constraints)
//
//                                val existingWidth = columnWidths[columnIndex] ?: 0
//                                val maxWidth = maxOf(existingWidth, placeable.width)
//
//                                if (maxWidth > existingWidth) {
//                                    columnWidths[columnIndex] = maxWidth
//                                }
//
//                                layout(width = maxWidth, height = placeable.height) {
//                                    placeable.placeRelative(0, 0)
//                                }
//                            }) {
//                                cellContent(columnIndex, rowIndex)
//                            }
//                        }
//                    }
//
//                    afterRow?.invoke(rowIndex)
//                }
//            }
//        }
//    }
//}
//
///**
// * The horizontally scrollable table with header and content.
// * @param columnCount the count of columns in the table
// * @param cellWidth the width of column, can be configured based on index of the column.
// * @param data the data to populate table.
// * @param modifier the modifier to apply to this layout node.
// * @param headerCellContent a block which describes the header cell content.
// * @param cellContent a block which describes the cell content.
// */
//@Composable
//fun <T> Table(
//    columnCount: Int,
//    cellWidth: (index: Int) -> Dp,
//    data: List<T>,
//    modifier: Modifier = Modifier,
//    headerCellContent: @Composable (index: Int) -> Unit,
//    cellContent: @Composable (index: Int, item: T) -> Unit,
//) {
//    Surface(
//        modifier = modifier
//    ) {
//        LazyRow(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            items(columnCount) { columnIndex ->
//                Column {
//                    (0..data.size).forEach { index ->
//                        Surface(
//                            border = BorderStroke(1.dp, Color.LightGray),
//                            contentColor = Color.Transparent,
//                            modifier = Modifier.width(cellWidth(columnIndex))
//                        ) {
//                            if (index == 0) {
//                                headerCellContent(columnIndex)
//                            } else {
//                                cellContent(columnIndex, data[index - 1])
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//data class Person(val name: String, val age: Int, val hasDrivingLicence: Boolean, val email: String)