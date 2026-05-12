package com.banco.atm.domain.model;

import java.time.LocalDateTime;

public class Transaction {
    public enum Type { RETIRO, DEPOSITO, TRANSFERENCIA, CONSULTA, CAMBIO_NIP }

    private Type type;
    private double amount;
    private String cardNumber;
    private LocalDateTime dateTime;
    private String description;

    public Transaction(Type type, double amount, String cardNumber, String description) {
        this.type = type;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.dateTime = LocalDateTime.now();
        this.description = description;
    }

    public Type getType() { return type; }
    public double getAmount() { return amount; }
    public String getCardNumber() { return cardNumber; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f - %s", dateTime, type, amount, description);
    }
}
