# 08 — Appendix: numerical inventory

Figures obtained by direct counting on commit `70c092a` (see 00). Line counts include tests when the module contains some.

## A. Modules and code

| Module | Java files | Java lines | Other resources | In reactor |
|---|---:|---:|---:|---|
| `violet-framework` | 212 | 32,157 | 119 | Yes |
| `violetplugin-classdiagram` | 24 | 1,501 | 4 | Yes |
| `violetplugin-sequencediagram` | 8 | 1,501 | 4 | Yes |
| `violetplugin-activitydiagram` | 12 | 1,318 | 4 | Yes |
| `violetplugin-objectdiagram` | 8 | 957 | 4 | Yes |
| `violetplugin-statediagram` | 7 | 673 | 4 | Yes |
| `violetplugin-usecasediagram` | 7 | 580 | 4 | Yes |
| `violetproduct-swing` | 31 | 5,410 | 47 | Yes |
| `violetproduct-exe` | 0 | 0 | 1 | Yes |
| `violetproduct-deb` | 0 | 0 | 6 | Yes |
| `violetproduct-eclipseplugin` | 21 | 2,590 | 1 | **No** |
| `violetproduct-jnlp` | 0 | 0 | 2 | **No** |
| `violetproduct-rpm` | 0 | 0 | 3 | **No** |
| **Total Java** | **330** | **≈ 46,700** | | |

## B. Resources (all modules)

| Type | Count |
|---|---:|
| `.properties` files (i18n and configuration) | 81 |
| PNG images | 63 |
| GIF images | 12 |
| JPEG images | 4 |
| XML files | 17 |
| HTML files | 11 |
| XSL stylesheets | 8 |
| TrueType fonts | 1 (`Andale_Mono.ttf`) |
| SVG files | 0 |
| CSS stylesheets | 0 (the style is embedded in the HTML pages) |
| `META-INF/services` files (plugins) | 6 |

`.properties` files per language: French 25, German 25, Polish 3, English (no suffix) the rest.

## C. Tests

| File | Module | Subject |
|---|---|---|
| `XMLPersistenceServiceTest` | framework | XML reading and writing |
| `JSONPersistenceServiceTest` | framework | JSON service (dead code, 05 §3) |
| `LegacyXhtmlCompatibilityTest` | swing | Reading of the `sample.*.violet.html` examples |
| `LegacyXmlWriteCompatibilityTest` | swing | Exact form of the written XML |
| `ImageNodeClipboardSerializationTest` | swing | Image in the clipboard |
| `GridStickerSerializationTest` | swing | Grid not serialisable |

Test resources: `test.class.violet.html` (framework); six `sample.*.violet.html` (swing).

## D. Coupling to the interface (files importing AWT/Swing)

| Module | Files | Total | Share |
|---|---:|---:|---:|
| `violet-framework` | 139 | 212 | 66 % |
| Plugins (six) | 27 | 66 | 41 % |
| `violetproduct-swing` | 24 | 31 | 77 % |
| **Excluding Eclipse** | **190** | **309** | **≈ 61 %** |
| `violetproduct-eclipseplugin` (SWT) | 6 | 21 | 29 % |

Plugins, detail: activity 9/12; class 3/24; object 4/8; sequence 5/8; state 4/7; use case 2/7.

## E. Extension points and components

| Element | Count |
|---|---:|
| Diagram types (plugins) | 6 |
| Behaviour classes (`behavior/`) | 35 |
| Behaviours registered in the workspace | 24 |
| Specific undo handlers | 8 |
| Undo composite | 1 |
| `draw(Graphics2D)` classes | 35 |
| Classes overriding `getShape()` | 16 |
| Theme classes | 6 |
| Menu entries (`MenuFactory.properties`) | 146 lines |
| Java files of the side bar (`workspace/sidebar/`) | 21 |
| CLI options parsed | 5 (`-reset`, `-english`, `-help`, `-kioskMode`, `-cheerpjMode`) |
| Interface languages | 4 (en, fr, de, pl) |

## F. Persistence

| Element | Lines | Status |
|---|---:|---|
| `XMLPersistenceService` | 1,098 | Active (core) |
| `XHTMLPersistenceService` | 203 | Active (default of the Swing application) |
| `Violet016BackportFormatService` | 201 | Active (7 mapping entries) |
| `JSONPersistenceService` | 598 | **Not used in production** |
| `StandardJavaFilePersistenceService` | 327 | **Not instantiated** |
| `XHTMLFileTemplate.violet.html` | 282 | Active (template) |
| File reading and writing (`JFileReader`, `JFileWriter`, etc.) | 38 to 48 each | Active |

## G. Dead code and deletion candidates

| Element | Lines | Reason |
|---|---:|---|
| `NanoHTTPD` | 699 | No reference |
| `JSONPersistenceService` (+ test) | 598 | Used by its test only |
| `StandardJavaFilePersistenceService` | 327 | Not instantiated |
| Unused inner classes of `XHTMLPersistenceService` | ~30 | Not instantiated |
| `FileCompatiliblyServiceToDelete` | — | No reference |
| `NETWORK_GUESTCONFIG_*` constants | — | No use |
| "Java code" / "Python code" menus | — | Empty listeners, commented menu |
| `file.open_remote` shortcut | — | No menu element |
| `antlr`, `javaws` dependencies | — | No import |
| `spring-boot-maven-plugin` plugin | — | No Spring import |
| `violetproduct-eclipseplugin` | 2,590 | Outside reactor, obsolete |

## H. Third-party dependencies (version, observed usage)

| Library | Version | Relevant files |
|---|---|---:|
| FlatLaf | 3.3 | 1 |
| pgslookandfeel | 1.1.1 | 3 |
| l2fprod-common-tasks | 7.3 | 2 |
| commons-vfs | 1.0 | 1 |
| commons-codec | 1.16.0 | 1 |
| FreeHEP graphicsio + pdf | 2.4 | 2 |
| jaxmejs | 0.5 | 1 |
| antlr | 2.7.5 | **0** |
| javaws | 1.6.0 | **0** |
| JUnit Jupiter | 5.10.2 / 5.11.3 | tests |
| XStream | (documentation: 1.4.21) | **0** |
| Gson | (documentation: 2.8.9) | **0** |

## I. Delivery channels

| Channel | Mechanism | Status |
|---|---|---|
| Executable JAR | Shade plugin, `UMLEditorApplication` | Active |
| Windows `.exe` | keytool (key generation) | Active |
| Debian `.deb` | jdeb | Active |
| RPM | `violet.desktop`, `violet.sh`, `violet.png` files | Disabled |
| JNLP | Velocity template, keystore | Disabled |
| SourceForge | scp in `maven-antrun-plugin` | Active on `deploy` |
| GitHub Packages | `distributionManagement` | Active on `deploy` |
| GitHub Pages | JAR and `index.html` (CheerpJ) | Active on `master` |
| Certificate workflow | cosign | Disabled (branch `disabled`) |

## J. Declared versions (inconsistencies)

| Source | Application version | Java |
|---|---|---|
| Root POM (`applicationVersion`) | 2.3.2-SNAPSHOT | 17 (release) |
| CI (`maven-publish.yml`) | Reads the POM | 21 (build JDK) |
| `copilot-instructions.md` | 2.3.0-SNAPSHOT | 21 |
| `README.md` | — | 17 minimum |
| `VersionChecker` (startup check) | — | 1.6 minimum |
| `violetproduct-eclipseplugin` (manifest) | 2.0.0 | JavaSE-1.6 |
