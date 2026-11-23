package model;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

import java.util.Objects;

/**
 * Базовий клас банківського рахунку.
 */
public abstract class BankAccount {

    private String customerName;
    private Long accountNumber;
    private Long userId;
    private String userPassword;
    private Double accountBalance;

    /**
     * Конструктор згідно з сигнатурою UML:
     * (customerName, accountNumber, userId, userPassword).
     * Баланс встановлюється у 0 при створенні об'єкту.
     */
    public BankAccount(String customerName, Long accountNumber, Long userId, String userPassword) {
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.userPassword = userPassword;
        this.accountBalance = 0.0;
    }

    /**
     * Додатковий конструктор для зручності створення рахунку.
     */
    public BankAccount(String customerName, Long userId, String userPassword) {
        this(customerName, userId, userId, userPassword);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        if (accountBalance != null && accountBalance < 0.0) {
            throw new InsufficientFundsException("Account balance cannot be negative: " + accountBalance);
        }
        this.accountBalance = accountBalance;
    }

    /**
     * Внесення коштів на рахунок.
     *
     * @param amount сума внеску (повинна бути більше 0)
     */
    public void deposit(double amount) {
        if (amount <= 0.0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0: " + amount);
        }
        this.accountBalance += amount;
    }

    /**
     * Зняття коштів з рахунку.
     *
     * @param amount сума зняття (повинна бути більше 0 і не перевищувати баланс)
     */
    public void withdraw(double amount) {
        if (amount <= 0.0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0: " + amount);
        }
        if (amount > this.accountBalance) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Current balance: " + this.accountBalance + ", requested: " + amount
            );
        }
        this.accountBalance -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "customerName='" + customerName + '\'' +
                ", accountNumber=" + accountNumber +
                ", userId=" + userId +
                ", accountBalance=" + accountBalance +
                '}';
    }
}
