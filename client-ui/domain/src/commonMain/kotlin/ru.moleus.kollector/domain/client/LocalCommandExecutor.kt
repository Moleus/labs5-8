package ru.moleus.kollector.domain.client

import commands.CommandManager
import commands.ExecutionPayload
import commands.ExecutionResult
import common.context.CommandExecutor

class LocalCommandExecutor(
    private val commandManager: CommandManager,
): CommandExecutor {

    override fun execute(
        payload: ExecutionPayload,
    ) : ExecutionResult {
        require(commandManager.isRegistered(payload.commandName)) { "Command can't be executed locally" }
        return commandManager.executeCommand(payload)
    }
}