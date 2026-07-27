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

> Replace `VERSION` with the latest GitHub release tag (for example `1.0.0`).

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

    @SubConfigObject
    object Messages {

        @ConfigField
        var join = "&aWelcome"

        @ConfigField
        var leave = "&cGoodbye"
    }
}
```

---

## Java

```java
@Config(filePath = "config.yml")
public final class Config {

    @ConfigField
    public static String language = "en";

    @ConfigField
    public static boolean enabled = true;

    @SubConfigObject
    public static final class Messages {

        @ConfigField
        public static String join = "&aWelcome";

        @ConfigField
        public static String leave = "&cGoodbye";
    }
}
```

Produces

```yaml
language: en
enabled: true

Messages:
  join: "&aWelcome"
  leave: "&cGoodbye"
```

---

# API

Load configuration

### Kotlin

```kotlin
ConfigProcessor.loadYamlObject(MyConfig::class.java)
```

### Java

```java
ConfigProcessor.INSTANCE.loadYamlObject(MyConfig.class);
```

Save configuration

### Kotlin

```kotlin
ConfigProcessor.saveYamlObject(MyConfig::class.java)
```

### Java

```java
ConfigProcessor.INSTANCE.saveYamlObject(MyConfig.class);
```

---

# Documentation

Complete documentation is available in the GitHub Wiki.

* Getting Started
* Installation
* Configuration Objects
* ConfigField
* SubConfigObject
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
