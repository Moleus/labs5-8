# Lab 8: Collection management Application 

## Server
- Java 11
- Custom annotation processor - generates DTO class for each entity.
- Custom ORM library `perfORM`. Built on top of Bytebuddy and reflections. 
Creates queries, configures db tables, adds constraints based on annotations, generates entities.
- PostgreSQL stores users and users' data.

## Client
- Kotlin
- Compose Multiplatform (Linux and Android)
- Most of the code is platform independent.
- UI is based on MVI architecture.
- Reactive handlers are based on Reaktive library.
- Gradle feature-modules (auth, connection, map view, editor)
- Android app is signed when building the release.

## Common
- DTOs, commands and communication objects are shared between client and server.

