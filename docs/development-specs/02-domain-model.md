# 02 — Domain model

This document describes the business entities, their attributes, their types and their invariants. It constitutes the **data contract** to reproduce in the target. The field names cited are those of the current code; they also form the file format (see 03).

Evidence level: [V] = observed in the code; [D] = deduced; [?] = to confirm.

## 1. Overview

```
IGraph  (diagram)
 ├── nodes : ordered list of INode            ── order = drawing order (implicit z-order) [D]
 ├── edges : ordered list of IEdge
 └── gridSticker : alignment grid (transient, not saved)

INode  (box / element)
 ├── id, location, borderWidth, revision
 ├── children : INode[]  ─┐ hierarchy (e.g. class inside a package)
 └── parent : INode     ◄─┘
 └── specific properties (text, colours, ...)

IEdge  (link)
 ├── id, startNode, endNode, startLocation, endLocation
 ├── transitionPoints : ITransitionPoint[]   (waypoints)
 └── style: lineStyle, angleStyle, bentStyle, arrowheads, labels, colours
```

Key relations [V]:
- A node knows its graph (`graph`, transient) and its parent (`parent`).
- An edge **references** its two ends by object (`startNode`, `endNode`). **The node does not keep the list of its edges**: the `removeConnection` method of `AbstractNode` is empty, and `addConnection` only checks that the edge has an end node.

## 2. Graph interface `IGraph` [V]

| Operation | Role |
|---|---|
| `getNodePrototypes()` / `getEdgePrototypes()` | Templates shown in the toolbar |
| `getAllNodes()` / `getAllEdges()` | Content |
| `addNode(node, point)` | Adds a node at a position (with adjustment, see 2.1) |
| `connect(edge, start, startPt, end, endPt)` | Links two nodes with an edge |
| `removeNode(...)` / `removeEdge(...)` | Deletion |
| `findNode(Id)` / `findNode(Point2D)` / `findEdge(Id)` / `findEdge(Point2D)` | Lookup |
| `draw(Graphics2D)` | Full drawing |
| `getClipBounds()` / `setBounds(Rectangle2D)` | Occupied area |
| `getGridSticker()` / `setGridSticker(...)` | Alignment grid |

### 2.1 Adding a node [V]

`AbstractGraph.addNode`:
1. **Reassigns a new UUID** to the node (`newNode.setId(new Id())`), on every addition, including when pasting or undoing.
2. Attaches the node to the graph.
3. Corrects the position for cropped nodes (for example `ImageNode`): the visible corner must land on the insertion point.
4. Handles the special case of `NoteNode` (always attached to the graph).

> **Consequence [D]:** a node changes identifier every time it is re-inserted (undo of a deletion, redo, paste). Any target that stores references by identifier must account for this, otherwise references break between two saves.

### 2.2 Connection [V]

`AbstractGraph.connect`:
- Adds the end nodes if they are not yet in the graph.
- Sets the ends and the positions of the edge.
- Calls `start.addConnection(edge)`, which returns `edge.getEndNode() != null`.
- If true: assigns a new `Id` to the edge and adds it to the list.

> **Anomaly [V]:** if `start` is `null`, the call `start.addConnection(e)` throws a `NullPointerException`. Nothing in the signature prevents it. The target must require non-null ends.

> **No type validation [V]:** `connect` does **not** check that the edge type is compatible with the node types. A dependency edge can link two `ActorNode`s. The only business rules are those of the specialised graphs (see section 6).

## 3. Base entities

### 3.1 Node `AbstractNode` [V]

| Field | Type | Persisted | Role |
|---|---|:---:|---|
| `children` | `ArrayList<INode>` | yes | Children (package → classes) |
| `parent` | `INode` | no (ignored when writing) | Parent in the hierarchy |
| `graph` | `IGraph` | no (transient) | Graph containing the node |
| `location` | `Point2D` | yes | Position relative to parent or graph |
| `toolTip` | `String` | no (transient) | Tooltip, taken from the language bundle |
| `z` | `int` | no (transient) | Stacking order [V declaration; usage not traced] |
| `id` | `Id` | yes | UUID identifier |
| `revision` | `Integer` | yes [?] | Revision counter; default value 0 |
| `borderWidth` | `int` (default 1) | yes | Border thickness |

