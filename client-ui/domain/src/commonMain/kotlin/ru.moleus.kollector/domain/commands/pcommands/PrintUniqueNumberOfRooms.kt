package ru.moleus.kollector.domain.commands.pcommands

import commands.*
import ru.moleus.kollector.domain.collection.CollectionFilter

class PrintUniqueNumberOfRooms(private val collectionFilter: CollectionFilter) : AbstractCommand(
    CommandInfo.of(
        "print_unique_number_of_rooms",
        "Print set of 'numberOfRooms' values",
        true,
        0,
        CommandType.OTHER,
        ExecutionMode.CLIENT
    )
) {
    override fun execute(payload: ExecutionPayload): ExecutionResult {
        val numberOfRooms = collectionFilter.uniqueNumberOfRooms
        val message = numberOfRooms.toString()
        return ExecutionResult.valueOf(true, message)
    }
}