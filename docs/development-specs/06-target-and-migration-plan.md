# 06 — Target, target architecture and migration plan

The target technology has not been chosen. This document compares the options against the analysed code, proposes a **conditional** recommendation, and describes a phased plan that does not depend on the final choice.

## 1. Decision criteria

| Criterion | Why it matters here |
|---|---|
| Rendering fidelity (text, arrows, routing) | Existing files must display in a comparable way (05, C-1 to C-3) |
| Reuse of business logic | The model and rules (02) are the most stable part |
| Compatibility of `.violet.html` files | Non-negotiable data contract (03) |
| Platform coverage | Currently: Windows (`.exe`), Debian (`.deb`), browser (CheerpJ) |
| Native web mode | Would replace CheerpJ and the online editor (04, F-7/F-8) |
| Available skills | Not provided; **to be supplied by the team** (R-14) |
| GPLv2+ licence | The target and its dependencies must be compatible (R-12) |
| Rewrite cost | Depends on how much Java code can be kept (61 % touches AWT, 05, 1.1) |

## 2. Options compared

Qualitative assessment [D], to be validated by a prototype (phase 1).

| Option | Description | Reuse of Java code | Rendering | Desktop | Web | Main risk |
|---|---|---|---|---|---|---|
| **A. Web: TypeScript + UI framework + SVG** | Declarative rendering in SVG, centralised state, Vite | None (rewrite); model portable by translation | Good for shapes and arrows; native HTML text (faithful to HTML semantics, not to Swing) | Via B | **Native** | Hit-testing and large diagrams in SVG; rewriting 61 % of the code |
| **B. Desktop wrapper: option A in Electron or Tauri** | Same UI as A, packaged | Same as A | Same as A | Yes (Windows, macOS, Linux) | Yes | Size and memory use (Electron); signing to redo |
| **C. Modern JVM: Kotlin + Compose Multiplatform** | Java → Kotlin translation, Skia drawing | **High** (Kotlin ≈ Java, direct interop) | Good; text through the Skia engine (not HTML) | Yes | Experimental (Wasm) | Web maturity; HTML rendering must be re-implemented |
| **D. .NET: C# + Avalonia** | Rewrite in C#, Avalonia UI | Low | Good | Yes | Via WebAssembly (beta) | Full rewrite; new build chain |
| **E. Stay on Java: JavaFX (Canvas)** | Replace Swing with JavaFX | Medium (domain kept) | Canvas; text via `WebView` or a custom engine | Yes | No (CheerpJ does not apply to JavaFX) | Does not solve the dependency on Swing HTML; Java 17 legacy |

### 2.1 Quick reading

- **Web first** (and desktop derived from it): option **A** or **B**. It removes CheerpJ and the online editor, replacing them with a native application. It requires a complete rewrite of the UI and of the drawing logic.
- **Continuity of code first**: option **C**. It keeps the structure of the model, the rules and the tests, and leads to a progressive Java → Kotlin migration.
- **Desktop delivery only, simplest path**: option **E** is the least costly, but it keeps the debt (JVM, unresolved Swing HTML).

### 2.2 Conditional recommendation

- If the goal includes **the browser as a first-class platform**: **A** (with **B** as a desktop extension).
- If the goal is **desktop first**, with a Java team: **C**.
- In all cases, **start with phases 0 and 1 (below)**: they are common to all options and allow the choice to be made on real data.

## 3. Generic target architecture

Whatever the technology, the target should separate five layers. This separation is what is missing today (05, C-5).

```
┌────────────────────────────────────────────────────────────┐
│ 5. Interface (menus, side bar, dialogs, themes)            │
├────────────────────────────────────────────────────────────┤
│ 4. Interactions (tools, mouse, keyboard) → commands        │
├────────────────────────────────────────────────────────────┤
│ 3. Rendering (geometry, text, arrows) — no UI dependency   │
├────────────────────────────────────────────────────────────┤
│ 2. Persistence (.violet.html format, XML, migrations)      │
├────────────────────────────────────────────────────────────┤
│ 1. Domain (graph, nodes, edges, rules, undo)               │
└────────────────────────────────────────────────────────────┘
```

### 3.1 Dependency rules

- Layer **1** depends on no interface library and no platform geometry.
- Layer **2** depends only on layer **1** and an XML/HTML parser.
- Layer **3** produces a **neutral geometric representation** (shapes, paths, measured text) consumed by the concrete renderers (screen, PNG export, PDF, printing).
- Layer **4** turns events into **commands** (see 02, section 7); it never modifies the model directly.
- Layer **5** observes the model and sends commands.

### 3.2 Diagram plugins

A diagram type should be described **declaratively** (nodes, edges, editable fields, rules, default styles, localised labels) rather than by rendering code. This description can serve the model, the toolbar and file validation at the same time. Specific drawing code remains only where the shape requires it.

> **Decision to take [?]:** must plugins remain **addable by third parties** (R-8)? If so, the declarative description becomes a public API to version.

## 4. What must be kept identical

| Element | Reason |
|---|---|
| `.violet.html` format (reading and writing) | Compatibility of existing files |
| Names of diagram types and extensions | Users and existing documents refer to them |
| Set of user behaviours (shortcuts, drag and drop, selection) | Usage parity (04) |
| Translations (fr, de, pl, en) | Work already done; reusable as is through conversion |
| PNG and PDF exports | Features in use |

## 5. What must be dropped or replaced

