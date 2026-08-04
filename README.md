# SeasonYamlFramework

<p align="center">
    <b>Lightweight annotation-based YAML configuration framework for Kotlin and Java.</b>
</p>

<p align="center">
    Automatically map objects to YAML files with minimal boilerplate.
</p>

<p align="center">
    <a href="https://github.com/Parham099/SeasonYamlFramework">GitHub</a>
    •
    <a href="https://github.com/Parham099/SeasonYamlFramework/wiki">Wiki</a>
    •
    <a href="https://jitpack.io/#Parham099/SeasonYamlFramework">JitPack</a>
</p>

---

# Features

* ✅ Annotation-based configuration
* ✅ Kotlin & Java support
* ✅ Automatic serialization/deserialization
* ✅ Nested configuration objects
* ✅ YAML-backed object maps
* ✅ Type-safe API
* ✅ Lightweight
* ✅ Built on SnakeYAML
* ✅ No Kotlin Reflection dependency
* ✅ Simple API
* ✅ Easy integration

---

# Installation

## Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Parham099:SeasonYamlFramework:VERSION")
}
```

---

## Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation "com.github.Parham099:SeasonYamlFramework:VERSION"
}
```

---

## Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
<dependency>
    <groupId>com.github.Parham099</groupId>
    <artifactId>SeasonYamlFramework</artifactId>
    <version>VERSION</version>
</dependency>
</dependencies>
```

> Replace `VERSION` with the latest GitHub release tag.

---

# Quick Start

## Kotlin

```kotlin
@Config("plugins/MyPlugin/config.yml")
object Configs {

