package common.context

import com.arkivanov.decompose.value.ValueObserver
import model.data.Flat

interface EntityProvider {
    fun getAll(): List<Flat>
    fun subscribe(onUpdate: ValueObserver<Set<Flat>>)
    fun getById(id: Long): Flat
}