Extension methods to implement per node type: `draw(Graphics2D)`, `getShape()`, `getBounds()` [V, `copilot-instructions`].

### 3.2 Edge `AbstractEdge` [V]

| Field | Type | Persisted | Role |
|---|---|:---:|---|
| `startNode` / `endNode` | `INode` | yes (reference) | Ends |
| `startLocation` / `endLocation` | `Point2D` | yes | Attachment points, relative |
| `transitionPoints` | `ITransitionPoint[]` | yes | Intermediate waypoints |
| `id` | `Id` | yes | UUID identifier |
| `revision` | `Integer` | yes [?] | Revision counter |
| `toolTip` | `String` | no | Tooltip |
| `borderWidth` | `int` (default 1) | yes | Thickness |

### 3.3 Segmented-line edge `SegmentedLineEdge` [V]

Base class of most edges, extending `AbstractEdge`.

| Field | Type | Known values |
|---|---|---|
| `lineStyle` | `LineStyle` | line style (solid, dashed, ...) |
| `angleStyle` | `AngleStyle` | angle style |
| `bentStyle` | `BentStyle` | shape of the route (see 4.1) |
| `startArrowHead` / `endArrowHead` | `ArrowHead` | see 4.2 |
| `startLabel` / `middleLabel` / `endLabel` | `String` | labels at the three positions |
| `backgroundColor` / `borderColor` / `textColor` | `Color` | colours |

### 3.4 Graph `AbstractGraph` [V]

| Field | Type | Persisted |
|---|---|:---:|
| `nodes` | `ArrayList<INode>` | yes |
| `edges` | `ArrayList<IEdge>` | yes |
| `gridSticker` | `IGridSticker` | **no** (transient) |
| `minBounds` | `Rectangle2D` | no (transient) |

> The alignment grid is therefore **not** saved [V]. It is a display preference, not diagram data [D].

## 4. Property types

### 4.1 Route styles [V]

`BentStyle` (a class with singleton instances, not a Java enum): `STRAIGHT`, `FREE`, `HV`, `VH`, `HVH`, `VHV`, `AUTO`.
- Semantics deduced from the names [D]: `HV` = horizontal then vertical; `VH` = the reverse; `HVH` / `VHV` = three segments; `AUTO` = chosen according to the position of the ends; `FREE` = route guided by the transition points. **The exact geometry of each style must be read in `SegmentedLineEdge` before implementation** (see 05).

> **Format point:** these styles are **unique instances** compared by reference (`==`). A target must serialise them by name and restore the corresponding singleton, otherwise equality breaks.

### 4.2 Arrowheads `ArrowHead` [V]

Seven values: `NONE`, `TRIANGLE`, `BLACK_TRIANGLE`, `V`, `HALF_V`, `DIAMOND`, `BLACK_DIAMOND`. These are also singleton instances.

### 4.3 Other value types [V]

| Type | Representation | Remark |
|---|---|---|
| `Id` | UUID v4 string (`UUID.randomUUID()`) | Value equality; `null` → not equal |
| `Point2D` / `Rectangle2D` | `java.awt.geom` (double) | Geometry; **Java/AWT type** to replace |
| `Color` | `java.awt.Color` (RGBA) | Serialised as `red/green/blue/alpha` |
| `CropInsets` | cropping margins | Omitted when empty |
| `EdgeTransitionPoint` | point (x, y) | Implements `ITransitionPoint` |
| `IGridSticker` | grid interface | Not persisted |
| `MultiLineString` | multi-line text, see 4.4 | Central type of the model |
| `Image` / `ImageIcon` | AWT images | Serialised in `<resources>` (see 03) |

### 4.4 Multi-line text `MultiLineString` [V]

Fields: `text` (persisted), `defaultJustification`, `defaultSize`, `defaultUnderlined`, `label` (`JLabel`, transient), `isBoundsDirty`, `bounds` (transient).

