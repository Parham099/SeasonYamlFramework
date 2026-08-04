package ir.parham099.season.yamlframework.models

import ir.parham099.season.yamlframework.TestAddressesYamlObject
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Test

class TestYamlObjectMap {

    @Test
    fun testLoadYamlObjectMap() {
        val companies = TestAddressesYamlObject.companies

        assertEquals(setOf("0", "1"), companies.getKeys())

        val parham = companies["0"]
        val google = companies["1"]

        assertNotNull(parham)
        assertNotNull(google)

        assertEquals("Parham", parham.name)
        assertEquals(220, parham.age)

        assertEquals("Google", google.name)
        assertEquals(999, google.age)
    }
}