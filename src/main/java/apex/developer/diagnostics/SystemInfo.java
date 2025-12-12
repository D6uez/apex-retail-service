/**
 * SystemInfo displays application and environment information.
 * 
 * @author David
 * @version 0.0.1
 */
package apex.developer.diagnostics;

import java.time.Instant;

public class SystemInfo {
    /**
     * Main method that outputs developer, project, and system information.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println(
                "Developer: David\nTeam: Apex Retail Solutions\nProject: Inventory & Sales Management System\nJava Version: "
                        + System.getProperty("java.version") + "\n" + Instant.now() + "\nApplication Version: 0.0.1");
    }
}