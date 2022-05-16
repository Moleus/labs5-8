import auth.Authenticator
import data.DtoBuilder
import data.EntityProvider

interface ClientContext :
    EntityProvider,
    Authenticator,
    DtoBuilder