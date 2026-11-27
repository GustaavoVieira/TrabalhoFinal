package trabalhofinal.service;

import trabalhofinal.exception.*;
import trabalhofinal.model.*;
import trabalhofinal.repository.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Gerencia relatórios, estatísticas, rankings e consultas de equipes.
 */
public class GerenciadorRelatorios {

    public static boolean adicionarEquipe(Equipe equipe) {
        if (equipe == null) {
            JOptionPane.showMessageDialog(null, "Equipe inválida!");
            return false;
        }
        if (RepositorioDados.equipeJaCadastrada(equipe.getNome(), equipe.getModalidade())) {
            JOptionPane.showMessageDialog(null, "Esta equipe já está cadastrada!");
            return false;
        }
        RepositorioDados.getEquipes().add(equipe);
        return true;
    }

    public static void consultarEquipes() {
        List<Equipe> equipes = RepositorioDados.getEquipes();
        if (equipes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma equipe cadastrada!");
            return;
        }
        StringBuilder sb = new StringBuilder("EQUIPES CADASTRADAS:\n\n");
        for (Equipe e : equipes) sb.append("• ").append(e.resumo()).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void consultarHistoricoEquipe() {
        List<Equipe> equipes = RepositorioDados.getEquipes();
        if (equipes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma equipe cadastrada!");
            return;
        }
        String[] opcoes = equipes.stream()
                .map(e -> e.getNome() + " – " + e.getModalidade()).toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Selecione a equipe:", "Histórico da Equipe", JOptionPane.QUESTION_MESSAGE,
                null, opcoes, opcoes[0]);
        if (escolha == null) return;
        equipes.stream()
                .filter(e -> (e.getNome() + " – " + e.getModalidade()).equals(escolha))
                .findFirst()
                .ifPresent(Equipe::listarHistoricoCampeonatos);
    }

    public static void relatorioAtletas() {
        List<Usuario> usuarios = RepositorioDados.getUsuarios();
        long qtd = usuarios.stream().filter(u -> u instanceof Atleta).count();
        long suspensos = usuarios.stream()
                .filter(u -> u instanceof Atleta && ((Atleta) u).isSuspenso()).count();

        JOptionPane.showMessageDialog(null,
                "📊 RELATÓRIO DE ATLETAS\n\n"
                        + "• Total de atletas cadastrados: " + qtd + "\n"
                        + "• Atletas aptos: " + (qtd - suspensos) + "\n"
                        + "• Atletas suspensos: " + suspensos);
    }

    public static void relatorioEquipes() {
        List<Equipe> equipes = RepositorioDados.getEquipes();
        long ativas = equipes.stream().filter(EntidadeBase::isAtivo).count();
        JOptionPane.showMessageDialog(null,
                "📊 RELATÓRIO DE EQUIPES\n\n"
                        + "• Equipes cadastradas: " + equipes.size() + "\n"
                        + "• Equipes ativas: " + ativas);
    }

    public static void relatorioJogosPorModalidade() {
        List<Campeonato> campeonatos = RepositorioDados.getCampeonatos();
        if (campeonatos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum campeonato cadastrado!");
            return;
        }
        Map<String, Integer> contagem = new LinkedHashMap<>();
        for (Campeonato c : campeonatos) {
            String mod = c.getModalidade();
            int realizados = 0;
            for (int i = 0; i < c.getQtdJogos(); i++) {
                if (c.getJogo(i).isRealizado()) realizados++;
            }
            contagem.merge(mod, realizados, Integer::sum);
        }
        StringBuilder sb = new StringBuilder("📊 JOGOS REALIZADOS POR MODALIDADE:\n\n");
        for (Map.Entry<String, Integer> e : contagem.entrySet()) {
            sb.append("• ").append(e.getKey()).append(": ").append(e.getValue()).append(" partida(s) finalizada(s)\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void relatorioRankingAtletas() {
        List<Atleta> listaAtletas = new ArrayList<>();
        for (Usuario u : RepositorioDados.getUsuarios()) {
            if (u instanceof Atleta a) listaAtletas.add(a);
        }
        if (listaAtletas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum atleta cadastrado.");
            return;
        }
        listaAtletas.sort((a1, a2) -> {
            int c1 = Integer.compare(a2.getHistorico().size(), a1.getHistorico().size());
            if (c1 != 0) return c1;
            return Integer.compare(a2.getNivelDesempenho(), a1.getNivelDesempenho());
        });
        StringBuilder sb = new StringBuilder("🏆 RANKING OFICIAL DE ATLETAS (Participações & Desempenho):\n\n");
        for (int i = 0; i < listaAtletas.size(); i++) {
            Atleta a = listaAtletas.get(i);
            sb.append(i + 1).append("º Lugar: ").append(a.getNome())
              .append(" (").append(a.getModalidade()).append(")")
              .append(" - ").append(a.getHistorico().size()).append(" eventos no histórico | ")
              .append("Desempenho: ").append(a.getNivelDesempenho()).append("/10\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Ranking de Atletas", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void relatorioOcupacaoEspacos() {
        List<EspacoEsportivo> espacos = RepositorioDados.getEspacos();
        if (espacos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum espaço esportivo cadastrado!");
            return;
        }
        StringBuilder sb = new StringBuilder("📊 TAXA DE OCUPAÇÃO E UTILIZAÇÃO DOS ESPAÇOS:\n\n");
        for (EspacoEsportivo e : espacos) {
            long totalReservas = RepositorioDados.getReservas().stream()
                    .filter(r -> r.getEspaco().equals(e)).count();
            long totalJogos = RepositorioDados.getCampeonatos().stream()
                    .flatMap(c -> c.getJogos().stream())
                    .filter(j -> j.getEspaco().equals(e)).count();
            long totalUtilizacoes = totalReservas + totalJogos;

            sb.append("• ").append(e.getNome())
              .append(" [").append(e.getTipo()).append(" - ").append(e.getStatusFuncionamento()).append("]:\n")
              .append("   - Reservas: ").append(totalReservas)
              .append(" | Jogos: ").append(totalJogos)
              .append(" | Total de agendamentos: ").append(totalUtilizacoes).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
