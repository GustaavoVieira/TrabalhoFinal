package trabalhofinal;

import javax.swing.*;

public class Main {

    private static Usuario[] usuarios = new Usuario[100];
    private static Equipe[] equipes = new Equipe[100];

    private static int qtdUsuarios = 0;
    private static int qtdEquipes = 0;

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

    public static Equipe[] getEquipes() {
        return equipes;
    }

    public static int getQtdEquipes() {
        return qtdEquipes;
    }
}