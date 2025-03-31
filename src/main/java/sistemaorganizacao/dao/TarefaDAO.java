package sistemaorganizacao.dao;

import sistemaorganizacao.model.Tarefa;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {
    private List<Tarefa> tarefas = new ArrayList<>();
    private int proximoId = 1;
    
    public void adicionarTarefa(Tarefa tarefa) {
        tarefa.setId(proximoId++);
        tarefas.add(tarefa);
    }
    
    public List<Tarefa> listarTarefas() {
        return new ArrayList<>(tarefas);
    }
}