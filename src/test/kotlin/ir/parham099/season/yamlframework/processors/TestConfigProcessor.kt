package ir.parham099.season.yamlframework.processors

import ir.parham099.season.yamlframework.TestAddressesYamlObject
import ir.parham099.season.yamlframework.TestStatics
import org.junit.jupiter.api.Test
import java.io.File

class TestConfigProcessor {
    @Test
    fun testSaveYamlObject() {
        ConfigProcessor.saveYamlObject(
            TestAddressesYamlObject::class.java
        )

        val generated = File(TestStatics.addressesYamlPath)
        val correct = File(TestStatics.correctAddressesYamlPath)

        // check is it saved correctly
        if (correct.readText() != generated.readText()) {
            assert(false)
        }
    }

    @Test
    fun testLoadYamlObject() {
        ConfigProcessor.loadYamlObject(
            TestAddressesYamlObject.javaClass
        )
    }
}