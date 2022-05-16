package root.app

import data.DtoBuilder

class MockDtoBuilder : DtoBuilder {
    override fun setValue(fieldName: String, value: String) {
        println("Setting value '$value' on field '$fieldName'")
    }
}