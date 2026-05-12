package com.banco.atm.domain.service;

import com.banco.atm.domain.model.Account;
import com.banco.atm.domain.model.Transaction;
import com.banco.atm.infrastructure.network.BankServerMock;
import com.banco.atm.infrastructure.persistence.TransactionLogger;


public class TransactionService {

    private static final double LIMITE_DIARIO = 10000;

    private BankServerMock server;
    private TransactionLogger logger;
    private double retiradoHoy = 0;

    public TransactionService(BankServerMock server, TransactionLogger logger) {
        this.server = server;
        this.logger = logger;
    }

    public void resetLimiteDiario() {
        retiradoHoy = 0;
    }

    public boolean withdraw(Account account, double amount) {
        if (account == null || amount <= 0) return false;
        if (amount > account.getBalance()) return false;
        if (retiradoHoy + amount > LIMITE_DIARIO) return false;

        account.withdraw(amount);
        retiradoHoy += amount;

        logger.logTransaction(new Transaction(
            Transaction.Type.RETIRO, amount,
            account.getCardNumber(), "Retiro en cajero"
        ));
        return true;
    }

    public boolean deposit(Account account, double amount) {
        if (account == null || amount <= 0) return false;

        account.deposit(amount);

        logger.logTransaction(new Transaction(
            Transaction.Type.DEPOSITO, amount,
            account.getCardNumber(), "Depósito en cajero"
        ));
        return true;
    }

    public boolean transfer(Account account, String targetCard, double amount) {
        if (account == null || amount <= 0) return false;
        if (amount > account.getBalance()) return false;

        boolean ok = server.transfer(account.getCardNumber(), targetCard, amount);
        if (ok) {
            logger.logTransaction(new Transaction(
                Transaction.Type.TRANSFERENCIA, amount,
                account.getCardNumber(), "Transferencia a " + targetCard
            ));
        }
        return ok;
    }

    public double getLimiteDiario() { return LIMITE_DIARIO; }
    public double getRetiradoHoy() { return retiradoHoy; }
}
