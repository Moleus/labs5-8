package ru.moleus.kollector.feature.builder.integration

import ru.moleus.kollector.feature.builder.store.EditorStore
import ru.moleus.kollector.feature.builder.Builder

internal val stateToModel: (EditorStore.State) -> (Builder.Model) =
    {
        Builder.Model(
            filledValues = it.filledValues,
            isLoading = it.isLoading,
            isError = it.isError,
            errorMsg = it.errorMsg,
            isSuccess = it.isSuccess
        )
    }
