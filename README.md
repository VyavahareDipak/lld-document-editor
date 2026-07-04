# Document Editor - Low Level Design (LLD)

> A simple document editor designed using SOLID principles and clean object-oriented design.

## Problem Statement

Design a document editor that supports:

- Add text
- Add images
- Render the document
- Save the document to storage
- Easily support new element types in the future
    - Tables
    - Videos
    - Charts
    - etc.

The design should be scalable, maintainable and easy to extend.

---

# Requirements

### Functional Requirements

- Add Text
- Add Image
- Render document
- Save document

### Non Functional Requirements

- Easy to extend
- Follow SOLID principles
- Low coupling
- High cohesion
- Support multiple storage implementations

---

# Design Evolution

Instead of jumping directly to the final solution, this project shows the complete design evolution.

---

# Design 1 — Naive Design

## Idea

A single `DocumentEditor` class performs every responsibility.

- Stores document elements
- Adds text
- Adds image
- Renders document
- Saves document

```text
                Client
                   |
                   v
          +------------------+
          | DocumentEditor   |
          |------------------|
          | addText()        |
          | addImage()       |
          | render()         |
          | save()           |
          +------------------+
```

## Problems

- Violates Single Responsibility Principle
- Difficult to add new document element types
- Tight coupling with storage implementation
- Large God Object
- Hard to test

---

# Design 2 — Applying SOLID

The design is broken into multiple responsibilities.

```
                                      +----------------------+
                                      |    DocumentEditor    |
                                      +----------------------+
                                      | - document: Document |
                                      | - storage:Persistence|
                                      +----------------------+
                                      | +addText()           |
                                      | +addImage()          |
                                      | +render()            |
                                      | +save()              |
                                      +----------------------+
                                           |          |
                                       uses|          +---------------- uses
                                           |                               |
                                           v                               v

                           +---------------------------+      +----------------------+
                           |         Document          |      |  <<interface>>       |
                           +---------------------------+      |     Persistence      |
                           | - elements: List<Element> |      +----------------------+
                           +---------------------------+      | +save(Document)      |
                           | +addElement()             |      +----------------------+
                           | +render()                 |                ^
                           +---------------------------+                |
                                      |                                 |
                           contains * |                                 |
                                      |                          +------+-------------------+
                                      |                          |                          |
                                      v                          |                          |
                    +----------------------------------+     +----------------------+  +----------------------+
                    |      <<interface>>               |     |     FileStorage      |  |      DBStorage       |
                    |      DocumentElement             |     +----------------------+  +----------------------+
                    +----------------------------------+     | +save(Document)      |  | +save(Document)      |
                    | +render()                        |     +----------------------+  +----------------------+
                    +----------------------------------+  
                               ^              ^           
                               |              |           
                +------------------------+  +------------------------+
                |      TextElement       |  |      ImageElement      |
                +------------------------+  +------------------------+
                | - text: String         |  | - path: String         |
                +------------------------+  +------------------------+
                | +render()              |  | +render()              |
                +------------------------+  +------------------------+                                 
```

## Document Elements

```
                 DocumentElement 
                        ^
          --------------|--------------
          |                           |
     TextElement                ImageElement
```

Each element knows how to render itself.

Adding a new element only requires creating another implementation.

Example:

- VideoElement
- TableElement
- ChartElement

No existing code changes.

---

## Document

The document only manages document elements.

```
Document

+ addElement()
+ List<DocumentElement>
```

Responsibilities:

- Store elements
- Manage document contents

---

## Persistence

Storage is abstracted using an interface.

```
Persistence
      ^
      |
------------------------
|                      |
FileStorage       DBStorage
```

Now storage implementation can change without affecting the editor.

---

## DocumentEditor

The editor coordinates everything.

```
DocumentEditor

- Document
- Persistence

addText()
addImage()
render()
save()
```

---

# SOLID Principles Used

## 1. Single Responsibility Principle (SRP)

Each class has only one reason to change.

| Class | Responsibility |
|--------|----------------|
| Document | Manage document contents |
| DocumentElement | Render an element |
| Persistence | Save document |
| DocumentEditor | Coordinate operations |

