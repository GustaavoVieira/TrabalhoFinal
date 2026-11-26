
package trabalhofinal;

import javax.swing.*;

public class Coordenador extends Usuario {

    public Coordenador(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public void exibirMenu() {

        int opcao;

        do {

            String[] opcoes = {
                    "Usuários",
                    "Equipes",
                    "Campeonatos e jogos",
                    "Espaços esportivos e reservas",
                    "Sair"
            };

            opcao = JOptionPane.showOptionDialog(
                    null,
                    "Bem-vindo, Coordenador!\nEscolha uma opção:",
                    "Menu do Coordenador",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (opcao) {

                case 0:
                    menuUsuarios();
                    break;

                case 1:
                    menuEquipes();
                    break;

                case 2:
                    menuCampeonatos();
                    break;

                case 3:
                    menuEspacos();
                    break;

                case 4:
                case -1:
                    JOptionPane.showMessageDialog(null,
                            "Saindo do menu do coordenador...");
                    break;
            }

        } while (opcao != 4 && opcao != -1);
    }

    public void menuUsuarios() {

        int opcao;

        do {

            String[] opcoes = {
                    "Cadastrar usuário",
                    "Consultar usuários",
                    "Voltar"
            };

            opcao = JOptionPane.showOptionDialog(
                    null,
                    "GERENCIAMENTO DE USUÁRIOS",
                    "Menu de Usuários",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (opcao) {

                case 0:
                    Main.cadastrarUsuario();
                    break;

                case 1:
                    Main.consultarUsuarios();
                    break;
            }

        } while (opcao != 2 && opcao != -1);
    }

    public void menuEquipes() {

        int opcao;

        do {

            String[] opcoes = {
                    "Consultar equipes",
                    "Voltar"
            };

            opcao = JOptionPane.showOptionDialog(
                    null,
                    "GERENCIAMENTO DE EQUIPES",
                    "Menu de Equipes",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (opcao) {

                case 0:
                    Main.consultarEquipes();
                    break;
            }

        } while (opcao != 1 && opcao != -1);
    }

    public void menuCampeonatos() {

        int opcao;

        do {

            String[] opcoes = {
                    "Cadastrar campeonato",
                    "Adicionar equipe ao campeonato",
                    "Cadastrar jogo",
                    "Consultar campeonatos",
                    "Registrar resultado de jogo",
                    "Consultar resultados",
                    "Voltar"
            };

            opcao = JOptionPane.showOptionDialog(
                    null,
                    "GERENCIAMENTO DE CAMPEONATOS E JOGOS",
                    "Menu de Campeonatos",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (opcao) {

                case 0:
                    Main.cadastrarCampeonato();
                    break;

                case 1:
                    Main.adicionarEquipeCampeonato();
                    break;

                case 2:
                    Main.cadastrarJogo();
                    break;

                case 3:
                    Main.consultarCampeonatos();
                    break;

                case 4:
                    Main.registrarResultado();
                    break;

                case 5:
                    Main.consultarResultados();
                    break;
            }

        } while (opcao != 6 && opcao != -1);
    }

    public void menuEspacos() {

        int opcao;

        do {

            String[] opcoes = {
                    "Cadastrar espaço esportivo",
                    "Consultar espaços esportivos",
                    "Realizar reserva",
                    "Consultar reservas",
                    "Cancelar reserva",
                    "Voltar"
            };

            opcao = JOptionPane.showOptionDialog(
                    null,
                    "GERENCIAMENTO DE ESPAÇOS E RESERVAS",
                    "Menu de Espaços Esportivos",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (opcao) {

                case 0:
                    Main.cadastrarEspaco();
                    break;

                case 1:
                    Main.consultarEspacos();
                    break;

                case 2:
                    Main.realizarReserva();
                    break;

                case 3:
                    Main.consultarReservas();
                    break;
                case 4:
                    Main.cancelarReserva();
                    break;
            }

        } while (opcao != 5 && opcao != -1);
    }
}
