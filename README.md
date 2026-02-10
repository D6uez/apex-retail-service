# Apex Retail – Inventory & Sales Management System

## Project Overview

**Apex Retail** is an educational Inventory & Sales Management System developed incrementally to model real-world retail workflows while reinforcing core Java programming concepts.

The system is intentionally built **chapter by chapter**, aligning strictly with textbook topics and progressively introducing architectural patterns used in production-grade software.

This project emphasizes:
- Correct object-oriented design
- Data integrity and validation
- Clear separation of responsibilities
- Incremental complexity without overengineering

---

## System Purpose

The system provides a controlled environment for exploring retail concepts such as:

- Product and category modeling
- Inventory tracking and stock adjustments
- Sales and restocking workflows
- Business-rule validation
- Layered application design

Each feature is introduced **only when the supporting language and design concepts have been covered**.

---

## Target Beneficiaries

- **Learners** building strong Java fundamentals through applied projects
- **Junior developers** practicing clean architecture and validation discipline
- **Instructors or reviewers** evaluating incremental system design
- **Portfolio reviewers** seeking evidence of structured problem-solving

---

## Chapter Progression Roadmap

The system evolves through these textbook chapters:

1. **Introduction to Computers and Java** - System configuration and environment validation
2. **Java Fundamentals** - Core language syntax and utility classes
3. **A First Look at Classes and Objects** - Domain modeling and encapsulation
4. **Decision Structures** - Business logic and workflow coordination
5. **Loops and Files** - Batch processing and data persistence
6. **A Second Look at Classes and Objects** - Advanced object design
7. **Arrays and the ArrayList Class** - Dynamic collections management
8. **Text Processing and Wrapper Classes** - Data formatting and validation
9. **Inheritance** - Hierarchical domain modeling
10. **Exceptions and Advanced File I/O** - Robust error handling and data persistence
11. **JavaFX: GUI Programming and Basic Controls** - Desktop application interface
12. **JavaFX: Advanced Controls** - Sophisticated user interface components
13. **JavaFX: Graphics, Effects, and Media** - Rich visual presentation
14. **Recursion** - Advanced algorithms and data processing
15. **Databases** - Persistent storage and data management

---

## Chapter 1: Introduction to Computers and Java

### Overview
The **SystemInfo** diagnostic utility verifies that the development environment is correctly configured.

### Purpose
- Confirm Java installation and version
- Verify Maven project setup
- Validate application entry-point execution
- Establish a baseline executable program

---

## Chapter 2: Java Fundamentals

### Overview
The **TemperatureConverter** utility demonstrates the construction of a reusable, fully validated utility class.

### Concepts Applied
- Static utility methods
- Constants and encapsulation
- Input validation
- Exception handling
- Automated unit testing

### Features
Converts between:
- Celsius ↔ Fahrenheit
- Celsius ↔ Kelvin
- Fahrenheit ↔ Kelvin

Enforces absolute zero constraints and throws `IllegalArgumentException` for invalid inputs.

---

## Chapter 3: A First Look at Classes and Objects

### Overview
Introduces the domain layer, modeling real-world business entities while enforcing strict invariants.

### Product Domain Object
- Represents retail inventory items
- Enforces data validation at creation
- Manages inventory quantity safely
- Prevents invalid states through encapsulation

### Category Domain Object
- Classifies products logically
- Immutable after creation
- Provides stable grouping for reporting
- Overrides `equals()`, `hashCode()`, and `toString()`

---

## Chapter 4: Decision Structures

### Overview
Introduces the service layer, coordinating domain objects using decision structures.

### InventoryService
- Coordinates product inventory operations
- Validates transaction-level inputs
- Delegates stock changes to domain objects
- Implements sell and restock operations with business logic

### Validation Strategy
Multi-layer validation with domain, service, and application layers each handling appropriate validation concerns.

---

## Chapter 5: Loops and Files

### Overview
Introduces iterative processing through a menu-driven batch application with file-based data persistence.

### Concepts Applied
- Sentinel-controlled loops
- File I/O operations
- Transaction logging
- Batch processing with re-prompting on invalid input

### InventoryBatchManager
Interactive command-line interface supporting:
- Sell and restock operations with file persistence
- Transaction history tracking
- Graceful exit with summary reporting

---

## Chapter 6: A Second Look at Classes and Objects

### Overview
Revisits class design with deeper focus on object integrity and composition.

### Concepts Reviewed
- Constructor overloading
- Defensive copying
- Aggregate classes
- Immutability vs mutability patterns

### Design Decisions
- Domain objects validate data at construction
- No-argument constructors intentionally omitted
- Mutators limited to protect invariants
- Objects cannot exist in partially valid states

---

## Chapter 7: Arrays and the ArrayList Class

### Overview
Replaces fixed data structures with dynamic collections using ArrayList.

