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
    }
}