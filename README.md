# Apex Retail – Inventory & Sales Management System

## Project Overview

**Apex Retail** is a comprehensive Inventory & Sales Management System designed to streamline retail operations, inventory tracking, and sales processing for small to medium-sized retail businesses.

### System Purpose

This system provides retailers with a centralized platform to manage:

- Product inventory and stock levels
- Sales transactions and order processing
- Customer information and purchase history
- Supplier and vendor relationships
- Business analytics and reporting

### Target Beneficiaries

- **Retail Business Owners** seeking to digitize and optimize their operations
- **Store Managers** needing real-time inventory visibility
- **Sales Associates** requiring fast and accurate point-of-sale functionality
- **Business Analysts** looking for data-driven insights into sales performance

### Future Capabilities

The system will eventually include:

- Real-time inventory tracking and automated reordering
- Multi-channel sales integration (in-store, online, mobile)
- Customer relationship management (CRM) features
- Advanced reporting and analytics dashboard
- Employee management and shift scheduling
- Supplier management and purchase order processing
- Barcode/QR code scanning capabilities

---

## Chapter 1: SystemInfo Diagnostic Tool

### Overview

The **SystemInfo** utility verifies that the development environment is properly configured.

**Purpose:**

- Confirm Java installation and version compatibility
- Verify Maven build tool functionality
- Ensure project structure is set up correctly
- Confirm development environment readiness

### Running SystemInfo

#### Prerequisites

- Java 21 (LTS version)
- Apache Maven 3.6 or later
- Recommended IDE: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

#### Maven (Command Line)

```bash
cd path/to/apex-retail-system
mvn clean compile exec:java
```

## IDE Instructions

1. Open the project as a Maven project.
2. Navigate to `src/main/java/com/apexretail/diagnostics/SystemInfo.java`.
3. Run the `main` method:
   - **IntelliJ:** Click the green play button.
   - **Eclipse:** Right-click → Run As → Java Application.
   - **VS Code:** Click Run or press F5.

### Expected Output

```text
Developer: David
Team: Apex Retail Solutions
Project: Inventory & Sales Management System
Java Version: 21.x.x
Timestamp: 2024-01-15T10:30:00Z
Application Version: 0.0.1
```

## Chapter 2: TemperatureConverter Utility

### Overview

The `TemperatureConverter` utility provides robust, reusable methods for converting temperatures between Celsius, Fahrenheit, and Kelvin.

- **Purpose:** Demonstrate utility class development, input validation, and automated testing.
- **Scope:** Can be integrated into real-world applications if temperature calculations are needed (e.g., temperature-sensitive inventory, shipping conditions).

### Utility Class: TemperatureConverter.java

**Features:**

- Converts:
  - Celsius ↔ Fahrenheit
  - Celsius ↔ Kelvin
  - Fahrenheit ↔ Kelvin
  - Kelvin ↔ Fahrenheit
- Validates inputs against **absolute zero**, throwing exceptions if violated.
- Encapsulates constants as **private fields**.
- Fully reusable and maintainable.

**Example Usage:**

```java
double f = TemperatureConverter.celsiusToFahrenheit(100.0);
double k = TemperatureConverter.celsiusToKelvin(0.0);
double c = TemperatureConverter.fahrenheitToCelsius(32.0);
```

### Automated Testing: TemperatureConverterTest.java

**Purpose:** Ensure correctness, robustness, and precision of the `TemperatureConverter` utility.

**Test Coverage:**

- Known conversions – water freezing/boiling points, human body temperature, absolute zero, room temperature.
- Symmetry / Round-trip – C↔F↔C, C↔K↔C, F↔K↔F.
- Absolute zero boundary checks – throws `IllegalArgumentException` below absolute zero.
- Special double values – `NaN`, `POSITIVE_INFINITY`, `NEGATIVE_INFINITY`.
- Extreme but valid values – `Double.MAX_VALUE` and minimal positive values.
- Precision accumulation – 100 repeated round-trip conversions to detect floating-point drift.
- Direct vs indirect consistency – F→K vs F→C→K comparison.
- Zero and range testing – validates correct behavior across negative, zero, and positive temperatures.

### Running Tests (JUnit 5 / Maven)

```bash
cd path/to/apex-retail-system
mvn test
```

### Expected Output

All tests pass without errors or failures.

### Development Notes

