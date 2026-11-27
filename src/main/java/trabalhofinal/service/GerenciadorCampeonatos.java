package trabalhofinal.service;

import trabalhofinal.exception.*;
import trabalhofinal.model.*;
import trabalhofinal.repository.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Gerencia campeonatos, inscrições de equipes e tabela de jogos.
 */
public class GerenciadorCampeonatos {

    public static void cadastrarCampeonato() {
        List<Campeonato> campeonatos = RepositorioDados.getCampeonatos();
        if (campeonatos.size() >= RepositorioDados.LIMITE_CAMPEONATOS) {
            JOptionPane.showMessageDialog(null, "Limite de campeonatos atingido!");
            return;
        }
        String nome = JOptionPane.showInputDialog("Nome do campeonato:");
        if (nome == null || nome.trim().isEmpty()) return;
        String modalidade = JOptionPane.showInputDialog("Modalidade esportiva:");
        if (modalidade == null || modalidade.trim().isEmpty()) return;
        if (RepositorioDados.campeonatoJaCadastrado(nome.trim(), modalidade.trim())) {
            JOptionPane.showMessageDialog(null, "Campeonato já cadastrado!");
            return;
        }
        String regulamento = JOptionPane.showInputDialog("Regulamento (opcional):");

        Campeonato c = new Campeonato(nome.trim(), modalidade.trim());
        if (regulamento != null && !regulamento.trim().isEmpty()) {
            c.setRegulamento(regulamento.trim());
        }
        campeonatos.add(c);

        for (Usuario u : RepositorioDados.getUsuarios()) {
            u.receberNotificacao("Novo Campeonato Criado!",
                    "Foi aberto o campeonato '" + nome.trim()
                            + "' (" + modalidade.trim() + "). Inscreva sua equipe!");
        }
        JOptionPane.showMessageDialog(null, "Campeonato cadastrado com sucesso!");
    }

    public static void editarCampeonato() {
        Campeonato c = InputHelper.selecionarCampeonato();
        if (c == null) return;
        String novoNome = JOptionPane.showInputDialog("Nome atual: " + c.getNome() + "\nNovo nome (deixe vazio para manter):");
        if (novoNome != null && !novoNome.trim().isEmpty()) c.setNome(novoNome.trim());

        String novoReg = JOptionPane.showInputDialog("Regulamento atual: " + c.getRegulamento() + "\nNovo regulamento:");
        if (novoReg != null && !novoReg.trim().isEmpty()) c.setRegulamento(novoReg.trim());

        JOptionPane.showMessageDialog(null, "Campeonato atualizado com sucesso!");
    }

    public static void removerCampeonato() {
        Campeonato c = InputHelper.selecionarCampeonato();
        if (c == null) return;
        int conf = JOptionPane.showConfirmDialog(null,
                "Deseja realmente remover o campeonato '" + c.getNome() + "'?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            RepositorioDados.getCampeonatos().remove(c);
            JOptionPane.showMessageDialog(null, "Campeonato removido com sucesso!");
        }
    }

    public static void encerrarCampeonato() {
        Campeonato c = InputHelper.selecionarCampeonato();
        if (c != null) c.encerrar();
    }

    public static void consultarCampeonatos() {
        List<Campeonato> campeonatos = RepositorioDados.getCampeonatos();
        if (campeonatos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum campeonato cadastrado!");
            return;
        }
        for (Campeonato c : campeonatos) {
            c.listarEquipes();
            c.listarJogos();
        }
    }

