package sistemaorganizacao.dao;

import sistemaorganizacao.model.Usuario;
import java.sql.*;

public class UsuarioDAO {
    public void atualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setInt(3, usuario.getId());
            stmt.executeUpdate();
        }
    }

    public void atualizarSenha(int usuarioId, String novaSenha) throws SQLException {
        String sql = "UPDATE usuarios SET senha = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, novaSenha);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
        }
    }

    public boolean verificarSenha(int usuarioId, String senha) throws SQLException {
        String sql = "SELECT senha FROM usuarios WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String senhaArmazenada = rs.getString("senha");
                return senhaArmazenada.equals(senha);
            }
            return false;
        }
    }
}