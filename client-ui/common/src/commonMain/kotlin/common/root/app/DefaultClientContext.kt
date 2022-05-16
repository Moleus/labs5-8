package root.app

import auth.Authenticator
import ClientContext
import data.DtoBuilder
import data.EntityProvider

class DefaultClientContext(
    authenticator: Authenticator,
    entityProvider: EntityProvider,
    dtoBuilder: DtoBuilder
) : ClientContext,
    Authenticator by authenticator,
    EntityProvider by entityProvider,
    DtoBuilder by dtoBuilder