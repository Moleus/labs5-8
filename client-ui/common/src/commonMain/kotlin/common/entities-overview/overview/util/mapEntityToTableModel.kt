package common.`entities-overview`.overview.util

import data.Flat
import overview.ui.table.lazy.model.FlatModel
import overview.ui.table.lazy.model.TableModel

fun toTableModel(flat: Flat): TableModel = FlatModel(flat)

