package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Representa um campeonato esportivo.
 * Implementa Relatorio para geração de classificação e estatísticas.
 */
public class Campeonato extends EntidadeBase implements Relatorio {

    public static final String STATUS_ABERTO    = "ABERTO";
    public static final String STATUS_ENCERRADO = "ENCERRADO";

    private final List<Equipe> equipes = new ArrayList<>();
    private final List<Jogo>   jogos   = new ArrayList<>();
    private String modalidade;
    private String regulamento;
    private String status;

    public Campeonato(String nome, String modalidade) {
        super(nome);
        setModalidade(modalidade);
        this.status      = STATUS_ABERTO;
        this.regulamento = "";
    }

    // ── Equipes ──────────────────────────────────────────────────────────────

    public void adicionarEquipe(Equipe equipe) {
        // Regra 6: campeonato encerrado
        if (STATUS_ENCERRADO.equals(status)) {
            throw new CampeonatoEncerradoException(
                    "O campeonato '" + getNome() + "' está encerrado e não aceita novas inscrições.");
        }
        if (equipe == null) {
            JOptionPane.showMessageDialog(null, "Equipe inválida!"); return;
        }
        if (!equipe.getModalidade().equalsIgnoreCase(this.modalidade)) {
            JOptionPane.showMessageDialog(null,
                    "A modalidade da equipe não corresponde ao campeonato!"); return;
        }
        for (Equipe e : equipes) {
            if (e == equipe || (e.getNome().equalsIgnoreCase(equipe.getNome())
                    && e.getModalidade().equalsIgnoreCase(equipe.getModalidade()))) {
                JOptionPane.showMessageDialog(null, "Esta equipe já está no campeonato!"); return;
            }
        }
        equipes.add(equipe);
        // Notifica todos os atletas da equipe
        Notificacao n = new Notificacao(
                "Novo campeonato",
                "Sua equipe '" + equipe.getNome() + "' foi inscrita no campeonato '" + getNome() + "'.",
                equipe.getTecnico().getEmail());
        JOptionPane.showMessageDialog(null, "Equipe adicionada ao campeonato!");
    }

    // ── Jogos ────────────────────────────────────────────────────────────────

    public void adicionarJogo(Jogo jogo) {
        if (STATUS_ENCERRADO.equals(status)) {
            throw new CampeonatoEncerradoException(
                    "Não é possível adicionar jogos a um campeonato encerrado.");
        }
        if (jogo == null) {
            JOptionPane.showMessageDialog(null, "Jogo inválido!"); return;
        }
        if (!equipes.contains(jogo.getEquipe1()) || !equipes.contains(jogo.getEquipe2())) {
            JOptionPane.showMessageDialog(null,
                    "As duas equipes precisam participar do campeonato!"); return;
        }
        for (Jogo j : jogos) {
            boolean mesmasEquipes = (j.getEquipe1() == jogo.getEquipe1()
                    && j.getEquipe2() == jogo.getEquipe2())
                    || (j.getEquipe1() == jogo.getEquipe2()
                    && j.getEquipe2() == jogo.getEquipe1());
            boolean mesmoHorario = j.getData().equals(jogo.getData())
                    && j.getHorario().equals(jogo.getHorario());
            boolean equipeEmConflito = j.getEquipe1() == jogo.getEquipe1()
                    || j.getEquipe2() == jogo.getEquipe1()
                    || j.getEquipe1() == jogo.getEquipe2()
                    || j.getEquipe2() == jogo.getEquipe2();
            if (mesmasEquipes) {
                JOptionPane.showMessageDialog(null, "Esta partida já foi cadastrada!"); return;
            }
            if (equipeEmConflito && mesmoHorario) {
                JOptionPane.showMessageDialog(null,
                        "Uma das equipes já possui um jogo nesta data e horário!"); return;
            }
        }
        jogos.add(jogo);
        JOptionPane.showMessageDialog(null, "Jogo cadastrado com sucesso!");
    }

