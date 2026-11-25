import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Usuario usuario = new Usuario("admin", "1234");
        Impressora impressora = new Impressora();

        boolean logado = false;
        for (int tentativa = 1; tentativa <= 3; tentativa++) {
            System.out.print("Usuario: ");
            String login = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            if (usuario.autenticar(login, senha)) {
                logado = true;
                break;
            }

            int restantes = 3 - tentativa;
            if (restantes > 0) {
                System.out.println("Usuario ou senha incorretos. Tentativas restantes: " + restantes);
            }
        }

        if (!logado) {
            System.out.println("Numero maximo de tentativas atingido. Programa encerrado.");
            return;
        }

        System.out.println("Login realizado com sucesso!");

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===== MENU =====");
            System.out.println("1 - Configurar conexao");
            System.out.println("2 - Abrir conexao");
            System.out.println("3 - Fechar conexao");
            System.out.println("4 - Imprimir texto");
            System.out.println("5 - Imprimir QR Code");
            System.out.println("6 - Imprimir codigo de barras");
            System.out.println("7 - Imprimir XML SAT");
            System.out.println("8 - Imprimir XML de cancelamento SAT");
            System.out.println("9 - Abrir gaveta");
            System.out.println("10 - Sinal sonoro (bip)");
            System.out.println("0 - Sair");
            System.out.println("Conexao: " + (impressora.isConexaoAberta() ? "ABERTA" : "FECHADA"));
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas numeros.");
                opcao = -1;
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.println("Tipo: 1 = USB, 2 = Serial, 3 = Rede, 4 = Bluetooth");
                    int tipo = lerInteiro(scanner, "Tipo (1 a 4): ");
                    System.out.print("Modelo (ex.: i8 ou i9): ");
                    String modelo = scanner.nextLine();
                    System.out.print("Conexao (USB, COM2 ou IP): ");
                    String conexao = scanner.nextLine();
                    int parametro = lerInteiro(scanner, "Parametro (0 para USB): ");
                    impressora.configurar(tipo, modelo, conexao, parametro);
                    break;
                case 2:
                    impressora.abrirConexao();
                    break;
                case 3:
                    impressora.fecharConexao();
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    System.out.println("Opcao " + opcao + " ainda nao implementada.");
                    break;
                case 0:
                    System.out.println("Programa encerrado.");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    // Repete a pergunta ate o usuario digitar um numero valido
    private static int lerInteiro(Scanner scanner, String pergunta) {
        while (true) {
            System.out.print(pergunta);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Digite apenas numeros.");
            }
        }
    }
}