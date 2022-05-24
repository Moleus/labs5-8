package common.context

import commands.CommandManager
import model.ModelDto
import model.builder.BuilderWrapper

interface ClientContext :
    EntityProvider,
    BuilderWrapper<ModelDto>,
    CommandManager,
    Exchanger,
    Session