package common.context

import commands.ExecutionResult

interface CommandExecutor {
    fun execute(
        payload: commands.ExecutionPayload
    ) : ExecutionResult
}