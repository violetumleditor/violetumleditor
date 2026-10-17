# 01 — Current architecture

Reference: commit `70c092a` (see 00-readme). Evidence level is given in brackets.

## 1. Overview

Violet is a Java desktop application (Swing) for editing UML diagrams, organised as a **Maven multi-module** project. The core is a library (`violet-framework`) that provides the graph model, the Java2D drawing engine, mouse/keyboard interactions, persistence, internationalisation and a plugin system. Each diagram type is an independent **plugin**. The Swing product assembles everything.

```
                 ┌──────────────────────────────────────────┐
                 │  violetproduct-swing  (application)      │
                 │  UMLEditorApplication, menus, MainFrame  │
                 └───────────────┬──────────────────────────┘
                                 │ depends on
        ┌────────────────────────┼─────────────────────────────────┐
        ▼                        ▼                                 ▼
 violetplugin-*  (×6)     violet-framework  (core)           packaging
 Graph + Nodes + Edges    model, workspace, behaviors,      exe / deb
 + SPI IDiagramPlugin     persistence, plugins, DI, i18n
```

## 2. Modules (Maven)

Figures verified by counting `.java` files under `src/` (tests included).

| Module | Role | Java files | Java lines | Other resources | In root reactor |
|---|---|---:|---:|---:|---|
| `violet-framework` | Core: model, workspace, persistence, plugins, DI, themes | 212 | 32,157 | 119 | Yes |
| `violetplugin-classdiagram` | Class diagram | 24 | 1,501 | 4 | Yes |
| `violetplugin-sequencediagram` | Sequence diagram | 8 | 1,501 | 4 | Yes |
| `violetplugin-activitydiagram` | Activity diagram | 12 | 1,318 | 4 | Yes |
| `violetplugin-objectdiagram` | Object diagram | 8 | 957 | 4 | Yes |
| `violetplugin-statediagram` | State diagram | 7 | 673 | 4 | Yes |
| `violetplugin-usecasediagram` | Use case diagram | 7 | 580 | 4 | Yes |
| `violetproduct-swing` | Desktop application (entry point) | 31 | 5,410 | 47 | Yes |
| `violetproduct-exe` | Windows `.exe` packaging (keytool signing) | 0 | 0 | 1 | Yes |
| `violetproduct-deb` | Debian `.deb` packaging (jdeb plugin) | 0 | 0 | 6 | Yes |
| `violetproduct-eclipseplugin` | Eclipse integration (SWT) | 21 | 2,590 | 1 | **No** |
| `violetproduct-jnlp` | Java Web Start (disabled) | 0 | 0 | 2 | **No** |
| `violetproduct-rpm` | RPM package (disabled) | 0 | 0 | 3 | **No** |

### Remarks on the modules [V]

- The `violetproduct-eclipseplugin` module contains code but is **not built** by `mvn package` from the root. Its `MANIFEST.MF` declares `Bundle-RequiredExecutionEnvironment: JavaSE-1.6` and `Bundle-Version: 2.0.0.qualifier`: it is very probably **obsolete** [D]. Its `pom.xml` references a parent in version `1.0.0`, inconsistent with the current parent `1.0.0-SNAPSHOT`.
- `violetproduct-jnlp` contains a `keystore.jks` **committed to the repository** (`violetproduct-jnlp/src/main/jnlp/keystore.jks`). Its role (signing key or test key) has not been determined [?]. It must be examined before the repository is published in any new form.

## 3. Layers of the `violet-framework` core

Root package: `com.horstmann.violet`.

| Layer | Package | Responsibility | Java files [V] |
|---|---|---|---:|
| Graph model | `product.diagram.abstracts` | `IGraph`, `INode`, `IEdge`, base classes, style properties | 33 |
| Common elements | `product.diagram.common` | `NoteNode`, `NoteEdge`, `ImageNode`, `PointNode`, `DiagramLink*` | 6 |
| Workspace | `workspace` | `Workspace` (facade), `EditorPart` (canvas), `SideBar` (tools) | — |
| Behaviours | `workspace.editorpart.behavior` | One class per user interaction (see 04) | 35 |
| Side bar | `workspace.sidebar.*` | Drawing tools, colours, thicknesses, navigator | — |
| Persistence | `framework.file.persistence` | Reading and writing diagrams | 13 |
| Export | `framework.file.export` | PNG, PDF | 1 |
| Files | `framework.file` | `GraphFile`, `IFile`, choosers, naming | — |
| Plugins | `framework.plugin` | SPI discovery (`PluginLoader`), registry (`PluginRegistry`), extension point | — |
| Injection | `framework.injection` | Home-made container "Manioc" | 7 |
| Themes | `framework.theme` | Swing themes (see section 8) | 10 |
| Preferences | `framework.userpreferences` | User preferences and launch options | — |
| Swing components | `framework.swingextension` | Custom Swing components | — |
| Property editors | `framework.propertyeditor` | Swing editors for properties | — |
| Dialogs | `framework.dialog` | `DialogFactory` | — |
| Printing | `framework.printer` | Printing | — |
| Utilities | `framework.util` | IDs, PDF, version, browser launcher, HTTP server (unused) | — |

