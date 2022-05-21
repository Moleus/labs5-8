package ru.moleus.kollector.domain.commands.pcommands

import commands.*
import ru.moleus.kollector.domain.collection.CollectionFilter
import java.util.*

class FilterContainsName(private val collectionFilter: CollectionFilter) : AbstractCommand(
    CommandInfo.of(
        "filter_contains_name",
        "Print elements with specified string in names",
        true,
        1,
        CommandType.OTHER,
        ExecutionMode.CLIENT
    )
) {
    override fun execute(payload: ExecutionPayload): ExecutionResult {
        val filter = payload.inlineArg
        val elements = collectionFilter.filterContainsName(filter)
        val message = Arrays.toString(elements)
        return ExecutionResult.valueOf(true, message)
    }
}