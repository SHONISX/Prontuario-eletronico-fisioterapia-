import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dao.ComentarioDAO;
import dao.EvolucaoDAO;
import dao.PacienteDAO;
import dao.UsuarioDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import model.Comentario;
import model.Evolucao;
import model.Paciente;
import model.Usuario;

public class Server {

    static UsuarioDAO usuarioDAO = new UsuarioDAO();
    static PacienteDAO pacienteDAO = new PacienteDAO();
    static EvolucaoDAO evolucaoDAO = new EvolucaoDAO();
    static ComentarioDAO comentarioDAO = new ComentarioDAO();

    public static void main(String[] args) throws Exception {

        // =========================================
        // PORTA LOCAL OU PORTA DA HOSPEDAGEM
        // =========================================
        String portEnv = System.getenv("PORT");

        int port;

        if (portEnv != null && !portEnv.isEmpty()) {
            port = Integer.parseInt(portEnv);
        } else {
            port = 8080;
        }

        // 0.0.0.0 permite acesso externo quando hospedado
        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port),
                0
        );

        // =========================
        // LOGIN
        // =========================
        server.createContext("/login", exchange -> {

            // Responde ao preflight CORS do navegador
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {

            exchange.getResponseHeaders().set(
            "Access-Control-Allow-Origin", "*"
            );

            exchange.getResponseHeaders().set(
            "Access-Control-Allow-Methods", "GET, POST, OPTIONS"
            );

            exchange.getResponseHeaders().set(
            "Access-Control-Allow-Headers", "Content-Type"
            );

            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
            }

            try {

                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

                    String body = new String(
                            exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8
                    );

                    String email = extract(body, "email");
                    String senha = extract(body, "senha");

                    System.out.println("Tentativa de login: " + email);

                    Usuario usuario = usuarioDAO.buscarUsuario(email, senha);

            if (usuario != null) {

            String json =
                    "{"
                    + "\"status\":\"OK\","
                    + "\"id\":" + usuario.getIdUsuario() + ","
                    + "\"nome\":\"" + escapeJson(usuario.getNome()) + "\","
                    + "\"tipo\":\"" + escapeJson(usuario.getTipo()) + "\""
                    + "}";

            sendResponse(exchange, json);

            } else {

    sendResponse(exchange, "{\"status\":\"ERRO\"}");
}

            return;
                }

                sendResponse(exchange, "INVALID_METHOD");

            } catch (Exception e) {

                e.printStackTrace();

                try {
                    sendResponse(exchange, "ERRO_INTERNO");
                } catch (Exception ignored) {
                }
            }
        });

                // =========================
                // USUÁRIO / COLABORADOR
                // =========================
            server.createContext("/usuario", exchange -> {

             // CORS / preflight
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin", "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods", "GET, POST, OPTIONS"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers", "Content-Type"
        );

        exchange.sendResponseHeaders(204, -1);
        exchange.close();
        return;
            }

            try {

        String method = exchange.getRequestMethod();

        // =========================
        // CADASTRAR COLABORADOR
        // =========================
        if ("POST".equalsIgnoreCase(method)) {

            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            Usuario usuario = new Usuario();

            usuario.setNome(extract(body, "nome"));
            usuario.setEmail(extract(body, "email"));
            usuario.setSenha(extract(body, "senha"));
            usuario.setTipo(extract(body, "tipo"));

            boolean ok = usuarioDAO.cadastrar(usuario);

            sendResponse(exchange, ok ? "OK" : "ERRO");
            return;
        }

        sendResponse(exchange, "INVALID_METHOD");

    } catch (Exception e) {

        e.printStackTrace();

        try {
            sendResponse(exchange, "ERRO_INTERNO");
        } catch (Exception ignored) {
        }
    }
});

        // =========================
        // PACIENTE
        // =========================
        server.createContext("/paciente", exchange -> {

            try {

                String method = exchange.getRequestMethod();
                String query = exchange.getRequestURI().getQuery();

                // =========================
                // GET POR ID
                // =========================
                if ("GET".equalsIgnoreCase(method)
                        && query != null
                        && query.contains("id=")) {

                    int id = Integer.parseInt(
                            query.substring(query.indexOf("id=") + 3)
                    );

                    Paciente p = pacienteDAO.buscarPorId(id);

                    // Verifica se paciente existe
                    if (p == null) {

                        sendResponse(exchange, "{\"erro\":\"Paciente não encontrado\"}");
                        return;
                    }

                    String json =
                            "{"
                            + "\"id\":" + p.getIdPaciente() + ","
                            + "\"nome\":\"" + escapeJson(p.getNome()) + "\","
                            + "\"queixaPrincipal\":\"" + escapeJson(p.getQueixaPrincipal()) + "\","
                            + "\"objetivos\":\"" + escapeJson(p.getObjetivos()) + "\","
                            + "\"condutas\":\"" + escapeJson(p.getCondutas()) + "\""
                            + "}";

                    sendResponse(exchange, json);
                    return;
                }

                // =========================
        // EVOLUÇÃO
        // =========================
        server.createContext("/evolucao", exchange -> {

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {

                exchange.getResponseHeaders().set(
                    "Access-Control-Allow-Origin", "*"
                );

                exchange.getResponseHeaders().set(
                    "Access-Control-Allow-Methods",
                    "GET, POST, OPTIONS"
                );

                exchange.getResponseHeaders().set(
                    "Access-Control-Allow-Headers",
                    "Content-Type"
                );

                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }

            try {

                String method = exchange.getRequestMethod();
                String query = exchange.getRequestURI().getQuery();

        // =========================
        // SALVAR EVOLUÇÃO
        // =========================
        if ("POST".equalsIgnoreCase(method)) {

            String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
            );

            Evolucao evolucao = new Evolucao();

            evolucao.setIdPaciente(
                Integer.parseInt(extract(body, "idPaciente"))
            );

            evolucao.setIdUsuario(
                Integer.parseInt(extract(body, "idUsuario"))
            );

            evolucao.setDescricao(
                extract(body, "descricao")
            );

            boolean ok = evolucaoDAO.inserir(evolucao);

            sendResponse(
                exchange,
                ok ? "OK" : "ERRO"
            );

            return;
        }

        // =========================
        // LISTAR POR PACIENTE
        // =========================
        if ("GET".equalsIgnoreCase(method)
                && query != null
                && query.contains("idPaciente=")) {

            int idPaciente = Integer.parseInt(
                query.substring(
                    query.indexOf("idPaciente=") + 11
                )
            );

            List<Evolucao> lista =
                evolucaoDAO.listarPorPaciente(idPaciente);

            StringBuilder json =
                new StringBuilder("[");

            for (int i = 0; i < lista.size(); i++) {

                Evolucao e = lista.get(i);

                json.append("{")
                    .append("\"idEvolucao\":")
                    .append(e.getIdEvolucao())
                    .append(",")

                    .append("\"idPaciente\":")
                    .append(e.getIdPaciente())
                    .append(",")

                    .append("\"idUsuario\":")
                    .append(e.getIdUsuario())
                    .append(",")

                    .append("\"nomeUsuario\":\"")
                    .append(escapeJson(e.getNomeUsuario()))
                    .append("\",")

                    .append("\"descricao\":\"")
                    .append(escapeJson(e.getDescricao()))
                    .append("\",")

                    .append("\"data\":\"")
                    .append(
                        e.getData() != null
                        ? e.getData().toString()
                        : ""
                    )
                    .append("\"")

                    .append("}");

                if (i < lista.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");

            sendResponse(exchange, json.toString());
            return;
        }

        sendResponse(exchange, "INVALID_METHOD");

    } catch (Exception e) {

        e.printStackTrace();

        try {
            sendResponse(exchange, "ERRO_INTERNO");
        } catch (Exception ignored) {
        }
    }
});
                
                // =========================
                // LISTAR TODOS
                // =========================
                if ("GET".equalsIgnoreCase(method)) {

                    List<Paciente> lista = pacienteDAO.listar();

                    StringBuilder json = new StringBuilder("[");

                    for (int i = 0; i < lista.size(); i++) {

                        Paciente p = lista.get(i);

                        json.append("{")
                                .append("\"id\":")
                                .append(p.getIdPaciente())
                                .append(",")
                                .append("\"nome\":\"")
                                .append(escapeJson(p.getNome()))
                                .append("\",")
                                .append("\"idade\":")
                                .append(p.getIdade())
                                .append(",")
                                .append("\"diagnostico\":\"")
                                .append(escapeJson(p.getDiagnostico()))
                                .append("\"")
                                .append("}");

                        if (i < lista.size() - 1) {
                            json.append(",");
                        }
                    }

                    json.append("]");

                    sendResponse(exchange, json.toString());
                    return;
                }

                // =========================
            // COMENTÁRIO
            // =========================
            server.createContext("/comentario", exchange -> {

                if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {

                    exchange.getResponseHeaders().set(
                        "Access-Control-Allow-Origin", "*"
                    );

                    exchange.getResponseHeaders().set(
                        "Access-Control-Allow-Methods",
                        "GET, POST, OPTIONS"
                    );

                    exchange.getResponseHeaders().set(
                        "Access-Control-Allow-Headers",
                        "Content-Type"
                    );

                    exchange.sendResponseHeaders(204, -1);
                    exchange.close();
                    return;
                }

                try {

                    String method = exchange.getRequestMethod();
                    String query = exchange.getRequestURI().getQuery();

                    // =========================
                    // SALVAR COMENTÁRIO
                    // =========================
                    if ("POST".equalsIgnoreCase(method)) {

                        String body = new String(
                            exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8
                        );

                        Comentario comentario =
                            new Comentario();

                        comentario.setIdEvolucao(
                            Integer.parseInt(
                                extract(body, "idEvolucao")
                            )
                        );

                        comentario.setIdUsuario(
                            Integer.parseInt(
                                extract(body, "idUsuario")
                            )
                        );

                        comentario.setComentario(
                            extract(body, "comentario")
                        );

                        boolean ok =
                            comentarioDAO.inserir(comentario);

                        sendResponse(
                            exchange,
                            ok ? "OK" : "ERRO"
                        );

                        return;
                    }

                    // =========================
                    // LISTAR COMENTÁRIOS
                    // =========================
                    if ("GET".equalsIgnoreCase(method)
                            && query != null
                            && query.contains("idEvolucao=")) {

                        int idEvolucao =
                            Integer.parseInt(
                                query.substring(
                                    query.indexOf("idEvolucao=") + 11
                                )
                            );

                        List<Comentario> lista =
                            comentarioDAO.listarPorEvolucao(
                                idEvolucao
                            );

                        StringBuilder json =
                            new StringBuilder("[");

                        for (int i = 0; i < lista.size(); i++) {

                            Comentario c = lista.get(i);

                            json.append("{")

                                .append("\"idComentario\":")
                                .append(c.getIdComentario())
                                .append(",")

                                .append("\"idEvolucao\":")
                                .append(c.getIdEvolucao())
                                .append(",")

                                .append("\"nomeUsuario\":\"")
                                .append(
                                    escapeJson(
                                        c.getNomeUsuario()
                                    )
                                )
                                .append("\",")

                                .append("\"comentario\":\"")
                                .append(
                                    escapeJson(
                                        c.getComentario()
                                    )
                                )
                                .append("\",")

                                .append("\"data\":\"")
                                .append(
                                    c.getDataComentario() != null
                                    ? c.getDataComentario().toString()
                                    : ""
                                )
                                .append("\"")

                                .append("}");

                            if (i < lista.size() - 1) {
                                json.append(",");
                            }
                        }

                        json.append("]");

                        sendResponse(exchange, json.toString());
                        return;
                    }

                    sendResponse(exchange, "INVALID_METHOD");

                } catch (Exception e) {

                    e.printStackTrace();

                    try {
                        sendResponse(exchange, "ERRO_INTERNO");
                    } catch (Exception ignored) {
                    }
                }
            });


                // =========================
                // SALVAR PACIENTE
                // =========================
                if ("POST".equalsIgnoreCase(method)) {

                    String body = new String(
                            exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8
                    );

                    Paciente p = new Paciente();

                    p.setNome(extract(body, "nome"));
                    p.setIdade(Integer.parseInt(extract(body, "idade")));
                    p.setDiagnostico(extract(body, "diagnostico"));
                    p.setQueixaPrincipal(extract(body, "queixaPrincipal"));
                    p.setObjetivos(extract(body, "objetivos"));
                    p.setCondutas(extract(body, "condutas"));

                    pacienteDAO.inserir(p);

                    sendResponse(exchange, "OK");
                    return;
                }

                sendResponse(exchange, "INVALID_METHOD");

            } catch (Exception e) {

                e.printStackTrace();

                try {
                    sendResponse(exchange, "ERRO_INTERNO");
                } catch (Exception ignored) {
                }
            }
        });

        // =========================
        // ROTA DE TESTE
        // =========================
        server.createContext("/", exchange -> {

            String response =
                    "{"
                    + "\"status\":\"online\","
                    + "\"sistema\":\"Prontuario Eletronico\""
                    + "}";

            sendResponse(exchange, response);
        });

        // =========================
        // INICIAR SERVIDOR
        // =========================
        server.setExecutor(null);
        server.start();

        System.out.println("==============================");
        System.out.println("SERVIDOR INICIADO COM SUCESSO");
        System.out.println("Porta: " + port);
        System.out.println("==============================");
    }

    // =========================
    // EXTRAÇÃO JSON SIMPLES
    // =========================
    private static String extract(String body, String key) {

        String pattern = "\"" + key + "\":\"";

        int start = body.indexOf(pattern);

        if (start == -1) {
            return "";
        }

        start += pattern.length();

        int end = body.indexOf("\"", start);

        if (end == -1) {
            return "";
        }

        return body.substring(start, end);
    }

    // =========================
    // ESCAPAR JSON
    // =========================
    private static String escapeJson(String texto) {

        if (texto == null) {
            return "";
        }

        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    // =========================
    // RESPOSTA JSON
    // =========================
    // 
    
    private static void sendResponse(
        HttpExchange exchange,
        String response
) throws IOException {

    byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

    // =========================
    // CORS
    // =========================
    exchange.getResponseHeaders().set(
            "Access-Control-Allow-Origin",
            "*"
    );

    exchange.getResponseHeaders().set(
            "Access-Control-Allow-Methods",
            "GET, POST, OPTIONS"
    );

    exchange.getResponseHeaders().set(
            "Access-Control-Allow-Headers",
            "Content-Type"
    );

    exchange.getResponseHeaders().set(
            "Content-Type",
            "application/json; charset=UTF-8"
    );

    exchange.sendResponseHeaders(200, bytes.length);

    try (OutputStream os = exchange.getResponseBody()) {
        os.write(bytes);
    }
}
}