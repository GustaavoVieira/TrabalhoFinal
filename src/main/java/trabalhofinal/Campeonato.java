package trabalhofinal;

import javax.swing.*;

public class Campeonato {

    private String nome;
    private String modalidade;
    private Equipe [] equipes;
    private int qtdEquipes;
    private Jogo[] jogos;
    private int qtdJogos;

    public Campeonato(String nome, String modalidade) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O nome do campeonato não pode ficar vazio!");
        }

        if (modalidade == null || modalidade.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "A modalidade não pode ficar vazia!");
        }

        this.nome = nome.trim();
        this.modalidade = modalidade.trim();
        this.equipes = new Equipe[50];
        this.qtdEquipes = 0;
        this.jogos = new Jogo[100];
        this.qtdJogos = 0;
    }

    public void adicionarEquipe(Equipe equipe) {

        if (equipe == null) {
            JOptionPane.showMessageDialog(null,
                    "Equipe inválida!");
            return;
        }

        if (!modalidade.equalsIgnoreCase(equipe.getModalidade())) {
            JOptionPane.showMessageDialog(null,
                    "A modalidade da equipe não corresponde ao campeonato!");
            return;
        }

        for (int i = 0; i < qtdEquipes; i++) {

            if (equipes[i] == equipe
                    || (equipes[i].getNome().equalsIgnoreCase(equipe.getNome())
                    && equipes[i].getModalidade().equalsIgnoreCase(
                    equipe.getModalidade()))) {

                JOptionPane.showMessageDialog(null,
                        "Esta equipe já está no campeonato!");
                return;
            }
        }

        if (qtdEquipes >= equipes.length) {
            JOptionPane.showMessageDialog(null,
                    "Limite de equipes atingido!");
            return;
        }

        equipes[qtdEquipes] = equipe;
        qtdEquipes++;

        JOptionPane.showMessageDialog(null,
                "Equipe adicionada ao campeonato!");
    }

    public void adicionarJogo(Jogo jogo) {

        if (jogo == null) {
            JOptionPane.showMessageDialog(null,
                    "Jogo inválido!");
            return;
        }

        boolean equipe1Encontrada = false;
        boolean equipe2Encontrada = false;

        for (int i = 0; i < qtdEquipes; i++) {

            if (equipes[i] == jogo.getEquipe1()) {
                equipe1Encontrada = true;
            }

            if (equipes[i] == jogo.getEquipe2()) {
                equipe2Encontrada = true;
            }
        }

        if (!equipe1Encontrada || !equipe2Encontrada) {
            JOptionPane.showMessageDialog(null,
                    "As duas equipes precisam participar do campeonato!");
            return;
        }

        if (jogo.getEquipe1() == jogo.getEquipe2()) {
            JOptionPane.showMessageDialog(null,
                    "Uma equipe não pode jogar contra ela mesma!");
            return;
        }

        for (int i = 0; i < qtdJogos; i++) {

            Jogo jogoExistente = jogos[i];

            boolean mesmasEquipes =
                    (jogoExistente.getEquipe1() == jogo.getEquipe1()
                            && jogoExistente.getEquipe2() == jogo.getEquipe2())
                            || (jogoExistente.getEquipe1() == jogo.getEquipe2()
                            && jogoExistente.getEquipe2() == jogo.getEquipe1());

            boolean mesmoHorario =
                    jogoExistente.getData().equals(jogo.getData())
                            && jogoExistente.getHorario().equals(jogo.getHorario());

            boolean equipeEmConflito =
                    jogoExistente.getEquipe1() == jogo.getEquipe1()
                            || jogoExistente.getEquipe2() == jogo.getEquipe1()
                            || jogoExistente.getEquipe1() == jogo.getEquipe2()
                            || jogoExistente.getEquipe2() == jogo.getEquipe2();

            if (equipeEmConflito && mesmoHorario) {
                JOptionPane.showMessageDialog(null,
                        "Uma das equipes já possui um jogo "
                                + "nesta data e horário!");
                return;
            }
        }

        if (qtdJogos >= jogos.length) {
            JOptionPane.showMessageDialog(null,
                    "Limite de jogos atingido!");
            return;
        }

        jogos[qtdJogos] = jogo;
        qtdJogos++;

        JOptionPane.showMessageDialog(null,
                "Jogo cadastrado com sucesso!");
    }

    public void listarEquipes() {

        if (qtdEquipes == 0) {
            JOptionPane.showMessageDialog(null,
                    "Não existem equipes neste campeonato!");
            return;
        }

        StringBuilder lista = new StringBuilder("CAMPEONATO: " + nome
                + "\nMODALIDADE: " + modalidade
                + "\n\nEQUIPES PARTICIPANTES:\n\n");

        for (int i = 0; i < qtdEquipes; i++) {
            lista.append(equipes[i].getNome()).append("\n");
        }

        JOptionPane.showMessageDialog(null, lista.toString());
    }

    public void listarJogos() {

        if (qtdJogos == 0) {
            JOptionPane.showMessageDialog(null,
                    "Não existem jogos cadastrados!");
            return;
        }

        StringBuilder lista = new StringBuilder(
                "JOGOS DO CAMPEONATO: " + nome + "\n\n");

        for (int i = 0; i < qtdJogos; i++) {

            lista.append("Jogo ").append(i + 1).append("\n");

            lista.append("Equipe 1: ")
                    .append(jogos[i].getEquipe1().getNome())
                    .append("\n");

            lista.append("Equipe 2: ")
                    .append(jogos[i].getEquipe2().getNome())
                    .append("\n");

            lista.append("Data: ")
                    .append(jogos[i].getData())
                    .append("\n");

            lista.append("Horário: ")
                    .append(jogos[i].getHorario())
                    .append("\n");

            lista.append("Espaço: ")
                    .append(jogos[i].getEspaco().getNome())
                    .append("\n\n");
        }

        JOptionPane.showMessageDialog(null, lista.toString());
    }

    public void registrarResultadoJogo() {

        if (qtdJogos == 0) {
            JOptionPane.showMessageDialog(null,
                    "Não existem jogos cadastrados!");
            return;
        }

        String[] opcoes = new String[qtdJogos];

        for (int i = 0; i < qtdJogos; i++) {
            opcoes[i] = jogos[i].getEquipe1().getNome()
                    + " x " + jogos[i].getEquipe2().getNome()
                    + " - " + jogos[i].getData()
                    + " às " + jogos[i].getHorario();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Selecione o jogo:",
                "Registrar resultado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return;

        for (int i = 0; i < qtdJogos; i++) {

            if (opcoes[i].equals(escolha)) {

                if (jogos[i].isRealizado()) {
                    JOptionPane.showMessageDialog(null,
                            "O resultado deste jogo já foi registrado!");
                    return;
                }

                jogos[i].registrarResultado();
                return;
            }
        }
    }

    public void consultarResultados() {

        boolean encontrou = false;

        StringBuilder lista = new StringBuilder(
                "RESULTADOS DO CAMPEONATO: " + nome + "\n\n");

        for (int i = 0; i < qtdJogos; i++) {

            if (jogos[i].isRealizado()) {

                encontrou = true;

                lista.append(jogos[i].getEquipe1().getNome())
                        .append(" ")
                        .append(jogos[i].getPlacarEquipe1())
                        .append(" x ")
                        .append(jogos[i].getPlacarEquipe2())
                        .append(" ")
                        .append(jogos[i].getEquipe2().getNome())
                        .append("\n\n");
            }
        }

        if (!encontrou) {
            JOptionPane.showMessageDialog(null,
                    "Ainda não existem resultados registrados!");
            return;
        }

        JOptionPane.showMessageDialog(null, lista.toString());
    }

    public String getNome() {
        return nome;
    }

    public String getModalidade() {
        return modalidade;
    }

    public int getQtdEquipes() {
        return qtdEquipes;
    }

    public Equipe getEquipe(int indice) {
        if (indice >= 0 && indice < qtdEquipes) {
            return equipes[indice];
        }

        return null;
    }

    public int getQtdJogos() {
        return qtdJogos;
    }

    public Jogo getJogo(int indice) {
        if (indice >= 0 && indice < qtdJogos) {
            return jogos[indice];
        }

        return null;
    }
}
