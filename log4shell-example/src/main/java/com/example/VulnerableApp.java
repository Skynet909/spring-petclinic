// package com.example;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.apache.logging.log4j.core.net.JndiManager;

// /**
//  * Deliberately vulnerable test app
//  * Directly calls vulnerable JNDI method to test reachability detection
//  */
// public class VulnerableApp {
//     private static final Logger logger = LogManager.getLogger(VulnerableApp.class);

//     public static void main(String[] args) {
//         System.out.println("=== Vulnerable Test App ===");
        
//         if (args.length > 0) {
//             String userInput = args[0];
//             processInput(userInput);
//         } else {
//             processInput("default-input");
//         }
//     }

//     /**
//      * Process user input - vulnerable path to JNDI lookup
//      */
//     public static void processInput(String input) {
//         // This creates a direct call path to the vulnerable method
//         // that Soot can trace
//         try {
//             MyFunc(input);
//         } catch (Exception e) {
//             System.out.println("Error: " + e.getMessage());
//         }
//     }

//     /**
//      * Direct call to vulnerable JNDI manager
//      */
//     public static void MyFunc(String jndiString) {
//         try {
//             // Direct instantiation to ensure call graph sees it
//             JndiManager manager = JndiManager.getDefaultManager();
//             Integer result = (Integer) manager.lookup(jndiString);
//             System.out.println("Result: " + result);
//         } catch (Exception e) {
//             System.out.println("JNDI lookup failed: " + e.getMessage());
//         }
//     }
// }

//Testing deep call chain
package com.example;

import org.apache.logging.log4j.core.net.JndiManager;

/**
 * Deliberately vulnerable test app with a DEEP call chain to test truncation.
 */
public class VulnerableApp {

    public static void main(String[] args) {
        System.out.println("=== Deep Call Chain Vulnerable Test App ===");
        String userInput = args.length > 0 ? args[0] : "test-input";
        processInput(userInput);
    }

    public static void processInput(String input) {
        // Start the deep call chain
        intermediateCall1(input);
    }

    // NEW: A long chain of methods to create a deep path for the analyzer to find.
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
        // The final method in the chain calls the vulnerable lookup.
        MyFunc(s);
    }

    /**
     * Direct call to the vulnerable JNDI manager method.
     */
    public static void MyFunc(String jndiString) {
        try {
            JndiManager manager = JndiManager.getDefaultManager();
            Object result = manager.lookup(jndiString);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("JNDI lookup failed: " + e.getMessage());
        }
    }
}
