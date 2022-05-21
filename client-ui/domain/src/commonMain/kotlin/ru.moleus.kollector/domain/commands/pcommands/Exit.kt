package ru.moleus.kollector.domain.commands.pcommands

import commands.*
import interfaces.Exitable

class Exit(private val target: Exitable) : AbstractCommand(
    CommandInfo.of(
        "exit",
        "Exit without saving collection",
        true,
        0,
        CommandType.OTHER,
        ExecutionMode.CLIENT
    )
) {
    override fun execute(payload: ExecutionPayload): ExecutionResult {
        target.exit()
        return ExecutionResult.valueOf(true, "Exit command executed successfully")
    }
}