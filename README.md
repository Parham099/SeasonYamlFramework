# SeasonYmlFramework

A lightweight, annotation-based YAML configuration framework for Kotlin and Java.

SeasonYmlFramework allows you to map Kotlin objects directly to YAML files using annotations, without manually reading or writing configuration values.

## Features

- Annotation-based configuration
- Automatic object loading and saving
- Nested configuration objects
- Type-safe API
- Lightweight
- No Kotlin Reflection dependency
- Java compatible
- Built on SnakeYAML

## Installation

### Gradle

```kotlin
dependencies {
    implementation("your.group:season-yml-framework:VERSION")
}
```

### Maven

```xml
<dependency>
    <groupId>your.group</groupId>
    <artifactId>season-yml-framework</artifactId>
    <version>VERSION</version>
</dependency>
```

---

## Quick Example

```kotlin
@Config("config.yml")
object Configs {

    @ConfigField
    var prefix = "[Server]"

    @ConfigField
    var maxPlayers = 100

    @SubConfigObject
    object Database {

        @ConfigField
        var host = "localhost"

        @ConfigField
        var port = 3306
    }
}
```

Load:

```kotlin
ConfigProcessor.loadYamlObject(Configs::class.java)
```

Save:

```kotlin
ConfigProcessor.saveYamlObject(Configs::class.java)
```

Generated YAML:

```yaml
prefix: "[Server]"
maxPlayers: 100

Database:
  host: localhost
  port: 3306
```

---

## Documentation

Complete documentation is available in the Wiki.

- Getting Started
- Annotations
- Nested Objects
- Custom Paths
- API Reference
- Examples

---