# 05 — Technical coupling, dead code, anomalies and risks

This document lists what will make the migration costly: coupling to Swing/AWT, dead code not to be carried over, observed anomalies and the associated risks.

Evidence level: [V] = observed; [D] = deduced; [?] = to confirm.

## 1. Coupling to the Java UI (AWT / Swing)

### 1.1 Proportion of files concerned [V]

Files importing `java.awt`, `java.awt.geom` or `javax.swing`:

| Module | Files concerned | Total Java | Share |
|---|---:|---:|---:|
| `violet-framework` | 139 | 212 | 66 % |
| `violetplugin-activitydiagram` | 9 | 12 | 75 % |
| `violetplugin-classdiagram` | 3 | 24 | 13 % |
| `violetplugin-objectdiagram` | 4 | 8 | 50 % |
| `violetplugin-sequencediagram` | 5 | 8 | 63 % |
| `violetplugin-statediagram` | 4 | 7 | 57 % |
| `violetplugin-usecasediagram` | 2 | 7 | 29 % |
| `violetproduct-swing` | 24 | 31 | 77 % |
| **Total excluding Eclipse** | **190** | **309** | **≈ 61 %** |
| `violetproduct-eclipseplugin` (SWT) | 6 | 21 | 29 % |

**Reading:** more than six Java files out of ten depend directly on the Java UI. The pure "business" layer is therefore the minority, and a migration that rewrites the graphical part touches most of the code.

### 1.2 Most used AWT types [V]

| Type | Number of files using this type |
|---|---:|
| `java.awt.geom.Point2D` | 84 |
| `java.awt.Graphics2D` | 63 |
| `java.awt.geom.Rectangle2D` | 62 |
| `java.awt.Color` | 62 |
| `java.awt.event.MouseEvent` | 38 |
| `javax.swing.JPanel` | 26 |
| `java.awt.Component` | 25 |
| `java.awt.Dimension` | 24 |
| `java.awt.Graphics` | 22 |

In addition: 35 classes define `draw(Graphics2D)` and 16 override `getShape()` [V]. Drawing is therefore spread across **all node and edge types**, not centralised.

## 2. Critical coupling points

Ranked by estimated migration difficulty [D].

| # | Point | Location | Difficulty | Suggested strategy |
|---|---|---|:---:|---|
| C-1 | **HTML text rendered by `JLabel`** (measurement and display) | `MultiLineString` | **Very high** | Define a supported HTML subset (tags used in the existing files), implement a text layout, accept and document layout differences |
| C-2 | **Java2D engine**: strokes, arrowheads (7 types), angles, image cropping, rounded corners | `SegmentedLineEdge` (692 l.), `ImageNode` (539 l.), 35 `draw` classes | **High** | Port the geometry with numeric tests on the corpus; compare renderings against reference images |
| C-3 | **Edge routing** (`BentStyle`: HV, VH, HVH, VHV, AUTO, FREE) | `SegmentedLineEdge`, `ActivityDiagramGraph` | **High** | Specify each style by examples (input → expected route), then implement |
| C-4 | **Hover and click detection** (`getShape`, `contains`) | 16 classes | Medium | Reproduce the shapes; test selection on the corpus |
| C-5 | **Model and view mixed together**: `MultiLineString` holds a `JLabel`, `ImageNode` holds `ImageIcon`/`Image`, `draw` receives `Graphics2D` | Model | **High** | Separate a pure model (no AWT) from a rendering projection. This is the main design investment |
| C-6 | **Reflection-based serialisation**: the schema is the code | `XMLPersistenceService` (1,098 l.) | High | Write an explicit schema (or a per-class mapping) reproducing the tag names; validate on the corpus |
| C-7 | **Dependency injection through static state** (`BeanFactory`, `BeanInjector`) | `ManiocFramework` (987 l.) | Medium | Explicit passing of dependencies through constructors |
| C-8 | **Static prototypes**: `static final` lists of instances, tooltips frozen at the locale | Plugins, `*DiagramGraph` | Low to medium | Build prototypes on demand, with the current locale |
| C-9 | **Style singletons** compared by reference (`BentStyle`, `ArrowHead`) | `property/` | Low | Replace with enumerations, keeping the name correspondence |
| C-10 | **Swing threads and event queue** (`EventQueue`, `javax.swing.Timer`, shutdown hooks) | 8 files + `AutoSaveBehavior` | Medium | Explicit scheduler, without dependency on the Swing loop |
| C-11 | **Clipboard and drag-and-drop** (`java.awt.datatransfer`, `DropTarget`) | 4 files | Medium | Clipboard API of the target |
| C-12 | **Printing** (`java.awt.print`) | 1 file | Low | Printing API of the target |
| C-13 | **Image encoding** (`ImageIO`, `BufferedImage`) | 17 files | Low to medium | Native equivalent in the target |
| C-14 | **Preferences** (`java.util.prefs`) | `DefaultUserPreferencesDao` | Low | Storage of the target + migration (04, section 7) |
| C-15 | **FlatLaf / pgslookandfeel themes** | `framework.theme` | Low (to replace) | Do not port: use the target's theme |
| C-16 | **SWT bridge** (Eclipse) | `violetproduct-eclipseplugin` (21 files) | — | Drop (obsolete module, section 3) |
| C-17 | **CheerpJ interoperability** (JavaScript native methods) | `cheerpj` (8 classes) | — | Replace with a native web version |

