package ru.moleus.kollector.domain.bootsrap

import commands.CommandManager
import commands.CommandManagerImpl
import common.context.ClientContext
import common.context.EntityProvider
import common.context.Exchanger
import common.context.Session
import model.builder.ModelDtoBuilderWrapper
import model.data.ModelDtoBuilder
import ru.moleus.kollector.domain.collection.CollectionFilter
import ru.moleus.kollector.domain.collection.CollectionUpdater
import ru.moleus.kollector.domain.collection.FlatProvider
import ru.moleus.kollector.domain.commands.pcommands.*
import ru.moleus.kollector.domain.communication.*
import ru.moleus.kollector.domain.mocks.DefaultClientContext
import kotlin.concurrent.thread

object ClientBootstrapper {
    private lateinit var clientExchanger: Exchanger
    private lateinit var clientAuthenticator: Authenticator
    private lateinit var collectionFilter: CollectionFilter
    private lateinit var flatProvider: EntityProvider
    private lateinit var clientCommandManager: CommandManager
    private lateinit var clientSession: Session

    /**
     * Takes active client session as a parameter and returns full context with
     * ready to use classes and loaded collection.
     */
    fun initClientContext(session: Session): ClientContext {
        this.clientSession = session
        initClientExchanger()
        initCollectionManager()
        initCommandManager()
        addRemoteCommands()
        startCollectionUpdater()

        return DefaultClientContext(
            entityProvider = this.flatProvider,
            dtoBuilder = ModelDtoBuilderWrapper(ModelDtoBuilder()),
            commandManager = this.clientCommandManager,
            exchanger = this.clientExchanger,
            session = this.clientSession
        )
    }

    private fun initClientExchanger() {
        val clientTransceiver = ClientTransceiver(clientSession)
        this.clientExchanger = ClientExchanger(clientTransceiver)
        this.clientAuthenticator = ClientAuthenticator(clientExchanger)
        // TODO block user on a loading screen while retreiving full collection and commands list.
    }

    private fun initCollectionManager() {
        //TODO: initialize in background. while starting app.
        this.collectionFilter = CollectionFilter(clientExchanger.requestFullCollection())
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

    private fun startCollectionUpdater() {
        thread(start = true) {
            CollectionUpdater(clientExchanger, collectionFilter).run()
        }
    }
}