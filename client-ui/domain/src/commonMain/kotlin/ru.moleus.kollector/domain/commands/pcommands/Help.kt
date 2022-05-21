package ru.moleus.kollector.domain.commands.pcommands

import commands.*

class Help(private val commandManager: CommandManager) : AbstractCommand(
    CommandInfo.of(
        "help",
        "Shows info about accessible commands",
        true,
        0,
        CommandType.OTHER,
        ExecutionMode.CLIENT
    )
) {
    override fun execute(payload: ExecutionPayload): ExecutionResult {
        val nameToCommand = commandManager.userAccessibleCommandsInfo
        val longestNameLength: Int = nameToCommand.maxOf { it.value.name.length }
        val commandsWithDescriptions: String = nameToCommand.values.map {
           info ->
            String.format("%" + longestNameLength + "s: %s", info.name, info.description)
        }.joinToString(separator = System.lineSeparator())
        return ExecutionResult.valueOf(true, commandsWithDescriptions)
    }
}