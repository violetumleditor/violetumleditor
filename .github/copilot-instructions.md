# Violet UML Editor — Copilot Instructions

## Project Overview

Violet is an open-source UML diagram editor written in Java. It supports class, sequence, activity, state, object, and use case diagrams. The application runs as a **Swing desktop app** and as a **web app** (via JWT/Jetty). It was originally created by Cay S. Horstmann and is licensed under GPLv2+.

- **Group ID**: `com.horstmann.violet`
- **Version**: `2.3.0-SNAPSHOT`
- **Java version**: 21 (compiler release 21)
- **Build system**: Maven 3 multi-module

## Module Structure

```
violetumleditor/                          ← Parent POM (pom packaging)
├── violet-framework/                     ← Core framework (jar) — graph model, persistence, UI, DI, plugin system
├── violetplugin-activitydiagram/         ← Activity diagram plugin
├── violetplugin-classdiagram/            ← Class diagram plugin
├── violetplugin-communicationdiagram/    ← Communication diagram (not yet implemented)
├── violetplugin-objectdiagram/           ← Object diagram plugin
├── violetplugin-sequencediagram/         ← Sequence diagram plugin
├── violetplugin-statediagram/            ← State diagram plugin
├── violetplugin-usecasediagram/          ← Use case diagram plugin
├── violetproduct-swing/                  ← Swing desktop application
├── violetproduct-web/                    ← Web application (JWT + Jetty 10, WAR)
├── violetproduct-exe/                    ← Windows .exe packager
├── violetproduct-deb/                    ← Debian .deb packager
├── violetproduct-jnlp/                   ← Java Web Start (disabled)
└── violetproduct-rpm/                    ← RPM packager (disabled)
```

## Build & Run

```bash
# Full build
mvn clean package

# Run Swing desktop app
java -cp "violetproduct-swing/target/*:violet-framework/target/*:violetplugin-*/target/*" com.horstmann.violet.UMLEditorApplication

# Run web app (Jetty server on port 8080)
java -cp "violetproduct-web/target/classes:..." com.horstmann.violet.web.UMLEditorJettyServer
```

The Maven compiler plugin is configured with `-Xlint:deprecation` and `-Xlint:unchecked`.

## Architecture

### Core Framework (`violet-framework`)

The framework follows a **layered architecture** with clear separation:

| Layer | Package | Responsibility |
|-------|---------|---------------|
| **Graph Model** | `com.horstmann.violet.product.diagram.abstracts` | IGraph, INode, IEdge — pure model |
| **Workspace** | `com.horstmann.violet.workspace` | EditorPart (canvas), SideBar (tools), Workspace facade |
| **Behaviors** | `com.horstmann.violet.workspace.editorpart.behavior` | 40+ modular UI interaction handlers |
| **Persistence** | `com.horstmann.violet.framework.file` | XStream XML serialization, file I/O, export |
| **Plugin System** | `com.horstmann.violet.framework.plugin` | ServiceLoader-based plugin discovery |
| **DI Container** | `com.horstmann.violet.framework.injection` | Custom lightweight IoC (Manioc) |
| **Themes** | `com.horstmann.violet.framework.theme` | Swing look-and-feel themes |
| **UI Extensions** | `com.horstmann.violet.framework.swingextension` | Custom Swing components |

### Key Interfaces & Base Classes

| Interface | Base Class | Purpose |
|-----------|-----------|---------|
| `IGraph` | `AbstractGraph` | Diagram container (nodes + edges + rendering) |
| `INode` | `AbstractNode` → `RectangularNode`, `EllipticalNode` | Diagram element |
| `IEdge` | `AbstractEdge` → `SegmentedLineEdge`, `ShapeEdge` | Connector between nodes |
| `IWorkspace` | `Workspace` | Main workspace (editor + sidebar + file) |
| `IEditorPart` | `EditorPart` (JPanel) | Canvas for drawing |
| `IEditorPartBehavior` | — | Individual interaction handler (strategy pattern) |
| `IDiagramPlugin` | — | Plugin descriptor |
| `IFilePersistenceService` | `XStreamBasedPersistenceService` | Read/write diagrams |
| `ITheme` | `BasicTheme`, `FlatLightTheme`, etc. | UI theme definition |

### Dependency Injection — Manioc (custom, NOT Spring)

This project uses a **custom lightweight DI framework** called Manioc, defined in `com.horstmann.violet.framework.injection.bean.ManiocFramework`.

