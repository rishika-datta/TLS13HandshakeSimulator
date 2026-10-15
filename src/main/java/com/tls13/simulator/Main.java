
package com.tls13.simulator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n==================================");
            System.out.println("     TLS 1.3 HANDSHAKE SIMULATOR");
            System.out.println("==================================");
            System.out.println("1. Learn about TLS 1.3");
            System.out.println("2. Simulate handshake steps");
            System.out.println("3. Test secure message exchange");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a number from 1 to 4.");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nTLS 1.3 provides secure communication.");
                    System.out.println("It authenticates the server and");
                    System.out.println("establishes keys for encrypted data.");
                    break;

                case 2:
                    simulateHandshake(scanner);
                    break;

                case 3:
                    System.out.println("\nTo test a REAL TLS connection:");
                    System.out.println("1. Run TLSServer.java first.");
                    System.out.println("2. Run TLSClient.java separately.");
                    System.out.println("3. Check the protocol and exchanged message.");
                    break;

                case 4:
                    System.out.println("Exiting TLS 1.3 Simulator.");
                    break;

                default:
                    System.out.println("Invalid choice. Enter 1, 2, 3, or 4.");
            }

        } while (true);

        // Scanner cleanup is not essential for this small console application.
    }

    private static void simulateHandshake(Scanner scanner) {
        String[] steps = {
                "ClientHello: Client proposes TLS 1.3.",
                "ServerHello: Server selects connection parameters.",
                "Certificate: Server provides its certificate when required.",
                "Certificate Verification: Client checks the server's identity.",
                "Key Establishment: Both sides derive handshake and traffic keys.",
                "Handshake Complete: Secure session is ready."
        };

        System.out.println("\n--- TLS 1.3 HANDSHAKE ---");

        for (int i = 0; i < steps.length; i++) {
            System.out.println("\nStep " + (i + 1) + ": " + steps[i]);

            if (i < steps.length - 1) {
                System.out.print("Press Enter for the next step...");
                scanner.nextLine();
            }
        }

        System.out.println("\nSimulation completed!");
        System.out.println("Note: These steps are a visual simulation,");
        System.out.println("not a trace of the actual TLS handshake.");
    }
}
