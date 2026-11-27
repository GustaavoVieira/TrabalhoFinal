package trabalhofinal.service;

import trabalhofinal.exception.*;
import trabalhofinal.model.*;
import trabalhofinal.repository.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Gerencia espaços esportivos, buscas/filtros, reservas e verificação de disponibilidade.
 */
public class GerenciadorEspacosReservas {

    public static void cadastrarEspaco() {
        List<EspacoEsportivo> espacos = RepositorioDados.getEspacos();
        if (espacos.size() >= RepositorioDados.LIMITE_ESPACOS) {
            JOptionPane.showMessageDialog(null, "Limite de espaços atingido!");
            return;
        }
        String nome = JOptionPane.showInputDialog("Nome do espaço esportivo:");
        if (nome == null || nome.trim().isEmpty()) return;
        if (RepositorioDados.espacoJaCadastrado(nome.trim())) {
            JOptionPane.showMessageDialog(null, "Já existe um espaço com esse nome!");
            return;
        }
        String[] tipos = {"Quadra", "Campo", "Ginásio", "Piscina", "Outro"};
        String tipo = (String) JOptionPane.showInputDialog(null, "Selecione o tipo do espaço:",
                "Tipo", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo == null) return;

        String localizacao = JOptionPane.showInputDialog("Localização do espaço no campus:");
        if (localizacao == null || localizacao.trim().isEmpty()) return;

        int capacidade;
        while (true) {
            String val = JOptionPane.showInputDialog("Capacidade máxima de pessoas:");
            if (val == null) return;
            try {
                capacidade = Integer.parseInt(val.trim());
                if (capacidade <= 0) {
                    JOptionPane.showMessageDialog(null, "A capacidade deve ser maior que zero!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite um número inteiro válido.");
            }
        }
        espacos.add(new EspacoEsportivo(nome.trim(), tipo, localizacao.trim(), capacidade));
        JOptionPane.showMessageDialog(null, "Espaço esportivo cadastrado com sucesso!");
    }

    public static void consultarEspacos() {
        List<EspacoEsportivo> espacos = RepositorioDados.getEspacos();
        if (espacos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum espaço esportivo cadastrado!");
            return;
        }
        StringBuilder sb = new StringBuilder("ESPAÇOS ESPORTIVOS CADASTRADOS:\n\n");
        for (EspacoEsportivo e : espacos) sb.append("• ").append(e.resumo()).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void buscarEFiltarEspacos() {
        List<EspacoEsportivo> espacos = RepositorioDados.getEspacos();
        if (espacos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum espaço cadastrado!");
            return;
        }
        String[] opcoes = {"Filtrar por Tipo", "Filtrar por Status", "Buscar por Nome"};
        int op = JOptionPane.showOptionDialog(null, "Selecione o filtro desejado:",
                "Busca e Filtro de Espaços", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        List<EspacoEsportivo> filtrados = new ArrayList<>();
        switch (op) {
            case 0 -> {
                String[] tipos = {"Quadra", "Campo", "Ginásio", "Piscina"};
                String t = (String) JOptionPane.showInputDialog(null, "Selecione o tipo:",
                        "Filtro", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
                if (t == null) return;
                for (EspacoEsportivo e : espacos) if (e.getTipo().equalsIgnoreCase(t)) filtrados.add(e);
            }
            case 1 -> {
                String[] status = {EspacoEsportivo.STATUS_ATIVO, EspacoEsportivo.STATUS_MANUTENCAO, EspacoEsportivo.STATUS_INATIVO};
                String s = (String) JOptionPane.showInputDialog(null, "Selecione o status:",
                        "Filtro", JOptionPane.QUESTION_MESSAGE, null, status, status[0]);
                if (s == null) return;
                for (EspacoEsportivo e : espacos) if (e.getStatusFuncionamento().equalsIgnoreCase(s)) filtrados.add(e);
            }
            case 2 -> {
                String termo = JOptionPane.showInputDialog("Digite o nome ou parte do nome:");
                if (termo == null || termo.trim().isEmpty()) return;
                for (EspacoEsportivo e : espacos)
                    if (e.getNome().toLowerCase().contains(termo.trim().toLowerCase())) filtrados.add(e);
            }
            default -> { return; }
        }

        if (filtrados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum espaço encontrado com os critérios informados.");
            return;
        }
        StringBuilder sb = new StringBuilder("🔍 RESULTADO DA BUSCA DE ESPAÇOS:\n\n");
        for (EspacoEsportivo e : filtrados) sb.append("• ").append(e.resumo()).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void editarEspaco() {
        EspacoEsportivo e = InputHelper.selecionarEspaco();
        if (e != null) e.editar();
    }

    public static void removerEspaco() {
        EspacoEsportivo e = InputHelper.selecionarEspaco();
        if (e == null) return;
        int conf = JOptionPane.showConfirmDialog(null,
                "Deseja realmente remover o espaço '" + e.getNome() + "'?",
                "Remover Espaço", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            RepositorioDados.getEspacos().remove(e);
            JOptionPane.showMessageDialog(null, "Espaço removido com sucesso!");
        }
    }

    // ── Reservas ──────────────────────────────────────────────────────────────

    public static void adicionarReserva(Reserva r) {
        if (r != null) RepositorioDados.getReservas().add(r);
    }

    public static void realizarReserva() {
        List<Reserva> reservas = RepositorioDados.getReservas();
        List<Usuario> usuarios = RepositorioDados.getUsuarios();
        if (reservas.size() >= RepositorioDados.LIMITE_RESERVAS) {
            JOptionPane.showMessageDialog(null, "Limite de reservas atingido!");
            return;
        }
        EspacoEsportivo espaco = InputHelper.selecionarEspaco();
        if (espaco == null) return;
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum usuário cadastrado!");
            return;
        }
        String[] opcoesUsr = usuarios.stream()
                .map(u -> u.getNome() + " – " + u.getEmail()).toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Responsável pela reserva:", "Usuários",
                JOptionPane.QUESTION_MESSAGE, null, opcoesUsr, opcoesUsr[0]);
        if (escolha == null) return;
        Usuario responsavel = usuarios.stream()
                .filter(u -> (u.getNome() + " – " + u.getEmail()).equals(escolha))
                .findFirst().orElse(null);
        if (responsavel == null) return;

        String data    = InputHelper.solicitarData();    if (data == null) return;
        String horario = InputHelper.solicitarHorario(); if (horario == null) return;

        try {
            if (verificarDisponibilidade(espaco, data, horario)) {
                Reserva res = new Reserva(espaco, responsavel, data, horario);
                reservas.add(res);
                responsavel.receberNotificacao("Reserva Confirmada",
                        "Reserva no espaço '" + espaco.getNome() + "' para " + data + " às " + horario + " confirmada com sucesso!");
                JOptionPane.showMessageDialog(null, "Reserva realizada com sucesso!");
            }
        } catch (ReservaConflitoException | EspacoEmManutencaoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro de Reserva", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void gerenciarAprovacaoReservas() {
        List<Reserva> pendentes = new ArrayList<>();
        for (Reserva r : RepositorioDados.getReservas()) {
            if (Reserva.STATUS_PENDENTE.equalsIgnoreCase(r.getStatus())) {
                pendentes.add(r);
            }
        }
        if (pendentes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há solicitações de reservas pendentes de aprovação.");
            return;
        }
        for (Reserva r : pendentes) {
            int op = JOptionPane.showConfirmDialog(null,
                    "Solicitação de Reserva:\n"
                            + "Espaço: " + r.getEspaco().getNome() + "\n"
                            + "Data: " + r.getData() + " às " + r.getHorario() + "\n"
                            + "Solicitante: " + r.getResponsavel().getNome() + "\n"
                            + (r.getEquipe() != null ? "Equipe: " + r.getEquipe().getNome() + "\n" : "")
                            + "\nDeseja APROVAR esta reserva?",
                    "Aprovação de Reservas", JOptionPane.YES_NO_CANCEL_OPTION);

            if (op == JOptionPane.YES_OPTION) {
                r.aprovar();
                JOptionPane.showMessageDialog(null, "Reserva aprovada e solicitante notificado!");
            } else if (op == JOptionPane.NO_OPTION) {
                String motivo = JOptionPane.showInputDialog("Informe o motivo da rejeição:");
                r.rejeitar(motivo != null ? motivo : "Horário indisponível ou manutenção");
                JOptionPane.showMessageDialog(null, "Reserva rejeitada e solicitante notificado.");
            } else {
                break;
            }
        }
    }

    public static void consultarReservas() {
        List<Reserva> reservas = RepositorioDados.getReservas();
        if (reservas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma reserva cadastrada!");
            return;
        }
        StringBuilder sb = new StringBuilder("HISTÓRICO E RESERVAS DO SISTEMA:\n\n");
        for (Reserva r : reservas) {
            sb.append("• ").append(r.getEspaco().getNome())
              .append(" | ").append(r.getData()).append(" às ").append(r.getHorario())
              .append(" | Solicitante: ").append(r.getResponsavel().getNome())
              .append(" [Status: ").append(r.getStatus()).append("]\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void cancelarReserva() {
        List<Reserva> reservas = RepositorioDados.getReservas();
        if (reservas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma reserva cadastrada!");
            return;
        }
        String[] opcoes = new String[reservas.size()];
        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            opcoes[i] = r.getEspaco().getNome() + " | " + r.getData()
                    + " " + r.getHorario() + " | " + r.getResponsavel().getNome();
        }
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Selecione a reserva a cancelar:", "Cancelar Reserva",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;
        for (int i = 0; i < reservas.size(); i++) {
            if (opcoes[i].equals(escolha)) {
                Usuario resp = reservas.get(i).getResponsavel();
                reservas.remove(i);
                resp.receberNotificacao("Cancelamento de Reserva",
                        "Sua reserva para o espaço (" + escolha + ") foi cancelada pela coordenação.");
                JOptionPane.showMessageDialog(null, "Reserva cancelada com sucesso!");
                return;
            }
        }
    }

    public static boolean verificarDisponibilidade(EspacoEsportivo espaco, String data, String horario) {
        if (espaco.estaEmManutencao()) {
            throw new EspacoEmManutencaoException(
                    "Regra 5 violada: O espaço '" + espaco.getNome()
                            + "' está em MANUTENÇÃO e não pode receber reservas ou jogos.");
        }
        for (Reserva r : RepositorioDados.getReservas()) {
            if (r.getEspaco().equals(espaco)
                    && r.getData().equals(data)
                    && r.getHorario().equals(horario)
                    && !Reserva.STATUS_REJEITADA.equalsIgnoreCase(r.getStatus())) {
                throw new ReservaConflitoException(
                        "Regra 2 violada: Conflito de horário! O espaço '" + espaco.getNome()
                                + "' já possui uma reserva em " + data + " às " + horario);
            }
        }
        for (Campeonato cam : RepositorioDados.getCampeonatos()) {
            for (int i = 0; i < cam.getQtdJogos(); i++) {
                Jogo j = cam.getJogo(i);
                if (j.getEspaco().equals(espaco)
                        && j.getData().equals(data)
                        && j.getHorario().equals(horario)) {
                    throw new ReservaConflitoException(
                            "Regra 2 violada: Conflito de horário! Já existe um jogo agendado neste espaço em "
                                    + data + " às " + horario);
                }
            }
        }
        return true;
    }
}
