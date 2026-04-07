---
name: dev-workflow
description: Use when branching, running CI-aligned Gradle tasks, or coordinating releases for this Android persistence library.
---

# Dev workflow – Contentstack Android Persistence

## When to use

- You need the canonical build, test, or lint commands.
- You are opening a PR and must align with GitHub Actions and branch rules.
- You are changing publishing or signing-related Gradle properties.

## Instructions

### Branches and PRs

- Merges into `master` from branches other than `staging` are blocked by [.github/workflows/check-branch.yml](../../.github/workflows/check-branch.yml); use the staging → master flow expected by the org unless instructed otherwise.
- Other workflows cover CodeQL, policy/SCA scans, Jira linkage, and Maven snapshot/release publishing—inspect `.github/workflows/` when your change affects security scanning or release automation.

### Commands (local)

- **Full CI-like build:** `./gradlew clean build` (matches [publish-release.yml](../../.github/workflows/publish-release.yml) before publish).
- **Lint:** `./gradlew :app:lint`.
- **Unit tests:** `./gradlew test` once `src/test` exists.
- **Instrumented tests:** `./gradlew connectedAndroidTest` with a device or emulator.

### Versioning and artifacts

- Library coordinates and version live in `gradle.properties` (`GROUP`, `POM_ARTIFACT_ID`, `VERSION_NAME`, etc.); `app/build.gradle` references `GROUP`, `POM_ARTIFACT_ID`, `VERSION_NAME` for `mavenPublishing`.
- Do not bump `VERSION_NAME` or publishing metadata casually—coordinate with maintainers and release workflow secrets.

## References

- [AGENTS.md](../../AGENTS.md) — top-level stack and command table.
- [android-platform/SKILL.md](../android-platform/SKILL.md) — Gradle, Realm, and Maven plugin details.
- [testing/SKILL.md](../testing/SKILL.md) — test layout when adding coverage.
