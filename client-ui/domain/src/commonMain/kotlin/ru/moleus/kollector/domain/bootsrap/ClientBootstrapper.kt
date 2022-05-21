package ru.moleus.kollector.domain.bootsrap

import commands.CommandManager
import commands.CommandManagerImpl
import commands.CommandNameToInfo
import common.context.ClientContext
import common.context.EntityProvider
import common.context.Exchanger
import common.exceptions.InvalidCredentialsException
import ru.moleus.kollector.domain.app.ClientMain
import ru.moleus.kollector.domain.collection.CollectionFilter
import ru.moleus.kollector.domain.collection.FlatProvider
import ru.moleus.kollector.domain.commands.pcommands.*
import ru.moleus.kollector.domain.communication.*
import ru.moleus.kollector.domain.exceptions.ResponseCodeException
import java.io.IOException

object ClientBootstrapper {
    private lateinit var clientExchanger: Exchanger
    private lateinit var clientAuthenticator: Authenticator
    private lateinit var collectionFilter: CollectionFilter
    private lateinit var flatProvider: EntityProvider
    private lateinit var clientCommandManager: CommandManager

    fun initClientContext(): ClientContext {
        initClientExchanger()
        initCollectionManager()
        initCommandManager()

        TODO()
//        return DefaultClientContext(
//            MockEntityProvider(),
//            ModelDtoBuilderWrapper(FlatDtoBuilder()),
//            initClientExchanger()
//        )
    }

    private fun initClientExchanger() {
        val clientSession = ConnectionSession("localhost", 2222)
        val clientTransceiver = ClientTransceiver(clientSession.getSocketChannel())
        this.clientExchanger = ClientExchanger(clientTransceiver, clientSession)
        this.clientAuthenticator = ClientAuthenticator(clientExchanger)
        // TODO block user on a loading screen while retreiving full collection and commands list.
    }

    private fun initCollectionManager() {
        this.collectionFilter = CollectionFilter(clientExchanger.receiveFullCollection())
        this.flatProvider = FlatProvider(collectionFilter)
    }

    private fun initCommandManager() {
        this.clientCommandManager = CommandManagerImpl()
        clientCommandManager.registerCommands(
            Help(clientCommandManager),
            Info(collectionFilter),
            FilterContainsName(collectionFilter),
            PrintUniqueNumberOfRooms(collectionFilter),
            PrintFieldDescendingNew(collectionFilter),
            Login(clientAuthenticator),
            Register(clientAuthenticator)
        )
    }

    private fun addRemoteCommands() {
        val commandNameToInfo = clientExchanger.requestAccessibleCommandsInfo()
        clientCommandManager.addCommandsInfo(commandNameToInfo)
    }
}