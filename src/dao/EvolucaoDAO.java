package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Evolucao;

public class EvolucaoDAO {

    // ==========================
    // INSERIR EVOLUÇÃO
    // ==========================
    public boolean inserir(Evolucao evolucao) {

        String sql = "INSERT INTO Evolucoes(idPaciente, idUsuario, descricao) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, evolucao.getIdPaciente());
            stmt.setInt(2, evolucao.getIdUsuario());
            stmt.setString(3, evolucao.getDescricao());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao inserir evolução.");
            e.printStackTrace();

            return false;
        }
    }

    // ==========================
    // LISTAR EVOLUÇÕES
    // ==========================
    public List<Evolucao> listar() {

        List<Evolucao> lista = new ArrayList<>();

        String sql = "SELECT * FROM Evolucoes ORDER BY data DESC";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Evolucao evolucao = new Evolucao();

                evolucao.setIdEvolucao(rs.getInt("idEvolucao"));
                evolucao.setIdPaciente(rs.getInt("idPaciente"));
                evolucao.setIdUsuario(rs.getInt("idUsuario"));
                evolucao.setDescricao(rs.getString("descricao"));

                lista.add(evolucao);

            }

        } catch (SQLException e) {

            System.out.println("Erro ao listar evoluções.");
            e.printStackTrace();

        }

        return lista;
    }

    // ==========================
    // BUSCAR EVOLUÇÃO
    // ==========================
    public Evolucao buscarPorId(int idEvolucao) {

        String sql = "SELECT * FROM Evolucoes WHERE idEvolucao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvolucao);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Evolucao evolucao = new Evolucao();

                evolucao.setIdEvolucao(rs.getInt("idEvolucao"));
                evolucao.setIdPaciente(rs.getInt("idPaciente"));
                evolucao.setIdUsuario(rs.getInt("idUsuario"));
                evolucao.setDescricao(rs.getString("descricao"));

                return evolucao;

            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar evolução.");
            e.printStackTrace();

        }

        return null;
    }

    // ==========================
    // ATUALIZAR EVOLUÇÃO
    // ==========================
    public boolean atualizar(Evolucao evolucao) {

        String sql = "UPDATE Evolucoes SET descricao=? WHERE idEvolucao=?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, evolucao.getDescricao());
            stmt.setInt(2, evolucao.getIdEvolucao());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao atualizar evolução.");
            e.printStackTrace();

            return false;
        }
    }

    // ==========================
    // EXCLUIR EVOLUÇÃO
    // ==========================
    public boolean excluir(int idEvolucao) {

        String sql = "DELETE FROM Evolucoes WHERE idEvolucao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvolucao);

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao excluir evolução.");
            e.printStackTrace();

            return false;
        }
    }

}