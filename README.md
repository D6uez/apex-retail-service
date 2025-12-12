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
│   │               └── utilities/
│   │                   └── TemperatureConverter.java
│   └── test/
│       └── java/
│           └── com/apexretail/utilities/
│               └── TemperatureConverterTest.java
├── pom.xml
└── README.md
```

### Development Team

- **Primary Developer & Maintainer:** David
- **Team:** Apex Retail Solutions
- **Contact:** Internal repository/team directory
