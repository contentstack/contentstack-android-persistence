---
name: code-review
description: Use when preparing or reviewing a pull request for this library—scope, API safety, and release impact.
---

# Code review – Contentstack Android Persistence

## When to use

- Before requesting review or when acting as reviewer on a PR touching Java, Gradle, or CI.

## Instructions

### Checklist

- **Scope:** Change matches the PR description; no unrelated refactors or formatting-only churn unless agreed.
- **API / compatibility:** Public classes under `com.contentstack.sdk.persistence`—consider binary compatibility and documented initialization flow ([android-persistence-sdk/SKILL.md](../android-persistence-sdk/SKILL.md)).
- **Dependencies:** Version bumps justified; security-related forces (Gson, Kotlin stdlib) preserved or improved with a note in the PR.
- **Build:** `./gradlew clean build` and `./gradlew :app:lint` succeed locally; new tests pass if added.
- **Secrets:** No keys, tokens, or signing material committed.

### Severity (optional)

- **Blocker:** Breaks build, publish, or documented public API without version/changelog strategy; security regression.
- **Major:** Behavioral sync/persistence bug, or dependency change that needs explicit consumer communication.
- **Minor:** Style, logging, JavaDoc, non-user-facing cleanup.

## References

- [dev-workflow/SKILL.md](../dev-workflow/SKILL.md) — CI and branch expectations.
- [AGENTS.md](../../AGENTS.md) — project purpose and commands.
