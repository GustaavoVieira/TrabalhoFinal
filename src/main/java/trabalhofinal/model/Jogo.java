package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Representa uma partida/jogo de um campeonato.
 * Implementa controle de escalação e regras de suspensão (Regra de Negócio 7).
 */
public class Jogo {

    private Equipe equipe1;
    private Equipe equipe2;
    private EspacoEsportivo espaco;
    private String data;
    private String horario;
    private int placarEquipe1;
    private int placarEquipe2;
    private boolean realizado;

    private final List<Atleta> escalacaoEquipe1 = new ArrayList<>();
    private final List<Atleta> escalacaoEquipe2 = new ArrayList<>();

    public Jogo(Equipe equipe1, Equipe equipe2,
                EspacoEsportivo espaco, String data, String horario) {

        if (equipe1 == null || equipe2 == null) {
            throw new IllegalArgumentException("As equipes não podem ser nulas!");
        }
        if (equipe1 == equipe2) {
            throw new IllegalArgumentException("Uma equipe não pode jogar contra ela mesma!");
        }
        if (espaco == null) {
            throw new IllegalArgumentException("O jogo precisa possuir um espaço esportivo!");
        }
        if (data == null || data.trim().isEmpty() || horario == null || horario.trim().isEmpty()) {
            throw new IllegalArgumentException("Data e horário são obrigatórios!");
        }

        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.espaco = espaco;
        this.data = data.trim();
        this.horario = horario.trim();
        this.placarEquipe1 = 0;
        this.placarEquipe2 = 0;
        this.realizado = false;
    }

    // ── Escalação (Regra de Negócio 7) ───────────────────────────────────────

    /**
     * Escala um atleta para a partida.
     * Regra 7: Atletas suspensos não podem ser escalados.
     */
    public void escalarAtleta(Equipe equipe, Atleta atleta) {
        if (equipe == null || atleta == null) {
            throw new IllegalArgumentException("Equipe e atleta são obrigatórios.");
        }

        if (atleta.isSuspenso()) {
            throw new AtletaSuspensException(
                    "O atleta " + atleta.getNome() + " está suspenso e não pode ser escalado para esta partida!");
        }

        if (equipe.equals(equipe1)) {
            if (!escalacaoEquipe1.contains(atleta)) {
                escalacaoEquipe1.add(atleta);
            }
        } else if (equipe.equals(equipe2)) {
            if (!escalacaoEquipe2.contains(atleta)) {
                escalacaoEquipe2.add(atleta);
            }
        } else {
            throw new IllegalArgumentException("A equipe indicada não participa deste jogo!");
        }
    }

    public void listarEscalacoes() {
        StringBuilder sb = new StringBuilder("ESCALAÇÃO DO JOGO\n\n");
        sb.append(equipe1.getNome()).append(":\n");
        if (escalacaoEquipe1.isEmpty()) {
            sb.append("  (Nenhum atleta escalado ainda)\n");
        } else {
            for (Atleta a : escalacaoEquipe1) {
                sb.append("  • ").append(a.getNome()).append(" [").append(a.getMatricula()).append("]\n");
            }
        }

        sb.append("\n").append(equipe2.getNome()).append(":\n");
        if (escalacaoEquipe2.isEmpty()) {
            sb.append("  (Nenhum atleta escalado ainda)\n");
        } else {
            for (Atleta a : escalacaoEquipe2) {
                sb.append("  • ").append(a.getNome()).append(" [").append(a.getMatricula()).append("]\n");
            }
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Escalações", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Alteração de horário e local (Notificações) ──────────────────────────

    public void remarcar(String novaData, String novoHorario, EspacoEsportivo novoEspaco) {
        this.data = novaData.trim();
        this.horario = novoHorario.trim();
        if (novoEspaco != null) {
            this.espaco = novoEspaco;
        }

        String aviso = "O jogo entre " + equipe1.getNome() + " e " + equipe2.getNome()
                + " foi remarcado para " + this.data + " às " + this.horario
                + " no espaço " + this.espaco.getNome();

        if (equipe1.getTecnico() != null) {
            equipe1.getTecnico().receberNotificacao("Alteração de Horário de Jogo", aviso);
        }
        if (equipe2.getTecnico() != null) {
            equipe2.getTecnico().receberNotificacao("Alteração de Horário de Jogo", aviso);
        }
        for (Atleta a : escalacaoEquipe1) {
            a.receberNotificacao("Alteração de Horário de Jogo", aviso);
        }
        for (Atleta a : escalacaoEquipe2) {
            a.receberNotificacao("Alteração de Horário de Jogo", aviso);
        }
    }

    // ── Registro de Resultados ───────────────────────────────────────────────

    public void registrarResultado() {
        if (realizado) {
            JOptionPane.showMessageDialog(null, "O resultado deste jogo já foi registrado!");
            return;
        }

        int placar1;
        int placar2;

        while (true) {
            String res1 = JOptionPane.showInputDialog("Digite o placar da equipe " + equipe1.getNome() + ":");
            if (res1 == null) return;
            try {
                placar1 = Integer.parseInt(res1.trim());
                if (placar1 < 0) {
                    JOptionPane.showMessageDialog(null, "O placar não pode ser negativo!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite apenas números inteiros!");
            }
        }

        while (true) {
            String res2 = JOptionPane.showInputDialog("Digite o placar da equipe " + equipe2.getNome() + ":");
            if (res2 == null) return;
            try {
                placar2 = Integer.parseInt(res2.trim());
                if (placar2 < 0) {
                    JOptionPane.showMessageDialog(null, "O placar não pode ser negativo!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite apenas números inteiros!");
            }
        }

        placarEquipe1 = placar1;
        placarEquipe2 = placar2;
        realizado = true;

        // Atualiza histórico dos atletas participantes
        String descJogo = "Jogo: " + equipe1.getNome() + " " + placar1 + " x " + placar2 + " " + equipe2.getNome();
        for (Atleta a : escalacaoEquipe1) a.adicionarHistorico(descJogo);
        for (Atleta a : escalacaoEquipe2) a.adicionarHistorico(descJogo);

        JOptionPane.showMessageDialog(null, "Resultado registrado com sucesso!");
    }

    public void exibirDados() {
        String resultado = realizado ? (placarEquipe1 + " x " + placarEquipe2) : "Ainda não realizado";
        JOptionPane.showMessageDialog(null,
                "EQUIPE 1: " + equipe1.getNome()
                        + "\nEQUIPE 2: " + equipe2.getNome()
                        + "\nEspaço: " + espaco.getNome()
                        + "\nData: " + data
                        + "\nHorário: " + horario
                        + "\nResultado: " + resultado);
    }

    // ── Getters / Setters ────────────────────────────────────────────────────

    public Equipe getEquipe1()              { return equipe1; }
    public Equipe getEquipe2()              { return equipe2; }
    public EspacoEsportivo getEspaco()      { return espaco; }
    public String getData()                 { return data; }
    public String getHorario()              { return horario; }
    public boolean isRealizado()            { return realizado; }
    public int getPlacarEquipe1()           { return placarEquipe1; }
    public int getPlacarEquipe2()           { return placarEquipe2; }
    public List<Atleta> getEscalacaoEquipe1(){ return new ArrayList<>(escalacaoEquipe1); }
    public List<Atleta> getEscalacaoEquipe2(){ return new ArrayList<>(escalacaoEquipe2); }
}
