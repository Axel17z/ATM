package com.banco.atm.domain.model;

public class Account {
    private String cardNumber;
    private String pin;
    private double balance;
    private boolean blocked;
    private int failedAttempts;

    public Account(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
        this.blocked = false;
        this.failedAttempts = 0;
    }

    public String getCardNumber() { return cardNumber; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }
    public boolean isBlocked() { return blocked; }
    public int getFailedAttempts() { return failedAttempts; }

    public void setPin(String pin) { this.pin = pin; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    public void incrementFailedAttempts() {
        this.failedAttempts++;
        if (this.failedAttempts >= 3) {
            this.blocked = true;
        }
    }

    public void resetFailedAttempts() { this.failedAttempts = 0; }

    public void withdraw(double amount) { this.balance -= amount; }
    public void deposit(double amount) { this.balance += amount; }
}