Exposed attributes: `text`, `justification`, `underlined`, `size`.

**Structuring fact:** the rendering and the measurement of text go through a **`javax.swing.JLabel`** loaded with an HTML string:

```java
html = "<html><div style=\"" + blockStyle + "\">" + content + "</div></html>";
```

- The text is escaped or interpreted as HTML depending on whether it starts with `<html>` (case-insensitive detection).
- The size is obtained with `getLabel().getPreferredSize()`.
- The project requires **not** using `MultiLineLabel.java` (rendering without HTML) for multi-line text [V, `copilot-instructions`].

> **Major consequence [D]:** a diagram's text is a **subset of HTML 3.2** interpreted by the Swing engine. The dimensions of the boxes depend on that engine. Reproducing the layout faithfully in another technology requires either re-implementing this HTML subset or accepting differences (see 05).

## 5. Catalogue of diagram types [V]

Each plugin provides a graph, nodes and edges. The **prototypes** are template instances created once, in a `static` block, then shown in the toolbar.

| Diagram | Graph class | Extension | Nodes (prototypes) | Edges (prototypes) |
|---|---|---|---|---|
| Class | `ClassDiagramGraph` | `.class.violet.html` | `ClassNode`, `InterfaceNode`, `PackageNode`, `NoteNode`, `ImageNode` | `DependencyEdge`, `InheritanceEdge`, `InterfaceInheritanceEdge`, `AssociationEdge`, `AggregationEdge`, `CompositionEdge`, `NoteEdge` |
| Sequence | `SequenceDiagramGraph` | `.seq.violet.html` | `LifelineNode`, `ActivationBarNode`, `NoteNode` | `CallEdge`, `ReturnEdge`, `NoteEdge` |
| Activity | `ActivityDiagramGraph` | `.activity.violet.html` | `ScenarioStartNode`, `ActivityNode`, `DecisionNode`, `SynchronizationBarNode`, `SignalSendingNode`, `SignalReceiptNode`, `ScenarioEndNode`, `NoteNode` | `ActivityTransitionEdge`, `NoteEdge` |
| States | `StateDiagramGraph` | `.state.violet.html` | `StateNode`, `CircularInitialStateNode`, `CircularFinalStateNode` | `StateTransitionEdge` |
| Objects | `ObjectDiagramGraph` | `.object.violet.html` | `ObjectNode`, `FieldNode` | `ObjectRelationshipEdge`, `ObjectReferenceEdge` |
| Use case | `UseCaseDiagramGraph` | `.ucase.violet.html` | `UseCaseNode`, `ActorNode` | `UseCaseRelationshipEdge` |

Cross-cutting types (`NoteNode`, `NoteEdge`, `ImageNode`, `PointNode`, `DiagramLinkNode`) live in the core (`product.diagram.common`) and are reused by several diagrams.

### 5.1 Attributes specific to each node type [V]

| Node | Specific fields | Notes |
|---|---|---|
| `ClassNode` | `name`, `attributes`, `methods` (3 × `MultiLineString`) | Classic UML compartments |
| `InterfaceNode` | `name`, `methods` | |
| `PackageNode` | `name`, `content` | Can contain children (hierarchy) |
| `ObjectNode` | `name` | |
| `FieldNode` | `name`, `value`, `equalSeparator`; `verticalLocation`, `horizontalLocation` (transient) | Object field: `name = value` |
| `ActivityNode` | `name` | |
| `DecisionNode` | `condition` | |
| `SignalSendingNode` / `SignalReceiptNode` | `signal` | |
| `LifelineNode` | `name`; `maxYOverAllLifeLineNodes` (transient) | Lifeline |
| `ActivationBarNode` | `lifeline` (transient), `locationCache` (transient) | Activation bar, always on a lifeline |
| `StateNode` | `name` | |
| `ActorNode` / `UseCaseNode` | `name` | |
| `NoteNode` | `text` | Free note |
| `ImageNode` | `image` (AWT), `text` (`MultiLineString`); `imageIcon` (transient) | Imported image |
| `DiagramLinkNode` | `label`, `diagramLink` | Link to another diagram |
| `PointNode` | — | Point without content |

