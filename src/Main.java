import dao.ComentarioDAO;
import dao.EvolucaoDAO;
import dao.PacienteDAO;
import dao.UsuarioDAO;
import java.util.List;
import java.util.Scanner;
import model.Comentario;
import model.Evolucao;
import model.Paciente;
import model.Usuario;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static UsuarioDAO usuarioDAO = new UsuarioDAO();
    static PacienteDAO pacienteDAO = new PacienteDAO();
    static EvolucaoDAO evolucaoDAO = new EvolucaoDAO();
    static ComentarioDAO comentarioDAO = new ComentarioDAO();

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("      SISTEMA DE PRONTUARIO ELETRONICO");
        System.out.println("         CLINICA DE FISIOTERAPIA");
        System.out.println("==============================================");

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Senha: ");
        String senha = sc.nextLine();

        if (!usuarioDAO.login(email, senha)) {

            System.out.println("\nLogin invalido.");
            return;

        }

        System.out.println("\nLogin realizado com sucesso!");

        int opcao;

        do {

            System.out.println("\n==============================================");
            System.out.println("                MENU PRINCIPAL");
            System.out.println("==============================================");

            System.out.println("1 - Cadastrar Paciente");
            System.out.println("2 - Listar Pacientes");
            System.out.println("3 - Buscar Paciente");
            System.out.println("4 - Atualizar Paciente");
            System.out.println("5 - Excluir Paciente");
            System.out.println("6 - Nova Evolucao");
            System.out.println("7 - Listar Evolucoes");
            System.out.println("8 - Novo Comentario");
            System.out.println("9 - Cadastrar Usuario");
            System.out.println("0 - Sair");

            System.out.print("\nEscolha: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {

                case 1:

                    Paciente paciente = new Paciente();

                    System.out.print("Nome: ");
                    paciente.setNome(sc.nextLine());

                    System.out.print("Idade: ");
                    paciente.setIdade(Integer.parseInt(sc.nextLine()));

                    System.out.print("Diagnostico: ");
                    paciente.setDiagnostico(sc.nextLine());

                    System.out.print("Queixa Principal: ");
                    paciente.setQueixaPrincipal(sc.nextLine());

                    System.out.print("Objetivos: ");
                    paciente.setObjetivos(sc.nextLine());

                    System.out.print("Condutas: ");
                    paciente.setCondutas(sc.nextLine());

                    if (pacienteDAO.inserir(paciente)) {

                        System.out.println("\nPaciente cadastrado com sucesso!");

                    } else {

                        System.out.println("\nErro ao cadastrar paciente.");

                    }

                    break;

                case 2:

                    List<Paciente> pacientes = pacienteDAO.listar();

                    if (pacientes.isEmpty()) {

                        System.out.println("\nNenhum paciente cadastrado.");

                    } else {

                        System.out.println("\n=========== PACIENTES ===========");

                        for (Paciente p : pacientes) {

                            System.out.println("--------------------------------");

                            System.out.println("ID: " + p.getIdPaciente());

                            System.out.println("Nome: " + p.getNome());

                            System.out.println("Idade: " + p.getIdade());

                            System.out.println("Diagnostico: " + p.getDiagnostico());

                        }

                    }

                    break;

                case 3:

                    System.out.print("Digite o ID do paciente: ");

                    int idBusca = Integer.parseInt(sc.nextLine());

                    Paciente encontrado = pacienteDAO.buscarPorId(idBusca);

                    if (encontrado == null) {

                        System.out.println("\nPaciente nao encontrado.");

                    } else {

                        System.out.println("\nPaciente encontrado:");

                        System.out.println("ID: " + encontrado.getIdPaciente());

                        System.out.println("Nome: " + encontrado.getNome());

                        System.out.println("Idade: " + encontrado.getIdade());

                        System.out.println("Diagnostico: " + encontrado.getDiagnostico());

                        System.out.println("Queixa Principal: " + encontrado.getQueixaPrincipal());

                        System.out.println("Objetivos: " + encontrado.getObjetivos());

                        System.out.println("Condutas: " + encontrado.getCondutas());

                    }

                    break;

                    case 4:

                    System.out.print("ID do paciente: ");

                    int idAtualizar = Integer.parseInt(sc.nextLine());

                    Paciente pacienteAtualizar = pacienteDAO.buscarPorId(idAtualizar);

                    if (pacienteAtualizar == null) {

                        System.out.println("\nPaciente não encontrado.");

                    } else {

                        System.out.print("Novo nome: ");
                        pacienteAtualizar.setNome(sc.nextLine());

                        System.out.print("Nova idade: ");
                        pacienteAtualizar.setIdade(Integer.parseInt(sc.nextLine()));

                        System.out.print("Novo diagnóstico: ");
                        pacienteAtualizar.setDiagnostico(sc.nextLine());

                        System.out.print("Nova queixa principal: ");
                        pacienteAtualizar.setQueixaPrincipal(sc.nextLine());

                        System.out.print("Novos objetivos: ");
                        pacienteAtualizar.setObjetivos(sc.nextLine());

                        System.out.print("Novas condutas: ");
                        pacienteAtualizar.setCondutas(sc.nextLine());

                        if (pacienteDAO.atualizar(pacienteAtualizar)) {

                            System.out.println("\nPaciente atualizado com sucesso!");

                        } else {

                            System.out.println("\nErro ao atualizar paciente.");

                        }

                    }

                    break;

                case 5:

                    System.out.print("ID do paciente: ");

                    int idExcluir = Integer.parseInt(sc.nextLine());

                    if (pacienteDAO.excluir(idExcluir)) {

                        System.out.println("\nPaciente excluído com sucesso.");

                    } else {

                        System.out.println("\nErro ao excluir paciente.");

                    }

                    break;

                case 6:

                    Evolucao evolucao = new Evolucao();

                    System.out.print("ID do paciente: ");
                    evolucao.setIdPaciente(Integer.parseInt(sc.nextLine()));

                    System.out.print("ID do usuário: ");
                    evolucao.setIdUsuario(Integer.parseInt(sc.nextLine()));

                    System.out.println("Descrição da evolução:");
                    evolucao.setDescricao(sc.nextLine());

                    if (evolucaoDAO.inserir(evolucao)) {

                        System.out.println("\nEvolução cadastrada com sucesso!");

                    } else {

                        System.out.println("\nErro ao cadastrar evolução.");

                    }

                    break;

                case 7:

                    List<Evolucao> evolucoes = evolucaoDAO.listar();

                    if (evolucoes.isEmpty()) {

                        System.out.println("\nNenhuma evolução cadastrada.");

                    } else {

                        System.out.println("\n========== EVOLUÇÕES ==========");

                        for (Evolucao e : evolucoes) {

                            System.out.println("-----------------------------------");

                            System.out.println("ID Evolução: " + e.getIdEvolucao());

                            System.out.println("Paciente: " + e.getIdPaciente());

                            System.out.println("Usuário: " + e.getIdUsuario());

                            System.out.println("Descrição:");

                            System.out.println(e.getDescricao());

                        }

                    }

                    break;

                case 8:

                    Comentario comentario = new Comentario();

                    System.out.print("ID da evolução: ");
                    comentario.setIdEvolucao(Integer.parseInt(sc.nextLine()));

                    System.out.print("ID do usuário: ");
                    comentario.setIdUsuario(Integer.parseInt(sc.nextLine()));

                    System.out.println("Comentário:");

                    comentario.setComentario(sc.nextLine());

                    if (comentarioDAO.inserir(comentario)) {

                        System.out.println("\nComentário salvo com sucesso!");

                    } else {

                        System.out.println("\nErro ao salvar comentário.");

                    }

                    break;

                case 9:

                    Usuario usuario = new Usuario();

                    System.out.print("Nome: ");
                    usuario.setNome(sc.nextLine());

                    System.out.print("Email: ");
                    usuario.setEmail(sc.nextLine());

                    System.out.print("Senha: ");
                    usuario.setSenha(sc.nextLine());

                    System.out.print("Tipo (Administrador/Fisioterapeuta): ");
                    usuario.setTipo(sc.nextLine());

                    if (usuarioDAO.cadastrar(usuario)) {

                        System.out.println("\nUsuário cadastrado com sucesso!");

                    } else {

                        System.out.println("\nErro ao cadastrar usuário.");
                    }
                    break;
                case 0:
                    System.out.println("\nEncerrando o sistema...");
                    break;
                default:
                    System.out.println("\nOpção inválida.");
            }
        } while (opcao != 0);
        sc.close();
    }
}