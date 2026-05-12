package com.banco.atm.tests;

import com.banco.atm.application.ATMController;

public class ATMTest {

    public static void main(String[] args) {
        ATMController controller = new ATMController();

        System.out.println("=== TEST LOGIN ===");
        boolean login = controller.login("123456789", "1234");
        System.out.println("Login correcto: " + login);

        System.out.println("\n=== TEST SALDO ===");
        System.out.println("Saldo: $" + controller.consultarSaldo());

        System.out.println("\n=== TEST RETIRO ===");
        boolean retiro = controller.retirar(500);
        System.out.println("Retiro $500: " + retiro);
        System.out.println("Saldo después: $" + controller.consultarSaldo());

        System.out.println("\n=== TEST DEPÓSITO ===");
        boolean deposito = controller.depositar(200);
        System.out.println("Depósito $200: " + deposito);
        System.out.println("Saldo después: $" + controller.consultarSaldo());

        System.out.println("\n=== TEST TRANSFERENCIA ===");
        boolean transferencia = controller.transferir("987654321", 1000);
        System.out.println("Transferencia $1000 a 987654321: " + transferencia);
        System.out.println("Saldo después: $" + controller.consultarSaldo());

        System.out.println("\n=== TEST CAMBIO NIP ===");
        boolean pinOk = controller.cambiarPin("5678");
        System.out.println("Cambio NIP a 5678: " + pinOk);
        boolean pinMal = controller.cambiarPin("abc");
        System.out.println("Cambio NIP a 'abc' (debe fallar): " + pinMal);

        System.out.println("\n=== TEST BLOQUEO ===");
        ATMController ctrl2 = new ATMController();
        ctrl2.login("123456789", "0000"); // intento 1
        ctrl2.login("123456789", "0000"); // intento 2
        ctrl2.login("123456789", "0000"); // intento 3 - bloquea
        System.out.println("Tarjeta bloqueada: " + ctrl2.isTarjetaBloqueada());
    }
}
