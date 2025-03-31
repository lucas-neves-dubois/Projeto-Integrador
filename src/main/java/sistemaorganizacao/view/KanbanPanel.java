package sistemaorganizacao.view;

import sistemaorganizacao.model.Tarefa;
import sistemaorganizacao.dao.TarefaDAO;
import sistemaorganizacao.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class KanbanPanel extends JPanel {
    private List<Tarefa> tarefas;
    private CalendarioPanel calendarioPanel;
    private PerfilPanel perfilPanel;
    private JPanel aFazerPanel, emProgressoPanel, concluidoPanel;
    private final Color backgroundColor = new Color(30, 30, 30);
    private final Color panelColor = new Color(45, 45, 45);
    private final Color taskColor = new Color(60, 60, 60);
    private Usuario usuario;
    private Map<String, Integer> statusCountCache = new HashMap<>();
    private Timer statsUpdateTimer;
    private TarefaDAO tarefaDAO;
    private final Object statsLock = new Object();

    public KanbanPanel(List<Tarefa> tarefas, Usuario usuario) {
        this.tarefaDAO = new TarefaDAO();
        this.tarefas = new ArrayList<>(tarefas);
        this.usuario = usuario;
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        atualizarCacheEstatisticas();
        
        statsUpdateTimer = new Timer(2000, e -> {
            if(!isVisible()) return;
            atualizarCacheEstatisticas();
        });
        statsUpdateTimer.setRepeats(true);
        statsUpdateTimer.start();

        JPanel columnsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        columnsPanel.setBackground(backgroundColor);

        aFazerPanel = createColumnPanel("A Fazer", new Color(52, 152, 219));
        emProgressoPanel = createColumnPanel("Em Progresso", new Color(241, 196, 15));
        concluidoPanel = createColumnPanel("Concluído", new Color(46, 204, 113));

        columnsPanel.add(aFazerPanel);
        columnsPanel.add(emProgressoPanel);
        columnsPanel.add(concluidoPanel);

        add(columnsPanel, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        atualizarColunas();
    }

    private void atualizarCacheEstatisticas() {
        if(!isVisible()) return;
        
        synchronized(statsLock) {
            Map<String, Integer> counts = new HashMap<>();
            counts.put("A Fazer", 0);
            counts.put("Em Progresso", 0);
            counts.put("Concluído", 0);
            
            for(Tarefa t : tarefas) {
                counts.computeIfPresent(t.getStatus(), (k,v) -> v+1);
            }
            
            this.statusCountCache = counts;
            
            if(perfilPanel != null) {
                perfilPanel.atualizarEstatisticas(counts);
            }
        }
    }

    public void atualizarEstatisticasLocal(String statusAntigo, String statusNovo) {
        SwingUtilities.invokeLater(() -> {
            synchronized(statsLock) {
                if (statusAntigo != null) {
                    statusCountCache.computeIfPresent(statusAntigo, (k, v) -> v > 0 ? v - 1 : 0);
                }
                if (statusNovo != null) {
                    statusCountCache.compute(statusNovo, (k, v) -> v == null ? 1 : v + 1);
                }
                
                if (perfilPanel != null) {
                    perfilPanel.atualizarEstatisticas(statusCountCache);
                }
            }
        });
    }

    public void setCalendarioPanel(CalendarioPanel calendarioPanel) {
        this.calendarioPanel = calendarioPanel;
    }

    public void setPerfilPanel(PerfilPanel perfilPanel) {
        this.perfilPanel = perfilPanel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(backgroundColor);
        panel.add(createAddButton());
        return panel;
    }

    private JButton createAddButton() {
        JButton button = new JButton("+ Nova Tarefa");
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 80, 160)),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 140, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215));
            }
        });

        button.addActionListener(e -> showAddTaskDialog());
        return button;
    }

    private JPanel createColumnPanel(String title, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.setBackground(panelColor);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(color.darker().darker());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JLabel countLabel = new JLabel("0", SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        countLabel.setForeground(Color.WHITE);
        headerPanel.add(countLabel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        tasksPanel.setBackground(panelColor);

        JScrollPane scrollPane = new JScrollPane(tasksPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(panelColor);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTaskPanel(Tarefa task) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 90)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(taskColor);
        panel.addMouseListener(new TaskMouseAdapter(task));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(task.getTitulo());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(240, 240, 240));

        JPanel priorityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        priorityPanel.setOpaque(false);
        
        JLabel priorityText = new JLabel(task.getPrioridade());
        priorityText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        priorityText.setForeground(getPriorityColor(task.getPrioridade()));
        
        priorityPanel.add(priorityText);
        priorityPanel.add(new JLabel(getPriorityIcon(task.getPrioridade())));
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(priorityPanel, BorderLayout.EAST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel dateLabel = new JLabel(task.getDataEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(200, 200, 200));

        if (!task.getDescricao().isEmpty()) {
            JTextArea descLabel = new JTextArea(task.getDescricao());
            descLabel.setEditable(false);
            descLabel.setLineWrap(true);
            descLabel.setWrapStyleWord(true);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            descLabel.setForeground(new Color(220, 220, 220));
            descLabel.setBackground(taskColor);
            descLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            infoPanel.add(descLabel);
        }

        infoPanel.add(dateLabel);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        JPopupMenu contextMenu = createTaskPopupMenu(task);
        panel.setComponentPopupMenu(contextMenu);
        
        return panel;
    }

    private JPopupMenu createTaskPopupMenu(Tarefa task) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(new Color(60, 60, 60));
        menu.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        
        JMenuItem moveToInProgress = new JMenuItem("Mover para Em Progresso");
        styleMenuItem(moveToInProgress, new Color(241, 196, 15));
        moveToInProgress.addActionListener(e -> moveToStatus(task, "Em Progresso"));
        
        JMenuItem moveToConcluded = new JMenuItem("Mover para Concluído");
        styleMenuItem(moveToConcluded, new Color(46, 204, 113));
        moveToConcluded.addActionListener(e -> moveToStatus(task, "Concluído"));
        
        JMenuItem moveToAFazer = new JMenuItem("Mover para A Fazer");
        styleMenuItem(moveToAFazer, new Color(52, 152, 219));
        moveToAFazer.addActionListener(e -> moveToStatus(task, "A Fazer"));
        
        menu.add(moveToInProgress);
        menu.add(moveToConcluded);
        menu.add(moveToAFazer);
        
        if ("Administrador".equalsIgnoreCase(usuario.getPerfil())) {
            menu.addSeparator();
            
            JMenuItem editTask = new JMenuItem("Editar Tarefa");
            styleMenuItem(editTask, new Color(155, 89, 182));
            editTask.addActionListener(e -> editarTarefa(task));
            menu.add(editTask);
            
            JMenuItem deleteTask = new JMenuItem("Excluir Tarefa");
            styleMenuItem(deleteTask, new Color(231, 76, 60));
            deleteTask.addActionListener(e -> excluirTarefa(task));
            menu.add(deleteTask);
        }
        
        return menu;
    }

    private void styleMenuItem(JMenuItem item, Color color) {
        item.setBackground(new Color(60, 60, 60));
        item.setForeground(Color.WHITE);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(60, 60, 60));
            }
        });
    }

    private Color getPriorityColor(String prioridade) {
        switch (prioridade) {
            case "Alta": return new Color(231, 76, 60);
            case "Média": return new Color(241, 196, 15);
            default: return new Color(46, 204, 113);
        }
    }

    private Icon getPriorityIcon(String prioridade) {
        Color color = getPriorityColor(prioridade);
        int size = 12;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillOval(0, 0, size, size);
        g2d.dispose();
        return new ImageIcon(image);
    }

    private void atualizarColunas() {
        atualizarColuna(aFazerPanel, "A Fazer");
        atualizarColuna(emProgressoPanel, "Em Progresso");
        atualizarColuna(concluidoPanel, "Concluído");
    }

    private void atualizarColuna(JPanel panel, String status) {
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(1);
        JPanel tasksPanel = (JPanel) scrollPane.getViewport().getView();
        tasksPanel.removeAll();

        int taskCount = 0;
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getStatus().equals(status)) {
                tasksPanel.add(createTaskPanel(tarefa));
                tasksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                taskCount++;
            }
        }

        if (taskCount == 0) {
            JLabel emptyLabel = new JLabel("Nenhuma tarefa", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            emptyLabel.setForeground(new Color(150, 150, 150));
            tasksPanel.add(emptyLabel);
        }

        ((JLabel) ((JPanel) panel.getComponent(0)).getComponent(1)).setText(String.valueOf(taskCount));
        tasksPanel.revalidate();
        tasksPanel.repaint();
    }

    private void showAddTaskDialog() {
        NovaTarefaDialog dialog = new NovaTarefaDialog();
        dialog.setPreferredSize(new Dimension(500, 400));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setSize(500, 600);
        dialog.setResizable(false);
        Tarefa novaTarefa = dialog.showDialog();

        if (novaTarefa != null) {
            tarefaDAO.adicionarTarefa(novaTarefa);
            tarefas = tarefaDAO.listarTarefas();
            
            atualizarEstatisticasLocal(null, novaTarefa.getStatus());
            atualizarColunas();
            
            if (calendarioPanel != null) {
                calendarioPanel.atualizarTarefas(tarefas);
            }
        }
    }

    private void moveToStatus(Tarefa task, String status) {
        String statusAnterior = task.getStatus();
        tarefaDAO.mudarStatusTarefa(task.getId(), status);
        task.setStatus(status);
        
        atualizarEstatisticasLocal(statusAnterior, status);
        atualizarColunas();
        
        if (calendarioPanel != null) {
            calendarioPanel.atualizarTarefas(tarefas);
        }
    }

    private void editarTarefa(Tarefa task) {
        NovaTarefaDialog dialog = new NovaTarefaDialog();
        dialog.setPreferredSize(new Dimension(500, 400));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setDados(task);
        dialog.setSize(500, 600);
        dialog.setResizable(false);

        Tarefa tarefaEditada = dialog.showDialog();

        if (tarefaEditada != null) {
            tarefaDAO.atualizarTarefa(tarefaEditada);
            tarefas = tarefaDAO.listarTarefas();
            
            atualizarColunas();
            
            if (calendarioPanel != null) {
                calendarioPanel.atualizarTarefas(tarefas);
            }
        }
    }

    private void excluirTarefa(Tarefa task) {
        UIManager.put("OptionPane.background", new Color(60, 60, 60));
        UIManager.put("Panel.background", new Color(60, 60, 60));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", new Color(80, 80, 80));
        UIManager.put("Button.foreground", Color.WHITE);
        
        Object[] options = {"Sim", "Não"};
        int confirm = JOptionPane.showOptionDialog(this, 
            "Deseja realmente excluir esta tarefa?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String statusAnterior = task.getStatus();
            tarefaDAO.excluirTarefa(task.getId());
            tarefas = tarefaDAO.listarTarefas();
            
            atualizarEstatisticasLocal(statusAnterior, null);
            atualizarColunas();
            
            if (calendarioPanel != null) {
                calendarioPanel.atualizarTarefas(tarefas);
            }
        }
        
        UIManager.put("OptionPane.background", UIManager.get("control"));
        UIManager.put("Panel.background", UIManager.get("control"));
        UIManager.put("OptionPane.messageForeground", UIManager.get("text"));
    }

    private class TaskMouseAdapter extends MouseAdapter {
        private final Tarefa task;
        public TaskMouseAdapter(Tarefa task) { this.task = task; }

        @Override
        public void mouseEntered(MouseEvent e) {
            ((JPanel) e.getSource()).setBackground(new Color(80, 80, 80));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((JPanel) e.getSource()).setBackground(taskColor);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (statsUpdateTimer != null) {
            statsUpdateTimer.stop();
        }
        super.finalize();
    }
}