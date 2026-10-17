# Violet UML Editor — Specifications for a Technology Migration

## 1. Purpose

This folder describes, from static analysis of the source code, the current behaviour of **Violet UML Editor**. It is meant as the reference for a migration to another technology.

It is deliberately **independent of the target technology**: it describes *what the system does* (data model, file format, features, requirements) rather than *how it is coded today*. The choice of target is treated separately (document 06).

## 2. Source analysed

| Item | Value |
|---|---|
| Repository | https://github.com/violetumleditor/product |
| Analysed commit | `70c092a9aa02a895c0aff80a4ac463bb24a4dd8a` |
| Commit date | 2026-04-19 ("feat: add support for pre-filled filename in file chooser dialogs") |
| Retrieval method | Shallow clone (`--depth 1`), git history not explored |
| Analysis date | 2026-10-17 |
| Declared application version | `2.3.2-SNAPSHOT` (`applicationVersion` property of the root POM) |

## 3. Confidence legend

Each important statement is tagged with its level of evidence:

- **[V] Verified** — observed directly in the source code (file reading, counting, grep).
- **[D] Deduced** — logical conclusion drawn from several verified elements, not tested at runtime.
- **[?] To confirm** — hypothesis or point that requires execution or further verification.

## 4. Limits of the analysis (important)

1. **The build could not be run.** Maven Central was unreachable from the analysis environment (DNS resolution failure for the `maven-resources-plugin`). A direct `javac` compilation of the `violet-framework` module reports 100 errors, all caused by missing third-party dependencies (FlatLaf, FreeHEP, commons-vfs, pgslookandfeel, l2fprod, etc.). **Consequence: no unit test was executed, and the behaviour was not observed at runtime.**
2. **Static analysis only.** The behaviours described in documents 04 and 05 are deduced from the code. A reference corpus (document 06, phase 0) is needed to validate them.
3. **Git history not analysed** (shallow clone). The historical reasons for some choices (removal of XStream, abandonment of CheerpJ, etc.) are inferred from test and file names.

## 5. Discrepancies between the repository documentation and the code

Several repository documents are outdated. These specifications follow the **code**, not the documentation.

| Document / source | Claim | Observed reality [V] |
|---|---|---|
| `.github/copilot-instructions.md` | Version `2.3.0-SNAPSHOT` | POM: `2.3.2-SNAPSHOT` |
| `.github/copilot-instructions.md` | Java 21 (release 21) | POM: `maven.compiler.release` = **17**. CI builds with JDK 21 but targets bytecode 17. |
| `.github/copilot-instructions.md` | Module `violetproduct-web` (JWT + Jetty) | **Absent** from disk and from the root POM |
| `.github/copilot-instructions.md` | Module `violetplugin-communicationdiagram` | **Absent** (only a commented-out dependency in the Swing POM) |
| `.github/copilot-instructions.md` | Persistence via XStream 1.4.21 | **No XStream import** anywhere in the code (0 files) |
| `.github/copilot-instructions.md` | "The project has no unit tests" | **6 test classes** exist and the Surefire plugin is configured |
| `.github/copilot-instructions.md` | Do not introduce Spring | No Spring import, but a `spring-boot-maven-plugin` is configured in the Swing POM [V] |
| `CHEERPJ-MIGRATION-COMPLETE.md` | CheerpJ files created and `violetproduct-cheeprj` module removed | **Verified**: the 4 files cited exist, the module is no longer in the root POM. The document remains imprecise (see document 04, CLI section). |
| CLI help (`LaunchingPreferences` / `UMLEditorApplication`) | Options `-cheeprjMode` and `-autoSave` | The code parses **`-cheerpjMode`** (different spelling). **No `-autoSave` option** is parsed. Auto-save is in fact **always active**. |
| `README.md` | JDK 17 minimum | Consistent with the root POM |

These discrepancies are themselves useful for the migration: the documentation must not be used as the source of truth.

## 6. Index of documents

| # | Document | Content |
|---|---|---|
| 00 | `00-readme.md` | This document: scope, method, limits, discrepancies |
| 01 | `01-current-architecture.md` | Modules, layers, startup sequence, plugins, dependency injection, third-party dependencies, packaging |
| 02 | `02-domain-model.md` | Business entities, fields, property types, rules per diagram type, invariants |
| 03 | `03-file-format.md` | `.violet.html` format, "legacy" XML, read/write, backward compatibility, examples, edge cases |
| 04 | `04-features-and-ux.md` | Functional catalogue: menus, toolbar, 24 interaction behaviours, export, preferences, i18n |
| 05 | `05-technical-coupling-and-risks.md` | Coupling to Swing/AWT/Java2D, dead code, observed anomalies, migration risks |
| 06 | `06-target-and-migration-plan.md` | Comparison of possible targets, recommendation, target architecture, phases and exit criteria |
| 07 | `07-requirements.md` | Numbered functional and non-functional requirements, with acceptance criteria |
| 08 | `08-inventory-appendix.md` | Numeric inventory: files, lines of code, resources, classes, dependencies |

## 7. Key figures [V]

- **330 Java files**, roughly **46,700 lines** (sources and tests combined), all modules included.
- **8 Java modules in the reactor**: the core (`violet-framework`), **6 diagram plugins**, and the Swing application. The `exe` and `deb` modules contain no Java code.
- **6 diagram types**: class, sequence, activity, state, object, use case.
- **24 interaction behaviours** registered in the workspace, backed by **35 behaviour classes** in total, including **8 specific undo/redo handlers** and **1 composite**.
- **Languages**: English (default), French, German, Polish.
- **Delivery platforms**: Windows executable (`.exe`), Debian package (`.deb`). JNLP and RPM are present but **not in the reactor**.

## 8. Target decision: not yet taken

Document 06 compares several targets and recommends an approach, but **the final choice belongs to the team**. Documents 01 to 05 and 07 are written to remain valid whatever target is chosen.
