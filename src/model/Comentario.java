package model;

import java.time.LocalDateTime;

public class Comentario {

    private int idComentario;
    private int idEvolucao;
    private int idUsuario;
    private String comentario;
    private LocalDateTime dataComentario;
    private String nomeUsuario;

    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public int getIdEvolucao() {
        return idEvolucao;
    }

    public void setIdEvolucao(int idEvolucao) {
        this.idEvolucao = idEvolucao;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataComentario() {
        return dataComentario;
    }

    public String getNomeUsuario() {
    return nomeUsuario;
    }

public void setNomeUsuario(String nomeUsuario) {
    this.nomeUsuario = nomeUsuario;
    }

    public void setDataComentario(LocalDateTime dataComentario) {
        this.dataComentario = dataComentario;
    }
}

    