package editor.integration

//import ClientContext
//import com.arkivanov.decompose.ComponentContext
//import com.arkivanov.decompose.value.MutableValue
//import data.EntityProvider
//import common.`entities-overview`.editor
//import common.`entities-overview`.editorMode
//import toTableModel
//
//class UpdaterComponent(
//    componentContext: ComponentContext,
//    clientContext: ClientContext,
//    entityProvider: EntityProvider,
//    entityId: Long,
//    onClose: () -> Unit,
//    onSubmit: () -> Unit,
//) : AbstractEditorComponent(
//    componentContext,
//    clientContext,
//    onClose,
//    onSubmit,
//) {
//
//    private val entityModel = toTableModel(entityProvider.getById(id = entityId))
//
//    override val model =
//        MutableValue(
//            Editor.Model(
//                mode = EditorMode.UPDATE,
//                entityModel = entityModel,
//                filledValues = entityModel.displayedAttributesInTable.associate { it.label to it.getValueAsText() }
//            )
//        )
//}