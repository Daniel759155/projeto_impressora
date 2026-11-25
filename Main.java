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
                    configurarConexao(scanner, impressora);
                    break;
                case 2:
                    impressora.abrirConexao();
                    break;
                case 3:
                    impressora.fecharConexao();
                    break;
                case 4:
                    if (conexaoPronta(impressora)) {
                        imprimirTexto(scanner, impressora);
                    }
                    break;
                case 5:
                    if (conexaoPronta(impressora)) {
                        imprimirQRCode(scanner, impressora);
                    }
                    break;
                case 6:
                    if (conexaoPronta(impressora)) {
                        imprimirCodigoBarras(scanner, impressora);
                    }
                    break;
                case 7:
                    if (conexaoPronta(impressora)) {
                        System.out.print("Arquivo XML de venda (ENTER = XMLSAT.xml): ");
                        String arquivo = scanner.nextLine().trim();
                        if (arquivo.isEmpty()) {
                            arquivo = "XMLSAT.xml";
                        }
                        impressora.imprimirXMLSAT(arquivo);
                    }
                    break;
                case 8:
                    if (conexaoPronta(impressora)) {
                        System.out.print("Arquivo XML de cancelamento (ENTER = CANC_SAT.xml): ");
                        String arquivo = scanner.nextLine().trim();
                        if (arquivo.isEmpty()) {
                            arquivo = "CANC_SAT.xml";
                        }
                        impressora.imprimirXMLCancelamentoSAT(arquivo);
                    }
                    break;
                case 9:
                    if (conexaoPronta(impressora)) {
                        impressora.abrirGaveta();
                    }
                    break;
                case 10:
                    if (conexaoPronta(impressora)) {
                        int quantidade = lerInteiro(scanner, "Quantos bips (1 a 63)? ", 1, 63);
                        impressora.sinalSonoro(quantidade);
                    }
                    break;
                case 0:
                    // Fecha a conexao antes de sair, se ainda estiver aberta
                    if (impressora.isConexaoAberta()) {
                        impressora.fecharConexao();
                    }
                    System.out.println("Programa encerrado.");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    // Regra importante: sem conexao aberta, o programa nao pergunta nada de impressao.
    // Escrita uma vez so e usada em todas as opcoes que precisam da impressora.
    private static boolean conexaoPronta(Impressora impressora) {
        if (!impressora.isConexaoAberta()) {
            System.out.println("Conexao fechada. Abra a conexao antes.");
            return false;
        }
        return true;
    }

    // ---------------- Perguntas de cada opcao ----------------

    private static void configurarConexao(Scanner scanner, Impressora impressora) {
        System.out.println("Tipo: 1 = USB, 2 = Serial, 3 = Rede, 4 = Bluetooth");
        int tipo = lerInteiro(scanner, "Tipo (1 a 4): ", 1, 4);
        String modelo = lerTexto(scanner, "Modelo (ex.: i8 ou i9): ");
        String conexao = lerTexto(scanner, "Conexao (USB, COM2 ou IP): ");
        int parametro = lerInteiro(scanner, "Parametro (0 para USB): ", 0, 1000000);
        impressora.configurar(tipo, modelo, conexao, parametro);
    }

    private static void imprimirTexto(Scanner scanner, Impressora impressora) {
        String texto = lerTexto(scanner, "Texto a imprimir: ");
        int posicao = lerInteiro(scanner, "Posicao (0 = esquerda, 1 = centro, 2 = direita): ", 0, 2);
        int estilo = lerInteiro(scanner, "Estilo (0 = fonte A, 1 = fonte B, 2 = sublinhado, 4 = invertido, 8 = negrito; pode somar): ", 0, 15);
        int tamanho = lerInteiro(scanner, "Tamanho (0 = normal; 1 a 7 = altura maior): ", 0, 7);
        int copias = lerInteiro(scanner, "Quantas copias (1 a 10)? ", 1, 10);
        impressora.imprimirTexto(texto, posicao, estilo, tamanho, copias);
    }

    private static void imprimirQRCode(Scanner scanner, Impressora impressora) {
        String dados = lerTexto(scanner, "Conteudo do QR Code: ");
        int tamanho = lerInteiro(scanner, "Tamanho (1 a 6): ", 1, 6);
        int nivel = lerInteiro(scanner, "Nivel de correcao (1 a 4): ", 1, 4);
        impressora.imprimirQRCode(dados, tamanho, nivel);
    }

    private static void imprimirCodigoBarras(Scanner scanner, Impressora impressora) {
        System.out.println("Tipos: 0 = UPC-A, 2 = EAN13, 3 = EAN8, 4 = CODE 39, 8 = CODE 128");
        int tipo = -1;
        while (tipo != 0 && tipo != 2 && tipo != 3 && tipo != 4 && tipo != 8) {
            tipo = lerInteiro(scanner, "Tipo (0, 2, 3, 4 ou 8): ", 0, 8);
            if (tipo != 0 && tipo != 2 && tipo != 3 && tipo != 4 && tipo != 8) {
                System.out.println("Tipo invalido. Escolha 0, 2, 3, 4 ou 8.");
            }
        }
        String dados = lerTexto(scanner, "Dados do codigo de barras: ");
        int altura = lerInteiro(scanner, "Altura (1 a 255): ", 1, 255);
        int largura = lerInteiro(scanner, "Largura (1 a 6): ", 1, 6);
        int hri = lerInteiro(scanner, "Numero escrito junto (1 = acima, 2 = abaixo, 3 = os dois, 4 = nao imprime): ", 1, 4);
        impressora.imprimirCodigoBarras(tipo, dados, altura, largura, hri);
    }

    // ---------------- Leitura do que o usuario digita ----------------

    // Repete a pergunta ate o usuario digitar um numero valido entre minimo e maximo
    private static int lerInteiro(Scanner scanner, String pergunta, int minimo, int maximo) {
        while (true) {
            System.out.print(pergunta);
            try {
                int numero = Integer.parseInt(scanner.nextLine());
                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }
                System.out.println("Digite um numero de " + minimo + " a " + maximo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Digite apenas numeros.");
            }
        }
    }

    // Repete a pergunta ate o usuario digitar alguma coisa
    private static String lerTexto(Scanner scanner, String pergunta) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(pergunta);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("O campo nao pode ficar vazio.");
            }
        }
        return texto;
    }
}