package sistemaorganizacao;

import sistemaorganizacao.dao.TarefaDAO;
import sistemaorganizacao.model.Tarefa;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Sistema de Organização de Tarefas (Versão Simplificada) ===");
        
        TarefaDAO tarefaDAO = new TarefaDAO();
        
        try {
            System.out.println("\nAdicionando tarefas de exemplo...");
            
            Tarefa t1 = new Tarefa(1, "Reunião de planejamento", 
                    "Discutir objetivos do trimestre", 
                    LocalDate.now().plusDays(2), "Alta");
            
            Tarefa t2 = new Tarefa(2, "Atualizar documentação", 
                    "Revisar manual do usuário", 
                    LocalDate.now().plusDays(5), "Média");
            
            tarefaDAO.adicionarTarefa(t1);
            tarefaDAO.adicionarTarefa(t2);
            
            System.out.println("Tarefas adicionadas com sucesso!");
            
            System.out.println("\nLista de Tarefas:");
            System.out.println("ID | Título               | Status   | Data Entrega | Prioridade");
            System.out.println("------------------------------------------------------------");
            
            for (Tarefa t : tarefaDAO.listarTarefas()) {
                System.out.printf("%2d | %-20s | %-8s | %12s | %-8s%n",
                        t.getId(),
                        t.getTitulo(),
                        t.getStatus(),
                        t.getDataEntrega(),
                        t.getPrioridade());
            }
            
        } catch (Exception e) {
            System.err.println("Erro no sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}