**Key annotations:**
- `@ManagedBean` — registers a class as a DI-managed singleton or prototype
- `@InjectedBean` — field injection
- `@PostConstruct` / `@PreDestroy` — lifecycle hooks
- `@ImplementedBy` — specifies the implementation class for an interface

**Usage pattern:**
```java
@ManagedBean
public class MyService {
    @InjectedBean
    private PluginRegistry pluginRegistry;

    public MyService() {
        BeanInjector.getInjector().inject(this);
    }
}
```

**Resource bundles** are injected via `@ResourceBundleBean` and `ResourceBundleInjector`.

> **Important**: Do NOT introduce Spring Framework. The project intentionally uses its own lightweight DI.

### Plugin System

Plugins are discovered via **Java SPI (ServiceLoader)**:

1. Each plugin module implements `IDiagramPlugin`
2. Registers in `META-INF/services/com.horstmann.violet.framework.plugin.IDiagramPlugin`
3. `PluginLoader` discovers plugins at startup via `ServiceLoader.load(IDiagramPlugin.class)`
4. Plugins are stored in the `PluginRegistry` singleton

**To create a new diagram plugin:**
1. Create a new Maven module `violetplugin-<name>`
2. Add dependency on `violet-framework`
3. Implement `IDiagramPlugin` with graph class, node types, edge types, file extension
4. Implement `<Name>Graph extends AbstractGraph` — define allowed node/edge types
5. Implement node classes extending `RectangularNode`/`EllipticalNode`/`AbstractNode`
6. Implement edge classes extending `SegmentedLineEdge`/`AbstractEdge`
7. Register via `META-INF/services/com.horstmann.violet.framework.plugin.IDiagramPlugin`

### Existing Diagram Plugins

| Plugin | Graph Class | File Extension | Key Nodes | Key Edges |
|--------|------------|---------------|-----------|-----------|
| Class | `ClassDiagramGraph` | `.class.violet.html` | ClassNode, InterfaceNode, PackageNode | AssociationEdge, InheritanceEdge, CompositionEdge, AggregationEdge, DependencyEdge |
| Sequence | `SequenceDiagramGraph` | `.seq.violet.html` | ActivationBarNode, LifelineNode | CallEdge, ReturnEdge |
| Activity | `ActivityDiagramGraph` | `.activity.violet.html` | ActivityNode, DecisionNode, SynchronizationBarNode | ActivityTransitionEdge |
| State | `StateDiagramGraph` | `.state.violet.html` | StateNode, CircularInitialStateNode, CircularFinalStateNode | StateTransitionEdge |
| Object | `ObjectDiagramGraph` | `.object.violet.html` | ObjectNode, FieldNode | ObjectRelationshipEdge, ObjectReferenceEdge |
| Use Case | `UseCaseDiagramGraph` | `.ucase.violet.html` | UseCaseNode, ActorNode | UseCaseRelationshipEdge |

All file extensions end with `.violet.html` — diagram files are XML wrapped in an HTML container for portability.

### Persistence

- **Primary format**: XStream-based XML serialization (`XStreamBasedPersistenceService`)
- **Custom converters**: `Point2DConverter`, `Rectangle2DConverter`, `ImageConverter`
- **Backward compatibility**: `Violet016BackportFormatService` maps old class names to current ones
- **XStream config**: UTF-8, DOM driver, ID references for circular refs, unknown elements ignored

### Behavior System

UI interactions are implemented as **independent behavior classes** (Chain of Responsibility pattern) managed by `EditorPartBehaviorManager`:

- **Drawing**: `AddNodeBehavior`, `AddEdgeBehavior`, `AddTransitionPointBehavior`
- **Selection**: `SelectByClickBehavior`, `SelectByLassoBehavior`, `SelectAllBehavior`
- **Modification**: `DragSelectedBehavior`, `ResizeNodeBehavior`, `ColorizeBehavior`
- **Navigation**: `DragGraphBehavior`, `ZoomByWheelBehavior`
- **Undo/Redo**: `UndoRedoCompoundBehavior` + 9 specific undo behaviors
- **Context**: `ShowMenuOnRightClickBehavior`, `EditSelectedBehavior`

All behaviors implement `IEditorPartBehavior` and are fired by the behavior manager on mouse/keyboard events.

### Web Product (`violetproduct-web`)

- **Web toolkit**: JWT (Java Web Toolkit) 4.11.4 — server-side web framework
- **Server**: Embedded Jetty 10.0.25 on port 8080
- **Entry point**: `UMLEditorJettyServer` → `UMLEditorWebServlet` (extends `WtServlet`) → `UMLEditorWebApplication`
- **Rendering**: `CustomWebGraphics2D` adapts Java2D Graphics2D to JWT's `WPainter` for HTML5 rendering
- **Theme**: WBootstrapTheme
- **Currently active plugin**: Only class diagram (other plugins commented out in web POM)

