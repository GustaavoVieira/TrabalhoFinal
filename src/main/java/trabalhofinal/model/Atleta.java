package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Atleta extends Usuario implements Notificavel, Relatorio {

    private String matricula;
    private String curso;
    private String modalidade;
    private boolean suspenso = false;
    private int nivelDesempenho = 7;
    private String disponibilidadeHorario = "Noite";
    private final List<String> historico = new ArrayList<>();

    public Atleta(String nome, String email, String senha,
                  String matricula, String curso, String modalidade) {
        super(nome, email, senha);
        setMatricula(matricula);
        this.curso      = curso;
        this.modalidade = modalidade;
    }

    public void solicitarEquipe(List<Equipe> equipes) {
        if (equipes == null || equipes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem equipes cadastradas!");
            return;
        }

        List<Equipe> compativeis = new ArrayList<>();
        for (Equipe e : equipes) {
            if (e.getModalidade().equalsIgnoreCase(this.modalidade)) {
                compativeis.add(e);
            }
        }

        if (compativeis.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Não há equipes da sua modalidade (" + modalidade + ") cadastradas!");
            return;
        }

        String[] opcoes = new String[compativeis.size()];
        for (int i = 0; i < compativeis.size(); i++) {
            opcoes[i] = compativeis.get(i).getNome() + " – " + compativeis.get(i).getModalidade();
        }

        String escolha = (String) JOptionPane.showInputDialog(null,
                "Escolha a equipe desejada:", "Solicitar participação",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;

        for (Equipe equipe : compativeis) {
            if ((equipe.getNome() + " – " + equipe.getModalidade()).equals(escolha)) {
                if (equipe.getTecnico() == null) {
                    JOptionPane.showMessageDialog(null, "Esta equipe não possui técnico!");
                    return;
                }
                SolicitacaoEquipe solicitacao = new SolicitacaoEquipe(this, equipe);
                equipe.getTecnico().receberSolicitacao(solicitacao);
                break;
            }
        }
    }

    public void sugerirEquipesInteligente() {
        List<SistemaFormacaoEquipes.Sugestao> sugestoes =
                SistemaFormacaoEquipes.sugerirEquipesParaAtleta(this, RepositorioDados.getEquipes());

        if (sugestoes.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Nenhuma equipe compatível encontrada para o seu perfil no momento.");
            return;
        }

        StringBuilder sb = new StringBuilder(" EQUIPES SUGERIDAS PARA VOCÊ :\n\n");
        for (int i = 0; i < sugestoes.size(); i++) {
            SistemaFormacaoEquipes.Sugestao s = sugestoes.get(i);
            sb.append(i + 1).append(". ").append(s.getEquipe().getNome())
              .append(" [Compatibilidade: ").append(s.getPontuacaoCompatibilidade()).append("%]\n")
              .append("   Motivo: ").append(s.getJustificativa()).append("\n\n");
        }

        int escolha = JOptionPane.showConfirmDialog(null,
                sb.toString() + "Deseja solicitar entrada na equipe mais recomendada ("
                        + sugestoes.get(0).getEquipe().getNome() + ")?",
                "Sugestão de Equipes", JOptionPane.YES_NO_OPTION);

        if (escolha == JOptionPane.YES_OPTION) {
            Equipe top = sugestoes.get(0).getEquipe();
            if (top.getTecnico() != null) {
                SolicitacaoEquipe sol = new SolicitacaoEquipe(this, top);
                top.getTecnico().receberSolicitacao(sol);
            }
        }
    }

    public void consultarMeusJogosEResultados() {
        List<Campeonato> camps = RepositorioDados.getCampeonatos();
        StringBuilder sb = new StringBuilder(" SEUS JOGOS E RESULTADOS:\n\n");
        boolean temJogos = false;

        for (Campeonato c : camps) {
            for (Jogo j : c.getJogos()) {
                boolean participou = j.getEscalacaoEquipe1().contains(this)
                        || j.getEscalacaoEquipe2().contains(this)
                        || j.getEquipe1().getAtletas().contains(this)
                        || j.getEquipe2().getAtletas().contains(this);

                if (participou) {
                    temJogos = true;
                    sb.append("• ").append(c.getNome()).append(": ")
                      .append(j.getEquipe1().getNome()).append(" ")
                      .append(j.isRealizado() ? (j.getPlacarEquipe1() + " x " + j.getPlacarEquipe2()) : "vs")
                      .append(" ").append(j.getEquipe2().getNome())
                      .append(" (").append(j.getData()).append(" às ").append(j.getHorario()).append(")")
                      .append(j.isRealizado() ? " [Finalizado]" : " [Agendado]")
                      .append("\n");
                }
            }
        }

        if (!temJogos) {
            JOptionPane.showMessageDialog(null, "Você ainda não participou de jogos cadastrados.");
        } else {
            JOptionPane.showMessageDialog(null, sb.toString(), "Meus Jogos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void consultarEstatisticasPessoais() {
        int totalJogos = 0;
        for (Campeonato c : RepositorioDados.getCampeonatos()) {
            for (Jogo j : c.getJogos()) {
                if (j.isRealizado() && (j.getEscalacaoEquipe1().contains(this) || j.getEscalacaoEquipe2().contains(this))) {
                    totalJogos++;
                }
            }
        }

        String stats = " ESTATÍSTICAS PESSOAIS DE " + getNome().toUpperCase() + "\n\n"
                + "• Modalidade: " + modalidade + "\n"
                + "• Nível de Desempenho: " + nivelDesempenho + "/10\n"
                + "• Disponibilidade: " + disponibilidadeHorario + "\n"
                + "• Partidas oficiais disputadas: " + totalJogos + "\n"
                + "• Eventos no histórico: " + historico.size() + "\n"
                + "• Situação disciplinar: " + (suspenso ? "⚠ SUSPENSO" : "Regular / Apto");

        JOptionPane.showMessageDialog(null, stats, "Estatísticas Pessoais", JOptionPane.INFORMATION_MESSAGE);
    }

    public void adicionarHistorico(String evento) {
        if (evento != null && !evento.trim().isEmpty()) {
            historico.add(evento.trim());
        }
    }

    public void consultarHistorico() {
        if (historico.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum evento no seu histórico ainda.");
            return;
        }
        StringBuilder sb = new StringBuilder(" SEU HISTÓRICO ESPORTIVO\n\n");
        for (int i = 0; i < historico.size(); i++) {
            sb.append(i + 1).append(". ").append(historico.get(i)).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    @Override
    public void receberNotificacao(String titulo, String mensagem) {
        new Notificacao(titulo, mensagem, getEmail());
        JOptionPane.showMessageDialog(null,
                "🔔 " + titulo + "\n\n" + mensagem, "Notificação",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void consultarNotificacoes() {
        List<Notificacao> notifs = Notificacao.getNotificacoesPara(getEmail());
        if (notifs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma notificação.");
            return;
        }
        StringBuilder sb = new StringBuilder(" SUAS NOTIFICAÇÕES\n\n");
        for (Notificacao n : notifs) {
            sb.append(n).append("\n");
            n.marcarComoLida();
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    @Override
    public void gerarRelatorio() {
        exibirDados();
        consultarHistorico();
    }

    @Override
    public String resumo() {
        return getNome() + " [" + matricula + "] – " + modalidade
                + (suspenso ? " ⚠ SUSPENSO" : "")
                + " (Desempenho: " + nivelDesempenho + "/10)";
    }

    @Override
    public void exibirDados() {
        super.exibirDados();
        JOptionPane.showMessageDialog(null,
                "Matrícula: " + matricula
                        + "\nCurso: " + curso
                        + "\nModalidade: " + modalidade
                        + "\nNível de Desempenho: " + nivelDesempenho + "/10"
                        + "\nDisponibilidade: " + disponibilidadeHorario
                        + "\nStatus: " + (suspenso ? "SUSPENSO" : "Ativo"));
    }

    @Override
    public void exibirMenu() {
        int opcao;
        do {
            String[] opcoes = {
                    "Meu Perfil e Histórico",
                    "Equipes",
                    "Competições e Avisos",
                    "Sair"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "Bem-vindo, Atleta " + getNome() + "!\nEscolha uma opção:",
                    "Menu do Atleta", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0 -> menuPerfil();
                case 1 -> menuEquipes();
                case 2 -> menuCompeticoes();
                case 3, -1 -> JOptionPane.showMessageDialog(null, "Saindo do menu do atleta...");
            }
        } while (opcao != 3 && opcao != -1);
    }

    private void menuPerfil() {
        int opcao;
        do {
            String[] opcoes = {
                    "Consultar meus dados",
                    "Minhas estatísticas pessoais",
                    "Consultar meu histórico",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "MEU PERFIL E HISTÓRICO", "Perfil do Atleta",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0 -> exibirDados();
                case 1 -> consultarEstatisticasPessoais();
                case 2 -> consultarHistorico();
            }
        } while (opcao != 3 && opcao != -1);
    }

    private void menuEquipes() {
        int opcao;
        do {
            String[] opcoes = {
                    "Solicitar entrada em equipe",
                    "Sugerir equipes",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "EQUIPES", "Equipes - Atleta",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0 -> solicitarEquipe(RepositorioDados.getEquipes());
                case 1 -> sugerirEquipesInteligente();
            }
        } while (opcao != 2 && opcao != -1);
    }

    private void menuCompeticoes() {
        int opcao;
        do {
            String[] opcoes = {
                    "Campeonatos disponíveis",
                    "Meus jogos e resultados",
                    "Ver notificações",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "COMPETIÇÕES E AVISOS", "Competições - Atleta",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0 -> GerenciadorCampeonatos.consultarCampeonatos();
                case 1 -> consultarMeusJogosEResultados();
                case 2 -> consultarNotificacoes();
            }
        } while (opcao != 3 && opcao != -1);
    }

    public void setMatricula(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("A matrícula não pode ficar vazia!");
        }
        this.matricula = matricula.trim();
    }
    public String getMatricula()             { return matricula; }
    public String getCurso()                 { return curso; }
    public String getModalidade()            { return modalidade; }

    public boolean isSuspenso()              { return suspenso; }
    public void suspender()                  { this.suspenso = true; }
    public void reativar()                   { this.suspenso = false; }

    public int getNivelDesempenho()          { return nivelDesempenho; }
    public void setNivelDesempenho(int n)    { this.nivelDesempenho = n; }

    public String getDisponibilidadeHorario(){ return disponibilidadeHorario; }
    public void setDisponibilidadeHorario(String disp) { this.disponibilidadeHorario = disp; }

    public List<String> getHistorico()       { return new ArrayList<>(historico); }
}
