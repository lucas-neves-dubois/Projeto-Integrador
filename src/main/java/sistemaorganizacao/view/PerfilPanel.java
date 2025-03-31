package sistemaorganizacao.view;

import sistemaorganizacao.model.Usuario;
import sistemaorganizacao.dao.TarefaDAO;
import sistemaorganizacao.dao.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.Map;

public class PerfilPanel extends JPanel {
    private Usuario usuario;
    private JLabel nomeLabel;
    private JLabel emailLabel;
    private JLabel perfilLabel;
    private JLabel avatarLabel;
    private JPanel statsPanel;
    private JButton editButton;
    private UsuarioDAO usuarioDAO;

    public PerfilPanel(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        this.usuario = usuario;
        this.usuarioDAO = new UsuarioDAO();
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(280, -1));
        setBackground(new Color(50, 50, 50));
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        initComponents();
    }

    private void initComponents() {
        avatarLabel = new JLabel();
        nomeLabel = new JLabel("", SwingConstants.LEFT);
        emailLabel = new JLabel("", SwingConstants.LEFT);
        perfilLabel = new JLabel("", SwingConstants.LEFT);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(50, 50, 50));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(50, 50, 50));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        updateUserInfo();
        
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        avatarPanel.setBackground(new Color(50, 50, 50));
        avatarPanel.add(avatarLabel);
        
        topPanel.add(avatarPanel, BorderLayout.NORTH);
        
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(nomeLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(emailLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(perfilLabel);
        
        topPanel.add(infoPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        statsPanel = createStatsPanel(null);
        add(statsPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(50, 50, 50));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 15, 20, 15));
        
        editButton = new JButton("Editar Perfil");
        styleEditButton(editButton);
        editButton.addActionListener(e -> showEditDialog());
        editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(createActionPanel());
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleEditButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(80, 80, 80));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 100, 140));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(80, 80, 80));
            }
        });
    }

    private void showEditDialog() {
        JDialog editDialog = new JDialog();
        editDialog.setTitle("Editar Perfil");
        editDialog.setModal(true);
        editDialog.setLayout(new BorderLayout());
        editDialog.getContentPane().setBackground(new Color(50, 50, 50));
        editDialog.setSize(400, 500); 
        editDialog.setResizable(false);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30)); 
        formPanel.setBackground(new Color(50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(criarLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField nomeField = criarTextField(usuario.getNome());
        formPanel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(criarLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField emailField = criarTextField(usuario.getEmail());
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(criarLabel("Senha Atual:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JPasswordField senhaAtualField = criarPasswordField();
        formPanel.add(senhaAtualField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(criarLabel("Nova Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JPasswordField novaSenhaField = criarPasswordField();
        formPanel.add(novaSenhaField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(criarLabel("Confirmar Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        JPasswordField confirmarSenhaField = criarPasswordField();
        formPanel.add(confirmarSenhaField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(50, 50, 50));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); 
        
        JButton saveButton = criarBotao("Salvar", new Color(0, 150, 136), e -> {
            String novoNome = nomeField.getText().trim();
            String novoEmail = emailField.getText().trim();
            String senhaAtual = new String(senhaAtualField.getPassword());
            String novaSenha = new String(novaSenhaField.getPassword());
            String confirmarSenha = new String(confirmarSenhaField.getPassword());

            if (novoNome.isEmpty() || novoEmail.isEmpty()) {
                showError(editDialog, "Nome e email não podem estar vazios!");
                return;
            }

            boolean alterarSenha = !novaSenha.isEmpty() || !confirmarSenha.isEmpty();
            
            if (alterarSenha) {
                if (!novaSenha.equals(confirmarSenha)) {
                    showError(editDialog, "As novas senhas não coincidem!");
                    return;
                }
                
                try {
                    if (!usuarioDAO.verificarSenha(usuario.getId(), senhaAtual)) {
                        showError(editDialog, "Senha atual incorreta!");
                        return;
                    }
                } catch (SQLException ex) {
                    showError(editDialog, "Erro ao verificar senha: " + ex.getMessage());
                    return;
                }
            }

            usuario.setNome(novoNome);
            usuario.setEmail(novoEmail);

            try {
                usuarioDAO.atualizarUsuario(usuario);
                
                if (alterarSenha) {
                    usuarioDAO.atualizarSenha(usuario.getId(), novaSenha);
                }
                
                updateUserInfo();
                editDialog.dispose();
                showSuccess(editDialog, "Perfil atualizado com sucesso!");
            } catch (SQLException ex) {
                showError(editDialog, "Erro ao atualizar perfil: " + ex.getMessage());
            }
        });

        JButton cancelButton = criarBotao("Cancelar", new Color(100, 100, 100), e -> editDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        buttonPanel.add(cancelButton);

        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }

    private void showError(Component parent, String message) {
        UIManager.put("OptionPane.background", new Color(60, 60, 60));
        UIManager.put("Panel.background", new Color(60, 60, 60));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        JOptionPane.showMessageDialog(parent, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(Component parent, String message) {
        UIManager.put("OptionPane.background", new Color(60, 60, 60));
        UIManager.put("Panel.background", new Color(60, 60, 60));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        JOptionPane.showMessageDialog(parent, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private JTextField criarTextField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private JPasswordField criarPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private JButton criarBotao(String texto, Color cor, ActionListener action) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(cor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(cor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(cor);
            }
        });
        
        button.addActionListener(action);
        return button;
    }

    private JPanel createStatsPanel(Map<String, Integer> cache) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(60, 60, 60));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(80, 80, 80)),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        
        JLabel titleLabel = new JLabel("ESTATÍSTICAS", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        int aFazer = cache != null ? cache.getOrDefault("A Fazer", 0) : contarTarefasPorStatus("A Fazer");
        int emProgresso = cache != null ? cache.getOrDefault("Em Progresso", 0) : contarTarefasPorStatus("Em Progresso");
        int concluidas = cache != null ? cache.getOrDefault("Concluído", 0) : contarTarefasPorStatus("Concluído");
        
        panel.add(createStatItem("A Fazer", String.valueOf(aFazer), Color.decode("#3498db")));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(createStatItem("Em Progresso", String.valueOf(emProgresso), Color.decode("#f39c12")));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(createStatItem("Concluídas", String.valueOf(concluidas), Color.decode("#2ecc71")));
        
        return panel;
    }

    private JPanel createStatItem(String label, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 60));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelLabel.setForeground(new Color(220, 220, 220));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.RIGHT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(color);
        
        panel.add(labelLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.EAST);
        
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        JButton logoutButton = new JButton("Sair");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(200, 40));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(100, 100, 100));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(200, 60, 60));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(100, 100, 100));
            }
        });

        logoutButton.addActionListener(e -> confirmLogout());
        
        panel.add(logoutButton);
        
        return panel;
    }

    private void confirmLogout() {
        UIManager.put("OptionPane.background", new Color(60, 60, 60));
        UIManager.put("Panel.background", new Color(60, 60, 60));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", new Color(80, 80, 80));
        UIManager.put("Button.foreground", Color.WHITE);
        
        int confirm = JOptionPane.showOptionDialog(
            this,
            "Deseja realmente sair do sistema?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[]{"Sim", "Não"},
            "Não"
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void updateUserInfo() {
        nomeLabel.setText(usuario.getNome());
        emailLabel.setText(usuario.getEmail());
        perfilLabel.setText("Perfil: " + usuario.getPerfil());
        
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        nomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        emailLabel.setFont(font);
        perfilLabel.setFont(font.deriveFont(Font.PLAIN, 12));
        
        nomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        perfilLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        nomeLabel.setForeground(Color.WHITE);
        emailLabel.setForeground(Color.WHITE);
        perfilLabel.setForeground(Color.WHITE);
        
        updateAvatar();
    }

    private void updateAvatar() {
        int size = 120;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        String initial = usuario.getNome().substring(0, 1).toUpperCase();
        
        Color bgColor = new Color(70, 70, 70);
        
        g2d.setColor(bgColor);
        g2d.fillOval(0, 0, size, size);
        
        g2d.setColor(Color.WHITE);
        Font font = new Font("Arial", Font.BOLD, 48);
        g2d.setFont(font);
        
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth(initial)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        
        g2d.drawString(initial, x, y);
        g2d.dispose();
        
        avatarLabel.setIcon(new ImageIcon(image));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private Color getPerfilColor(String perfil) {
        return Color.WHITE;
    }

    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        this.usuario = usuario;
        updateUserInfo();
    }

    public void atualizarEstatisticas(Map<String, Integer> cache) {
        SwingUtilities.invokeLater(() -> {
            remove(statsPanel);
            statsPanel = createStatsPanel(cache);
            add(statsPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });
    }

    private int contarTarefasPorStatus(String status) {
        try {
            return new TarefaDAO().contarTarefasPorStatus(status);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}