## 4. Startup sequence [V]

Entry point: `com.horstmann.violet.UMLEditorApplication.main`. Sequence observed in the constructor:

1. Creation of `LaunchingPreferences(args)` (parsing of CLI options, see 04).
2. Manual registration in the `BeanFactory` factory:
   - `IFilePersistenceService` → `XHTMLPersistenceService`
   - `IUserPreferencesDao` → `DefaultUserPreferencesDao`
   - `IFileChooserService` → `CheerpJFileChooserService` if `-cheerpjMode`, otherwise `JFileChooserService`
   - `CheerpJInterfaceService` (registered **in all cases**)
3. Injection of the application's dependencies (`BeanInjector`).
4. If `-reset`: user preferences are reset.
5. If `-english`: `Locale.setDefault(Locale.ENGLISH)`.
6. If `-help`: help is displayed, then `System.exit(0)`.
7. `createDefaultWorkspace()`: plugin loading, graphical theme management, splash screen, Java version check, creation of the main window `MainFrame`.

The comment of `createDefaultWorkspace` explicitly lists: "plugins loading + GUI theme management + a splash screen + jvm checking" [V].

### Java version check anomaly [V]

`VersionChecker` compares the JVM version to the constant `JAVA_VERSION = "1.6"`. The real minimum imposed by the POM is **Java 17**. The check is therefore **obsolete**: it does not block a JVM that is too old and does not express the real constraint.

## 5. Plugin system [V]

### Discovery

- Each diagram module declares its `IDiagramPlugin` class in `src/main/resources/META-INF/services/com.horstmann.violet.framework.plugin.IDiagramPlugin`. Six such files were found, one per plugin.
- The core loads plugins through `java.util.ServiceLoader` (in `PluginLoader` and in `XMLPersistenceService.initializeTagTypes`). The `PluginRegistry` is a singleton.
- In the final JAR (`violetproduct-swing`), the `maven-shade-plugin` **concatenates** the `META-INF/services` files (`AppendingTransformer`). Without this mechanism, only the first plugin would be discovered.

### `IDiagramPlugin` contract [V]

```java
public interface IDiagramPlugin extends AbstractPlugin {
    String getName();                 // display name (localised)
    String getCategory();             // menu category (localised)
    String getFileExtension();        // e.g. ".class.violet.html"
    String getFileExtensionName();    // file type label
    Class<? extends IGraph> getGraphClass();
}
```

`AbstractPlugin` adds `getDescription()`, `getProvider()` and `getVersion()` (the plugins return `"1.0.0"`).

### Backward-compatibility extension point

`Violet016FileFilterExtensionPoint` lets each plugin provide a **mapping table** from old class names (`com.horstmann.violet.*`) to the new package names. Example from the class diagram plugin: `com.horstmann.violet.ClassNode` → `com.horstmann.violet.product.diagram.classes.ClassNode`.

## 6. "Manioc" dependency injection [V]

The project uses a **home-made injection container** (`framework.injection.bean.ManiocFramework`, 987 lines), not Spring. The annotations are:

| Annotation | Role |
|---|---|
| `@ManagedBean` | Registers a class as a managed bean (singleton or prototype) |
| `@InjectedBean` | Field injection |
| `@ImplementedBy` | Selects the implementation of an interface |
| `@PostConstruct` / `@PreDestroy` | Lifecycle |
| `@ResourceBundleBean` | Injection of a `ResourceBundle` (i18n) |

Usage: `BeanInjector.getInjector().inject(this)` is called **inside the constructors** of many classes (for example `XMLPersistenceService`, `AutoSaveBehavior`). Beans are globally reachable through `BeanFactory.getFactory()`.

**Consequence for the migration [D]:** most dependencies are resolved through global static state rather than explicit passing. A target that replaces this state with constructor injection will require a cross-cutting refactoring (see 05).

