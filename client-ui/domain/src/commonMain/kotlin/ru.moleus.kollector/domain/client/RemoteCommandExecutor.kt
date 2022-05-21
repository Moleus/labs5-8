package ru.moleus.kollector.domain.client

import commands.ExecutionPayload
import commands.ExecutionResult
import common.context.CommandExecutor
import common.context.Exchanger

class RemoteCommandExecutor(
    private val exchanger: Exchanger
) : CommandExecutor {

    override fun execute(
        payload: ExecutionPayload
    ) : ExecutionResult {
        return exchanger.requestCommandExecution(payload)
    }
}
