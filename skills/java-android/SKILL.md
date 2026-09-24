---
name: java-android
description: Use for Java source layout, packages, and Android resource conventions in the app module.
---

# Java & Android layout – Contentstack Android Persistence

## When to use

- You add or move `.java` files, packages, or Android resources.
- You align new code with existing patterns (AndroidX, logging, Realm annotations).

## Instructions

### Layout

- Library code and any sample activity live in the single module **`app`**, namespace `com.contentstack.sdk.persistence` (see `app/build.gradle` `namespace`).
- Java sources: `app/src/main/java/com/contentstack/sdk/persistence/`.
- Manifest, themes, layouts: `app/src/main/AndroidManifest.xml`, `app/src/main/res/`.

### Language level

- `compileOptions` use Java 8 (`sourceCompatibility` / `targetCompatibility` `1.8`). Match that level unless the project explicitly migrates (would require Gradle and CI updates).

### Android patterns in this repo

- AndroidX artifacts are already in use (`androidx.appcompat`, Material, ConstraintLayout, Lifecycle). New UI or lifecycle code should stay on AndroidX, not legacy support libraries.
- Realm types use `io.realm` APIs and annotations; follow existing `RealmModel` / `@RealmField` usage when extending persisted models.

## References

- [android-persistence-sdk/SKILL.md](../android-persistence-sdk/SKILL.md) — API and sync responsibilities.
- [android-platform/SKILL.md](../android-platform/SKILL.md) — `compileSdk`, `minSdk`, and dependency versions.
