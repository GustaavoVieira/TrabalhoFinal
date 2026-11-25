package trabalhofinal;

import javax.swing.*;

public class Main {

    private static Usuario[] usuarios = new Usuario[100];
    private static Equipe[] equipes = new Equipe[100];
    private static Campeonato[] campeonatos = new Campeonato[100];

    private static int qtdUsuarios = 0;
    private static int qtdEquipes = 0;
    private static int qtdCampeonatos = 0;


    public static void main(String[] args) {
        menuPrincipal();
    }

    public static void login() {

        String email = JOptionPane.showInputDialog("Digite seu e-mail:");
        if (email == null || email.trim().isEmpty()) return;

        String senha = JOptionPane.showInputDialog("Digite sua senha:");
        if (senha == null || senha.isEmpty()) return;

        for (int i = 0; i < qtdUsuarios; i++) {

            Usuario usuario = usuarios[i];

            if (usuario.getEmail().equalsIgnoreCase(email)
                    && usuario.verificarSenha(senha)) {

                JOptionPane.showMessageDialog(null,
                        "Login realizado com sucesso!");

                usuario.exibirMenu();
                return;
            }
        }

        JOptionPane.showMessageDialog(null,
                "E-mail ou senha incorretos!");
    }

    public static void menuPrincipal() {

        int opcao;

        do {
            String[] opcoes;

            if (qtdUsuarios == 0) {
                opcoes = new String[]{
                        "Cadastrar primeiro Coordenador",
                        "Sair"
                };
            } else {
                opcoes = new String[]{
                        "Fazer login",
                        "Sair"
                };
            }

            opcao = JOptionPane.showOptionDialog(
                    null,
                    "Sistema de Gerenciamento Esportivo",
                    "Menu Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (qtdUsuarios == 0) {
                if (opcao == 0) {
                    cadastrarPrimeiroCoordenador();
                }
            } else {
                if (opcao == 0) {
                    login();
                }
            }

        } while (opcao != 1 && opcao != -1);
    }

    public static void cadastrarPrimeiroCoordenador() {

        String nome = JOptionPane.showInputDialog("Digite o nome:");
        if (nome == null || nome.trim().isEmpty()) return;

        String email = JOptionPane.showInputDialog("Digite o e-mail:");
        if (email == null || email.trim().isEmpty()) return;

        String senha = JOptionPane.showInputDialog("Digite a senha:");
        if (senha == null || senha.isEmpty()) return;

        Coordenador coordenador = new Coordenador(nome, email, senha);

        adicionarUsuario(coordenador);

        JOptionPane.showMessageDialog(null,
                "Primeiro coordenador cadastrado com sucesso!");
    }



    public static void cadastrarUsuario() {

        String[] tipos = {
                "Atleta",
                "Técnico",
                "Coordenador"
        };

        int tipo = JOptionPane.showOptionDialog(
                null,
                "Selecione o tipo de usuário:",
                "Cadastro de usuário",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                tipos,
                tipos[0]
        );

        if (tipo == -1) {
            return;
        }

        String nome = JOptionPane.showInputDialog("Digite o nome:");
        if (nome == null || nome.trim().isEmpty()) return;

        String email = JOptionPane.showInputDialog("Digite o e-mail:");
        if (email == null || email.trim().isEmpty()) return;

        String senha = JOptionPane.showInputDialog("Digite a senha:");
        if (senha == null || senha.isEmpty()) return;

        Usuario usuario = null;

        switch (tipo) {

            case 0:
                String matricula = JOptionPane.showInputDialog("Digite a matrícula:");
                if (matricula == null || matricula.trim().isEmpty()) return;

                String curso = JOptionPane.showInputDialog("Digite o curso:");
                if (curso == null || curso.trim().isEmpty()) return;

                String modalidade = JOptionPane.showInputDialog("Digite a modalidade:");
                if (modalidade == null || modalidade.trim().isEmpty()) return;

                usuario = new Atleta(nome, email, senha, matricula, curso, modalidade);
                break;

            case 1:
                usuario = new Tecnico(nome, email, senha);
                break;

            case 2:
                usuario = new Coordenador(nome, email, senha);
                break;
        }

        adicionarUsuario(usuario);

        JOptionPane.showMessageDialog(null,
                "Usuário cadastrado com sucesso!");
    }

    public static void cadastrarCampeonato() {

        if (qtdCampeonatos >= campeonatos.length) {
            JOptionPane.showMessageDialog(null,
                    "Limite de campeonatos atingido!");
            return;
        }

        String nome = JOptionPane.showInputDialog(
                "Digite o nome do campeonato:");

        if (nome == null || nome.trim().isEmpty()) return;

        String modalidade = JOptionPane.showInputDialog(
                "Digite a modalidade do campeonato:");

        if (modalidade == null || modalidade.trim().isEmpty()) return;

        Campeonato campeonato = new Campeonato(nome, modalidade);

        campeonatos[qtdCampeonatos] = campeonato;
        qtdCampeonatos++;

        JOptionPane.showMessageDialog(null,
                "Campeonato cadastrado com sucesso!");
    }

