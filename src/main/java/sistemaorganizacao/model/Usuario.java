package sistemaorganizacao.model;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String perfil;
    
    public Usuario(int id, String nome, String email, String perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getPerfil() { return perfil; }
    
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
}