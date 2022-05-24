package ru.moleus.kollector.ui.compose.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.moleus.kollector.feature.overview.details.EntityDetails
import ru.moleus.kollector.ui.compose.builder.UpdateButton
import ru.moleus.kollector.ui.compose.common.ScrollableList

@Composable
fun DetailsUi(component: EntityDetails) {
    val model by component.model.subscribeAsState()
    val entityModel = model.entityModel

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (model.isToolbarVisible) {
                TopBarWithBack(
                    text = "Flat details", onBack = component::onCloseClicked,
                    actions = {
                        UpdateButton { component.onUpdateClicked(entityModel.id.value) }
                    },
                )
            } else {
                TopBarWithSubmit(
                    text = "Flat details",
                    actions = {
                        UpdateButton { component.onUpdateClicked(entityModel.id.value) }
                    }
                )
            }
        }
        ScrollableList {
            items(items = entityModel.displayedAttributesInTable) { attribute ->
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 3.dp)) {
                    Text(
                        text = "${attribute.label}: ",
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .width(200.dp)
                            .padding(24.dp)
                    )
                    Text(
                        text = attribute.getValueAsText(),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    )
                }
            }
        }
    }
}