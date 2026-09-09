package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Usuario;

public class UsuarioDAO {

    // ==========================
    // LOGIN
    // ==========================

    public boolean login(String email, String senha) {

        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            System.out.println("Erro ao realizar login.");
            e.printStackTrace();

            return false;
        }
    }

    // ==========================
    // CADASTRAR USUÁRIO
    // ==========================

    public boolean cadastrar(Usuario usuario) {

        String sql = "INSERT INTO usuarios(nome, email, senha, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipo());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao cadastrar usuário.");
            e.printStackTrace();

            return false;

        }

    }

}