package sistemaorganizacao.view;

import sistemaorganizacao.dao.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private ActionListener loginListener;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(36, 36, 36));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(45, 45, 45));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80)), 
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel titleLabel = new JLabel("Login no Sistema");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(240, 240, 240));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 5, 0);

        JLabel emailLabel = new JLabel("E-mail:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(220, 220, 220));
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        styleTextField(emailField);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        contentPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(220, 220, 220));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        contentPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        contentPanel.add(passwordField, gbc);

        loginButton = new JButton("Entrar");
        styleButton(loginButton, new Color(0, 150, 136));
        loginButton.addActionListener(e -> {
            if (loginListener != null) {
                loginListener.actionPerformed(e);
            }
        });

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; 
        gbc.insets = new Insets(20, 0, 0, 0);
        contentPanel.add(loginButton, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        add(contentPanel, gbc);
    }

    private void styleTextField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(new Color(240, 240, 240));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        if (field instanceof JTextField) {
            ((JTextField) field).setCaretColor(Color.WHITE);
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        this.loginListener = listener;
    }
}