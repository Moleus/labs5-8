package ru.moleus.kollector.domain.commands.pcommands

import commands.*
import ru.moleus.kollector.domain.collection.CollectionFilter
import java.util.*

class PrintFieldDescendingNew(private val collectionManager: CollectionFilter) : AbstractCommand(
    CommandInfo.of(
        "print_field_descending_new",
        "Print 'new' values in descending order",
        true,
        0,
        CommandType.OTHER,
        ExecutionMode.CLIENT
    )
) {
    override fun execute(payload: ExecutionPayload): ExecutionResult {
        val fieldsNew = collectionManager.fieldDescendingNew
        val message = Arrays.toString(fieldsNew)
        return ExecutionResult.valueOf(true, message)
    }
}