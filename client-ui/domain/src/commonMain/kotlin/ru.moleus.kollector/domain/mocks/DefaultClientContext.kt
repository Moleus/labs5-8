package ru.moleus.kollector.domain.mocks

import commands.CommandManager
import common.context.ClientContext
import common.context.EntityProvider
import common.context.Exchanger
import common.context.Session
import model.ModelDto
import model.builder.BuilderWrapper

class DefaultClientContext(
    entityProvider: EntityProvider,
    dtoBuilder: BuilderWrapper<ModelDto>,
    commandManager: CommandManager,
    exchanger: Exchanger,
    session: Session
) : ClientContext,
    EntityProvider by entityProvider,
    BuilderWrapper<ModelDto> by dtoBuilder,
    CommandManager by commandManager,
    Exchanger by exchanger,
    Session by session