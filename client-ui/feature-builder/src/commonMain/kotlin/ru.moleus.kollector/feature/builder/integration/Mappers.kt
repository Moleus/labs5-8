package ru.moleus.kollector.feature.builder.integration

import ru.moleus.kollector.feature.builder.store.EditorStore
import editor.Editor

internal val stateToModel: (EditorStore.State) -> (Editor.Model) =
    {
        Editor.Model(
            filledValues = it.filledValues,
            isLoading = it.isLoading,
            isError = it.isError,
            errorMsg = it.errorMsg,
            isSuccess = it.isSuccess
        )
    }
