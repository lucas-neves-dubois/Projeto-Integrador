package sistemaorganizacao.view;

import sistemaorganizacao.model.Usuario;
import sistemaorganizacao.model.Tarefa;
import sistemaorganizacao.dao.TarefaDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class MainFrame extends JFrame {
    private PerfilPanel perfilPanel;
    private KanbanPanel kanbanPanel;
    private CalendarioPanel calendarioPanel;
    private Usuario usuario;
    

    public MainFrame(Usuario usuario) {
        super("Sistema de Organização de Tarefas");
        this.usuario = usuario;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 900);
        setResizable(false);
        setLocationRelativeTo(null);
        
        
        
        configurarTemaEscuro();
        
        List<Tarefa> tarefas = carregarTarefasDoBanco();
        
        perfilPanel = new PerfilPanel(usuario);
        kanbanPanel = new KanbanPanel(tarefas, usuario);
        calendarioPanel = new CalendarioPanel(tarefas);
        
        kanbanPanel.setCalendarioPanel(calendarioPanel);
        kanbanPanel.setPerfilPanel(perfilPanel);
        
        JTabbedPane centerPanel = new JTabbedPane();
        centerPanel.setBackground(new Color(50, 50, 50));
        centerPanel.setForeground(Color.WHITE);
        centerPanel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerPanel.addTab("Kanban", new JScrollPane(kanbanPanel));
        centerPanel.addTab("Calendário", calendarioPanel);
        centerPanel.setUI(new BasicTabbedPaneUI() {
        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setColor(GRAY_BORDER_COLOR);
            g2.drawLine(0, centerPanel.getHeight()-1, 
                       centerPanel.getWidth(), centerPanel.getHeight()-1);
            g2.dispose();
        }
    });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(50, 50, 50));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        perfilPanel.setPreferredSize(new Dimension(280, -1));
        centerPanel.setPreferredSize(new Dimension(getWidth() - 300, getHeight()));
        
        mainPanel.add(perfilPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
    

    private List<Tarefa> carregarTarefasDoBanco() {
        try {
            TarefaDAO tarefaDAO = new TarefaDAO();
            return tarefaDAO.listarTarefas();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tarefas: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return new java.util.ArrayList<>();
        }
    }
    private final Color GRAY_BORDER_COLOR = new Color(100, 100, 100);

    private void configurarTemaEscuro() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            
            UIManager.put("control", new Color(50, 50, 50));
            UIManager.put("nimbusBase", new Color(60, 60, 60));
            UIManager.put("text", Color.WHITE);
            
            UIManager.put("Button.background", new Color(80, 80, 80));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.border", BorderFactory.createLineBorder(new Color(100, 100, 100)));
            
            UIManager.put("TextField.background", new Color(70, 70, 70));
            UIManager.put("TextField.foreground", Color.WHITE);
            UIManager.put("TextField.caretForeground", Color.WHITE);
            
            UIManager.put("TextArea.background", new Color(70, 70, 70));
            UIManager.put("TextArea.foreground", Color.WHITE);
            
            UIManager.put("ComboBox.background", new Color(70, 70, 70));
            UIManager.put("ComboBox.foreground", Color.WHITE);
            
            UIManager.put("Label.foreground", Color.WHITE);
            
            UIManager.put("Panel.background", new Color(50, 50, 50));
            UIManager.put("OptionPane.background", new Color(50, 50, 50));
            
            UIManager.put("TabbedPane.background", new Color(50, 50, 50));
            UIManager.put("TabbedPane.foreground", Color.WHITE);
            UIManager.put("TabbedPane.selected", new Color(0, 120, 215));
            
            UIManager.put("TabbedPane.borderHightlightColor", GRAY_BORDER_COLOR);
            UIManager.put("TabbedPane.darkShadow", new Color(80, 80, 80));
            UIManager.put("TabbedPane.shadow", new Color(90, 90, 90));
            UIManager.put("TabbedPane.contentAreaColor", new Color(50, 50, 50));
            UIManager.put("ScrollPane.border", BorderFactory.createLineBorder(GRAY_BORDER_COLOR));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
}