package ru.moleus.kollector.domain.client

import interfaces.Exitable
import ru.moleus.kollector.domain.exceptions.ScriptExecutionException

interface Console : Exitable, ScriptExecutor {
    fun run()
    @Throws(ScriptExecutionException::class)
    override fun executeScript(filename: String)
}