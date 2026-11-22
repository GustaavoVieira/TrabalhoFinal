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
                    "1 - Cadastrar usuários",
                    "2 - Consultar usuários",
                    "3 - Consultar equipes",
                    "4 - Sair"
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
                case -1:
                    JOptionPane.showMessageDialog(null,
                            "Saindo do menu do coordenador...");
                    break;
            }
        } while (opcao != 3 && opcao != -1);
    }
}