## 7. Internationalisation [V]

- `.properties` files **co-located** with the Java sources (project convention).
- Languages present: **French** (25 files), **German** (25), **Polish** (3), English by default (files without suffix).
- Bundles are loaded with `ResourceBundle.getBundle(name, Locale.getDefault())`.
- **Point of attention:** the node and edge prototypes of the graphs are built in a `static { }` block: their tooltips (`setToolTip`) are **frozen at the locale in effect when the class is first loaded**. Switching language on the fly is therefore not possible without restarting [D, consistent with `-english` acting before the interface is created].

## 8. Themes and appearance [V]

- Dependencies: **FlatLaf 3.3** (modern look-and-feel), **pgslookandfeel 1.1.1** (historical theme).
- Interface `ITheme`, base class `AbstractTheme`, manager `ThemeManager`, descriptor `ThemeInfo` [V].
- Implementations present [V]: `BasicTheme`, `FlatLightTheme`, `BlueAmbianceTheme`, `ClassicMetalTheme`, `DarkAmbianceTheme`, `DarkBlueTheme`.
- The theme is chosen at startup; the presence of a splash screen and the `-kioskMode` option (undecorated window) change the window.

## 9. Third-party dependencies

Usage verified by counting Java imports.

| Library (version) | Declared in | Usage observed [V] | Status |
|---|---|---|---|
| FlatLaf 3.3 | framework | 1 file | Used |
| pgslookandfeel 1.1.1 | framework | 3 files | Used |
| l2fprod-common-tasks 7.3 | framework | 2 files | Used |
| commons-vfs 1.0 | framework | 1 file | Used, very old version |
| commons-codec 1.16.0 | framework | 1 file | Used (very little) |
| FreeHEP graphicsio + pdf 2.4 | framework | 2 files | Used for PDF export |
| jaxmejs 0.5 | framework | 1 file | Usage to confirm [?] |
| antlr 2.7.5 | framework | **0 imports** | Probably **unused** [D] |
| javaws 1.6.0 | framework | **0 `javax.jnlp` imports** | Probably **unused** [D]; JNLP API is not part of the JDK |
| JUnit Jupiter | framework: 5.10.2; swing: 5.11.3 | — | **Inconsistent versions** |
| spring-boot-maven-plugin + springloaded 1.2.0 | swing (build plugin) | **0 Spring imports** | Configuration leftover |
| maven-shade-plugin 1.3.3 | swing | — | Very old |
| maven-antrun-plugin 3.1.0 | swing | — | Publication to SourceForge via `scp` |
| XStream 1.4.21 | *(documentation only)* | **0 imports** | **No longer exists** in the code |

## 10. Packaging and distribution [V]

| Channel | Mechanism | Remarks |
|---|---|---|
| Executable JAR | `violetproduct-swing`, shade plugin, `mainClass` = `UMLEditorApplication` | Name: `violetumleditor-<version>.jar` |
| Windows `.exe` | `violetproduct-exe`: `keytool-maven-plugin` (key generation, CSR, certificate) + dependency copy | Signed with a key generated at build time |
| Debian `.deb` | `violetproduct-deb`: `jdeb` + JAR copy | — |
| RPM | `violetproduct-rpm` (`violet.desktop`, `violet.sh`, `violet.png`) | **Disabled** |
| Java Web Start (JNLP) | `violetproduct-jnlp` (Velocity template `template.vm`, keystore) | **Disabled** |
| SourceForge | `scp` deployment in `maven-antrun-plugin`, credentials via environment variables | Active during `deploy` |
| GitHub Packages | `distributionManagement` pointing to `maven.pkg.github.com/violetumleditor/product` | Active during `deploy` |
| GitHub Pages | `maven-publish.yml` workflow: publishes the latest JAR and `index.html` (download page) | Triggered on push to `master` |

### Continuous integration [V]

- `maven-publish.yml` workflow: JDK **21** (Temurin), build with `mvn -B package deploy`.
- Automatic injection of `app.version.number` and `app.release.date` (UTC build date) into `OtherStrings*.properties` before compilation.
- `get-certificate.yml` workflow: **disabled** (triggered on a branch named `disabled`), attempts to sign a file with cosign.

## 11. External Maven repository [V]

The root POM declares a Maven repository hosted on SourceForge (`https://violet.sourceforge.net/maven2/repo/`). A reproducible build therefore depends on the availability of this service, in addition to Maven Central.
