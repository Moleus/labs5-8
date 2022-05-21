package ru.moleus.kollector.domain.commands.pcommands

import commands.*
import ru.moleus.kollector.domain.client.ScriptExecutor
import ru.moleus.kollector.domain.exceptions.ScriptExecutionException

class ExecuteScript(executor: ScriptExecutor) : AbstractCommand(
    CommandInfo.of(
        "execute_script",
        "Run all commands from specified file",
        true,
        1,
        CommandType.OTHER,
        ExecutionMode.CLIENT
    )
) {
    private val executor: ScriptExecutor

    init {
        this.executor = executor
    }

    override fun execute(payload: ExecutionPayload): ExecutionResult {
        val fileName: String = payload.inlineArg
        try {
            executor.executeScript(fileName)
        } catch (e: ScriptExecutionException) {
            return ExecutionResult.valueOf(false, e.message)
        }
        return ExecutionResult.valueOf(true, "Script execution started")
    }
}