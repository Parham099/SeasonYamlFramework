package ir.parham099.season.yamlframework.models

import ir.parham099.season.yamlframework.TestStatics
import org.junit.jupiter.api.Test
import java.io.File

class TestFileYaml {
    var fileYaml: SeasonYaml = FileYaml(File(TestStatics.addressesYamlPath))

    @Test
    fun testLoad() {
        fileYaml = FileYaml(File(TestStatics.addressesYamlPath))
    }

    @Test
    fun testSave() {
        fileYaml.save()
    }

    @Test
    fun testGet() {
        fileYaml["test", ""]!!.ifEmpty {
            assert(false)
        }
    }

    @Test
    fun testSet() {
        fileYaml["test"] = 12345
        fileYaml["test", 0]!!.let {
            if (it != 12345) {
                assert(false)
            }
        }
    }

    @Test
    fun testRemove() {
        fileYaml -= "test"
        if (fileYaml.get<String>("test") != null) {
            assert(false)
        }
    }
}