package bigLazyTable.view.table

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import overview.ui.table.lazy.model.Attribute

/**
 * @author Marco Sprenger, Livio Näf
 */
@Composable
fun HeaderCell(attribute: Attribute<*>) {
    Cell(
        text = attribute.label,
        width = attribute.tableColumnWidth,
        color = Color.Black,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun TableCell(attribute: Attribute<*>) {
    Cell(
        text = attribute.getValueAsText(),
        width = attribute.tableColumnWidth,
        color = Color.Black,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun LoadingCell(attribute: Attribute<*>) {
    Cell(
        text = "...",
        width = attribute.tableColumnWidth,
        color = Color.Black,
        fontWeight = FontWeight.Normal
    )
}

@Composable
private fun Cell(
    text: String,
    width: Dp,
    color: Color,
    fontWeight: FontWeight,
) {
    Text(
        text = text,
        color = color,
        fontWeight = fontWeight,
        modifier = Modifier
            .width(width)
            .padding(8.dp)
    )
}