### Concepts Applied
- Dynamic collection management
- Index-based selection with bounds checking
- Iteration using enhanced for-loops
- Collection-backed inventory modeling

### Implementation Notes
- Inventory stored in `ArrayList<Product>`
- Input validation ensures safe collection access
- Standard collections preferred over custom implementations

---

## Chapter 8: Text Processing and Wrapper Classes
### Overview
Applies text processing and wrapper classes to strengthen data handling, validation, and formatting within the inventory system.

### Concepts Applied
- String manipulation and CSV format parsing
- Data conversion and validation using wrapper classes (`Long`, `Integer`)
- Precise decimal handling with `BigDecimal`
- Formatted console output for reporting

### Implementation Notes
- Product data parsed from CSV strings using `split()` and wrapper class parsers (`Long.parseLong()`)
- Prices stored and formatted as `BigDecimal` objects
- Input validation leverages wrapper classes to catch `NumberFormatException`
- Inventory display uses `printf()` for consistent, readable formatting

---

## Chapter 9: Inheritance, Abstract Classes, and Interfaces
### Overview
Explores object-oriented design by building a notification class hierarchy separate from the main system, focusing on abstraction and polymorphism.

### Concepts Applied
- Interface creation to define contracts (`Notifiable`)
- Abstract classes to provide shared implementation
- Concrete subclassing for specialized behavior
- Polymorphism through base class references

### Implementation Notes
- Practice exercises created `EmailNotification` and `SMSNotification` as subclasses of an abstract `Notification` class
- The `send()` method contract is defined in the `Notifiable` interface
- **Note:** This structure is a practice module and is not yet integrated into the core Point of Sale application

---

## Chapter 10: Exceptions and Advanced File I/O
### Overview
Implements persistent data storage by creating a repository layer, introducing robust file operations and comprehensive error handling.

### Concepts Applied
- Repository pattern for data access separation
- Checked and unchecked exception handling
- Try-with-resources for automatic stream management
- Data mapping between CSV strings and domain objects

### Implementation Notes
- File operations centralized in `InventoryFileRepository`
- `loadInventory()` reads and parses `inventoryFile.csv`, returning a `List<Product>`
- `writeFile()` persists the current inventory list back to CSV
- Malformed file data is caught and logged, preventing application crashes
- The application layer decides when to load from file or use default data

---

## Chapter 11: JavaFX: GUI Programming and Basic Controls

### Planned Features
- Desktop application with graphical interface
- Basic form controls for inventory management
- Real-time inventory display
- Tabbed interface for different workflows
- Responsive layout design

---

## Chapter 12: JavaFX: Advanced Controls

### Planned Features
- Data tables with sorting and filtering
- Chart visualizations for sales trends
- Searchable product catalogs
- Dialog boxes for user confirmation
- Custom styled components

---

## Chapter 13: JavaFX: Graphics, Effects, and Media

### Planned Features
- Product image display and management
- Animations for inventory changes
- Visual effects for alerts and notifications
- Dashboard with graphical metrics
- Enhanced user experience through multimedia

---

## Chapter 14: Recursion

### Planned Features
- Recursive inventory categorization
- Hierarchical reporting structures
- Advanced search algorithms
- Complex discount calculation
- Tree-based organization of product relationships

---

## Chapter 15: Databases

### Planned Features
- SQLite database integration
- JDBC-based data persistence
- CRUD operations for all domain objects
- Transaction management with ACID compliance
- Database-backed reporting and analytics

---

## Project Structure
```
apex-retail-system/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── apexretail/
│   │               ├── diagnostics/
│   │               │   └── SystemInfo.java
│   │               ├── utilities/
│   │               │   └── TemperatureConverter.java
│   │               ├── domain/
│   │               │   ├── Product.java
│   │               │   └── Category.java
│   │               ├── service/
│   │               │   └── InventoryService.java
│   │               └── application/
│   │                   └── InventoryBatchManager.java
│   └── test/
│       └── java/
│           └── com/
│               └── apexretail/
│                   └── utilities/
│                       └── TemperatureConverterTest.java
├── pom.xml
└── README.md
```
---

## Development Philosophy

- **Progressive Disclosure**: Features implemented only after corresponding textbook chapters
- **No Premature Optimization**: Avoid frameworks until language fundamentals are mastered
- **Clarity Over Cleverness**: Readable, maintainable code preferred over "smart" solutions
- **Validation First**: Data integrity and error handling prioritized from the start
- **Real-World Relevance**: Practical retail scenarios guide implementation choices

---

## Development Team

- **Primary Developer**: David
- **Team**: Apex Retail Solutions
- **Project Type**: Educational / Portfolio Development
- **Pedagogical Approach**: Textbook-aligned incremental system design

---
### Disclaimer
*Note: 'Apex Retail Solutions' is a fictional name created for this educational project.*