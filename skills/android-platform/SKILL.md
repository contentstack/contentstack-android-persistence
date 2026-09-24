---
name: android-platform
description: Use when changing Gradle/AGP/Realm versions, Android SDK levels, dependencies, or Maven Central publishing configuration.
---

# Android platform & tooling – Contentstack Android Persistence

## When to use

- You upgrade Android Gradle Plugin, Realm plugin, `compileSdkVersion` / `minSdkVersion` / `targetSdkVersion`.
- You add or bump dependencies in `app/build.gradle` or force resolutions in `configurations.all`.
- You touch `mavenPublishing`, signing, or POM fields driven by `gradle.properties`.

## Instructions

### Key files

- Root [build.gradle](../../build.gradle): AGP and Realm Gradle plugin classpath; `clean` task.
- [settings.gradle](../../settings.gradle): includes `:app` only.
- [app/build.gradle](../../app/build.gradle): `com.android.library`, `realm-android`, `com.vanniktech.maven.publish`, `dependencies`, Kotlin stdlib forces (CVE-related), `mavenPublishing` block.
- [gradle.properties](../../gradle.properties): `GROUP`, `POM_*`, `VERSION_NAME`, AndroidX flags, signing toggle.

### Android config (current baseline)

- `compileSdkVersion` / `targetSdkVersion` 34; `minSdkVersion` 24; Java 8 bytecode.
- Realm: `io.realm:realm-gradle-plugin` version aligned with root `buildscript` classpath.

### Dependencies

- Contentstack Android SDK: `com.contentstack.sdk:android`—persistence behavior assumes that stack API.
- Gson and Kotlin stdlib versions may be forced for security advisories; preserve comment context when bumping.

### Publishing

- Release CI runs `./gradlew publishAndReleaseToMavenCentral` with secrets—local changes to signing or portal IDs should stay consistent with [publish-release.yml](../../.github/workflows/publish-release.yml).

## References

- [dev-workflow/SKILL.md](../dev-workflow/SKILL.md) — commands and branch/CI expectations.
- [AGENTS.md](../../AGENTS.md) — quick reference table.
