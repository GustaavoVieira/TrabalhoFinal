package trabalhofinal;

import javax.swing.JOptionPane;
import trabalhofinal.repository.RepositorioDados;
import trabalhofinal.service.GerenciadorUsuarios;

public class Main {

    public static void main(String[] args) {
        menuPrincipal();
    }

    public static void menuPrincipal() {
        int opcao;
        do {
            boolean semUsuarios = RepositorioDados.getUsuarios().isEmpty();
            String[] opcoes = semUsuarios
                    ? new String[]{"Cadastrar primeiro Coordenador", "Sair"}
                    : new String[]{"Fazer login", "Sair"};

            opcao = JOptionPane.showOptionDialog(null,
                    "Sistema de Gerenciamento Esportivo Universitário",
                    "Menu Principal", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            if (semUsuarios) {
                if (opcao == 0) GerenciadorUsuarios.cadastrarPrimeiroCoordenador();
            } else {
                if (opcao == 0) GerenciadorUsuarios.login();
            }
        } while (opcao != 1 && opcao != -1);
    }
}