---

## 2. Open Closed Principle (OCP)

New document elements can be added without modifying existing code.

Example:

```
DocumentElement

    ^
    |
-----------------------------
|      |        |           |
Text  Image   Video      Table
```

---

## 3. Liskov Substitution Principle (LSP)

Anywhere a `DocumentElement` is expected, any implementation can be used.

```java
List<DocumentElement> elements;

elements.add(new TextElement());
elements.add(new ImageElement());
elements.add(new VideoElement());
```

---

## 4. Interface Segregation Principle (ISP)

Instead of one large interface, responsibilities are split.

Examples:

- DocumentElement
- Persistence

Clients only depend on what they actually use.

---

## 5. Dependency Inversion Principle (DIP)

High-level modules depend on abstractions.

Instead of:

```
DocumentEditor
      |
      v
 FileStorage
```

We have:

```
DocumentEditor
      |
      v
 Persistence
      ^
      |
---------------
|             |
FileStorage DBStorage
```

---

# Design 3 — Better Separation

One issue still remained.

The `Document` class was responsible for rendering.

Rendering is a separate concern.

Therefore rendering was extracted into its own component.

```
                                                        --------------------
                                                        |   client         |
                                                        +------------------+
                                                                  |
                                                   +--------------------------------------------------------------------------+
                                                   |                           |                                              |
                                                   |                           |                                              |    
                                      +----------------------+       +----------------------+                           +----------------------+
                                      |    DocumentEditor    |       |      Renderer        |                           |  <<interface>>       |
                                      +----------------------+       +----------------------+                           |     Persistence      |
                                      | - document: Document |       | +render(Document)    |                           +----------------------+
                                      +----------------------+       +----------------------+                           | +save(Document)      |
                                      | +addText()           |                |                                         +----------------------+
                                      | +addImage()          |                | uses                                              ^
                                      +----------------------+                |                                                   |
                                                 |  --------------------------+                                                   |
                                                 | uses                                                                           |
                                                 |                                                            +-------------------+-------------------+
                               +---------------------------+                                                  |                                       |                                                             |                                       |
                               |         Document          |                                       +----------------------+             +----------------------+
                               +---------------------------+                                       |     FileStorage      |             |      SQLStorage      |
                               | - elements: List<Element> |                                       +----------------------+             +----------------------+
                               +---------------------------+                                       | +save(Document)      |             | +save(Document)      |
                               | +addElement()             |                                       +----------------------+             +----------------------+
                               | +getElements()            |
                               +---------------------------+
                                      |
                           contains * |
                                      |
                                      v

                    +----------------------------------+
                    |      <<interface>>               |
                    |      DocumentElement             |
                    +----------------------------------+
                    | +render()                        |
                    +----------------------------------+
                               ^              ^
                               |              |
                +------------------------+  +------------------------+
                |      TextElement       |  |      ImageElement      |
                +------------------------+  +------------------------+
                | - text: String         |  | - path: String         |
                +------------------------+  +------------------------+
                | +render()              |  | +render()              |
                +------------------------+  +------------------------+
```

Responsibilities become much cleaner.

| Component | Responsibility |
|-----------|----------------|
| Document | Store document data |
| Renderer | Render document |
| Persistence | Save document |
| DocumentEditor | Coordinate workflow |

---

# Design Trade-offs

Separating responsibilities improves maintainability, but introduces more classes.

This is a common trade-off in Low Level Design.

In production systems:

- Business requirements come first.
- Design principles should guide the implementation, not dominate it.
- Over-engineering should be avoided.

---


# Project Structure

```
src/
    editor/
        DocumentEditor.java
    
    model/
        Document.java
        DocumentElement.java
        TextElement.java
        ImageElement.java
    renderer/
        Renderer.java
    storage/
        Persistence.java
        FileStorage.java
        DBStorage.java
    client/
        Main.java
bin\

```

---

# Key Takeaways

This project demonstrates:

- Object-Oriented Design
- SOLID Principles
- Abstraction
- Composition over inheritance
- Dependency Injection
- Separation of Concerns
- Design evolution from naive implementation to a scalable architecture