# 🖨️ Sistema de Controle de Impressora Elgin
Trabalho de Programação Orientada a Objetos (POO) com Java e impressora térmica Elgin i8 ou i9.
---

## 📘 Sobre o Projeto

*Programa de console em Java que pede login, mostra um menu e envia comandos para a impressora Elgin: imprimir texto, QR Code, código de barras e cupons SAT, abrir a gaveta e tocar o bip. A comunicação é feita pela DLL da Elgin, chamada pelo Java através da biblioteca JNA (arquivo `ImpressoraDLL.java`).**.

---

## 🧰 Tecnologias Utilizadas

- Java (JDK 17 ou superior)
- JNA 5.15.0
- DLL da Elgin (E1 Impressora, x64 e x86)
- IntelliJ IDEA, Git e GitHub
---

## 📁 Estrutura do Projeto

- `Main.java`: faz o login e mostra o menu
- `Usuario.java`: guarda login e senha e confere se estão certos
- `Impressora.java`: guarda os dados da conexão e tem um método para cada operação
- `ImpressoraDLL.java`: ligação do Java com a DLL (fornecido pelo professor)
- `Bibliotecas E1 Impressora/`: DLL da Elgin
- `libs Java/`: `jna-5.15.0.jar`
- `XMLSAT.xml` e `CANC_SAT.xml`: cupons de exemplo

## ⚙️ Instalação e Execução

## Como executar

1. Abra a pasta do projeto no IntelliJ e confirme que o `jna-5.15.0.jar` está em Project Structure → Libraries.
2. Em Run → Edit Configurations, confira se o Working directory é a pasta principal do projeto (onde está `Bibliotecas E1 Impressora`).
3. Ligue a impressora, conecte o cabo USB e execute a classe `Main`.

## Login de teste

- Usuário: `admin`
- Senha: `1234`

São 3 tentativas. A cada erro o programa mostra quantas restam e, na terceira falha, encerra.

---

## 📋 Funcionalidades do Menu
| # | Opção |
|---|-------|
| 1 | Configurar conexão |
| 2 | Abrir conexão |
| 3 | Fechar conexão |
| 4 | Imprimir texto |
| 5 | Imprimir QR Code |
| 6 | Imprimir código de barras |
| 7 | Imprimir XML SAT |
| 8 | Imprimir XML de cancelamento SAT |
| 9 | Abrir gaveta |
| 10 | Sinal sonoro (bip) |
| 0 | Sair |

## 🧪 Testes Realizados

- [x] Login correto e login errado 3 vezes
- [ ] Abrir e fechar a conexão
- [ ] Tentar imprimir com a conexão fechada
- [ ] Imprimir texto, QR Code e código de barras
- [ ] Imprimir XML SAT e XML de cancelamento
- [ ] Abrir gaveta e sinal sonoro
- [x] Digitar letras onde se espera número


## 📚 Documentação Adicional

- Manual da impressora Elgin
- Documentação do SDK E1_Impressora

---

## 👥 Autores


- André Bezerra da Silva
- Daniel de Souza Silva
- Gabriel Silva Jeronimo
