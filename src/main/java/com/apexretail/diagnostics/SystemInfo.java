package com.apexretail.diagnostics;

import java.time.Instant;

/**
 * Diagnostic utility that outputs basic application and environment
 * information.
 * Used for verifying the developer setup and runtime environment.
 * 
 * @author David
 * @version 0.0.1
 */
public class SystemInfo {

    /**
     * Prints developer, project, and system information.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Developer: David");
        System.out.println("Team: Apex Retail Solutions");
        System.out.println("Project: Inventory & Sales Management System");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Timestamp: " + Instant.now());
        System.out.println("Application Version: 0.0.1");
    }
}
