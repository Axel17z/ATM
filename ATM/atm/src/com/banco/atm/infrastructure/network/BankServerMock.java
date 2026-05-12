package com.banco.atm.infrastructure.network;

import com.banco.atm.domain.model.Account;
import java.util.HashMap;
import java.util.Map;

public class BankServerMock {

    private Map<String, Account> accounts = new HashMap<>();

    public BankServerMock() {
        // Cuentas de prueba
        accounts.put("123456789", new Account("123456789", "1234", 10000));
        accounts.put("987654321", new Account("987654321", "4321", 5000));
        accounts.put("111222333", new Account("111222333", "0000", 20000));
    }

    public Account findAccount(String cardNumber) {
        return accounts.get(cardNumber);
    }

    public boolean transfer(String fromCard, String toCard, double amount) {
        Account from = accounts.get(fromCard);
        Account to = accounts.get(toCard);

        if (from == null || to == null) return false;
        if (from.getBalance() < amount) return false;

        from.withdraw(amount);
        to.deposit(amount);
        return true;
    }
}
