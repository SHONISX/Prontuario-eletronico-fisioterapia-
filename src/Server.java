import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dao.PacienteDAO;
import dao.UsuarioDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import model.Paciente;

public class Server {

    static UsuarioDAO usuarioDAO = new UsuarioDAO();
    static PacienteDAO pacienteDAO = new PacienteDAO();

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

                    boolean ok = usuarioDAO.login(email, senha);

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
                    p.setQueixaPrincipal(extract(body, "queixa"));
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