| Element | Action | Justification |
|---|---|---|
| CheerpJ and the Java browser mode | Replace with the target's web version | Dependency on a third-party technology; the purpose of the migration |
| Online editor (transfer via `postMessage` and `window.name`) | Decision (03, section 6; S-3, S-4) | Moderate security risk |
| Dead code (05, section 3) | Delete | Reduces scope by about 1,700 lines |
| Eclipse module | Drop | Obsolete, outside the reactor |
| "Java code" / "Python code" menus | Decision (F-6) | Not implemented |
| FlatLaf and pgslookandfeel themes | Replace with the target's theme system | Tied to Swing |
| Manioc injection | Replace with explicit injection | Tied to static state (C-7) |
| Reader for 0.16 class names | **Keep** while 0.16 files exist | Compatibility |

## 6. Migration plan by phase

Each phase has a **verifiable output**. Moving to the next phase happens only after that output is validated.

### Phase 0 — Stabilisation and reference (common to all targets)

**Goal:** have a reproducible build and a behaviour reference.

Actions:
1. Set up a **reproducible build** (access to dependencies, fallback repository, cache) and run the 6 test classes (05, section 6).
2. Check the **nature of the keystore** and, if necessary, remove it from the repository (05, S-5).
3. Produce the **reference corpus**: one file per diagram type, all node and edge types, edge cases (03, sections 10 and 11), and the six sample files provided. For each file: XML, thumbnail PNG, exported PNG and PDF.
4. Remove the dead code (05, section 3) in a dedicated branch.
5. Fix trivial anomalies (A-1, A-9, A-10, A-11) without changing behaviour.
6. Document the HTML rendering differences (C-1) from the corpus.

**Output:** green build; versioned corpus; documented differences.

### Phase 1 — Decisions and prototype

**Goal:** settle the structural decisions and validate the target.

Decisions to take (requirements in 07):
- technology target (06, section 2);
- text strategy (05, C-1): accepted differences or HTML subset;
- identifier strategy (02, 2.1; A-2);
- future of third-party plugins (3.2);
- future of the online editor and browser mode (04, F-7, F-8);
- support for 0.16 files and XStream (03, section 4.4).

Prototype (time-boxed): **one class diagram** end to end, with reading and writing of the `.violet.html` format, rendering of a class, an association and an arrow, and PNG export.

**Output:** decisions recorded; prototype compared with the corpus (visual and structural).

### Phase 2 — Domain and persistence

**Goal:** reproduce layers 1 and 2 from the corpus.

- Implement the model (02) without interface dependency.
- Implement the reader and writer of the format (03), with the same tag names.
- Implement the 0.16 reader (03, section 4.2).
- Port the graph rules (02, section 6) and their tests.

**Exit criteria:**
- all corpus files are **read** without error and produce the same model (structural comparison);
- all files written by the target are **re-read by the current application** (cross test);
- the 6 diagram types are covered by tests.

### Phase 3 — Rendering and geometry

**Goal:** reproduce layer 3.

- Implement the shapes, the arrows (7 types), the routing styles (6 styles), the clipping margins.
- Implement text (C-1) according to the phase 1 decision.
- Implement PNG and PDF exports (and printing).

**Exit criteria:**
- visual comparison on the corpus with a tolerance defined in pixels;
- identical edge routing on the reference cases;
- readable PDF export with dimensions equal to the original.

### Phase 4 — Interactions and undo

**Goal:** reproduce layers 4 and 5 (functional part).

- Implement the 24 behaviours (04, section 4) as commands.
- Implement undo and redo as a command model.
- Implement auto-save (2 s of inactivity, shutdown) without a leaking hook (A-4).
- Implement the menus, shortcuts (04, 2.3) and the side bar.

**Exit criteria:**
- acceptance scenarios (07, section 3) pass;
- property: undoing then redoing an action returns to the same state;
- shortcuts identical to the current version, except documented decisions.

### Phase 5 — Internationalisation, preferences and themes

- Convert the fr, de, pl, en translations (C-8: prototypes built from the current locale).
- Implement preference storage and the **migration** from `java.util.prefs` (R-7).
- Implement themes (at least light and dark).

**Exit criteria:** language change without restart; preferences migrated on the test machine.

### Phase 6 — Distribution and continuous integration

- Define the **delivery channels** of the target (Windows, Debian, browser, others) and their signing (R-11).
- Set up continuous integration: build, tests, corpus, visual tests.
- Publish the web version on the chosen hosting, replacing GitHub Pages / CheerpJ.

**Exit criteria:** an installable version per channel, produced automatically.

### Phase 7 — Transition and retirement

- Coexistence period: the current application remains available; both versions read and write the same files (phase 2 criterion).
- Communication plan for users (removed features: code export, CheerpJ browser mode, peer-to-peer) and contributors.
- Removal of the old code once parity is validated by users.

## 7. Cross-cutting test strategy

| Level | What | Suggested tool (independent of the target) |
|---|---|---|
| Unit | Graph rules, routing, serialisation | Test framework of the target |
| Characterisation | The corpus: reading, writing, export | Comparison scripts |
| Visual | PNG rendering per diagram, tolerance in pixels | Image comparison |
| Properties | Round trip: reading then writing gives the same model | Randomised-data tests |
| Interaction | User scenarios (07, section 3) | End-to-end tests |
| Performance | Diagrams of 500 and 2,000 elements: opening, saving, rendering time | Benchmarks in CI |

## 8. Estimate (to be produced by the team)

This document does not estimate effort: no data on the team, the target date or skills has been provided. To estimate, the following are needed:
- the **team size** and its skills on the target;
- the **retained scope** (section 5);
- the **result of phase 1** (prototype).

Useful size indicators [V]: 330 Java files, about 46,700 lines, including about 1,700 dead lines and about 2,590 Eclipse lines outside the scope.

## 9. Risks specific to this phase

See 05, section 7 (R-1 to R-15). The most structuring ones for the choice of target are **R-1** (text rendering), **R-2** (file compatibility) and **R-14** (skills).
