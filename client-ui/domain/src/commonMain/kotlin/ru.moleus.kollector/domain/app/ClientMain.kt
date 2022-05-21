package ru.moleus.kollector.domain.app

import commands.CommandManagerImpl
import commands.CommandNameToInfo
import common.context.Exchanger
import communication.Transceiver
import ru.moleus.kollector.domain.exceptions.ResponseCodeException
import ru.moleus.kollector.domain.commands.pcommands.*
import ru.moleus.kollector.domain.communication.*
import common.exceptions.InvalidCredentialsException
import ru.moleus.kollector.domain.collection.CollectionFilter
import ru.moleus.kollector.domain.communication.ClientTransceiver
import kotlin.jvm.JvmStatic
import java.io.*

object ClientMain {
    @JvmStatic
    fun main(args: Array<String>) {
        val clientSession: Session = ConnectionSession("localhost", 2222)
        if (!waitForConnection(clientSession)) {
            println("Connection failed. Timeout reached.")
            return
        }
        val clientTransceiver: Transceiver = ClientTransceiver(clientSession.getSocketChannel())
        val exchanger: Exchanger = ClientExchanger(clientTransceiver, clientSession)
        val collectionFilter: CollectionFilter
        try {
            exchanger.requestFullCollection()
            collectionFilter = CollectionFilter(exchanger.receiveFullCollection())
        } catch (e: IOException) {
            println("Failed to load collection. Exiting with error: " + e.message)
            return
        } catch (e: InvalidCredentialsException) {
            println("Failed to load collection. Exiting with error: " + e.message)
            return
        }
        val clientCommandManager = CommandManagerImpl()
//        val userConsole: client.Console = UserConsole(writer, clientCommandManager, exchanger, collectionFilter)
        val authenticator: Authenticator = ClientAuthenticator(exchanger)
        clientCommandManager.registerCommands(
            Help(clientCommandManager),
//            Exit(userConsole),
//            ExecuteScript(userConsole),
            Info(collectionFilter),
            FilterContainsName(collectionFilter),
            PrintUniqueNumberOfRooms(collectionFilter),
            PrintFieldDescendingNew(collectionFilter),
            Login(authenticator),
            Register(authenticator)
        )
        val commandNameToInfo: CommandNameToInfo = try {
            getAccessibleCommandsInfo(exchanger)
        } catch (e: Exception) {
            when(e) {
                is ResponseCodeException, is IOException, is InvalidCredentialsException -> {
                    println("Can't get accessible commands: " + e.message)
                    return
                }
                else -> throw e
            }
        }
        clientCommandManager.addCommandsInfo(commandNameToInfo)
//        userConsole.run()
    }

    private fun waitForConnection(clientSession: Session): Boolean {
        return clientSession.reconnect(15)
    }

    @Throws(ResponseCodeException::class, IOException::class, InvalidCredentialsException::class)
    private fun getAccessibleCommandsInfo(exchanger: Exchanger): CommandNameToInfo {
        exchanger.requestAccessibleCommandsInfo()
        return exchanger.receiveAccessibleCommandsInfo()
    }
}