    public static void adicionarUsuario(Usuario usuario) {
        if (qtdUsuarios < usuarios.length) {
            usuarios[qtdUsuarios] = usuario;
            qtdUsuarios++;
        }
    }

    public static void adicionarEquipe(Equipe equipe) {

        if (qtdEquipes < equipes.length) {
            equipes[qtdEquipes] = equipe;
            qtdEquipes++;
        }
    }

    public static Equipe selecionarEquipeJogo(
            Campeonato campeonato, String mensagem) {

        if (campeonato.getQtdEquipes() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Este campeonato não possui equipes!");
            return null;
        }

        String[] opcoes = new String[campeonato.getQtdEquipes()];

        for (int i = 0; i < campeonato.getQtdEquipes(); i++) {
            Equipe equipe = campeonato.getEquipe(i);

            opcoes[i] = equipe.getNome()
                    + " - " + equipe.getModalidade();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                mensagem,
                "Selecionar equipe",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return null;

        for (int i = 0; i < campeonato.getQtdEquipes(); i++) {
            if (opcoes[i].equals(escolha)) {
                return campeonato.getEquipe(i);
            }
        }

        return null;
    }

    public static void cadastrarJogo() {

        Campeonato campeonato = selecionarCampeonato();

        if (campeonato == null) return;

        Equipe equipe1 = selecionarEquipeJogo(
                campeonato, "Selecione a primeira equipe:");

        if (equipe1 == null) return;

        Equipe equipe2 = selecionarEquipeJogo(
                campeonato, "Selecione a segunda equipe:");

        if (equipe2 == null) return;

        String data = JOptionPane.showInputDialog(
                "Digite a data do jogo:");

        if (data == null || data.trim().isEmpty()) return;

        String horario = JOptionPane.showInputDialog(
                "Digite o horário do jogo:");

        if (horario == null || horario.trim().isEmpty()) return;

        String local = JOptionPane.showInputDialog(
                "Digite o local do jogo:");

        if (local == null || local.trim().isEmpty()) return;

        Jogo jogo = new Jogo(equipe1, equipe2, data, horario, local);

        campeonato.adicionarJogo(jogo);
    }

    public static void consultarUsuarios() {

        if (qtdUsuarios == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum usuário cadastrado!");
            return;
        }

        String lista = "USUÁRIOS CADASTRADOS:\n\n";

        for (int i = 0; i < qtdUsuarios; i++) {

            Usuario usuario = usuarios[i];

            lista += "Nome: " + usuario.getNome()
                    + "\nE-mail: " + usuario.getEmail();

            if (usuario instanceof Atleta) {
                lista += "\nTipo: Atleta";
            } else if (usuario instanceof Tecnico) {
                lista += "\nTipo: Técnico";
            } else if (usuario instanceof Coordenador) {
                lista += "\nTipo: Coordenador";
            }

            lista += "\n\n";
        }

        JOptionPane.showMessageDialog(null, lista);
    }

    public static void consultarEquipes() {

        if (qtdEquipes == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhuma equipe cadastrada!");
            return;
        }

        String lista = "EQUIPES CADASTRADAS:\n\n";

        for (int i = 0; i < qtdEquipes; i++) {

            lista += "Nome: " + equipes[i].getNome()
                    + "\nModalidade: " + equipes[i].getModalidade()
                    + "\n\n";
        }

        JOptionPane.showMessageDialog(null, lista);
    }

    public static void consultarCampeonatos() {

        if (qtdCampeonatos == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum campeonato cadastrado!");
            return;
        }

        for (int i = 0; i < qtdCampeonatos; i++) {
            campeonatos[i].listarEquipes();
            campeonatos[i].listarJogos();
        }
    }

    public static Campeonato selecionarCampeonato() {

        if (qtdCampeonatos == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum campeonato cadastrado!");
            return null;
        }

        String[] opcoes = new String[qtdCampeonatos];

        for (int i = 0; i < qtdCampeonatos; i++) {
            opcoes[i] = campeonatos[i].getNome()
                    + " - " + campeonatos[i].getModalidade();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Selecione um campeonato:",
                "Campeonatos cadastrados",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return null;

        for (int i = 0; i < qtdCampeonatos; i++) {
            if (opcoes[i].equals(escolha)) {
                return campeonatos[i];
            }
        }

        return null;
    }

    public static void adicionarEquipeCampeonato() {

        Campeonato campeonato = selecionarCampeonato();

        if (campeonato == null) return;

        if (qtdEquipes == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhuma equipe cadastrada no sistema!");
            return;
        }

        String[] opcoes = new String[qtdEquipes];

        for (int i = 0; i < qtdEquipes; i++) {
            opcoes[i] = equipes[i].getNome()
                    + " - " + equipes[i].getModalidade();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Selecione uma equipe:",
                "Adicionar equipe",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return;

        for (int i = 0; i < qtdEquipes; i++) {
            if (opcoes[i].equals(escolha)) {
                campeonato.adicionarEquipe(equipes[i]);
                return;
            }
        }
    }

    public static Equipe[] getEquipes() {
        return equipes;
    }

    public static int getQtdEquipes() {
        return qtdEquipes;
    }
}