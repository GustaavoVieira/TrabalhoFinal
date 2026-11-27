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
 * Técnico esportivo – gerencia equipes, convoca atletas, cadastra escalações e solicita reservas.
 */
public class Tecnico extends Usuario {

    private final List<SolicitacaoEquipe> solicitacoes = new ArrayList<>();
    private final List<Equipe>            equipes      = new ArrayList<>();

    public Tecnico(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    // ── Equipes ──────────────────────────────────────────────────────────────

    public void criarEquipe() {
        if (equipes.size() >= 50) {
            JOptionPane.showMessageDialog(null, "Limite de equipes atingido!");
            return;
        }
        String nome = JOptionPane.showInputDialog("Nome da equipe:");
        if (nome == null || nome.trim().isEmpty()) return;
        String modalidade = JOptionPane.showInputDialog("Modalidade da equipe:");
        if (modalidade == null || modalidade.trim().isEmpty()) return;
        if (RepositorioDados.equipeJaCadastrada(nome.trim(), modalidade.trim())) {
            JOptionPane.showMessageDialog(null, "Já existe uma equipe com esse nome e modalidade!");
            return;
        }
        Equipe equipe = new Equipe(nome.trim(), modalidade.trim(), this);
        if (GerenciadorRelatorios.adicionarEquipe(equipe)) {
            equipes.add(equipe);
            JOptionPane.showMessageDialog(null, "Equipe criada com sucesso!");
        }
    }

    public void consultarEquipes() {
        if (equipes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma equipe cadastrada!");
            return;
        }
        StringBuilder lista = new StringBuilder("SUAS EQUIPES:\n\n");
        for (Equipe e : equipes) {
            lista.append("• ").append(e.resumo()).append("\n");
        }
        JOptionPane.showMessageDialog(null, lista.toString());
    }

    // ── Solicitações de Atletas ──────────────────────────────────────────────

    @Override
    public void receberSolicitacao(SolicitacaoEquipe solicitacao) {
        if (solicitacao == null || solicitacao.getAtleta() == null
                || solicitacao.getEquipe() == null) {
            JOptionPane.showMessageDialog(null, "Solicitação inválida!");
            return;
        }
        if (solicitacao.getEquipe().getTecnico() != this) {
            JOptionPane.showMessageDialog(null, "Esta equipe não pertence a este técnico!");
            return;
        }
        for (SolicitacaoEquipe s : solicitacoes) {
            if (s.getAtleta().getMatricula().equalsIgnoreCase(solicitacao.getAtleta().getMatricula())
                    && s.getEquipe() == solicitacao.getEquipe()
                    && (s.getSituacao().equals("PENDENTE") || s.getSituacao().equals("ACEITA"))) {
                JOptionPane.showMessageDialog(null,
                        "Este atleta já possui solicitação pendente ou já foi aceito!");
                return;
            }
        }
        solicitacoes.add(solicitacao);
        JOptionPane.showMessageDialog(null,
                "Solicitação de " + solicitacao.getAtleta().getNome()
                        + " recebida! Aguarda sua análise.");
    }

    public void gerenciarSolicitacoes() {
        boolean encontrou = false;
        for (SolicitacaoEquipe s : solicitacoes) {
            if (s.getSituacao().equals("PENDENTE") && s.getEquipe().getTecnico() == this) {
                encontrou = true;
                Atleta atleta = s.getAtleta();
                Equipe equipe = s.getEquipe();
                int resposta = JOptionPane.showConfirmDialog(null,
                        "Atleta: " + atleta.getNome()
                                + "\nMatrícula: " + atleta.getMatricula()
                                + "\nCurso: " + atleta.getCurso()
                                + "\nEquipe: " + equipe.getNome()
                                + "\n\nDeseja aceitar esta solicitação?",
                        "Analisar solicitação", JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    if (RepositorioDados.atletaJaEmEquipeDaModalidade(atleta.getMatricula(), equipe.getModalidade())) {
                        JOptionPane.showMessageDialog(null,
                                "Regra de negócio violada: O atleta já está em outra equipe da modalidade "
                                        + equipe.getModalidade() + "!", "Acesso Negado",
                                JOptionPane.ERROR_MESSAGE);
                        s.setSituacao("RECUSADA");
                    } else {
                        try {
                            equipe.adicionarAtleta(atleta);
                            s.setSituacao("ACEITA");
                            atleta.receberNotificacao("Solicitação Aprovada",
                                    "Você foi aceito na equipe '" + equipe.getNome() + "'!");
                        } catch (AtletaJaEmEquipeException | AtletaSuspensException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(),
                                    "Erro ao aceitar", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else if (resposta == JOptionPane.NO_OPTION) {
                    s.setSituacao("RECUSADA");
                    atleta.receberNotificacao("Solicitação Recusada",
                            "Sua solicitação para a equipe '" + equipe.getNome() + "' foi recusada pelo técnico.");
                    JOptionPane.showMessageDialog(null, "Solicitação recusada.");
                }
            }
        }
        if (!encontrou) {
            JOptionPane.showMessageDialog(null, "Não há solicitações pendentes.");
        }
    }

    // ── Convocação e Escalação (Requisitos Técnicos) ──────────────────────────

    public void convocarAtleta() {
        Equipe equipe = selecionarEquipe();
        if (equipe == null) return;

        List<Atleta> disponiveis = RepositorioDados.getAtletasPorModalidade(equipe.getModalidade());
        if (disponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Não há atletas cadastrados na modalidade " + equipe.getModalidade());
            return;
        }

        String[] opcoes = disponiveis.stream()
                .map(a -> a.getNome() + " [" + a.getMatricula() + "]" + (a.isSuspenso() ? " ⚠ SUSPENSO" : ""))
                .toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Selecione o atleta a convocar:", "Convocar Atleta",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;

        for (Atleta a : disponiveis) {
            if ((a.getNome() + " [" + a.getMatricula() + "]" + (a.isSuspenso() ? " ⚠ SUSPENSO" : "")).equals(escolha)) {
                a.receberNotificacao("Convocação Oficial",
                        "Você foi convocado pelo técnico " + getNome()
                                + " para a equipe '" + equipe.getNome() + "'!");
                SolicitacaoEquipe sol = new SolicitacaoEquipe(a, equipe);
                receberSolicitacao(sol);
                return;
            }
        }
    }

    public void cadastrarEscalacao() {
        Equipe equipe = selecionarEquipe();
        if (equipe == null) return;

        if (equipe.getAtletas().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sua equipe não possui atletas para escalar!");
            return;
        }

        // Busca jogos futuros desta equipe
        List<Jogo> jogosDisponiveis = new ArrayList<>();
        for (Campeonato c : RepositorioDados.getCampeonatos()) {
            for (Jogo j : c.getJogos()) {
                if (!j.isRealizado() && (j.getEquipe1().equals(equipe) || j.getEquipe2().equals(equipe))) {
                    jogosDisponiveis.add(j);
                }
            }
        }

        if (jogosDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há jogos agendados pendentes para esta equipe!");
            return;
        }

        String[] opcoesJogos = jogosDisponiveis.stream()
                .map(j -> j.getEquipe1().getNome() + " vs " + j.getEquipe2().getNome() + " (" + j.getData() + ")")
                .toArray(String[]::new);

        String escolhaJogo = (String) JOptionPane.showInputDialog(null,
                "Selecione a partida para cadastrar a escalação:", "Escalação",
                JOptionPane.QUESTION_MESSAGE, null, opcoesJogos, opcoesJogos[0]);
        if (escolhaJogo == null) return;

        Jogo jogoSelecionado = null;
        for (int i = 0; i < opcoesJogos.length; i++) {
            if (opcoesJogos[i].equals(escolhaJogo)) {
                jogoSelecionado = jogosDisponiveis.get(i);
                break;
            }
        }
        if (jogoSelecionado == null) return;

        // Seleciona atletas da equipe para escalar
        String[] opcoesAtletas = equipe.getAtletas().stream()
                .map(a -> a.getNome() + " [" + a.getMatricula() + "]" + (a.isSuspenso() ? " ⚠ SUSPENSO" : ""))
                .toArray(String[]::new);

        String escolhaAtleta = (String) JOptionPane.showInputDialog(null,
                "Selecione o atleta para incluir na escalação titular:", "Escalar Atleta",
                JOptionPane.QUESTION_MESSAGE, null, opcoesAtletas, opcoesAtletas[0]);
        if (escolhaAtleta == null) return;

        for (Atleta a : equipe.getAtletas()) {
            if ((a.getNome() + " [" + a.getMatricula() + "]" + (a.isSuspenso() ? " ⚠ SUSPENSO" : "")).equals(escolhaAtleta)) {
                try {
                    // Regra 7: atirará exceção se atleta estiver suspenso!
                    jogoSelecionado.escalarAtleta(equipe, a);
                    JOptionPane.showMessageDialog(null,
                            "Atleta " + a.getNome() + " escalado com sucesso para a partida!");
                } catch (AtletaSuspensException ex) {
                    JOptionPane.showMessageDialog(null,
                            ex.getMessage(), "Violação de Regra Disciplinar", JOptionPane.ERROR_MESSAGE);
                }
                break;
            }
        }
    }

    // ── Reserva de Espaço para Equipe (Requisito 5) ───────────────────────────

    public void solicitarReservaParaEquipe() {
        Equipe equipe = selecionarEquipe();
        if (equipe == null) return;
        EspacoEsportivo espaco = InputHelper.selecionarEspaco();
        if (espaco == null) return;

        String data = InputHelper.solicitarData();
        if (data == null) return;
        String horario = InputHelper.solicitarHorario();
        if (horario == null) return;

        try {
            if (GerenciadorEspacosReservas.verificarDisponibilidade(espaco, data, horario)) {
                Reserva r = new Reserva(espaco, this, equipe, data, horario, Reserva.STATUS_PENDENTE);
                GerenciadorEspacosReservas.adicionarReserva(r);
                JOptionPane.showMessageDialog(null,
                        "Solicitação de reserva para a equipe '" + equipe.getNome()
                                + "' enviada à coordenação! Status: Pendente de aprovação.");
            }
        } catch (ReservaConflitoException | EspacoEmManutencaoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro na Reserva", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Sugestão Inteligente de Reforços (Extra) ─────────────────────────────

    public void sugerirReforcosInteligente() {
        Equipe equipe = selecionarEquipe();
        if (equipe == null) return;

        List<Atleta> recomendados = SistemaFormacaoEquipes.sugerirAtletasParaEquipe(equipe, RepositorioDados.getUsuarios());
        if (recomendados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há atletas disponíveis para reforço nesta modalidade.");
            return;
        }

        StringBuilder sb = new StringBuilder(" ATLETAS RECOMENDADOS PARA REFORÇO (" + equipe.getModalidade() + "):\n\n");
        for (int i = 0; i < recomendados.size(); i++) {
            Atleta a = recomendados.get(i);
            sb.append(i + 1).append(". ").append(a.getNome())
              .append(" [Matrícula: ").append(a.getMatricula()).append("]")
              .append(" - Desempenho: ").append(a.getNivelDesempenho()).append("/10")
              .append(" | Horário: ").append(a.getDisponibilidadeHorario())
              .append("\n");
        }

        int escolha = JOptionPane.showConfirmDialog(null,
                sb.toString() + "\nDeseja convocar o atleta mais indicado (" + recomendados.get(0).getNome() + ")?",
                "Sugestão de Reforços (IA)", JOptionPane.YES_NO_OPTION);

        if (escolha == JOptionPane.YES_OPTION) {
            Atleta top = recomendados.get(0);
            top.receberNotificacao("Convocação de Reforço",
                    "A equipe '" + equipe.getNome() + "' identificou seu excelente perfil e convocou você como reforço!");
            SolicitacaoEquipe sol = new SolicitacaoEquipe(top, equipe);
            receberSolicitacao(sol);
        }
    }

    // ── Estatísticas e Integrantes ───────────────────────────────────────────

    public void consultarIntegrantes() {
        Equipe equipe = selecionarEquipe();
        if (equipe != null) equipe.listarAtletas();
    }

    public void removerAtletaEquipe() {
        Equipe equipe = selecionarEquipe();
        if (equipe == null) return;
        String matricula = JOptionPane.showInputDialog("Matrícula do atleta a remover:");
        if (matricula == null || matricula.trim().isEmpty()) return;
        equipe.removerAtleta(matricula.trim());
    }

    public void consultarEstatisticas() {
        Equipe equipe = selecionarEquipe();
        if (equipe == null) return;
        JOptionPane.showMessageDialog(null, equipe.resumo(), "Estatísticas da Equipe",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Menu do Técnico ──────────────────────────────────────────────────────

    @Override
    public void exibirMenu() {
        int opcao;
        do {
            String[] opcoes = {
                    "Equipes",
                    "Atletas e Solicitações",
                    "Jogos e Treinos",
                    "Sair"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "Bem‑vindo, Técnico " + getNome() + "!\nEscolha uma opção:",
                    "Menu do Técnico", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0 -> menuEquipes();
                case 1 -> menuAtletas();
                case 2 -> menuJogosETreinos();
                case 3, -1 -> JOptionPane.showMessageDialog(null, "Saindo do menu do técnico...");
            }
        } while (opcao != 3 && opcao != -1);
    }

    private void menuEquipes() {
        int opcao;
        do {
            String[] opcoes = {
                    "Criar equipe",
                    "Consultar minhas equipes",
                    "Visualizar elenco",
                    "Estatísticas da equipe",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "GERENCIAMENTO DE EQUIPES", "Equipes - Técnico",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0 -> criarEquipe();
                case 1 -> consultarEquipes();
                case 2 -> consultarIntegrantes();
                case 3 -> consultarEstatisticas();
            }
        } while (opcao != 4 && opcao != -1);
    }

    private void menuAtletas() {
        int opcao;
        do {
            String[] opcoes = {
                    "Gerenciar solicitações",
                    "Convocar atleta",
                    "Sugerir reforços   ",
                    "Remover atleta",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "ATLETAS E SOLICITAÇÕES", "Atletas - Técnico",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0 -> gerenciarSolicitacoes();
                case 1 -> convocarAtleta();
                case 2 -> sugerirReforcosInteligente();
                case 3 -> removerAtletaEquipe();
            }
        } while (opcao != 4 && opcao != -1);
    }

    private void menuJogosETreinos() {
        int opcao;
        do {
            String[] opcoes = {
                    "Cadastrar escalação para jogo",
                    "Solicitar reserva de espaço",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "JOGOS E TREINOS", "Jogos e Treinos - Técnico",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0 -> cadastrarEscalacao();
                case 1 -> solicitarReservaParaEquipe();
            }
        } while (opcao != 2 && opcao != -1);
    }

    // ── Utilitários ──────────────────────────────────────────────────────────

    private Equipe selecionarEquipe() {
        if (equipes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Você ainda não possui equipes cadastradas!");
            return null;
        }
        String[] opcoes = equipes.stream()
                .map(e -> e.getNome() + " – " + e.getModalidade())
                .toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Selecione uma de suas equipes:", "Suas Equipes", JOptionPane.QUESTION_MESSAGE,
                null, opcoes, opcoes[0]);
        if (escolha == null) return null;
        return equipes.stream()
                .filter(e -> (e.getNome() + " – " + e.getModalidade()).equals(escolha))
                .findFirst().orElse(null);
    }

    public List<Equipe> getEquipes()   { return equipes; }
    public int getQtdEquipes()         { return equipes.size(); }
}
