package ir.parham099.season.yamlframework.processors

import ir.parham099.season.yamlframework.TestAddressesYamlObject
import ir.parham099.season.yamlframework.TestStatics
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.io.File

class TestConfigProcessor {
    @Test
    fun testLoadAndSaveYamlObject() {
        ConfigProcessor.loadYamlObject(
            TestAddressesYamlObject.javaClass
        )
        println(TestAddressesYamlObject.companies.getKeys())

        ConfigProcessor.saveYamlObject(
            TestAddressesYamlObject::class.java
        )
    }
}