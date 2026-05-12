package com.banco.atm.presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.banco.atm.application.ATMController;
import com.banco.atm.domain.model.Transaction;
import java.util.List;

public class MainFrame extends JFrame {

    private ATMController controller;
    private JPanel mainPanel;

    public MainFrame() {
        controller = new ATMController();
        setTitle("ATM Inteligente - Banco Nacional");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(15, 23, 42));
        add(mainPanel);
        mostrarLogin();
    }

    private void limpiarPantalla() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JButton crearBoton(String texto) {
        JButton button = new JButton(texto);
        button.setBackground(new Color(37, 99, 235));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(220, 42));
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // DRY: un solo método reutilizado por retiro, deposito y transferencia
    private Double pedirMonto(String mensaje) {
        String texto = JOptionPane.showInputDialog(this, mensaje);
        if (texto == null || texto.trim().isEmpty()) return null;
        try {
            double monto = Double.parseDouble(texto);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a cero");
                return null;
            }
            return monto;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Monto invalido");
            return null;
        }
    }

    private void mostrarLogin() {
        limpiarPantalla();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(15, 23, 42));
        panel.setBorder(new EmptyBorder(50, 150, 50, 150));

        JLabel titulo = new JLabel("CAJERO AUTOMATICO", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Banco Nacional", SwingConstants.CENTER);
        subtitulo.setForeground(new Color(148, 163, 184));
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tarjetaLabel = new JLabel("Numero de Tarjeta");
        tarjetaLabel.setForeground(Color.WHITE);
        tarjetaLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField tarjetaField = new JTextField();
        tarjetaField.setMaximumSize(new Dimension(300, 40));
        tarjetaField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel pinLabel = new JLabel("NIP");
        pinLabel.setForeground(Color.WHITE);
        pinLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPasswordField pinField = new JPasswordField();
        pinField.setMaximumSize(new Dimension(300, 40));
        pinField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton ingresarButton = crearBoton("Ingresar");
        ingresarButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        ingresarButton.addActionListener(e -> {
            String card = tarjetaField.getText().trim();
            char[] pinChars = pinField.getPassword();
            String pin = new String(pinChars);
            java.util.Arrays.fill(pinChars, '0');

            if (card.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }

            if (controller.login(card, pin)) {
                mostrarMenu();
            } else {
                if (controller.isTarjetaBloqueada()) {
                    JOptionPane.showMessageDialog(this,
                        "Tarjeta bloqueada por multiples intentos fallidos.\nContacte a su banco.",
                        "Tarjeta Bloqueada", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Tarjeta o NIP incorrecto",
                        "Error de autenticacion", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel hint = new JLabel("Demo: Tarjeta 123456789 / NIP 1234");
        hint.setForeground(new Color(100, 116, 139));
        hint.setFont(new Font("Arial", Font.ITALIC, 12));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(subtitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(tarjetaLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(tarjetaField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(pinLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(pinField);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(ingresarButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(hint);

        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void mostrarMenu() {
        limpiarPantalla();

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBackground(new Color(15, 23, 42));
        panel.setBorder(new EmptyBorder(40, 150, 40, 150));

        JLabel titulo = new JLabel("MENU PRINCIPAL", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));

        JButton saldoButton     = crearBoton("Consultar Saldo");
        JButton retiroButton    = crearBoton("Retirar Efectivo");
        JButton depositoButton  = crearBoton("Depositar Efectivo");
        JButton transButton     = crearBoton("Transferir");
        JButton historialButton = crearBoton("Ver Historial");
        JButton pinButton       = crearBoton("Cambiar NIP");
        JButton salirButton     = crearBoton("Cerrar Sesion");
        salirButton.setBackground(new Color(185, 28, 28));

        saldoButton.addActionListener(e -> {
            double saldo = controller.consultarSaldo();
            JOptionPane.showMessageDialog(this,
                String.format("Saldo disponible: $%.2f\nRetirado hoy: $%.2f / $%.2f",
                    saldo, controller.getRetiradoHoy(), controller.getLimiteDiario()),
                "Consulta de Saldo", JOptionPane.INFORMATION_MESSAGE);
        });

        // DRY: los 3 usan pedirMonto() en lugar de repetir el mismo bloque try/catch
        retiroButton.addActionListener(e -> {
            Double monto = pedirMonto("Ingrese el monto a retirar:");
            if (monto == null) return;
            if (controller.retirar(monto)) {
                JOptionPane.showMessageDialog(this,
                    String.format("Retiro exitoso de $%.2f\nSaldo restante: $%.2f", monto, controller.consultarSaldo()),
                    "Retiro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo realizar el retiro.\nVerifique saldo o limite diario ($" + controller.getLimiteDiario() + ")",
                    "Retiro Fallido", JOptionPane.WARNING_MESSAGE);
            }
        });

        depositoButton.addActionListener(e -> {
            Double monto = pedirMonto("Ingrese el monto a depositar:");
            if (monto == null) return;
            if (controller.depositar(monto)) {
                JOptionPane.showMessageDialog(this,
                    String.format("Deposito exitoso de $%.2f\nNuevo saldo: $%.2f", monto, controller.consultarSaldo()),
                    "Deposito Exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo realizar el deposito");
            }
        });

        transButton.addActionListener(e -> {
            String targetCard = JOptionPane.showInputDialog(this, "Ingrese numero de tarjeta destino:");
            if (targetCard == null || targetCard.trim().isEmpty()) return;
            Double monto = pedirMonto("Ingrese el monto a transferir:");
            if (monto == null) return;
            if (controller.transferir(targetCard.trim(), monto)) {
                JOptionPane.showMessageDialog(this,
                    String.format("Transferencia exitosa de $%.2f\nSaldo restante: $%.2f", monto, controller.consultarSaldo()),
                    "Transferencia Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Transferencia fallida.\nVerifique tarjeta destino y saldo disponible.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        historialButton.addActionListener(e -> mostrarHistorial());

        pinButton.addActionListener(e -> {
            JPasswordField nuevoPinField = new JPasswordField();
            int result = JOptionPane.showConfirmDialog(this, nuevoPinField,
                "Ingrese nuevo NIP (4 digitos):", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) return;
            String nuevoPin = new String(nuevoPinField.getPassword());
            if (controller.cambiarPin(nuevoPin)) {
                JOptionPane.showMessageDialog(this, "NIP actualizado correctamente");
            } else {
                JOptionPane.showMessageDialog(this,
                    "NIP invalido. Debe tener exactamente 4 digitos numericos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        salirButton.addActionListener(e -> {
            controller.logout();
            mostrarLogin();
        });

        panel.add(titulo);
        panel.add(saldoButton);
        panel.add(retiroButton);
        panel.add(depositoButton);
        panel.add(transButton);
        panel.add(historialButton);
        panel.add(pinButton);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(new Color(15, 23, 42));
        bottom.add(salirButton);

        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(bottom, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void mostrarHistorial() {
        List<Transaction> historial = controller.obtenerHistorial();
        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay transacciones en esta sesion.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Transaction t : historial) {
            sb.append(t.toString()).append("\n");
        }
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Historial de Transacciones",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
