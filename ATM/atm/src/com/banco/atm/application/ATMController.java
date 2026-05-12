package com.banco.atm.application;

import com.banco.atm.domain.model.Transaction;
import com.banco.atm.domain.service.AuthService;
import com.banco.atm.domain.service.PinService;
import com.banco.atm.domain.service.TransactionService;
import com.banco.atm.infrastructure.network.BankServerMock;
import com.banco.atm.infrastructure.persistence.TransactionLogger;
import java.util.List;


public class ATMController {

    private AuthService        authService;
    private TransactionService transactionService;
    private PinService         pinService;
    private TransactionLogger  logger;

    public ATMController() {
        BankServerMock server = new BankServerMock();
        logger = new TransactionLogger();

        authService        = new AuthService(server, logger);
        transactionService = new TransactionService(server, logger);
        pinService         = new PinService(logger);
    }

    public boolean login(String card, String pin) {
        return authService.authenticate(card, pin);
    }

    public void logout() {
        authService.logout();
        transactionService.resetLimiteDiario();
    }

    public boolean isTarjetaBloqueada() {
        return authService.isBlocked();
    }

    // --- Consulta ---
    public double consultarSaldo() {
        return authService.getCurrentAccount().getBalance();
    }

    // --- Transacciones ---
    public boolean retirar(double amount) {
        return transactionService.withdraw(authService.getCurrentAccount(), amount);
    }

    public boolean depositar(double amount) {
        return transactionService.deposit(authService.getCurrentAccount(), amount);
    }

    public boolean transferir(String targetCard, double amount) {
        return transactionService.transfer(authService.getCurrentAccount(), targetCard, amount);
    }

    // --- NIP ---
    public boolean cambiarPin(String newPin) {
        return pinService.changePin(authService.getCurrentAccount(), newPin);
    }

    // --- Info ---
    public double getLimiteDiario()          { return transactionService.getLimiteDiario(); }
    public double getRetiradoHoy()           { return transactionService.getRetiradoHoy(); }
    public List<Transaction> obtenerHistorial() { return logger.getHistory(); }
}
