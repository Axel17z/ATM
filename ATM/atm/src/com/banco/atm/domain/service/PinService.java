package com.banco.atm.domain.service;

import com.banco.atm.domain.model.Account;
import com.banco.atm.domain.model.Transaction;
import com.banco.atm.infrastructure.persistence.TransactionLogger;


public class PinService {

    private TransactionLogger logger;

    public PinService(TransactionLogger logger) {
        this.logger = logger;
    }

    public boolean changePin(Account account, String newPin) {
        if (account == null) return false;
        if (newPin == null || newPin.length() != 4) return false;
        if (!newPin.matches("\\d{4}")) return false;

        account.setPin(newPin);

        logger.logTransaction(new Transaction(
            Transaction.Type.CAMBIO_NIP, 0,
            account.getCardNumber(), "Cambio de NIP"
        ));
        return true;
    }
}
