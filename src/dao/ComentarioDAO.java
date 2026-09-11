package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Comentario;

public class ComentarioDAO {

    // ==========================
    // INSERIR COMENTÁRIO
    // ==========================
    public boolean inserir(Comentario comentario) {

        String sql = "INSERT INTO comentarios(idEvolucao, idUsuario, comentario) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, comentario.getIdEvolucao());
            stmt.setInt(2, comentario.getIdUsuario());
            stmt.setString(3, comentario.getComentario());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao inserir comentário.");
            e.printStackTrace();

            return false;

        }

    }

    // ==========================
// LISTAR COMENTÁRIOS DA EVOLUÇÃO
// ==========================
public List<Comentario> listarPorEvolucao(int idEvolucao) {

    List<Comentario> lista = new ArrayList<>();

    String sql =
        "SELECT c.*, u.nome AS nomeUsuario " +
        "FROM comentarios c " +
        "INNER JOIN usuarios u ON c.idUsuario = u.idUsuario " +
        "WHERE c.idEvolucao = ? " +
        "ORDER BY c.dataComentario ASC";

    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idEvolucao);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Comentario comentario = new Comentario();

            comentario.setIdComentario(
                rs.getInt("idComentario")
            );

            comentario.setIdEvolucao(
                rs.getInt("idEvolucao")
            );

            comentario.setIdUsuario(
                rs.getInt("idUsuario")
            );

            comentario.setComentario(
                rs.getString("comentario")
            );

            comentario.setNomeUsuario(
                rs.getString("nomeUsuario")
            );

            if (rs.getTimestamp("dataComentario") != null) {
                comentario.setDataComentario(
                    rs.getTimestamp("dataComentario")
                .toLocalDateTime()
                );
            }

            lista.add(comentario);
        }

    } catch (SQLException e) {

        System.out.println("Erro ao listar comentários.");
        e.printStackTrace();
    }

    return lista;
}


    // ==========================
    // BUSCAR COMENTÁRIO
    // ==========================
    public Comentario buscarPorId(int idComentario) {

        String sql = "SELECT * FROM comentarios WHERE idComentario = ?";

        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idComentario);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Comentario comentario = new Comentario();

                comentario.setIdComentario(rs.getInt("idComentario"));
                comentario.setIdEvolucao(rs.getInt("idEvolucao"));
                comentario.setIdUsuario(rs.getInt("idUsuario"));
                comentario.setComentario(rs.getString("comentario"));

                return comentario;

            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar comentário.");
            e.printStackTrace();

        }

        return null;

    }

    // ==========================
    // ATUALIZAR COMENTÁRIO
    // ==========================
    public boolean atualizar(Comentario comentario) {

        String sql = "UPDATE comentarios SET comentario=? WHERE idComentario=?";

        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, comentario.getComentario());
            stmt.setInt(2, comentario.getIdComentario());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao atualizar comentário.");
            e.printStackTrace();

            return false;

        }

    }

    // ==========================
    // EXCLUIR COMENTÁRIO
    // ==========================
    public boolean excluir(int idComentario) {

        String sql = "DELETE FROM comentarios WHERE idComentario = ?";

        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idComentario);

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao excluir comentário.");
            e.printStackTrace();

            return false;

        }

    }

}