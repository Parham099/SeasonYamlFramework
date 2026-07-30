package ir.parham099.season.yamlframework

import ir.parham099.season.yamlframework.annotations.Config
import ir.parham099.season.yamlframework.annotations.ConfigField
import ir.parham099.season.yamlframework.annotations.SubConfigObject

@Config(TestStatics.addressesYamlPath)
object TestAddressesYamlObject {
    @ConfigField
    var test: String = "test"

    @SubConfigObject
    object Google {

        @ConfigField
        var search: String = "https://google.com"

        @ConfigField
        var maps: String = "https://maps.google.com"

        @ConfigField
        var drive: String = "https://drive.google.com"
    }

    @SubConfigObject
    object GitHub {

        @ConfigField
        var main: String = "https://github.com"

        @ConfigField
        var api: String = "https://api.github.com"

        @ConfigField
        var raw: String = "https://raw.githubusercontent.com"
    }

    @SubConfigObject
    object JetBrains {

        @ConfigField
        var website = "https://jetbrains.com"

        @ConfigField
        var kotlin = "https://kotlinlang.org"

        @ConfigField
        var exposed = "https://github.com/JetBrains/Exposed"

        @SubConfigObject
        object Products {

            @ConfigField
            var idea = "IntelliJ IDEA"

            @ConfigField
            var pycharm = "PyCharm"

            @ConfigField
            var rider = "Rider"
        }
    }
}