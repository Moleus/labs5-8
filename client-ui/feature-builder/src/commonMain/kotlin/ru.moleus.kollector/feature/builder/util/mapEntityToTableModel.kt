package ru.moleus.kollector.feature.builder.util

import model.data.Flat
import ru.moleus.kollector.data.local.model.table.FlatModel
import ru.moleus.kollector.data.local.model.table.TableModel

fun toTableModel(flat: Flat): TableModel = FlatModel(flat)

