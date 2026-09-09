package model;

import java.time.LocalDateTime;

public class Evolucao {

    private int idEvolucao;
    private int idPaciente;
    private int idUsuario;
    private String descricao;
    private LocalDateTime data;

    // ==========================
    // CONSTRUTORES
    // ==========================

    public Evolucao() {
    }

    public Evolucao(int idEvolucao, int idPaciente, int idUsuario,
                    String descricao, LocalDateTime data) {

        this.idEvolucao = idEvolucao;
        this.idPaciente = idPaciente;
        this.idUsuario = idUsuario;
        this.descricao = descricao;
        this.data = data;
    }

    // ==========================
    // GETTERS E SETTERS
    // ==========================

    public int getIdEvolucao() {
        return idEvolucao;
    }

    public void setIdEvolucao(int idEvolucao) {
        this.idEvolucao = idEvolucao;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    // ==========================
    // TO STRING
    // ==========================

    @Override
    public String toString() {

        return "Evolucao{" +
                "idEvolucao=" + idEvolucao +
                ", idPaciente=" + idPaciente +
                ", idUsuario=" + idUsuario +
                ", descricao='" + descricao + '\'' +
                ", data=" + data +
                '}';
    }
}