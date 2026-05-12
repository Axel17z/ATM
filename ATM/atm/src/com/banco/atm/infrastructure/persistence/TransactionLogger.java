package com.banco.atm.infrastructure.persistence;

import com.banco.atm.domain.model.Transaction;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionLogger {

    private List<Transaction> history = new ArrayList<>();
    private static final String LOG_FILE = "atm_log.txt";

    public void log(String message) {
        System.out.println("[LOG " + LocalDateTime.now() + "] " + message);
        writeToFile("[LOG " + LocalDateTime.now() + "] " + message);
    }

    public void logTransaction(Transaction transaction) {
        history.add(transaction);
        log(transaction.toString());
    }

    public List<Transaction> getHistory() {
        return new ArrayList<>(history);
    }

    private void writeToFile(String message) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(message);
        } catch (IOException e) {
            System.err.println("Error al escribir log: " + e.getMessage());
        }
    }
}
