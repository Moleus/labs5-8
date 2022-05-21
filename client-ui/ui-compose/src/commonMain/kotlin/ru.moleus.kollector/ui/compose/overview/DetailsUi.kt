package ru.moleus.kollector.ui.compose.overview

import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.moleus.kollector.feature.overview.details.EntityDetails

@Composable
fun DetailsUi(component: EntityDetails, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()
    val entityModel = model.entityModel

    Column(modifier = modifier.fillMaxSize()) {
        if (model.isToolbarVisible) {
            TopAppBar(
                title = { Text(text = "Элемент") },
                navigationIcon = {
                    IconButton(onClick = component::onCloseClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
        for (attribute in entityModel.displayedAttributesInTable) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${attribute.label}: ",
                    modifier = Modifier
                        .width(200.dp)
                        .padding(24.dp)
                )
                Text(
                    text = attribute.getValueAsText(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
            }
        }
    }
}
