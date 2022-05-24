package ru.moleus.kollector.feature.builder.integration

import ru.moleus.kollector.feature.builder.Builder
import ru.moleus.kollector.feature.builder.store.EditorStore

internal val stateToModel: (EditorStore.State) -> (Builder.Model) =
    {
        Builder.Model(
            filledValues = it.filledValues,
            isLoading = it.isLoading,
            isError = it.isError,
            errorMsg = it.errorMsg,
            isSuccess = it.isSuccess,
            isToolbarVisible = it.isToolbarVisible
        )
    }
