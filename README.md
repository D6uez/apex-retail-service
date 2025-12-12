# Apex Retail – Inventory & Sales Management System

## Project Description
A comprehensive inventory and sales management system designed for retail businesses. This system will streamline inventory tracking, sales processing, customer management, and reporting operations for retailers of all sizes.

### What the System Is For
The system provides a centralized platform for managing:
- Product inventory and stock levels
- Sales transactions and order processing
- Customer information and purchase history
- Supplier and vendor relationships
- Business analytics and reporting

### Who It Benefits
- **Retail Business Owners** - Gain better visibility into operations
- **Store Managers** - Streamline daily inventory and sales tasks
- **Sales Associates** - Process transactions efficiently
- **Inventory Specialists** - Track stock accurately
- **Business Analysts** - Access sales data and insights

### Future Capabilities
- Real-time inventory tracking with automated reorder points
- Integrated POS (Point of Sale) system
- Customer loyalty program management
- Multi-store inventory synchronization
- Advanced analytics dashboard
- Supplier management and purchase order automation
- Mobile app for on-the-go inventory checks

## Current Milestone: Chapter 1
### Systems Diagnostic Tool
We're starting with a minimal systems check to verify development environments are properly configured. The `SystemInfo` utility serves as a diagnostic tool to confirm:
- Java installation and version compatibility
- Build tool (Maven) functionality
- Basic project structure setup
- Development environment readiness

### Running the SystemInfo Utility

#### Using Maven from Command Line:
```bash
# Clean, compile, and execute in one command
mvn clean compile exec:java

From Your IDE (IntelliJ IDEA, Eclipse, VS Code):
Open the project as a Maven project

Navigate to src/main/java/apex/developer/diagnostics/SystemInfo.java

Run the main method:

Right-click on the file or class

Select "Run SystemInfo.main()"

Or use the IDE's run/debug buttons

Verify Successful Execution:
You should see output similar to:

text
Developer: David
Team: Apex Retail Solutions
Project: Inventory & Sales Management System
Java Version: 21.0.2
2024-01-15T14:30:00Z
Application Version: 0.0.1

Environment Requirements
Required Software
Java 21 (LTS version) - Download from Oracle

Maven 3.6+ - Download from Apache

Verification Commands
bash
# Check Java installation
java -version
# Should display: java version "21.x.x"

# Check Maven installation
mvn -version
# Should display Maven version and Java 21

# Verify environment variables
echo $JAVA_HOME  # macOS/Linux
echo %JAVA_HOME% # Windows
IDE Setup
IntelliJ IDEA: Enable "Maven" plugin, set JDK to 21

Eclipse: Install M2Eclipse plugin, configure Java 21 JRE

VS Code: Install Java Extension Pack, set "java.home" to JDK 21

Project Structure
text
apex-retail-system/
├── src/main/java/apex/developer/diagnostics/
│   └── SystemInfo.java          # Diagnostic utility
├── pom.xml                      # Maven configuration
└── README.md                    # This file
Development Team
Developer & Maintainer: David
Team: Apex Retail Solutions
Contact: [Your email/team contact]