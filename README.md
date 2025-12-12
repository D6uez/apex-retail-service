# Apex Retail – Inventory & Sales Management System

## Project Overview

**Apex Retail** is a comprehensive Inventory & Sales Management System designed to streamline retail operations, inventory tracking, and sales processing for small to medium-sized retail businesses.

### System Purpose
This system provides retailers with a centralized platform to manage their inventory, process sales transactions, track stock levels, and generate insightful business reports. It aims to replace manual processes and disparate systems with an integrated, efficient solution.

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

## Current Milestone: Chapter 1

### System Diagnostic Utility
We begin our development journey with a minimal systems check to verify that our development environment is properly configured.

**SystemInfo Diagnostic Tool**
- **Purpose**: Verifies Java installation, Maven configuration, and basic runtime environment
- **Functionality**: Outputs developer information, project details, Java version, and timestamp
- **Use Case**: First step in building the system - ensuring all team members have a consistent development setup

### Running the SystemInfo Tool

#### Prerequisites
Ensure you have the following installed:
- **Java 21** (OpenJDK or Oracle JDK)
- **Apache Maven** 3.6 or later

#### Using Maven (Command Line)
```bash
# Navigate to the project root directory (where pom.xml is located)
cd path/to/apex-retail-system

# Clean, compile, and execute the SystemInfo class
mvn clean compile exec:java

# Or if you want to run it directly after compilation
mvn compile exec:java
```
## Running from an IDE

1. **Open the project** in your preferred IDE (IntelliJ IDEA, Eclipse, VS Code)
2. **Import as Maven project** when prompted
3. **Navigate to** `src/main/java/com/apexretail/diagnostics/SystemInfo.java`
4. **Run the main method** using your IDE's run/debug functionality
   - **IntelliJ**: Click the green play button next to the main method
   - **Eclipse**: Right-click the file → Run As → Java Application
   - **VS Code**: Click "Run" above the main method or use F5

## Expected Output
When successfully executed, you should see output similar to:

Developer: David
Team: Apex Retail Solutions
Project: Inventory & Sales Management System
Java Version: 21.0.2
Timestamp: 2024-01-15T10:30:00Z
Application Version: 0.0.1

## Environment Requirements

### Mandatory Software
- **Java Development Kit (JDK) 21**
  - Download from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/#java21)
  - Verify installation: `java -version` should show "21.x.x"

- **Apache Maven 3.6+**
  - Download from [Apache Maven](https://maven.apache.org/download.cgi)
  - Verify installation: `mvn -version` should show version 3.6 or higher

### Recommended Development Tools
- **IDE**: IntelliJ IDEA (Community or Ultimate), Eclipse, or VS Code with Java extensions
- **Git**: For version control
- **Postman** or **cURL**: For future API testing

## Project Structure
```text
apex-retail-system/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── apexretail/
│   │               └── diagnostics/
│   │                   └── SystemInfo.java
│   └── test/
│       └── java/
│           └── (future test classes)
├── pom.xml
└── README.md
```
## Development Team
**Primary Developer & Maintainer**: David  
**Team**: Apex Retail Solutions  
**Contact**: Internal repository - see team directory for contact information

## Next Steps
1. Verify your environment runs the SystemInfo utility successfully
2. Review the project architecture documentation (coming soon)
3. Prepare for Chapter 2: Building the core domain models and data layer

---

*This README will evolve as the project progresses. Check back regularly for updates on new features, API documentation, and deployment instructions.*