package sistemaorganizacao.view;

import sistemaorganizacao.model.Tarefa;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class CalendarioPanel extends JPanel {
    private Map<LocalDate, List<Tarefa>> tarefasPorData = new HashMap<>();
    private LocalDate currentDate = LocalDate.now();
    private Color backgroundColor = new Color(30, 30, 30);
    private Color headerColor = new Color(20, 20, 20);
    private Color dayColor = new Color(45, 45, 45);
    private Color currentDayColor = new Color(0, 100, 150);
    private Color weekendColor = new Color(60, 60, 80);

    public CalendarioPanel(List<Tarefa> tarefas) {
        setLayout(new BorderLayout(5, 5));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        atualizarTarefas(tarefas);
    }

    public void atualizarTarefas(List<Tarefa> tarefas) {
        tarefasPorData.clear();
        for (Tarefa tarefa : tarefas) {
            LocalDate data = tarefa.getDataEntrega();
            tarefasPorData.computeIfAbsent(data, k -> new ArrayList<>()).add(tarefa);
        }
        atualizarUI();
    }

    private void atualizarUI() {
        removeAll();
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(headerColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setOpaque(false);
        
        navPanel.add(criarBotaoNavegacao("←", e -> {
            currentDate = currentDate.minusMonths(1);
            atualizarUI();
        }));
        
        navPanel.add(criarBotaoNavegacao("Hoje", e -> {
            currentDate = LocalDate.now();
            atualizarUI();
        }));
        
        navPanel.add(criarBotaoNavegacao("→", e -> {
            currentDate = currentDate.plusMonths(1);
            atualizarUI();
        }));
        
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        JLabel monthLabel = new JLabel(currentDate.format(monthFormatter), SwingConstants.CENTER);
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        monthLabel.setForeground(new Color(240, 240, 240));
        
        headerPanel.add(navPanel, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        
        JPanel weekdaysPanel = criarWeekdaysPanel();
        JPanel calendarPanel = criarCalendarPanel();
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(backgroundColor);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        contentPanel.add(weekdaysPanel, BorderLayout.NORTH);
        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private JPanel criarWeekdaysPanel() {
        String[] weekdays = {"DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SÁB"};
        JPanel panel = new JPanel(new GridLayout(1, 7, 2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        panel.setOpaque(false);
        
        for (String day : weekdays) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setForeground(new Color(220, 220, 220));
            panel.add(label);
        }
        return panel;
    }

    private JPanel criarCalendarPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 7, 2, 2));
        panel.setOpaque(false);
        YearMonth yearMonth = YearMonth.from(currentDate);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
        
        for (int i = 0; i < firstDayOfWeek; i++) {
            panel.add(new JLabel(""));
        }
        
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
            panel.add(criarDiaPanel(date));
        }
        return panel;
    }

    private JButton criarBotaoNavegacao(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.addActionListener(action);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 60, 60));
            }
        });
        
        return button;
    }

    private JPanel criarDiaPanel(LocalDate date) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        
        if (date.equals(LocalDate.now())) {
            panel.setBackground(currentDayColor);
        } else if (date.getDayOfWeek().getValue() % 7 == 0 || date.getDayOfWeek().getValue() % 7 == 6) {
            panel.setBackground(weekendColor);
        } else {
            panel.setBackground(dayColor);
        }
        
        JLabel dayLabel = new JLabel(String.valueOf(date.getDayOfMonth()), SwingConstants.CENTER);
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dayLabel.setForeground(new Color(240, 240, 240));
        dayLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(dayLabel, BorderLayout.NORTH);
        
        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        tasksPanel.setOpaque(false);
        tasksPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        
        if (tarefasPorData.containsKey(date)) {
            List<Tarefa> tarefasDoDia = tarefasPorData.get(date);
            int maxTasksToShow = 3;
            
            for (int i = 0; i < Math.min(tarefasDoDia.size(), maxTasksToShow); i++) {
                Tarefa tarefa = tarefasDoDia.get(i);
                JLabel taskLabel = new JLabel("• " + tarefa.getTitulo());
                taskLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                taskLabel.setForeground(getStatusColor(tarefa.getStatus()).brighter());
                taskLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
                tasksPanel.add(taskLabel);
            }
            
            if (tarefasDoDia.size() > maxTasksToShow) {
                JLabel moreLabel = new JLabel("+ " + (tarefasDoDia.size() - maxTasksToShow) + " mais...");
                moreLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                moreLabel.setForeground(new Color(200, 200, 200));
                tasksPanel.add(moreLabel);
            }
        }
        
        panel.add(tasksPanel, BorderLayout.CENTER);
        return panel;
    }

    private Color getStatusColor(String status) {
        switch (status) {
            case "A Fazer": return new Color(100, 200, 255);
            case "Em Progresso": return new Color(255, 200, 100);
            case "Concluído": return new Color(100, 255, 150);
            default: return new Color(220, 220, 220);
        }
    }
}