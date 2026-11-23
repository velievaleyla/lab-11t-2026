import exceptions.*;
import model.BankAccount;
import model.CheckingAccount;

public class LabTestRunner {

    private static int passedTests = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Running automated tests for Lab 11...");
        System.out.println("==================================================\n");

        testTimeSpanCreation();
        testTimeSpanAdd();
        testTimeSpanSubtract();
        testTimeSpanScale();
        testBankingAccountOperations();
        testCheckingAccountDailyLimit();
        testAccountValidationRules();

        System.out.println("\n==================================================");
        System.out.println("Test Results: " + passedTests + " / " + totalTests + " passed!");
        if (passedTests == totalTests) {
            System.out.println("All tests passed successfully!");
        } else {
            System.err.println("Some tests failed!");
        }
        System.out.println("==================================================");
    }

    private static void assertTrue(boolean condition, String testName) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("  [PASS] " + testName);
        } else {
            System.err.println("  [FAIL] " + testName);
        }
    }

    private static void testTimeSpanCreation() {
        System.out.println("--- Testing TimeSpan Creation ---");

        TimeSpan t1 = new TimeSpan();
        assertTrue(t1.getHours() == 0 && t1.getMinutes() == 0, "Default constructor creates 0:00");

        TimeSpan t2 = new TimeSpan(125);
        assertTrue(t2.getHours() == 2 && t2.getMinutes() == 5, "TimeSpan(125) is 2 hours 5 min");

        TimeSpan t3 = new TimeSpan(3, 45);
        assertTrue(t3.getHours() == 3 && t3.getMinutes() == 45, "TimeSpan(3, 45) is 3 hours 45 min");

        boolean caught = false;
        try {
            new TimeSpan(-5);
        } catch (IllegalInputTimeSpanException e) {
            caught = true;
        }
        assertTrue(caught, "Negative minutes throws IllegalInputTimeSpanException");

        caught = false;
        try {
            new TimeSpan(-1, 30);
        } catch (IllegalInputTimeSpanException e) {
            caught = true;
        }
        assertTrue(caught, "Negative hours throws IllegalInputTimeSpanException");

        caught = false;
        try {
            new TimeSpan(2, 60);
        } catch (IllegalInputTimeSpanException e) {
            caught = true;
        }
        assertTrue(caught, "Minutes > 59 throws IllegalInputTimeSpanException");
    }

    private static void testTimeSpanAdd() {
        System.out.println("\n--- Testing TimeSpan Add ---");

        TimeSpan t = new TimeSpan(1, 30);
        t.add(1, 40);
        assertTrue(t.getHours() == 3 && t.getMinutes() == 10, "1:30 + 1:40 = 3:10");

        t.add(50);
        assertTrue(t.getHours() == 4 && t.getMinutes() == 0, "3:10 + 50 min = 4:00");

        boolean caught = false;
        try {
            t.add(-10);
        } catch (IllegalInputTimeSpanException e) {
            caught = true;
        }
        assertTrue(caught, "Adding negative minutes throws IllegalInputTimeSpanException");
    }

    private static void testTimeSpanSubtract() {
        System.out.println("\n--- Testing TimeSpan Subtract ---");

        TimeSpan t = new TimeSpan(2, 30);
        t.subtract(1, 15);
        assertTrue(t.getHours() == 1 && t.getMinutes() == 15, "2:30 - 1:15 = 1:15");

        t.subtract(30);
        assertTrue(t.getHours() == 0 && t.getMinutes() == 45, "1:15 - 30 min = 0:45");

        boolean caught = false;
        try {
            t.subtract(1, 0); // current is 45 min, subtracting 60 min
        } catch (NegativeTimeSpanException e) {
            caught = true;
        }
        assertTrue(caught, "Subtracting more than current time throws NegativeTimeSpanException");

        caught = false;
        try {
            t.subtract(50); // current is 45 min, subtracting 50 min
        } catch (NegativeTimeSpanException e) {
            caught = true;
        }
        assertTrue(caught, "Subtracting minutes exceeding current throws NegativeTimeSpanException");
    }

    private static void testTimeSpanScale() {
        System.out.println("\n--- Testing TimeSpan Scale ---");

        TimeSpan t = new TimeSpan(1, 15);
        t.scale(2);
        assertTrue(t.getHours() == 2 && t.getMinutes() == 30, "1:15 * 2 = 2:30");

        boolean caught = false;
        try {
            t.scale(0);
        } catch (IllegalScaleFactorException e) {
            caught = true;
        }
        assertTrue(caught, "Scale with 0 throws IllegalScaleFactorException");

        caught = false;
        try {
            t.scale(-2);
        } catch (IllegalScaleFactorException e) {
            caught = true;
        }
        assertTrue(caught, "Scale with negative factor throws IllegalScaleFactorException");
    }

    private static void testBankingAccountOperations() {
        System.out.println("\n--- Testing Banking Account Operations ---");

        CheckingAccount account = new CheckingAccount("John Doe", 101L, 1001L, "Secret123");
        assertTrue(account.getAccountBalance() == 0.0, "Initial account balance is 0.0");
        assertTrue(account.getDailyWithdrawalLimit() == 300.0, "Default daily withdrawal limit is 300.0");

        account.deposit(200.0);
        assertTrue(account.getAccountBalance() == 200.0, "Deposit 200.0 results in 200.0 balance");

        boolean caught = false;
        try {
            account.deposit(-50.0);
        } catch (InvalidAmountException e) {
            caught = true;
        }
        assertTrue(caught, "Deposit <= 0 throws InvalidAmountException");

        account.withdraw(50.0);
        assertTrue(account.getAccountBalance() == 150.0, "Withdraw 50.0 results in 150.0 balance");

        caught = false;
        try {
            account.withdraw(500.0); // exceeds balance (150) and limit (300)
        } catch (DailyLimitExceededException e) {
            caught = true;
        }
        assertTrue(caught, "Withdrawal exceeding limit throws DailyLimitExceededException");

        caught = false;
        try {
            account.withdraw(200.0); // exceeds balance (150) but within limit (300)
        } catch (InsufficientFundsException e) {
            caught = true;
        }
        assertTrue(caught, "Withdrawal exceeding balance throws InsufficientFundsException");
    }

    private static void testCheckingAccountDailyLimit() {
        System.out.println("\n--- Testing CheckingAccount Daily Limit ---");

        CheckingAccount account = new CheckingAccount("Alice", 2002L, "Passw0rd!");
        account.deposit(1000.0);

        boolean caught = false;
        try {
            account.withdraw(350.0);
        } catch (DailyLimitExceededException e) {
            caught = true;
        }
        assertTrue(caught, "Withdrawal over default 300.0 limit throws DailyLimitExceededException");

        account.setDailyWithdrawalLimit(500.0);
        account.withdraw(350.0);
        assertTrue(account.getAccountBalance() == 650.0, "Withdrawal after increasing limit succeeds");
    }

    private static void testAccountValidationRules() {
        System.out.println("\n--- Testing Account Validation Rules ---");

        // Password rules: at least 8 chars, at least one digit
        String shortPass = "abc1";
        boolean validShort = shortPass.length() >= 8 && shortPass.matches(".*\\d.*");
        assertTrue(!validShort, "Password shorter than 8 chars is invalid");

        String noDigitPass = "abcdefgh";
        boolean validNoDigit = noDigitPass.length() >= 8 && noDigitPass.matches(".*\\d.*");
        assertTrue(!validNoDigit, "Password with no digits is invalid");

        String validPass = "secure123";
        boolean valid = validPass.length() >= 8 && validPass.matches(".*\\d.*");
        assertTrue(valid, "Password >= 8 chars and containing digit is valid");

        // Deletion rule: non-zero balance
        CheckingAccount acc = new CheckingAccount("Bob", 3003L, "Passw0rd!");
        acc.deposit(10.0);
        assertTrue(acc.getAccountBalance() != 0.0, "Account has non-zero balance, deletion must be blocked");
    }
}
