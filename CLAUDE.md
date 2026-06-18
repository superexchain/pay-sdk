# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository layout

This repo is a multi-language SDK monorepo with one top-level directory per target language: `csharp/`, `golang/`, `java/`, `python/`, `rust/`. Only `java/` is currently scaffolded; the others are empty placeholders awaiting their respective implementations.

## Java SDK (`java/sdk/`)

A Spring Boot 4.1 / Java 17 Maven multi-module project. Group ID is `com.novax`; the parent POM declares `<packaging>pom</packaging>` and lists modules. New SDK modules should be added as sibling directories to `sdk-core/` and registered in `java/sdk/pom.xml` `<modules>`.

- `sdk-core/` — the only existing module. Currently just a Spring Boot bootstrap (`SdkCoreApplication`) with no business logic. Uses Lombok (optional dep) and `spring-boot-starter-test` for tests.

### Common commands

All Maven commands run from `java/sdk/` (the reactor root):

```bash
# Build everything
mvn clean install

# Build a single module without rebuilding the rest
mvn -pl sdk-core -am clean install

# Run the Spring Boot app
mvn -pl sdk-core spring-boot:run

# Run all tests
mvn test

# Run a single test class / method
mvn -pl sdk-core test -Dtest=SdkCoreApplicationTests
mvn -pl sdk-core test -Dtest=SdkCoreApplicationTests#contextLoads
```

There is no Maven wrapper checked in — use a locally installed `mvn`.

## Cross-language consistency

When implementing the same SDK in a sibling language directory, mirror the module/package naming used in `java/` (`com.novax.sdk.*`) so the surface area stays recognizable across languages.
