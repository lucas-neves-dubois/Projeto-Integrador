package sistemaorganizacao.model;

import java.time.LocalDate;

public class Tarefa {
    private int id;
    private String titulo;
    private String descricao;
    private String status;
    private LocalDate dataEntrega;
    private String prioridade;
    
    public Tarefa(int id, String titulo, String descricao, LocalDate dataEntrega, String prioridade) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataEntrega = dataEntrega;
        this.prioridade = prioridade;
        this.status = "A Fazer";
    }
    
    public Tarefa(String titulo, String descricao, LocalDate dataEntrega, String prioridade) {
        this(0, titulo, descricao, dataEntrega, prioridade);
    }
    
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getStatus() { return status; }
    public LocalDate getDataEntrega() { return dataEntrega; }
    public String getPrioridade() { return prioridade; }
    
    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setStatus(String status) { this.status = status; }
    public void setDataEntrega(LocalDate dataEntrega) { this.dataEntrega = dataEntrega; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
}