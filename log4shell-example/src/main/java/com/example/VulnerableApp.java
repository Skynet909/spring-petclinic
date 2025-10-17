package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Deliberately vulnerable test app with a DEEP call chain to test truncation.
 * This version is modified to be vulnerable to Log4Shell (CVE-2021-44228)
 * and related vulnerabilities by logging user-provided input.
 */
public class VulnerableApp {

    // Initialize a Log4j logger
    private static final Logger logger = LogManager.getLogger(VulnerableApp.class);

    public static void main(String[] args) {
        System.out.println("=== Deep Call Chain Vulnerable Test App ===");
        // Example of a malicious string: "${jndi:ldap://127.0.0.1:1389/a}"
        String userInput = args.length > 0 ? args[0] : "test-input";
        System.out.println("Processing input: " + userInput);
        processInput(userInput);
    }

    public static void processInput(String input) {
        // Start the deep call chain
        intermediateCall1(input);
    }

    // A long chain of methods to create a deep path for the analyzer to find.
    // Each method just calls the next one in the sequence.
    public static void intermediateCall1(String s) { intermediateCall2(s); }
    public static void intermediateCall2(String s) { intermediateCall3(s); }
    public static void intermediateCall3(String s) { intermediateCall4(s); }
    public static void intermediateCall4(String s) { intermediateCall5(s); }
    public static void intermediateCall5(String s) { intermediateCall6(s); }
    public static void intermediateCall6(String s) { intermediateCall7(s); }
    public static void intermediateCall7(String s) { intermediateCall8(s); }
    public static void intermediateCall8(String s) { intermediateCall9(s); }
    public static void intermediateCall9(String s) { intermediateCall10(s); }
    public static void intermediateCall10(String s) { intermediateCall11(s); }

    public static void intermediateCall11(String s) {
        // The final method in the chain calls the vulnerable logging function, now named lookup.
        lookup(s);
    }

    /**
     * The vulnerable function that logs the user-controlled string.
     * Log4j versions < 2.15.0 will process JNDI lookups in the string,
     * leading to RCE (Remote Code Execution).
     * @param maliciousInput A string that could contain a JNDI lookup.
     */
    public static void lookup(String maliciousInput) {
        System.out.println("Logging the potentially malicious input via lookup...");
        // This is the vulnerable call. The logger will parse the input string and perform a lookup.
        logger.error(maliciousInput);
        System.out.println("Log entry created.");
    }
}
