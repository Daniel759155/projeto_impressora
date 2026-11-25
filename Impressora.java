import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class Impressora {

    // Representa "um comando da DLL" para tratarmos o retorno sempre do mesmo jeito
    private interface Comando {
        int executar();
    }

    // Assinatura do QR Code do XML de cancelamento (do enunciado, sem espacos e sem quebras de linha)
    private static final String ASS_QRCODE_CANCELAMENTO =
            "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";

    // Dados da conexao (comecam com os valores sugeridos para teste)
    private int tipo = 1;
    private String modelo = "i9";
    private String conexao = "USB";
    private int parametro = 0;

    // So vira true quando a DLL devolver 0 ao abrir a conexao
    private boolean conexaoAberta = false;

    public boolean isConexaoAberta() {
        return conexaoAberta;
    }

    // ---------------- Conexao ----------------

    public void configurar(int tipo, String modelo, String conexao, int parametro) {
        this.tipo = tipo;
        this.modelo = modelo;
        this.conexao = conexao;
        this.parametro = parametro;
        System.out.println("Configuracao salva: tipo=" + tipo + ", modelo=" + modelo
                + ", conexao=" + conexao + ", parametro=" + parametro);
    }

    public void abrirConexao() {
        if (conexaoAberta) {
            System.out.println("A conexao ja esta aberta.");
            return;
        }
        conexaoAberta = executar("abrir a conexao",
                () -> ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro),
                false);
    }

    public void fecharConexao() {
        if (!conexaoAberta) {
            System.out.println("A conexao ja esta fechada.");
            return;
        }
        boolean fechou = executar("fechar a conexao",
                () -> ImpressoraDLL.INSTANCE.FechaConexaoImpressora(),
                false);
        if (fechou) {
            conexaoAberta = false;
        }
    }

    // ---------------- Impressao ----------------

    // posicao: 0 = esquerda, 1 = centro, 2 = direita
    // estilo: 0 = fonte A, 1 = fonte B, 2 = sublinhado, 4 = invertido, 8 = negrito (pode somar)
    // tamanho: 0 = normal; 1 a 7 = altura maior
    public void imprimirTexto(String texto, int posicao, int estilo, int tamanho, int copias) {
        for (int i = 1; i <= copias; i++) {
            // ImpressaoTexto devolve a quantidade de dados enviados: so numero negativo e erro
            boolean ok = executar("imprimir o texto (copia " + i + ")",
                    () -> ImpressoraDLL.INSTANCE.ImpressaoTexto(texto + "\n", posicao, estilo, tamanho),
                    true);
            if (!ok) {
                return;
            }
        }
        cortarPapel();
    }

    public void imprimirQRCode(String dados, int tamanho, int nivelCorrecao) {
        boolean ok = executar("imprimir o QR Code",
                () -> ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, tamanho, nivelCorrecao),
                false);
        if (ok) {
            cortarPapel();
        }
    }

    // tipo: 0 = UPC-A, 2 = EAN13, 3 = EAN8, 4 = CODE 39, 8 = CODE 128
    // hri: 1 = numero acima, 2 = abaixo, 3 = os dois, 4 = nao imprime
    public void imprimirCodigoBarras(int tipo, String dados, int altura, int largura, int hri) {
        // No CODE 128 os dados precisam comecar com "{A" (ex.: {A012345678912)
        String dadosFinais = dados;
        if (tipo == 8 && !dados.startsWith("{")) {
            dadosFinais = "{A" + dados;
        }
        final String enviar = dadosFinais;
        boolean ok = executar("imprimir o codigo de barras",
                () -> ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(tipo, enviar, altura, largura, hri),
                false);
        if (ok) {
            cortarPapel();
        }
    }

    public void imprimirXMLSAT(String caminhoArquivo) {
        String xml = lerArquivo(caminhoArquivo);
        if (xml == null) {
            return;
        }
        boolean ok = executar("imprimir o XML SAT",
                () -> ImpressoraDLL.INSTANCE.ImprimeXMLSAT(xml, 0),
                false);
        if (ok) {
            cortarPapel();
        }
    }

    public void imprimirXMLCancelamentoSAT(String caminhoArquivo) {
        String xml = lerArquivo(caminhoArquivo);
        if (xml == null) {
            return;
        }
        boolean ok = executar("imprimir o XML de cancelamento SAT",
                () -> ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(xml, ASS_QRCODE_CANCELAMENTO, 0),
                false);
        if (ok) {
            cortarPapel();
        }
    }

    // ---------------- Outras operacoes ----------------

    public void abrirGaveta() {
        // pino 1, tempos do pulso 5 e 10 (valores de exemplo da documentacao)
        executar("abrir a gaveta",
                () -> ImpressoraDLL.INSTANCE.AbreGaveta(1, 5, 10),
                false);
    }

    // quantidade: de 1 a 63 bips
    public void sinalSonoro(int quantidade) {
        // 50 = duracao do bip, 5 = pausa entre os bips
        boolean ok = executar("tocar o sinal sonoro",
                () -> ImpressoraDLL.INSTANCE.SinalSonoro(quantidade, 50, 5),
                false);
        if (ok) {
            System.out.println("Bip!");
        }
    }

    // ---------------- Metodos auxiliares (escritos uma vez so) ----------------

    private void cortarPapel() {
        executar("cortar o papel", () -> ImpressoraDLL.INSTANCE.Corte(5), false);
    }

    // Roda um comando da DLL, confere o numero devolvido e mostra sucesso ou erro.
    // Tambem trata o caso de a DLL nao carregar.
    // retornoPositivoEhOk = true para comandos que devolvem "quantidade de dados" (so negativo e erro)
    private boolean executar(String acao, Comando comando, boolean retornoPositivoEhOk) {
        try {
            int retorno = comando.executar();
            boolean sucesso = retornoPositivoEhOk ? retorno >= 0 : retorno == 0;
            if (sucesso) {
                System.out.println("Sucesso ao " + acao + ".");
            } else {
                System.out.println("Erro ao " + acao + ". Codigo: " + retorno);
            }
            return sucesso;
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Nao foi possivel carregar a DLL: " + e.getMessage());
            return false;
        }
    }

    // Le o arquivo XML. Devolve null se der erro (e avisa o usuario).
    private String lerArquivo(String caminho) {
        try {
            return new String(Files.readAllBytes(Paths.get(caminho)), StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            System.out.println("Arquivo XML nao encontrado: " + caminho);
        } catch (IOException e) {
            System.out.println("Nao foi possivel ler o arquivo XML: " + e.getMessage());
        }
        return null;
    }
}