### 2.1 Detail of point C-1: HTML text [V]

Text is wrapped as follows:

```html
<html><div style="<blockStyle>">…content…</div></html>
```

- The size is measured by `JLabel.getPreferredSize()`: **the geometry of the boxes depends on Swing's rendering engine** (fonts, line spacing, line breaks).
- Text is interpreted as HTML only if it starts with `<html>`; otherwise it is escaped.
- The project requires not using `MultiLineLabel.java` (present in the repository, 1 file) [V].

> **Consequence:** it is **impossible** to reproduce pixel by pixel the existing boxes in another technology without reproducing Swing's HTML rendering engine. The choice to make early is therefore:
> - **(a)** accept layout differences, and document them;
> - **(b)** implement an HTML subset defined from the tags actually present in the corpus;
> - **(c)** simplify the text format and convert existing files (compatibility break).
>
> This decision affects the visual comparison of migrated files. It must appear in the requirements (07).

## 3. Dead code and components not to carry over [V]

| Element | Size | Observation | Recommended action |
|---|---:|---|---|
| `NanoHTTPD` (`framework.util`) | 699 l., 6 classes | **No reference** outside the file | Delete |
| `JSONPersistenceService` | 598 l. | No production reference; used **only** by its test | Delete the service and its test |
| `StandardJavaFilePersistenceService` | 327 l. | Not instantiated; `java.beans` format unused | Delete |
| Inner classes `XHTMLPersistenceServiceParserGetter` and `…ParserCallback` | ~30 l. | Declared, never instantiated in the file | Delete |
| `FileCompatiliblyServiceToDelete` | — | **No reference**; the name indicates planned deletion | Delete |
| `NETWORK_GUESTCONFIG_*` constants (peer-to-peer) | — | **No use** outside their declaration | Delete, together with the `file.open_remote` entry (04, 2.2) |
| `violetproduct-eclipseplugin` | 21 files, 2,590 l. | Outside the reactor; `JavaSE-1.6`; version 2.0.0; depends on a `violetproduct-swing.jar` | Drop, or rebuild separately if the need is confirmed |
| `violetproduct-jnlp`, `violetproduct-rpm` | — | Disabled (outside the reactor) | Drop, or explicitly re-enable |
| "Java code" / "Python code" entries in the File menu | — | Empty listeners, commented menu | Drop or implement (decision F-6, 04) |
| `antlr 2.7.5` dependency | — | **0 imports** | Delete [D] |
| `javaws 1.6.0` dependency | — | **0 imports** of `javax.jnlp` | Delete [D] |
| `spring-boot-maven-plugin` (Swing module) | — | **0 Spring imports**; the project's guideline forbids Spring | Delete |
| `AppletUserPreferencesDao` DAO | — | Returned by `PreferencesServiceFactory`; activation not traced | Confirm, then delete if unused [?] |
| `VersionChecker`: Java minimum `1.6` | — | Obsolete: the POM requires 17 | Replace the constraint with Java 17 (or the target's) |

**Volume of code not to carry over:** roughly **1,700 lines** of dead classes in the core (`NanoHTTPD`, JSON, `java.beans`, inner classes) [V], excluding Eclipse. In addition, 2,590 lines of unmaintained Eclipse code.

## 4. Observed anomalies

Ranked by subjective severity [D].

| # | Anomaly | Location | Effect | Proposed fix |
|---|---|---|---|---|
| A-1 | **NullPointerException** if the start end of an edge is `null` | `AbstractGraph.connect` (call to `start.addConnection`) | Crash at call | Validate arguments, raise an explicit error |
| A-2 | **Identifier reassignment** on every insertion | `AbstractGraph.addNode` | Unstable identifier after undo or paste | Reassign only if the identifier is missing or conflicting |
| A-3 | **`removeConnection` empty**: a node does not know its edges | `AbstractNode` | Deleting a node = scanning all edges; risk of orphan edges | Maintain a node → edges index, or guarantee cascading deletion |
| A-4 | **One JVM shutdown hook per workspace**, never removed | `AutoSaveBehavior` | Minor leak, repeated save at closing | Register a single hook, or remove it when the workspace closes |
| A-5 | **Byte-by-byte reading** of the file | `XHTMLPersistenceService.getInputStreamContent` | Slow on large files | Block reading or `readAllBytes` |
| A-6 | **HTML parsing with a regular expression** | `XHTMLPersistenceService.extractXmlContent` | Fragile if the page is modified | HTML parser, or documented fixed position |
| A-7 | **Thumbnail generated at every save** | `XHTMLPersistenceService.write` | Full rendering cost at every save, including the auto-save every 2 s | Regenerate only if the diagram changed; cost to measure |
| A-8 | **Whole XML document built in memory** (`StringBuilder`) | `XMLPersistenceService.write` | Memory roughly doubled for large diagrams [D] | Streamed writing |
| A-9 | **Wrong CLI help** (`-cheeprjMode`, `-autoSave`) | `UMLEditorApplication` | Misleading documentation | Fix the help |
| A-10 | **Outdated repository documentation** (XStream, Java 21, missing web module, "no tests") | `.github/copilot-instructions.md` | Bad decisions for contributors | Rewrite the documentation (see 00, section 5) |
| A-11 | **Inconsistent JUnit versions** (5.10.2 and 5.11.3) | Framework / Swing POMs | Confusion, risk of conflict | Align |
| A-12 | **External Maven repository** (SourceForge) declared | Root POM | Build depends on a third-party service | Move artefacts to a repository you control |
| A-13 | **No size limit** when reading a file | Persistence | Risk of memory exhaustion with a malicious file | Explicit limit |

## 5. Security [V/D]

| # | Topic | Finding | Level |
|---|---|---|---|
| S-1 | XXE (external XML entities) | DOCTYPE refused in both parsers | Protected |
| S-2 | Command execution | `BrowserLauncher` uses argument arrays; URLs come from the code | Low |
| S-3 | Transfer to the online editor | `postMessage` to a fixed origin; acknowledgement checked by `event.source` (not by `event.origin`) | Low to moderate |
| S-4 | Data in `window.name` | Fallback of the transfer: the diagram stays readable in the named window [D] | **Moderate** |
| S-5 | Key in the repository | `violetproduct-jnlp/src/main/jnlp/keystore.jks` is committed [V]; whether it is a production or test key is not established [?] | Check first |
| S-6 | Publication credentials | Passed through environment variables in CI (`SOURCEFORGE_*`) [V] | Acceptable |
| S-7 | Telemetry and network calls | **No network call** in the production code; `VersionChecker` does not contact any server [V] | Good: no known data collection |
| S-8 | Code execution on opening | The `.violet.html` file contains JavaScript; the script only reads and encodes the content [V]; absence of injection not tested [?] | To test |

## 6. Test debt [V]

- **6 test classes**, all covering format reading or writing, or image/grid serialisation: `XMLPersistenceServiceTest`, `JSONPersistenceServiceTest`, `LegacyXhtmlCompatibilityTest`, `LegacyXmlWriteCompatibilityTest`, `ImageNodeClipboardSerializationTest`, `GridStickerSerializationTest`.
- **No test** covers: the interaction behaviours (24), undo/redo, geometric rendering, edge routing, PNG/PDF export, internationalisation.
- The compatibility tests **encode precise values** (width `288.9729729729727`) that must be reproduced or tolerated.
- The CI workflow runs the tests (`mvn package deploy`, without `-DskipTests`) [V]. **They could not be run here** (dependencies absent) [V].

## 7. Migration risks

Qualitative assessment [D]: probability (P) and impact (I) rated H/M/L (high, medium, low).

| # | Risk | P | I | Mitigation |
|---|---|:---:|:---:|---|
| R-1 | Text rendering differences (C-1) | H | H | Explicit decision (5 options in 2.1); visual tests with tolerance |
| R-2 | Breaking compatibility of `.violet.html` files | H | H | Reference corpus (03, section 11); cross read/write tests |
| R-3 | Behaviour regression in the 24 interactions | H | M | Acceptance scenarios written before migration (07) |
| R-4 | Undo/redo regression | H | M | Command model tested; properties (undo then redo = identity) |
| R-5 | Loss of implicit behaviours (static values, frozen locale, shared prototypes) | M | M | Inventory (section 2, C-7/C-8); tests of language switching |
| R-6 | Identifier changes (A-2) | M | H | Decide the strategy before migrating data |
| R-7 | Migration of user preferences | M | L | Conversion script from the `java.util.prefs` location |
| R-8 | Java plugin API not carried over | M | M | Decide whether third parties must write plugins, and in what form |
| R-9 | Performance on large diagrams (A-7, A-8) | M | M | Benchmarks on diagrams of 500 and 2,000 elements |
| R-10 | Incomplete translations (Polish) | M | L | Inventory of keys per language; translation before release |
| R-11 | Packaging and signing (`.exe`, `.deb`, SourceForge) | M | M | Define the target's delivery channels from phase 0 |
| R-12 | Licence compatibility (GPLv2+) | L | H | Inventory of the licences of the target's dependencies (FlatLaf Apache-2.0 to check, FreeHEP, etc.) |
| R-13 | Signing key in the repository (S-5) | M | H | Immediate check, rotation if needed |
| R-14 | Team skills on the target | ? | H | To assess; drives the choice (06) |
| R-15 | Dependency on the online editor service (S-3, S-4) | M | L | Decide to keep or remove |

## 8. Immediate priorities (before any target decision)

1. Check the nature of the `keystore.jks` file (S-5).
2. Set up a **reproducible build** (access to dependencies, fallback repository) and run the 6 test classes.
3. Produce the **reference corpus** (03, section 11) with the current version.
4. Remove the dead code of section 3, which reduces the scope to migrate.
5. Decide the text strategy (C-1) and the identifier strategy (A-2).