    @ConfigField
    var prefix = "&6Server"

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

Modify values:

```kotlin
Configs.maxPlayers = 200
```

Save:

```kotlin
ConfigProcessor.saveYamlObject(Configs::class.java)
```

---

## Java

```java
@Config(filePath = "plugins/MyPlugin/config.yml")
public final class Configs {

    @ConfigField
    public static String prefix = "&6Server";

    @ConfigField
    public static int maxPlayers = 100;

    @SubConfigObject
    public static final class Database {

        @ConfigField
        public static String host = "localhost";

        @ConfigField
        public static int port = 3306;
    }
}
```

Load:

```java
ConfigProcessor.INSTANCE.loadYamlObject(Configs.class);
```

Save:

```java
ConfigProcessor.INSTANCE.saveYamlObject(Configs.class);
```

---

# Generated YAML

```yaml
prefix: "&6Server"
maxPlayers: 100

Database:
  host: localhost
  port: 3306
```

---

# YamlObjectMap

`YamlObjectMap` provides a way to map a YAML object map to instances of a Kotlin or Java class.

For example:

```yaml
companies:
  0:
    name: Parham
    age: 220

  1:
    name: Google
    age: 999
```

The objects can be represented by:

```kotlin
class Company(
    val name: String,
    val age: Int
)
```

Create the map:

```kotlin
val companies = YamlObjectMap(
    "companies",
    Company::class.java
)
```

`YamlObjectMap` does not create or manage its own YAML file. Instead, it receives the `SeasonYaml` instance from `ConfigProcessor`.

This allows configuration objects and `YamlObjectMap` instances to work with the same YAML data.

---

## Loading a YamlObjectMap

Normally, `ConfigProcessor` automatically loads `YamlObjectMap` fields when the configuration is loaded.

```kotlin
@Config("config.yml")
object Config {

    val companies = YamlObjectMap(
        "companies",
        Company::class.java
    )
}
```

Then:

```kotlin
ConfigProcessor.loadYamlObject(Config::class.java)
```

The `companies` map is automatically populated from:

```yaml
companies:
  0:
    name: Parham
    age: 220

  1:
    name: Google
    age: 999
```

You can then access the objects:

```kotlin
val company = Config.companies[0]

println(company?.name)
println(company?.age)
```

Or iterate through all entries:

```kotlin
for (key in Config.companies.getKeys()) {
    val company = Config.companies[key] ?: continue

    println("${company.name} age is ${company.age}")
}
```

---

## Adding or modifying objects

Use the `set` operator to add a new object or replace an existing one:

```kotlin
Config.companies[2] = Company(
    name = "JetBrains",
    age = 999
)
```

You can also retrieve and modify objects depending on whether their properties are mutable:

```kotlin
val company = Config.companies[0]
```

---

## Saving a YamlObjectMap

When a `YamlObjectMap` is a field inside a `@Config` object, `ConfigProcessor` automatically saves it.

```kotlin
ConfigProcessor.saveYamlObject(Config::class.java)
```

The processor internally calls:

```kotlin
companies.saveAll(yaml)
```

using the same `SeasonYaml` instance used by the rest of the configuration.

You normally do **not** need to call `saveAll()` yourself when using `ConfigProcessor`.

---

## Direct YamlObjectMap API

If you are working directly with a `SeasonYaml` instance, you can load and save the map manually.

### Load

```kotlin
map.loadAll(yaml)
```

### Save all

```kotlin
map.saveAll(yaml)
```

### Save one object

```kotlin
map.save(
    yaml,
    2,
    Company(
        name = "JetBrains",
        age = 999
    )
)
```

---

## YamlObjectMap API

| Method                   | Description                 |
| ------------------------ | --------------------------- |
| `map[key]`               | Gets an object by its key   |
| `map[key] = value`       | Adds or replaces an object  |
| `getKeys()`              | Returns all keys            |
| `loadAll(yaml)`          | Loads all objects from YAML |
| `save(yaml, key, value)` | Saves one object to YAML    |
| `saveAll(yaml)`          | Saves all objects to YAML   |

---

# Annotations

## @Config

Defines the configuration file.

### Kotlin

```kotlin
@Config("config.yml")
```

### Java

```java
@Config(filePath = "config.yml")
```

---

## @ConfigField

Marks a field to be serialized.

### Kotlin

```kotlin
@ConfigField
var language = "en"
```

### Java

```java
@ConfigField
public static String language = "en";
```

---

## @SubConfigObject

Creates a nested YAML section.

### Kotlin

```kotlin
@SubConfigObject
object Database
```

### Java

```java
@SubConfigObject
public static final class Database {
}
```

---

# Example

## Kotlin

```kotlin
@Config("config.yml")
object Config {

    @ConfigField
    var language = "en"

    @ConfigField
    var enabled = true

    val companies = YamlObjectMap(
        "companies",
        Company::class.java
    )

    @SubConfigObject
    object Messages {

        @ConfigField
        var join = "&aWelcome"

        @ConfigField
        var leave = "&cGoodbye"
    }
}

class Company(
    val name: String,
    val age: Int
)
```

After loading:

```kotlin
ConfigProcessor.loadYamlObject(Config::class.java)
```

the YAML can be:

```yaml
language: en
enabled: true

companies:
  0:
    name: Parham
    age: 220

  1:
    name: Google
    age: 999

Messages:
  join: "&aWelcome"
  leave: "&cGoodbye"
```

---

# API

## Load Configuration

### Kotlin

```kotlin
ConfigProcessor.loadYamlObject(MyConfig::class.java)
```

### Java

```java
ConfigProcessor.INSTANCE.loadYamlObject(MyConfig.class);
```

Loading automatically processes:

* `@ConfigField`
* `@SubConfigObject`
* `YamlObjectMap`

---

## Save Configuration

### Kotlin

```kotlin
ConfigProcessor.saveYamlObject(MyConfig::class.java)
```

### Java

```java
ConfigProcessor.INSTANCE.saveYamlObject(MyConfig.class);
```

Saving automatically processes:

* `@ConfigField`
* `@SubConfigObject`
* `YamlObjectMap`

---

## YamlObjectMap

### Create

```kotlin
val companies = YamlObjectMap(
    "companies",
    Company::class.java
)
```

### Get

```kotlin
val company = companies[0]
```

### Set

```kotlin
companies[0] = Company("Parham", 220)
```

### Get Keys

```kotlin
val keys = companies.getKeys()
```

### Load

```kotlin
companies.loadAll(yaml)
```

### Save

```kotlin
companies.save(
    yaml,
    0,
    Company("Parham", 220)
)
```

### Save All

```kotlin
companies.saveAll(yaml)
```

---

# Documentation

Complete documentation is available in the GitHub Wiki.

* Getting Started
* Installation
* Configuration Objects
* `@Config`
* `@ConfigField`
* `@SubConfigObject`
* `YamlObjectMap`
* Loading and Saving
* Custom Paths
* API Reference
* Examples
* Best Practices
* FAQ

---

# Contributing

Contributions, bug reports, and feature requests are welcome.

If you encounter a bug or have an idea for an improvement, feel free to open an Issue or submit a Pull Request.

---

# License

This project is licensed under the MIT License.
