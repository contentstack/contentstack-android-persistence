# Contentstack Android Persistence – Agent guide

*Universal entry point* for contributors and AI agents. Detailed conventions live in **skills/*/SKILL.md**.

## What this repo is

| Field | Detail |
| --- | --- |
| *Name:* | [contentstack/contentstack-android-persistence](https://github.com/contentstack/contentstack-android-persistence) |
| *Purpose:* | Android library that persists Contentstack sync data locally using Realm, so apps can work offline with the Contentstack Android SDK. |
| *Out of scope (if any):* | Does not ship a standalone HTTP client or replace the Contentstack Android SDK; persistence and sync orchestration sit on top of `com.contentstack.sdk:android` and Realm. |

## Tech stack (at a glance)

| Area | Details |
| --- | --- |
| Language | Java 8 (source/target compatibility); JDK 17 used in release CI (`setup-java`). |
| Build | Gradle with Android Gradle Plugin 8.2.x, Realm Android plugin 10.15.x; root `build.gradle`, `settings.gradle` (`:app`), `app/build.gradle`, `gradle.properties` (Maven coordinates, signing flags). |
| Tests | Android/JUnit via Gradle (`test`, `connectedAndroidTest` when present); no `src/test` or `src/androidTest` trees in this repo yet—add tests under `app/src/test/` or `app/src/androidTest/` as applicable. |
| Lint / coverage | Android Lint via `./gradlew :app:lint` (default Android lint integration). |
| Other | Realm (`io.realm:realm-gradle-plugin`), Maven Central publishing (`com.vanniktech.maven.publish`), Contentstack Android SDK `com.contentstack.sdk:android` (see `app/build.gradle`). |

## Commands (quick reference)

| Command Type | Command |
| --- | --- |
| Build | `./gradlew clean build` |
| Test | `./gradlew test` (unit); `./gradlew connectedAndroidTest` (instrumented, requires device/emulator) |
| Lint | `./gradlew :app:lint` |

**CI:** Release workflow runs `./gradlew clean build` before publish—see [.github/workflows/publish-release.yml](.github/workflows/publish-release.yml). Branch rules for `master` are in [.github/workflows/check-branch.yml](.github/workflows/check-branch.yml).

## Where the documentation lives: skills

| Skill | Path | What it covers |
| --- | --- | --- |
| Dev workflow | [skills/dev-workflow/SKILL.md](skills/dev-workflow/SKILL.md) | Branches, CI, Gradle commands, publishing expectations. |
| Android Persistence SDK | [skills/android-persistence-sdk/SKILL.md](skills/android-persistence-sdk/SKILL.md) | Public API, Realm + Contentstack boundaries, initialization. |
| Java & Android layout | [skills/java-android/SKILL.md](skills/java-android/SKILL.md) | Java conventions and module/source layout for this repo. |
| Android platform & tooling | [skills/android-platform/SKILL.md](skills/android-platform/SKILL.md) | Gradle, Realm plugin, Android config, Maven publish, dependencies. |
| Testing | [skills/testing/SKILL.md](skills/testing/SKILL.md) | Where to add tests, naming, and credentials policy. |
| Code review | [skills/code-review/SKILL.md](skills/code-review/SKILL.md) | PR checklist and severity guidance. |

An index with "when to use" hints is in [skills/README.md](skills/README.md).

## Using Cursor (optional)

If you use *Cursor*, [.cursor/rules/README.md](.cursor/rules/README.md) only points to *AGENTS.md*—same docs as everyone else.
