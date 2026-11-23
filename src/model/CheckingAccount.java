package model;

import exceptions.DailyLimitExceededException;

/**
 * Рахунок із денним лімітом на зняття коштів.
 */
public class CheckingAccount extends BankAccount {

    private double dailyWithdrawalLimit = 300.0;

    public CheckingAccount(String customerName, Long accountNumber, Long userId, String userPassword) {
        super(customerName, accountNumber, userId, userPassword);
        this.dailyWithdrawalLimit = 300.0;
    }

    public CheckingAccount(String customerName, Long accountNumber, Long userId, String userPassword, double dailyWithdrawalLimit) {
        super(customerName, accountNumber, userId, userPassword);
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public CheckingAccount(String customerName, Long userId, String userPassword) {
        super(customerName, userId, userPassword);
        this.dailyWithdrawalLimit = 300.0;
    }

    public CheckingAccount(String customerName, Long userId, String userPassword, double dailyWithdrawalLimit) {
        super(customerName, userId, userPassword);
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    @Override
    public void deposit(double amount) {
        super.deposit(amount);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > this.dailyWithdrawalLimit) {
            throw new DailyLimitExceededException(
                    "Withdrawal amount (" + amount + ") exceeds daily limit (" + this.dailyWithdrawalLimit + ")"
            );
        }
        super.withdraw(amount);
    }
}
