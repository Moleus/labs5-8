package ru.moleus.kollector.domain.client

import ru.moleus.kollector.domain.exceptions.ScriptExecutionException

interface ScriptExecutor {
    @Throws(ScriptExecutionException::class)
    fun executeScript(filename: String)
}