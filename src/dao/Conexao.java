package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private Conexao() {
        // Impede instanciar esta classe
    }

    public static Connection conectar() {

        try {

            // ==========================================
            // VERIFICA SE ESTAMOS NA HOSPEDAGEM
            // ==========================================

            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String database = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String password = System.getenv("MYSQLPASSWORD");

            String url;

            // ==========================================
            // BANCO ONLINE
            // ==========================================
            if (host != null && !host.isEmpty()) {

                url = "jdbc:mysql://"
                        + host + ":"
                        + port + "/"
                        + database
                        + "?useSSL=false"
                        + "&serverTimezone=America/Sao_Paulo"
                        + "&allowPublicKeyRetrieval=true";

                System.out.println("Conectando ao banco de dados ONLINE...");

            } else {

                // ======================================
                // BANCO LOCAL
                // ======================================

                url = "jdbc:mysql://localhost:3306/bdclinica_fisio"
                        + "?useSSL=false"
                        + "&serverTimezone=America/Sao_Paulo"
                        + "&allowPublicKeyRetrieval=true";

                user = System.getenv("DB_USER");
                password = System.getenv("DB_PASSWORD");

                if (user == null || user.isEmpty()) {
                    user = "root";
                }

                if (password == null || password.isEmpty()) {
                    System.out.println(
                            "ERRO: variável DB_PASSWORD não configurada."
                    );
                    return null;
                }

                System.out.println("Conectando ao banco de dados LOCAL...");
            }

            return DriverManager.getConnection(
                    url,
                    user,
                    password
            );

        } catch (SQLException e) {

            System.out.println(
                    "Erro ao conectar ao banco de dados."
            );

            e.printStackTrace();

            return null;
        }
    }
}