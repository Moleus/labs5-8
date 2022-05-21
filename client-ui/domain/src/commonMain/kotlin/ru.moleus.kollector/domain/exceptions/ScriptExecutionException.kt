package ru.moleus.kollector.domain.exceptions

class ScriptExecutionException(message: String?, scriptName: String?) :
    java.lang.Exception(String.format("Can't execute script '%s'. %s", scriptName, message))