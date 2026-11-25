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
        this.nome = nome;
        this.modalidade = modalidade;
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
            if (equipes[i] == equipe) {
                JOptionPane.showMessageDialog(null,
                        "Esta equipe já está no campeonato!");
                return;
            }
        }

        if (qtdEquipes < equipes.length) {
            equipes[qtdEquipes] = equipe;
            qtdEquipes++;

            JOptionPane.showMessageDialog(null,
                    "Equipe adicionada ao campeonato!");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Limite de equipes atingido!");
        }
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

        if (qtdJogos < jogos.length) {
            jogos[qtdJogos] = jogo;
            qtdJogos++;

            JOptionPane.showMessageDialog(null,
                    "Jogo cadastrado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Limite de jogos atingido!");
        }
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
                    .append("\n\n");
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
}