- Constants are **private**; tests validate behavior via public methods only.
- Tests serve **development and QA purposes**; not included in production runtime.
- Demonstrates **industry-standard practices**:
  - Encapsulation
  - Input validation
  - Unit testing
  - Edge-case handling
  - Precision considerations

# Chapter 3: Core Domain Models

## Overview

Chapter 3 introduces the core domain layer of the Apex Retail system. This layer models real-world business concepts using well-defined Java classes that enforce data integrity and business rules at the object level.

The goal of this chapter is to ensure that domain objects are always created in a valid state and remain consistent throughout their lifecycle.

## Domain Design Principles

The following principles guide the domain model design:

- Domain objects are responsible for validating their own data
- Validation occurs at construction time
- Objects must never exist in an invalid state
- Fields are encapsulated and accessed through getters
- Immutability is preferred where applicable
- Business rules are kept close to the data they protect

## Product Domain Object

The Product class represents an item managed within the retail inventory system.

### Responsibilities

- Store product identity and attributes
- Validate all incoming data at creation time
- Manage inventory quantity safely
- Enforce stock-related business rules

### Core Attributes

- Unique product identifier
- Product name
- Product price
- Quantity currently in stock
- Product category

### Validation Rules

- ID must be non-negative
- Name must not be null, empty, or blank
- Price must be zero or greater
- Initial stock quantity must be zero or greater
- Category must not be null

All validation is performed inside the constructor to ensure that a Product instance can never be created in an invalid state.

### Inventory Behavior

The Product class manages its own inventory state, including:

- Increasing stock quantity
- Decreasing stock quantity
- Preventing stock from dropping below zero
- Determining whether the product is currently in stock

Inventory-related validation is enforced internally to guarantee consistency regardless of where the product is used.

## Category Domain Object

The Category class represents a logical grouping of products within the system.

### Responsibilities

- Classify products for organization and reporting
- Provide a stable and immutable identifier for grouping

### Core Attributes

- Unique category identifier
- Category name
- Optional category description

### Validation Rules

- ID must be valid
- Name must not be null or blank
- Description is optional

### Design Characteristics

- Immutable after creation
- Getters only
- Equality based solely on ID
- Overrides equals(), hashCode(), and toString()

# Chapter 4: Inventory Service & Decision Structures

## Overview

Chapter 4 introduces the service layer and applies decision structures to implement business operations that coordinate domain objects.

This chapter focuses on separating business workflow logic from domain data integrity, while ensuring validations occur at the correct layer.

## Service Layer Purpose

The service layer is responsible for:

- Coordinating domain objects
- Enforcing transaction-level business rules
- Validating inputs before invoking domain behavior
- Representing real-world business actions

## InventoryService

The InventoryService manages inventory-related business operations for products.

### Responsibilities

- Sell products by reducing stock
- Restock products by increasing inventory
- Validate inputs before performing operations
- Delegate stock management to the Product domain object

### Key Operations

#### Sell Product

- Accepts a Product and a requested quantity
- Validates:
  - Product reference is not null
  - Quantity requested is greater than zero
- Delegates stock reduction to the product
- Relies on domain-level validation to prevent invalid stock changes

#### Restock Product

- Accepts a Product and a quantity to add
- Validates:
  - Product reference is not null
  - Quantity is greater than zero
- Delegates stock increase to the product

## Validation Strategy

Validation occurs at multiple layers for different reasons:

- **Domain Layer (Product)**:
  - Protects data integrity
  - Enforces invariants such as non-negative stock

- **Service Layer (InventoryService)**:
  - Validates method inputs
  - Represents business transaction rules
  - Coordinates operations safely

This approach avoids duplication while maintaining clear separation of responsibility.

## Exception Handling Philosophy

- Domain and service methods throw unchecked exceptions (IllegalArgumentException) when validation fails
- Exceptions propagate naturally to calling layers
- Try/catch blocks are reserved for application boundaries (UI, API, controllers), not core business logic

### Project Structure

```text
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
│   │               └── service/
│   │                   └── InventoryService.java
│   └── test/
│       └── java/
│           └── com/
│               └── apexretail/
│                   └── utilities/
│                       └── TemperatureConverterTest.java
├── pom.xml
└── README.md
```

### Development Team

- **Primary Developer & Maintainer:** David
- **Team:** Apex Retail Solutions
- **Contact:** Internal repository/team directory