    // ── Encerrar campeonato ───────────────────────────────────────────────────

    public void encerrar() {
        this.status = STATUS_ENCERRADO;
        JOptionPane.showMessageDialog(null, "Campeonato '" + getNome() + "' encerrado!");
    }

    // ── Classificação ────────────────────────────────────────────────────────

    /**
     * Calcula e exibe a tabela de classificação do campeonato.
     * Vitória = 3 pts | Empate = 1 pt | Derrota = 0 pts
     */
    public void gerarClassificacao() {
        if (equipes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma equipe no campeonato!"); return;
        }
        // Pontuação por equipe
        int[] pontos   = new int[equipes.size()];
        int[] vitorias = new int[equipes.size()];
        int[] empates  = new int[equipes.size()];
        int[] derrotas = new int[equipes.size()];
        int[] golsPro  = new int[equipes.size()];
        int[] golsCon  = new int[equipes.size()];

        for (Jogo j : jogos) {
            if (!j.isRealizado()) continue;
            int i1 = equipes.indexOf(j.getEquipe1());
            int i2 = equipes.indexOf(j.getEquipe2());
            if (i1 < 0 || i2 < 0) continue;
            int p1 = j.getPlacarEquipe1(), p2 = j.getPlacarEquipe2();
            golsPro[i1] += p1; golsCon[i1] += p2;
            golsPro[i2] += p2; golsCon[i2] += p1;
            if (p1 > p2)      { pontos[i1] += 3; vitorias[i1]++; derrotas[i2]++; }
            else if (p2 > p1) { pontos[i2] += 3; vitorias[i2]++; derrotas[i1]++; }
            else              { pontos[i1]++; pontos[i2]++; empates[i1]++; empates[i2]++; }
        }

        // Ordena por pontos (bubble sort simples)
        Integer[] ordem = new Integer[equipes.size()];
        for (int i = 0; i < ordem.length; i++) ordem[i] = i;
        for (int i = 0; i < ordem.length - 1; i++)
            for (int j = i + 1; j < ordem.length; j++)
                if (pontos[ordem[i]] < pontos[ordem[j]]) {
                    int tmp = ordem[i]; ordem[i] = ordem[j]; ordem[j] = tmp;
                }

        StringBuilder sb = new StringBuilder(
                " CLASSIFICAÇÃO – " + getNome() + " (" + modalidade + ")\n\n");
        sb.append(String.format("%-4s %-20s %3s %3s %3s %3s %4s %4s %3s\n",
                "Pos", "Equipe", "PJ", "V", "E", "D", "GP", "GC", "PTS"));
        sb.append("─".repeat(55)).append("\n");
        for (int pos = 0; pos < ordem.length; pos++) {
            int i = ordem[pos];
            int pj = vitorias[i] + empates[i] + derrotas[i];
            sb.append(String.format("%-4d %-20s %3d %3d %3d %3d %4d %4d %3d\n",
                    pos + 1, equipes.get(i).getNome(),
                    pj, vitorias[i], empates[i], derrotas[i],
                    golsPro[i], golsCon[i], pontos[i]));
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Classificação",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Listagens ─────────────────────────────────────────────────────────────

    public void listarEquipes() {
        if (equipes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem equipes neste campeonato!"); return;
        }
        StringBuilder lista = new StringBuilder(
                "CAMPEONATO: " + getNome() + "\nMODALIDADE: " + modalidade
                        + "\nSTATUS: " + status + "\n\nEQUIPES PARTICIPANTES:\n\n");
        for (Equipe e : equipes) lista.append("• ").append(e.getNome()).append("\n");
        JOptionPane.showMessageDialog(null, lista.toString());
    }

    public void listarJogos() {
        if (jogos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem jogos cadastrados!"); return;
        }
        StringBuilder lista = new StringBuilder("JOGOS DO CAMPEONATO: " + getNome() + "\n\n");
        int i = 1;
        for (Jogo j : jogos) {
            lista.append("Jogo ").append(i++).append("\n")
                    .append("  ").append(j.getEquipe1().getNome())
                    .append(" x ").append(j.getEquipe2().getNome()).append("\n")
                    .append("  Data: ").append(j.getData())
                    .append("  Horário: ").append(j.getHorario()).append("\n")
                    .append("  Espaço: ").append(j.getEspaco().getNome()).append("\n")
                    .append("  Resultado: ")
                    .append(j.isRealizado()
                            ? j.getPlacarEquipe1() + " x " + j.getPlacarEquipe2()
                            : "Pendente")
                    .append("\n\n");
        }
        JOptionPane.showMessageDialog(null, lista.toString());
    }

    // ── Resultados ───────────────────────────────────────────────────────────

    public void registrarResultadoJogo() {
        if (jogos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem jogos cadastrados!"); return;
        }
        String[] opcoes = new String[jogos.size()];
        int i = 0;
        for (Jogo j : jogos)
            opcoes[i++] = j.getEquipe1().getNome() + " x " + j.getEquipe2().getNome()
                    + " – " + j.getData() + " às " + j.getHorario();
        String escolha = (String) JOptionPane.showInputDialog(null, "Selecione o jogo:",
                "Registrar resultado", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;
        for (Jogo j : jogos) {
            String label = j.getEquipe1().getNome() + " x " + j.getEquipe2().getNome()
                    + " – " + j.getData() + " às " + j.getHorario();
            if (label.equals(escolha)) {
                if (j.isRealizado()) {
                    JOptionPane.showMessageDialog(null, "Resultado já registrado!"); return;
                }
                j.registrarResultado();
                return;
            }
        }
    }

    public void consultarResultados() {
        boolean encontrou = false;
        StringBuilder lista = new StringBuilder("RESULTADOS – " + getNome() + "\n\n");
        for (Jogo j : jogos) {
            if (j.isRealizado()) {
                encontrou = true;
                lista.append(j.getEquipe1().getNome()).append(" ")
                        .append(j.getPlacarEquipe1()).append(" x ")
                        .append(j.getPlacarEquipe2()).append(" ")
                        .append(j.getEquipe2().getNome()).append("\n");
            }
        }
        if (!encontrou) {
            JOptionPane.showMessageDialog(null, "Ainda não existem resultados registrados!"); return;
        }
        JOptionPane.showMessageDialog(null, lista.toString());
    }

    // ── Relatorio ────────────────────────────────────────────────────────────

    @Override
    public void gerarRelatorio() {
        gerarClassificacao();
    }

    @Override
    public String resumo() {
        return getNome() + " [" + modalidade + "] – " + status
                + " | Equipes: " + equipes.size() + " | Jogos: " + jogos.size();
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public String getModalidade() { return modalidade; }
    public void setModalidade(String modalidade) {
        if (modalidade == null || modalidade.trim().isEmpty())
            throw new IllegalArgumentException("A modalidade não pode ficar vazia!");
        this.modalidade = modalidade.trim();
    }

    public String getRegulamento() { return regulamento; }
    public void setRegulamento(String regulamento) { this.regulamento = regulamento; }

    public String getStatus() { return status; }

    public int getQtdEquipes() { return equipes.size(); }
    public Equipe getEquipe(int indice) {
        if (indice >= 0 && indice < equipes.size()) return equipes.get(indice);
        return null;
    }

    public int getQtdJogos() { return jogos.size(); }
    public Jogo getJogo(int indice) {
        if (indice >= 0 && indice < jogos.size()) return jogos.get(indice);
        return null;
    }

    public List<Equipe> getEquipes() { return new ArrayList<>(equipes); }
    public List<Jogo>   getJogos()   { return new ArrayList<>(jogos); }
}
