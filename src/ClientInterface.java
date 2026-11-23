import exceptions.*;
import model.BankAccount;
import model.CheckingAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientInterface {

    private final List<BankAccount> accounts = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    private final List<String> options = List.of(
            "1 - Create account",
            "2 - Delete account",
            "3 - Make account deposit",
            "4 - Make account withdrawal",
            "5 - Print account info",
            "6 - Exit"
    );

    public void start() throws IOException {
        String option;
        while (true) {
            printMainMenu();
            option = scanner.next();

            int choice;
            try {
                choice = Integer.parseInt(option);
            } catch (NumberFormatException e) {
                System.err.println("Error: Please enter a valid choice (1 thru 6)\n");
                flushError();
                continue;
            }

            switch (choice) {
                case 1:
                    try {
                        addNewAccount();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                        flushError();
                    }
                    break;
                case 2:
                    try {
                        deleteAccount();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                        flushError();
                    }
                    break;
                case 3:
                    try {
                        deposit();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                        flushError();
                    }
                    break;
                case 4:
                    try {
                        withdraw();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                        flushError();
                    }
                    break;
                case 5:
                    try {
                        printInfo();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                        flushError();
                    }
                    break;
                case 6:
                    return;
                default:
                    System.err.println("Error: Please enter a valid choice (1 thru 6)\n");
                    flushError();
            }
        }
    }

    private void flushError() {
        System.err.flush();
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }
    }

    private void printMainMenu() throws IOException {
        System.out.println("\n****************");
        System.out.println("Banking system: ");
        System.out.println("****************");
        options.forEach(System.out::println);
        System.out.print("\n>>> Choose your option: ");
    }

    private BankAccount findAccount(Long userId) {
        for (BankAccount account : accounts) {
            if (account.getUserId().equals(userId)) {
                return account;
            }
        }
        return null;
    }

    private Long parseUserId(String userIdStr) {
        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new AccountException("User ID must be a valid integer number: " + userIdStr);
        }
    }

    private double parseAmount(String amountStr) {
        try {
            return Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Amount must be a valid number: " + amountStr);
        }
    }

    private void addNewAccount() {
        System.out.println("\n****************");
        System.out.println("Creating new account: ");
        System.out.println("****************");

        System.out.print(">>> Enter customer name: ");
        var name = scanner.next();
        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        String password = scanner.next();
        System.out.print(">>> Confirm password: ");
        String confirmPassword = scanner.next();

        Long userId = parseUserId(userIdStr);

        if (findAccount(userId) != null) {
            throw new DuplicateUserIdException("Account with User ID " + userId + " already exists.");
        }

        if (password == null || password.length() < 8 || !password.matches(".*\\d.*")) {
            throw new InvalidPasswordException(
                    "Password must be at least 8 characters long and contain at least one digit."
            );
        }

        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException("Password confirmation does not match the entered password.");
        }

        CheckingAccount newAccount = new CheckingAccount(name, userId, password);
        accounts.add(newAccount);

        System.out.println("\nAccount successfully created for " + name + " (User ID: " + userId + ")!\n");
    }

    private void deleteAccount() {
        System.out.println("\n****************");
        System.out.println("Deleting account: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        Long userId = parseUserId(userIdStr);
        BankAccount account = findAccount(userId);

        if (account == null) {
            throw new AccountNotFoundException("Account with User ID " + userId + " not found.");
        }

        if (!account.getUserPassword().equals(password)) {
            throw new AuthenticationException("Incorrect password for User ID " + userId + ".");
        }

        if (account.getAccountBalance() != 0.0) {
            throw new NonZeroBalanceException(
                    "Cannot delete account with non-zero balance: " + account.getAccountBalance()
            );
        }

        accounts.remove(account);
        System.out.println("\nAccount with User ID " + userId + " has been successfully deleted.\n");
    }

    private void deposit() {
        System.out.println("\n****************");
        System.out.println("Making a deposit: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var amountStr = scanner.next();

        Long userId = parseUserId(userIdStr);
        BankAccount account = findAccount(userId);

        if (account == null) {
            throw new AccountNotFoundException("Account with User ID " + userId + " not found.");
        }

        if (!account.getUserPassword().equals(password)) {
            throw new AuthenticationException("Incorrect password for User ID " + userId + ".");
        }

        double amount = parseAmount(amountStr);
        account.deposit(amount);

        System.out.printf("%nSuccessfully deposited %.2f. New balance: %.2f%n%n", amount, account.getAccountBalance());
    }

    private void withdraw() {
        System.out.println("\n****************");
        System.out.println("Making a withdrawal: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var amountStr = scanner.next();

        Long userId = parseUserId(userIdStr);
        BankAccount account = findAccount(userId);

        if (account == null) {
            throw new AccountNotFoundException("Account with User ID " + userId + " not found.");
        }

        if (!account.getUserPassword().equals(password)) {
            throw new AuthenticationException("Incorrect password for User ID " + userId + ".");
        }

        double amount = parseAmount(amountStr);
        account.withdraw(amount);

        System.out.printf("%nSuccessfully withdrew %.2f. New balance: %.2f%n%n", amount, account.getAccountBalance());
    }

    private void printInfo() {
        System.out.println("\n****************");
        System.out.println("Printing account info: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        Long userId = parseUserId(userIdStr);
        BankAccount account = findAccount(userId);

        if (account == null) {
            throw new AccountNotFoundException("Account with User ID " + userId + " not found.");
        }

        if (!account.getUserPassword().equals(password)) {
            throw new AuthenticationException("Incorrect password for User ID " + userId + ".");
        }

        System.out.println("\nAccount info: ");
        System.out.println(" - customer name: " + account.getCustomerName());
        System.out.println(" - account balance: " + account.getAccountBalance());
        if (account instanceof CheckingAccount ca) {
            System.out.println(" - account type: Checking");
            System.out.println(" - account daily withdrawal limit: " + ca.getDailyWithdrawalLimit());
        } else {
            System.out.println(" - account type: Standard");
        }

        System.out.print("\nPress Enter to return to main menu...");
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
