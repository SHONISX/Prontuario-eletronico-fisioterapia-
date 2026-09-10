package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Paciente;

public class PacienteDAO {

    // ==========================
    // INSERIR PACIENTE
    // ==========================
    public boolean inserir(Paciente paciente) {

        String sql = "INSERT INTO pacientes(nome, idade, diagnostico, queixaPrincipal, objetivos, condutas) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNome());
            stmt.setInt(2, paciente.getIdade());
            stmt.setString(3, paciente.getDiagnostico());
            stmt.setString(4, paciente.getQueixaPrincipal());
            stmt.setString(5, paciente.getObjetivos());
            stmt.setString(6, paciente.getCondutas());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao inserir paciente.");
            e.printStackTrace();

            return false;
        }
    }

    // ==========================
    // LISTAR PACIENTES
    // ==========================
    public List<Paciente> listar() {

        List<Paciente> lista = new ArrayList<>();

        String sql = "SELECT * FROM pacientes ORDER BY nome";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Paciente paciente = new Paciente();

                paciente.setIdPaciente(rs.getInt("idPaciente"));
                paciente.setNome(rs.getString("nome"));
                paciente.setIdade(rs.getInt("idade"));
                paciente.setDiagnostico(rs.getString("diagnostico"));
                paciente.setQueixaPrincipal(rs.getString("queixaPrincipal"));
                paciente.setObjetivos(rs.getString("objetivos"));
                paciente.setCondutas(rs.getString("condutas"));

                lista.add(paciente);
            }

        } catch (SQLException e) {

            System.out.println("Erro ao listar pacientes.");
            e.printStackTrace();

        }

        return lista;
    }

    // ==========================
    // BUSCAR PACIENTE
    // ==========================
    public Paciente buscarPorId(int idPaciente) {

        String sql = "SELECT * FROM pacientes WHERE idPaciente = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Paciente paciente = new Paciente();

                paciente.setIdPaciente(rs.getInt("idPaciente"));
                paciente.setNome(rs.getString("nome"));
                paciente.setIdade(rs.getInt("idade"));
                paciente.setDiagnostico(rs.getString("diagnostico"));
                paciente.setQueixaPrincipal(rs.getString("queixaPrincipal"));
                paciente.setObjetivos(rs.getString("objetivos"));
                paciente.setCondutas(rs.getString("condutas"));

                return paciente;

            }

        } catch (SQLException e) {

            System.out.println("Erro ao buscar paciente.");
            e.printStackTrace();

        }

        return null;
    }

    // ==========================
    // ATUALIZAR PACIENTE
    // ==========================
    public boolean atualizar(Paciente paciente) {

        String sql = "UPDATE pacientes SET nome=?, idade=?, diagnostico=?, queixaPrincipal=?, objetivos=?, condutas=? WHERE idPaciente=?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNome());
            stmt.setInt(2, paciente.getIdade());
            stmt.setString(3, paciente.getDiagnostico());
            stmt.setString(4, paciente.getQueixaPrincipal());
            stmt.setString(5, paciente.getObjetivos());
            stmt.setString(6, paciente.getCondutas());
            stmt.setInt(7, paciente.getIdPaciente());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao atualizar paciente.");
            e.printStackTrace();

            return false;
        }
    }

    // ==========================
    // EXCLUIR PACIENTE
    // ==========================
    public boolean excluir(int idPaciente) {

        String sql = "DELETE FROM pacientes WHERE idPaciente = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Erro ao excluir paciente.");
            e.printStackTrace();

            return false;
        }
    }

}