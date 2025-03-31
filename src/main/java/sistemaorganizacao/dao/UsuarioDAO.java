package sistemaorganizacao.dao;

import sistemaorganizacao.model.Usuario;
import java.util.HashMap;
import java.util.Map;

public class UsuarioDAO {
    private static Map<Integer, Usuario> usuarios = new HashMap<>();
    private static Map<Integer, String> senhas = new HashMap<>();
    
    static {
        Usuario admin = new Usuario(1, "Administrador", "admin@email.com", "Administrador");
        usuarios.put(1, admin);
        senhas.put(1, "admin123");
        
        Usuario comum = new Usuario(2, "Usuário Comum", "usuario@email.com", "Comum");
        usuarios.put(2, comum);
        senhas.put(2, "123456");
    }
    
    public void atualizarUsuario(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    public void atualizarSenha(int usuarioId, String novaSenha) {
        senhas.put(usuarioId, novaSenha);
    }

    public boolean verificarSenha(int usuarioId, String senha) {
        return senhas.getOrDefault(usuarioId, "").equals(senha);
    }
    
    public Usuario buscarPorEmail(String email) {
        return usuarios.values().stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }
}