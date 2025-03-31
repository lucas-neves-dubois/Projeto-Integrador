package sistemaorganizacao;

import sistemaorganizacao.view.LoginPanel;
import sistemaorganizacao.view.MainFrame;
import sistemaorganizacao.model.Usuario;
import sistemaorganizacao.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        configurarLookAndFeel();
        
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Sistema de Organização");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(1300, 900);
            loginFrame.setResizable(false);
            loginFrame.setLocationRelativeTo(null);

            LoginPanel loginPanel = new LoginPanel();

            loginPanel.addLoginListener(e -> {
                String email = loginPanel.getEmail();
                String senha = loginPanel.getPassword();

                Usuario usuario = validarLogin(email, senha);
                if (usuario != null) {
                    loginFrame.dispose();
                    abrirTelaPrincipal(usuario);
                } else {
                    mostrarErroLogin(loginFrame);
                }
            });

            loginFrame.add(loginPanel);
            loginFrame.setVisible(true);
        });
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            
            // Configurações de tema escuro
            UIManager.put("Panel.background", new Color(50, 50, 50));
            UIManager.put("OptionPane.background", new Color(50, 50, 50));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Usuario validarLogin(String email, String senha) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        
        if (usuario != null && usuarioDAO.verificarSenha(usuario.getId(), senha)) {
            return usuario;
        }
        return null;
    }

    private static void mostrarErroLogin(JFrame parent) {
        UIManager.put("OptionPane.background", new Color(60, 60, 60));
        UIManager.put("Panel.background", new Color(60, 60, 60));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        
        JOptionPane.showMessageDialog(parent, 
            "Credenciais inválidas. Por favor, tente novamente.", 
            "Erro de Login", 
            JOptionPane.ERROR_MESSAGE);
    }

    private static void abrirTelaPrincipal(Usuario usuario) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(usuario);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}