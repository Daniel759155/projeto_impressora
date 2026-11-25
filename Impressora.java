public class Impressora {

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
        try {
            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);
            if (retorno == 0) {
                conexaoAberta = true;
                System.out.println("Conexao aberta com sucesso!");
            } else {
                System.out.println("Erro ao abrir a conexao. Codigo: " + retorno);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Nao foi possivel carregar a DLL: " + e.getMessage());
        }
    }

    public void fecharConexao() {
        if (!conexaoAberta) {
            System.out.println("A conexao ja esta fechada.");
            return;
        }
        try {
            int retorno = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();
            if (retorno == 0) {
                conexaoAberta = false;
                System.out.println("Conexao fechada com sucesso!");
            } else {
                System.out.println("Erro ao fechar a conexao. Codigo: " + retorno);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Nao foi possivel carregar a DLL: " + e.getMessage());
        }
    }
}