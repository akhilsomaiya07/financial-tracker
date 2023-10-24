package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import static java.time.LocalDate.*;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>,<time>,<vendor>,<type>,<amount>
        // For example: 2023-04-29,13:45:00,Amazon,PAYMENT,29.99
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        // Initialize an ArrayList to store transactions
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {

                // If the file exists, read its content
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                String line;

                while ((line = reader.readLine()) != null) {
                    // Split the line into its components using a comma as the delimiter
                    String[] parts = line.split("\\|");
                    if (parts.length == 5) {
                        LocalDate date = parse(parts[0], DATE_FORMATTER);
                        LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                        String Vendors = parts[2];
                        String type = parts[3];
                        double amount = Double.parseDouble(parts[4]);

                        // Create a Transaction object and add it to the ArrayList
                        Transaction  transaction = new Transaction(date, time, Vendors, type, amount);
                        transactions.add(transaction);
                    }
                }

                // Close the file after reading
                reader.close();

        } catch (IOException e) {
            // Handle any IO errors that may occur
            System.err.println("An error occurred while loading transactions: " + e.getMessage());
        }
    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Deposit` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
        System.out.println("Enter the date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();
        LocalDate date = LocalDate.parse (dateInput, DATE_FORMATTER);

        System.out.println("Enter the time (HH:mm:ss): ");
        String timeInput = scanner.nextLine();
        LocalTime time = LocalTime.parse (timeInput, TIME_FORMATTER);

        System.out.println("Enter the description: ");
        String description = scanner.nextLine();

        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Enter the amount: ");
        double amount = scanner.nextDouble();


        // Create new deposit and add to transactions

        Transaction deposit = new Transaction(date,time,description, vendor, amount);
        transactions.add(deposit);

        System.out.println("Deposit added successfully.");
        System.out.println("Do you want to do something else? (yes/no): ");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("no")) {
            System.out.println("Goodbye!");
            scanner.close();
        }
    }


    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Payment` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
        System.out.println("Enter payment details:");

        // Prompt the user for the date
        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.next();

        // Prompt the user for the time
        System.out.println("Invalid date and time format. Please use yyyy-MM-dd HH:mm:ss.");
        String time = scanner.next();

        // Prompt the user for the vendor
        System.out.print("Vendor: ");
        String vendor = scanner.next();

        // Prompt the user for the amount
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        amount = -1.0;

        while (amount < 0) {
            System.out.println("Enter the amount (positive number):");
            String amountInput = scanner.nextLine();

            try {
                amount = Double.parseDouble(amountInput);
                if (amount < 0) {
                    System.out.println("Amount must be a positive number. Please enter a valid amount.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
            }
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }
}