import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Usuario usuario = new Usuario("admin", "1234");

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
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas numeros.");
                opcao = -1;
                continue;
            }

            if (opcao >= 1 && opcao <= 10) {
                System.out.println("Opcao " + opcao + " ainda nao implementada.");
            } else if (opcao == 0) {
                System.out.println("Programa encerrado.");
            } else {
                System.out.println("Opcao invalida.");
            }
        }
    }
}