    public static void adicionarEquipeCampeonato() {
        Campeonato campeonato = InputHelper.selecionarCampeonato();
        if (campeonato == null) return;
        List<Equipe> equipes = RepositorioDados.getEquipes();
        if (equipes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma equipe cadastrada no sistema!");
            return;
        }
        List<Equipe> compativeis = new ArrayList<>();
        for (Equipe e : equipes) {
            if (e.getModalidade().equalsIgnoreCase(campeonato.getModalidade())) {
                compativeis.add(e);
            }
        }
        if (compativeis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há equipes da modalidade " + campeonato.getModalidade());
            return;
        }
        String[] opcoes = compativeis.stream()
                .map(e -> e.getNome() + " – " + e.getModalidade()).toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Selecione uma equipe:", "Adicionar equipe ao campeonato",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;
        for (Equipe e : compativeis) {
            if ((e.getNome() + " – " + e.getModalidade()).equals(escolha)) {
                try {
                    campeonato.adicionarEquipe(e);
                    e.adicionarCampeonato(campeonato.getNome());
                } catch (CampeonatoEncerradoException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Inscrição Recusada", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }
    }

    public static void cadastrarJogo() {
        Campeonato campeonato = InputHelper.selecionarCampeonato();
        if (campeonato == null) return;
        if (campeonato.getQtdEquipes() < 2) {
            JOptionPane.showMessageDialog(null,
                    "Regra 4: O campeonato precisa ter ao menos duas equipes participantes para cadastrar um jogo!");
            return;
        }
        Equipe equipe1 = InputHelper.selecionarEquipeJogo(campeonato, "Selecione a primeira equipe:");
        if (equipe1 == null) return;
        Equipe equipe2 = InputHelper.selecionarEquipeJogo(campeonato, "Selecione a segunda equipe:");
        if (equipe2 == null) return;
        if (equipe1 == equipe2) {
            JOptionPane.showMessageDialog(null, "Uma equipe não pode jogar contra ela mesma!");
            return;
        }
        EspacoEsportivo espaco = InputHelper.selecionarEspaco();
        if (espaco == null) return;
        String data    = InputHelper.solicitarData();    if (data == null) return;
        String horario = InputHelper.solicitarHorario(); if (horario == null) return;

        try {
            if (GerenciadorEspacosReservas.verificarDisponibilidade(espaco, data, horario)) {
                campeonato.adicionarJogo(new Jogo(equipe1, equipe2, espaco, data, horario));
            }
        } catch (ReservaConflitoException | EspacoEmManutencaoException | CampeonatoEncerradoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro no Cadastro de Jogo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void remarcarJogo() {
        Campeonato c = InputHelper.selecionarCampeonato();
        if (c == null) return;
        if (c.getQtdJogos() == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum jogo cadastrado neste campeonato!");
            return;
        }
        String[] opcoes = new String[c.getQtdJogos()];
        for (int i = 0; i < c.getQtdJogos(); i++) {
            Jogo j = c.getJogo(i);
            opcoes[i] = j.getEquipe1().getNome() + " vs " + j.getEquipe2().getNome() + " (" + j.getData() + " " + j.getHorario() + ")";
        }
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Selecione o jogo a remarcar:", "Remarcar Jogo",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;

        Jogo jogoSel = null;
        for (int i = 0; i < c.getQtdJogos(); i++) {
            if (opcoes[i].equals(escolha)) {
                jogoSel = c.getJogo(i);
                break;
            }
        }
        if (jogoSel == null) return;

        String novaData = InputHelper.solicitarData(); if (novaData == null) return;
        String novoHorario = InputHelper.solicitarHorario(); if (novoHorario == null) return;
        EspacoEsportivo novoEspaco = InputHelper.selecionarEspaco(); if (novoEspaco == null) return;

        try {
            if (GerenciadorEspacosReservas.verificarDisponibilidade(novoEspaco, novaData, novoHorario)) {
                jogoSel.remarcar(novaData, novoHorario, novoEspaco);
                JOptionPane.showMessageDialog(null, "Jogo remarcado e notificações disparadas para as equipes!");
            }
        } catch (ReservaConflitoException | EspacoEmManutencaoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro ao Remarcar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void registrarResultado() {
        Campeonato c = InputHelper.selecionarCampeonato();
        if (c != null) c.registrarResultadoJogo();
    }

    public static void consultarResultados() {
        Campeonato c = InputHelper.selecionarCampeonato();
        if (c != null) c.consultarResultados();
    }

    public static void exibirClassificacao() {
        Campeonato c = InputHelper.selecionarCampeonato();
        if (c != null) c.gerarClassificacao();
    }
}
