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
        System.out.println(getSystemInfo());
    }

    public static String getSystemInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Developer: David\n");
        sb.append("Team: Apex Retail Solutions\n");
        sb.append("Project: Inventory & Sales Management System\n");
        sb.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        sb.append("Timestamp: ").append(Instant.now()).append("\n");
        sb.append("Application Version: 0.0.1");
        return sb.toString();
    }
}