### Product Entry Points

| Product | Main Class | Output |
|---------|-----------|--------|
| Swing | `com.horstmann.violet.UMLEditorApplication` | Desktop app |
| Web | `com.horstmann.violet.web.UMLEditorJettyServer` | Jetty server |
| EXE | packaged via `keytool-maven-plugin` + `maven-dependency-plugin` | `.exe` file |
| DEB | packaged via `jdeb` plugin | `.deb` file |

## Key Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| XStream | 1.4.21 | XML serialization for diagram persistence |
| FlatLaf | 3.3 | Modern Swing Look-and-Feel |
| FreeHEP GraphicsIO | 2.4 | PDF export |
| commons-vfs | 1.0 | Virtual filesystem |
| JWT (Web Toolkit) | 4.11.4 | Web UI framework (web product) |
| Jetty | 10.0.25 | Embedded web server (web product) |
| Gson | 2.8.9 | JSON (web product) |
| PgsLookAndFeel | 1.1.1 | Swing theme |

## Coding Conventions

### Package Naming
- Framework: `com.horstmann.violet.framework.<subsystem>`
- Diagram abstracts: `com.horstmann.violet.product.diagram.abstracts`
- Plugin diagrams: `com.horstmann.violet.product.diagram.<type>` (e.g., `classes`, `sequence`)
- Plugin nodes: `com.horstmann.violet.product.diagram.<type>.nodes`
- Plugin edges: `com.horstmann.violet.product.diagram.<type>.edges`
- Swing app: `com.horstmann.violet` and `com.horstmann.violet.application`
- Web app: `com.horstmann.violet.web`

### Patterns to Follow
- New UI interactions → implement `IEditorPartBehavior`
- New diagram types → implement `IDiagramPlugin` + Graph/Node/Edge classes + SPI registration
- DI → use `@ManagedBean` / `@InjectedBean` / `BeanInjector.getInjector().inject(this)`
- i18n → use `@ResourceBundleBean` properties files co-located with Java sources
- Node rendering → override `draw()` / `getShape()` / `getBounds()` on AbstractNode subclass
- Edge rendering → override segmented line configuration on SegmentedLineEdge subclass

### Style
- Java 21 features are available (records, pattern matching, sealed classes, etc.)
- Copyright header: GPLv2+ with Cay S. Horstmann attribution
- Properties files (`*.properties`) are co-located with Java source files for i18n
- Resources (GIF, JPG, properties, XML) in `src/main/java` are included in the JAR

## Testing

**The project currently has no unit tests.** When adding tests:
- Use JUnit 5 (add dependency to POM if needed)
- Place tests in `src/test/java` mirroring the main source structure
- Maven Surefire 3.3.0 is already configured

## Common Tasks

### Adding a new node type to an existing diagram
1. Create a new class extending `RectangularNode` (or `EllipticalNode`, `AbstractNode`)
2. Implement `draw(Graphics2D)`, `getShape()`, `getBounds()`
3. Register it in the corresponding `*DiagramGraph.getNodePrototypes()`
4. Add i18n properties file alongside the Java class

### Adding a new edge type to an existing diagram
1. Create a new class extending `SegmentedLineEdge` (or `AbstractEdge`)
2. Configure arrow heads, line styles, bent styles
3. Register it in the corresponding `*DiagramGraph.getEdgePrototypes()`

### Adding a new behavior
1. Create a class implementing `IEditorPartBehavior`
2. Register it in `EditorPartBehaviorManager` setup

### Modifying persistence
- XStream aliases are auto-generated from class simple names
- Add custom converters in `XStreamBasedPersistenceService` if needed
- Maintain backward compatibility — old class name mappings go in `Violet016BackportFormatService`

### Java WebToolkit (JWT) docs:
- JWT user guide : https://www.webtoolkit.eu/jwt/latest/doc/userguide/userguide.html
- JWT API Javadoc: https://www.webtoolkit.eu/jwt/latest/doc/javadoc/
- JWT Widget Gallery : https://jwt.emweb.be/jwt-gallery/gallery/layout/
- JWT GitHub: https://github.com/emweb/jwt


### Special notes :
- Don't use MultiLineLabel.java in MultilineString.java which can only use JLabel to display text using HTML. This is because MultiLineLabel.java uses a custom implementation to display text and does not support HTML rendering, which is necessary for displaying multiline text properly in the application.