### 5.2 Attributes specific to each edge type [V]

| Edge | Specific fields |
|---|---|
| `CallEdge` | `signal` (boolean) |
| `StateTransitionEdge` | `angle` (double), `labelText` (String) |
| Other segmented-line edges | No specific field; the style comes from `SegmentedLineEdge` |

## 6. Business rules per diagram

The only specific rules are coded by overriding methods in the graphs. They are few.

### 6.1 Activity diagram [V]

Override of `connect` in `ActivityDiagramGraph`:
- If the edge is an `ActivityTransitionEdge` **and** the start is a `DecisionNode`:
  - If the end is a `SynchronizationBarNode`: route style `AUTO` (connection north/south).
  - Otherwise: route style `HV` (connection east/west).
- In all cases, a call to `super.connect`.

### 6.2 Sequence diagram [V]

Override of `addNode` in `SequenceDiagramGraph`:
- An `ActivationBarNode` **can only be added on an existing lifeline**: if no node is found at the insertion point, the addition is refused.

### 6.3 Absent rules [V]

In all other diagrams, no compatibility rule is coded: `connect` only checks that the end node exists (see 2.2), and `AddEdgeBehavior` (mouse interaction) only checks that the ends are non-null and that the selected tool is of the right type [V]. Concretely, nothing prevents linking two incompatible elements (for example an `ActorNode` and a `FieldNode`), nor creating an edge of any type between any nodes [V].

> **Decision to take for the target [?]:** reproduce the current permissive behaviour (fidelity), or introduce type validation (improvement). The choice must be explicit in the requirements (07).

## 7. Commands and undo/redo [V]

Undo is not a central mechanism of the model: it is implemented by **dedicated behaviours** that observe the actions. There are:

- `UndoRedoCompoundBehavior`: a composite that groups the actions of a single operation.
- Eight specific handlers: `UndoRedoOnAddBehavior`, `UndoRedoOnColorizeBehavior`, `UndoRedoOnCropBehavior`, `UndoRedoOnDragBehavior`, `UndoRedoOnEditBehavior`, `UndoRedoOnRemoveBehavior`, `UndoRedoOnResizeBehavior`, `UndoRedoOnTransitionPointChangeBehavior`.

For the migration, undo must be **specified as a command model** (each action produces an invertible command), rather than reproduced identically with these nine classes. The associated risks are described in 05.

> **Point of attention [V]:** re-inserting a node reassigns its identifier (see 2.1). An undo of a deletion therefore does not restore the original identifier. If the target uses the identifier as a stable key for history, this behaviour must be corrected, not copied.

## 8. Invariants to preserve

| # | Invariant | Source |
|---|---|---|
| I-1 | Every node and every edge has a UUID `Id` unique within the diagram | `addNode`, `connect` |
| I-2 | An edge has non-null start and end nodes | `connect` (to be strengthened, see 2.2) |
| I-3 | An `ActivationBarNode` is always on a lifeline | `SequenceDiagramGraph.addNode` |
| I-4 | An `ActivityTransitionEdge` leaving a decision follows the imposed route style | `ActivityDiagramGraph.connect` |
| I-5 | The `parent` / `children` hierarchy is consistent (a child has a single parent) | `AbstractNode` [D, not exhaustively verified] |
| I-6 | `BentStyle` and `ArrowHead` are compared by singleton identity | `BentStyle`, `ArrowHead` |
| I-7 | `transient` fields are never saved | `XMLPersistenceService` |
| I-8 | A node has no list of edges; edges are the only source of truth for links | `removeConnection` empty |

## 9. Points to decide for the target

1. **Identifiers**: keep the current UUID (compatibility) and correct its reassignment on re-insertion, or revise the strategy?
2. **Links**: keep reference by object, or move to an identifier table (simpler to serialise and test)?
3. **Text**: re-implement the Swing HTML subset, or simplify the format (plain text + styles) with conversion of existing files?
4. **Validation**: keep the permissive model, or add UML type constraints?
5. **Undo**: a command model to design, or a faithful reproduction of the nine handlers?
