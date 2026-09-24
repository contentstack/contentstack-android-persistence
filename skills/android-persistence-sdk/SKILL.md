---
name: android-persistence-sdk
description: Use when changing or explaining Realm-backed persistence, sync flow, or integration with the Contentstack Android Stack API.
---

# Android Persistence SDK – Contentstack Android Persistence

## When to use

- You edit or document `RealmStore`, `SyncManager`, `SyncStore`, `SyncPersistable`, or related persistence types under `com.contentstack.sdk.persistence`.
- You need to explain how consumers initialize the library with `Stack`, `Realm`, and `SyncManager`.
- You are tracing sync tokens, pagination, or error handling tied to Contentstack `Error` / sync APIs.

## Instructions

### Responsibilities

- Primary entry points for sync + local storage are `RealmStore` (Realm instance + storage) and `SyncManager` (`RealmStore` + `Stack`). Consumers obtain a `Stack` from `Contentstack.stack(...)`, a `Realm` instance, then construct these types as shown in the root [README.md](../../README.md).
- `SyncPersistable` and model classes using Realm (`RealmModel`, `@RealmField`) define what gets persisted; keep field mapping and reflection-based `CONTENT_TYPE_CLASS_MAPPER` / `CLASS_FIELDS_MAPPER` logic consistent when adding content types.
- This module **does not** reimplement network I/O; it delegates sync to the Contentstack Android SDK (`com.contentstack.sdk`). Changes that look like “REST client” or generic HTTP belong upstream, not here.

### Package and surface

- Public API lives under `app/src/main/java/com/contentstack/sdk/persistence/`. Prefer `@NonNull` and clear JavaDoc on constructors and methods intended for app developers.
- Avoid breaking binary compatibility for published methods without a version strategy coordinated via `gradle.properties` / release process.

### Product docs

- End-user documentation links are in [README.md](../../README.md) (Contentstack docs, example app repo).

## References

- [java-android/SKILL.md](../java-android/SKILL.md) — source tree and Java conventions.
- [android-platform/SKILL.md](../android-platform/SKILL.md) — Contentstack SDK and Realm dependency versions.
- [AGENTS.md](../../AGENTS.md) — purpose and out-of-scope boundaries.
