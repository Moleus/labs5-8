package ru.moleus.kollector.domain.commands.pcommands

import commands.*
import ru.moleus.kollector.domain.collection.CollectionFilter

class Info(private val collectionFilter: CollectionFilter) : AbstractCommand(
    CommandInfo.of(
        "info",
        "Displays general information about collection",
        true,
        0,
        CommandType.OTHER,
        ExecutionMode.CLIENT
    )
) {
    override fun execute(payload: ExecutionPayload): ExecutionResult {
        val collectionInformation = String.format(
            "Collection of Flats: %nInit time: %s%nVersion: %s%nNumber of elements: %s%n",
            collectionFilter.creationDateTime,
            collectionFilter.getCollectionVersion(),
            collectionFilter.size
        )
        return ExecutionResult.valueOf(true, collectionInformation)
    }
}