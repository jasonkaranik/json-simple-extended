# JSON.simple Extended

![Version](https://img.shields.io/badge/version-1.0.2-blue)

An extended version of the [JSON.simple](https://github.com/fangyidong/json-simple) library, with added features to simplify working with JSON in Java.

## Features

- **Dotted JSON Traversal**: Access and modify nested JSON values using dot notation (e.g., `"parent.child.value"`).

# Getting started

## Manual installation

To install manually, download the jar by clicking [here](https://nexus.jasonkaranik.com/service/rest/v1/search/assets/download?repository=maven-releases&maven.groupId=com.jasonkaranik&maven.artifactId=json-simple-extended&version=1.0.2&maven.extension=jar&maven.classifier=) and add it to your project's classpath.

## Maven:

Add the following repository and dependency inside the existing `<repositories>` and `<dependencies>` sections of your `pom.xml`:

```xml
<repositories>
    ...
    <repository>
        <id>jasonkaranik-nexus</id>
        <url>https://nexus.jasonkaranik.com/repository/maven-releases/</url>
    </repository>
</repositories>

<dependencies>
    ...
    <dependency>
        <groupId>com.jasonkaranik</groupId>
        <artifactId>json-simple-extended</artifactId>
        <version>1.0.2</version>
    </dependency>
</dependencies>
```

## Gradle:

Add the following repository and dependency inside the existing repositories and dependencies blocks of your `build.gradle` file:

```groovy
repositories {
    ...
    maven {
        url "https://nexus.jasonkaranik.com/repository/maven-releases/"
    }
}

dependencies {
    ...
    implementation "com.jasonkaranik:json-simple-extended:1.0.2"
}
```

## Usage Example

### Import the `JSONObject` class:

```java
import com.jasonkaranik.json.simple.extended.JSONObject;
```

### Parse a JSON string and access nested values with dot notation:

```java
JSONObject json = new JSONObject("{ \"first_name\": \"John\", \"last_name\": \"Doe\", \"country\": { \"code\": \"US\", \"calling_code\": \"+1\" } }");

System.out.println(json.get("first_name")); // John

System.out.println(json.get("country.code")); // US
```

### Handling Missing or Incorrect Keys & Modifying values:

```java
System.out.println(json.get("date_of_birth")); // null

if (!json.containsKey("phone_number")) {
    json.put("phone_number", "+1 (123) 456-7890");
}
```

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for more details.
