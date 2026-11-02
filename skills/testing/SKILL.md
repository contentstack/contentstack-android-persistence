---
name: testing
description: Use when adding or running unit/instrumented tests, test data, or policies for secrets and credentials.
---

# Testing – Contentstack Android Persistence

## When to use

- You add `src/test` or `src/androidTest` coverage.
- You need conventions for naming, structure, or what must not be committed.

## Instructions

### Layout (recommended)

- **Unit tests:** `app/src/test/java/` mirroring package `com.contentstack.sdk.persistence` (JUnit 4/5 per project choice once added).
- **Instrumented tests:** `app/src/androidTest/java/` for tests that need `Realm`, Android framework, or `Context`.

### Running

- Unit: `./gradlew test`
- Instrumented: `./gradlew connectedAndroidTest` (device/emulator required).

### Credentials and data

- Do **not** commit API keys, delivery tokens, stack tokens, or org-specific Realm files. Use build-time placeholders, `local.properties`, or CI secrets—same policy as for any Contentstack SDK sample.
- Prefer small fixtures (JSON snippets, in-memory Realm configuration) over live network calls in unit tests; mock or stub `Stack` / sync callbacks where feasible.

### Coverage

- No JaCoCo or coverage gate is documented in-repo; if you add reporting, document the Gradle task in [dev-workflow/SKILL.md](../dev-workflow/SKILL.md) and here.

## References

- [dev-workflow/SKILL.md](../dev-workflow/SKILL.md) — Gradle test tasks.
- [android-persistence-sdk/SKILL.md](../android-persistence-sdk/SKILL.md) — behavior under test.
