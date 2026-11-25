package trabalhofinal;

import javax.swing.*;

public class Coordenador extends Usuario{

    public Coordenador(String nome, String email, String senha){
        super(nome, email, senha);
    }

    @Override
    public void exibirMenu() {

        int opcao;

        do {

            String[] opcoes = {
                    "Cadastrar usuários",
                    "Consultar usuários",
                    "Consultar equipes",
                    "Cadastrar campeonato",
                    "Adicionar equipe ao campeonato",
                    "Cadastrar jogo",
                    "Consultar campeonatos",
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
                    Main.cadastrarUsuario();
                    break;

                case 1:
                    Main.consultarUsuarios();
                    break;

                case 2:
                    Main.consultarEquipes();
                    break;

                case 3:
                    Main.cadastrarCampeonato();
                    break;

                case 4:
                    Main.adicionarEquipeCampeonato();
                    break;

                case 5:
                    Main.cadastrarJogo();
                    break;

                case 6:
                    Main.consultarCampeonatos();
                    break;

                case 7:
                case -1:
                    JOptionPane.showMessageDialog(null,
                            "Saindo do menu do coordenador...");
                    break;
            }

        } while (opcao != 7 && opcao != -1);
    }
}
