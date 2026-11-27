package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

import javax.swing.*;

/**
 * Coordenador esportivo – administração geral do sistema.
 * Gerencia campeonatos, espaços esportivos, aprovação de reservas,
 * comunicados e relatórios estatísticos completos.
 */
public class Coordenador extends Usuario {

    public Coordenador(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    // ── Menu principal ────────────────────────────────────────────────────────

    @Override
    public void exibirMenu() {
        int opcao;
        do {
            String[] opcoes = {
                    "Usuários",
                    "Equipes",
                    "Campeonatos e Jogos",
                    "Espaços Esportivos",
                    "Gestão de Reservas",
                    "Comunicados e Notificações",
                    "Relatórios e Estatísticas",
                    "Sair"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "Bem-vindo, Coordenador " + getNome() + "!\nEscolha uma opção:",
                    "Menu do Coordenador", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            switch (opcao) {
                case 0 -> menuUsuarios();
                case 1 -> menuEquipes();
                case 2 -> menuCampeonatos();
                case 3 -> menuEspacos();
                case 4 -> menuReservas();
                case 5 -> menuComunicados();
                case 6 -> menuRelatorios();
                case 7, -1 -> JOptionPane.showMessageDialog(null, "Saindo do menu do coordenador...");
            }
        } while (opcao != 7 && opcao != -1);
    }

    // ── Sub-menus ─────────────────────────────────────────────────────────────

    public void menuUsuarios() {
        int opcao;
        do {
            String[] opcoes = {
                    "Cadastrar usuário",
                    "Consultar usuários",
                    "Suspender atleta",
                    "Reativar atleta",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "GERENCIAMENTO DE USUÁRIOS", "Menu de Usuários",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);
            switch (opcao) {
                case 0 -> GerenciadorUsuarios.cadastrarUsuario();
                case 1 -> GerenciadorUsuarios.consultarUsuarios();
                case 2 -> GerenciadorUsuarios.suspenderAtleta();
                case 3 -> GerenciadorUsuarios.reativarAtleta();
            }
        } while (opcao != 4 && opcao != -1);
    }

    public void menuEquipes() {
        int opcao;
        do {
            String[] opcoes = {
                    "Consultar equipes",
                    "Ver histórico da equipe",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "GERENCIAMENTO DE EQUIPES", "Menu de Equipes",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);
            switch (opcao) {
                case 0 -> GerenciadorRelatorios.consultarEquipes();
                case 1 -> GerenciadorRelatorios.consultarHistoricoEquipe();
            }
        } while (opcao != 2 && opcao != -1);
    }

    public void menuCampeonatos() {
        String[] opcoes = {
                "Cadastrar campeonato",
                "Editar campeonato",
                "Remover campeonato",
                "Adicionar equipe ao campeonato",
                "Encerrar campeonato",
                "Cadastrar jogo",
                "Remarcar jogo (Horário/Local)",
                "Consultar campeonatos / Jogos",
                "Registrar resultado de jogo",
                "Consultar resultados",
                "Ver classificação oficial",
                "Voltar"
        };
        while (true) {
            String escolha = (String) JOptionPane.showInputDialog(null,
                    "GERENCIAMENTO DE CAMPEONATOS E JOGOS\nSelecione uma opção:",
                    "Menu de Campeonatos",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]);

            if (escolha == null || escolha.equals("Voltar")) break;

            switch (escolha) {
                case "Cadastrar campeonato"            -> GerenciadorCampeonatos.cadastrarCampeonato();
                case "Editar campeonato"               -> GerenciadorCampeonatos.editarCampeonato();
                case "Remover campeonato"              -> GerenciadorCampeonatos.removerCampeonato();
                case "Adicionar equipe ao campeonato"  -> GerenciadorCampeonatos.adicionarEquipeCampeonato();
                case "Encerrar campeonato"             -> GerenciadorCampeonatos.encerrarCampeonato();
                case "Cadastrar jogo"                  -> GerenciadorCampeonatos.cadastrarJogo();
                case "Remarcar jogo (Horário/Local)"   -> GerenciadorCampeonatos.remarcarJogo();
                case "Consultar campeonatos / Jogos"   -> GerenciadorCampeonatos.consultarCampeonatos();
                case "Registrar resultado de jogo"     -> GerenciadorCampeonatos.registrarResultado();
                case "Consultar resultados"            -> GerenciadorCampeonatos.consultarResultados();
                case "Ver classificação oficial"       -> GerenciadorCampeonatos.exibirClassificacao();
            }
        }
    }

    public void menuEspacos() {
        int opcao;
        do {
            String[] opcoes = {
                    "Cadastrar espaço esportivo",
                    "Consultar todos os espaços",
                    "Buscar e filtrar espaços",
                    "Editar espaço esportivo",
                    "Remover espaço esportivo",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "GERENCIAMENTO DE ESPAÇOS ESPORTIVOS", "Menu de Espaços",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);
            switch (opcao) {
                case 0 -> GerenciadorEspacosReservas.cadastrarEspaco();
                case 1 -> GerenciadorEspacosReservas.consultarEspacos();
                case 2 -> GerenciadorEspacosReservas.buscarEFiltarEspacos();
                case 3 -> GerenciadorEspacosReservas.editarEspaco();
                case 4 -> GerenciadorEspacosReservas.removerEspaco();
            }
        } while (opcao != 5 && opcao != -1);
    }

    public void menuReservas() {
        int opcao;
        do {
            String[] opcoes = {
                    "Realizar nova reserva",
                    "Consultar reservas",
                    "Aprovar ou rejeitar reservas pendentes",
                    "Cancelar reserva",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "GESTÃO DE RESERVAS", "Menu de Reservas",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);
            switch (opcao) {
                case 0 -> GerenciadorEspacosReservas.realizarReserva();
                case 1 -> GerenciadorEspacosReservas.consultarReservas();
                case 2 -> GerenciadorEspacosReservas.gerenciarAprovacaoReservas();
                case 3 -> GerenciadorEspacosReservas.cancelarReserva();
            }
        } while (opcao != 4 && opcao != -1);
    }

    public void menuComunicados() {
        int opcao;
        do {
            String[] opcoes = {
                    "Enviar comunicado geral para todos os usuários",
                    "Ver histórico de notificações enviadas",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "COMUNICADOS E NOTIFICAÇÕES", "Menu de Notificações",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);
            switch (opcao) {
                case 0 -> GerenciadorUsuarios.enviarComunicadoGeral();
                case 1 -> GerenciadorUsuarios.consultarHistoricoNotificacoes();
            }
        } while (opcao != 2 && opcao != -1);
    }

    public void menuRelatorios() {
        int opcao;
        do {
            String[] opcoes = {
                    "Quantidade de atletas cadastrados",
                    "Quantidade de equipes ativas",
                    "Jogos realizados por modalidade",
                    "Ranking de equipes (classificação)",
                    "Ranking de atletas",
                    "Taxa de ocupação dos espaços",
                    "Voltar"
            };
            opcao = JOptionPane.showOptionDialog(null,
                    "RELATÓRIOS E ESTATÍSTICAS", "Relatórios",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opcoes, opcoes[0]);
            switch (opcao) {
                case 0 -> GerenciadorRelatorios.relatorioAtletas();
                case 1 -> GerenciadorRelatorios.relatorioEquipes();
                case 2 -> GerenciadorRelatorios.relatorioJogosPorModalidade();
                case 3 -> GerenciadorCampeonatos.exibirClassificacao();
                case 4 -> GerenciadorRelatorios.relatorioRankingAtletas();
                case 5 -> GerenciadorRelatorios.relatorioOcupacaoEspacos();
            }
        } while (opcao != 6 && opcao != -1);
    }
}
