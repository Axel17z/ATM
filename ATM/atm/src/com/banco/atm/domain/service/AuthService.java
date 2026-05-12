package com.banco.atm.domain.service;

import com.banco.atm.domain.model.Account;
import com.banco.atm.infrastructure.network.BankServerMock;
import com.banco.atm.infrastructure.persistence.TransactionLogger;
import com.banco.atm.infrastructure.security.PinEncryptor;


public class AuthService {

    private BankServerMock server;
    private TransactionLogger logger;
    private Account currentAccount;

    public AuthService(BankServerMock server, TransactionLogger logger) {
        this.server = server;
        this.logger = logger;
    }

    public boolean authenticate(String card, String pin) {
        currentAccount = server.findAccount(card);

        if (currentAccount == null) {
            logger.log("Tarjeta no encontrada: " + card);
            return false;
        }

        if (currentAccount.isBlocked()) {
            logger.log("Tarjeta bloqueada: " + card);
            return false;
        }

        String encryptedInput  = PinEncryptor.encrypt(pin);
        String encryptedStored = PinEncryptor.encrypt(currentAccount.getPin());

        if (encryptedInput.equals(encryptedStored)) {
            currentAccount.resetFailedAttempts();
            logger.log("Inicio de sesión correcto: " + card);
            return true;
        }

        currentAccount.incrementFailedAttempts();
        logger.log("Intento fallido (" + currentAccount.getFailedAttempts() + "/3): " + card);

        if (currentAccount.isBlocked()) {
            logger.log("Tarjeta bloqueada por intentos fallidos: " + card);
        }

        return false;
    }

    public void logout() {
        currentAccount = null;
        logger.log("Sesión cerrada");
    }

    public boolean isBlocked() {
        return currentAccount != null && currentAccount.isBlocked();
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }
}
