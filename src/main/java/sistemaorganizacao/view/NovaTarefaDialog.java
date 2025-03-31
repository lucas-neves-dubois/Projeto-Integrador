package sistemaorganizacao.view;

import sistemaorganizacao.model.Tarefa;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class NovaTarefaDialog extends JDialog {
    private JTextField tituloField;
    private JTextArea descricaoArea;
    private JComboBox<String> prioridadeCombo;
    private JSpinner dataSpinner;
    private boolean confirmed = false;
    private Tarefa tarefaExistente;
    private static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Color COR_BORDA = new Color(80, 80, 80);
    private static final Color COR_FUNDO = new Color(70, 70, 70);
    private static final Color COR_TEXTO = Color.WHITE;

    public NovaTarefaDialog() {
        setTitle("Nova Tarefa");
        setModal(true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(50, 50, 50));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        descricaoArea = new JTextArea(5, 30);
        descricaoArea.setFont(FONTE_PADRAO);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        descricaoArea.setBackground(COR_FUNDO);
        descricaoArea.setForeground(COR_TEXTO);
        descricaoArea.setCaretColor(COR_TEXTO);
        descricaoArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JScrollPane descricaoScroll = new JScrollPane(descricaoArea);
        descricaoScroll.setBorder(null);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(criarLabel("Título:"), gbc);
        
        gbc.gridy = 1;
        tituloField = criarTextField();
        tituloField.setPreferredSize(new Dimension(tituloField.getPreferredSize().width, 30));
        formPanel.add(tituloField, gbc);
        
        gbc.gridy = 2;
        formPanel.add(criarLabel("Descrição:"), gbc);
        
        gbc.gridy = 3;
        formPanel.add(descricaoScroll, gbc);
        
        gbc.gridy = 4;
        formPanel.add(criarLabel("Prioridade:"), gbc);
        
        gbc.gridy = 5;
        prioridadeCombo = criarComboBox();
        formPanel.add(prioridadeCombo, gbc);
        
        gbc.gridy = 6;
        formPanel.add(criarLabel("Data de Entrega:"), gbc);
        
        gbc.gridy = 7;
        dataSpinner = criarSpinner();
        formPanel.add(dataSpinner, gbc);

        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = criarPainelBotoes();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
    }

    public void setDados(Tarefa tarefa) {
        this.tarefaExistente = tarefa;
        setTitle("Editar Tarefa");
        tituloField.setText(tarefa.getTitulo());
        descricaoArea.setText(tarefa.getDescricao());
        prioridadeCombo.setSelectedItem(tarefa.getPrioridade());
        
        Date date = Date.from(tarefa.getDataEntrega().atStartOfDay(ZoneId.systemDefault()).toInstant());
        dataSpinner.setValue(date);
    }

    private JPanel criarPainelBotoes() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 50));
        
        JButton okButton = criarBotao("Confirmar", new Color(0, 150, 136), e -> confirmar());
        JButton cancelButton = criarBotao("Cancelar", new Color(100, 100, 100), e -> dispose());
        
        panel.add(okButton);
        panel.add(cancelButton);
        return panel;
    }

    private void confirmar() {
        if (tituloField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O título da tarefa é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        confirmed = true;
        dispose();
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(COR_TEXTO);
        label.setFont(FONTE_PADRAO);
        return label;
    }

    private JTextField criarTextField() {
        JTextField field = new JTextField(20);
        field.setFont(FONTE_PADRAO);
        field.setBackground(COR_FUNDO);
        field.setForeground(COR_TEXTO);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setCaretColor(COR_TEXTO);
        return field;
    }

    private JComboBox<String> criarComboBox() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        combo.setFont(FONTE_PADRAO);
        combo.setBackground(COR_FUNDO);
        combo.setForeground(COR_TEXTO);
        combo.setBorder(BorderFactory.createLineBorder(COR_BORDA));
        
        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton();
                button.setIcon(new ArrowIcon());
                button.setContentAreaFilled(false);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setBackground(COR_FUNDO);
                return button;
            }
        });
        
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(COR_FUNDO);
                setForeground(COR_TEXTO);
                if (isSelected) {
                    setBackground(new Color(0, 120, 215));
                }
                return this;
            }
        });
        
        return combo;
    }

    private static class ArrowIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            
            int[] xPoints = {x, x + 10, x + 5};
            int[] yPoints = {y, y, y + 5};
            g2.fillPolygon(xPoints, yPoints, 3);
            g2.dispose();
        }

        @Override public int getIconWidth() { return 10; }
        @Override public int getIconHeight() { return 5; }
    }

    private JSpinner criarSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        
        Component[] comps = editor.getComponents();
        for (Component comp : comps) {
            if (comp instanceof JTextField) {
                JTextField field = (JTextField) comp;
                field.setBackground(COR_FUNDO);
                field.setForeground(COR_TEXTO);
                field.setBorder(null);
                field.setCaretColor(COR_TEXTO);
                field.setFont(FONTE_PADRAO);
            }
        }
        
        return spinner;
    }

    private JButton criarBotao(String texto, Color cor, ActionListener action) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(cor);
        button.setForeground(COR_TEXTO);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor.darker()),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.addActionListener(action);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(cor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(cor);
            }
        });
        
        return button;
    }

    public Tarefa showDialog() {
        setLocationRelativeTo(null);
        setVisible(true);
        
        if (confirmed) {
            Date date = (Date) dataSpinner.getValue();
            LocalDate dataEntrega = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            if (tarefaExistente != null) {
                tarefaExistente.setTitulo(tituloField.getText());
                tarefaExistente.setDescricao(descricaoArea.getText());
                tarefaExistente.setPrioridade((String) prioridadeCombo.getSelectedItem());
                tarefaExistente.setDataEntrega(dataEntrega);
                return tarefaExistente;
            } else {
                return new Tarefa(
                    tituloField.getText(),
                    descricaoArea.getText(),
                    dataEntrega,
                    (String) prioridadeCombo.getSelectedItem()
                );
            }
        }
        return null;
    }
}