package sistemaorganizacao.dao;

import sistemaorganizacao.model.Tarefa;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TarefaDAO {
    private static Map<Integer, Tarefa> tarefas = new HashMap<>();
    private static int proximoId = 1;
    
    public void adicionarTarefa(Tarefa tarefa) {
        if (tarefa.getId() == 0) {
            tarefa.setId(proximoId++);
        }
        tarefas.put(tarefa.getId(), tarefa);
    }
    
    public List<Tarefa> listarTarefas() {
        return new ArrayList<>(tarefas.values());
    }
    
    public void mudarStatusTarefa(int id, String novoStatus) {
        Tarefa tarefa = tarefas.get(id);
        if (tarefa != null) {
            tarefa.setStatus(novoStatus);
        }
    }

    public void atualizarTarefa(Tarefa tarefa) {
        if (tarefas.containsKey(tarefa.getId())) {
            tarefas.put(tarefa.getId(), tarefa);
        }
    }

    public void excluirTarefa(int id) {
        tarefas.remove(id);
    }

    public int contarTarefasPorStatus(String status) {
        return (int) tarefas.values().stream()
            .filter(t -> t.getStatus().equals(status))
            .count();
    }
}