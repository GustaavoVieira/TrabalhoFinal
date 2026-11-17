
package trabalhofinal;

import javax.swing.JOptionPane;

public class Atleta extends Usuario {

    private String matricula;
    private String curso;
    private String modalidade;

    public Atleta(String nome, String email, String senha,
                  String matricula, String curso, String modalidade) {

        super(nome, email, senha);
        this.matricula = matricula;
        this.curso = curso;
        this.modalidade = modalidade;
    }


    public void solicitarEquipe(Equipe[] equipes, int qtdEquipes) {

        if (qtdEquipes == 0) {
            JOptionPane.showMessageDialog(null,
                    "Não existem equipes cadastradas!");
            return;
        }

        String[] opcoes = new String[qtdEquipes];

        for (int i = 0; i < qtdEquipes; i++) {
            opcoes[i] = equipes[i].getNome()
                    + " - " + equipes[i].getModalidade();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Escolha a equipe desejada:",
                "Solicitar participação",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) {
            return;
        }

        int indice = -1;

        for (int i = 0; i < qtdEquipes; i++) {
            if (escolha.equals(opcoes[i])) {
                indice = i;
                break;
            }
        }

        if (indice != -1) {

            Equipe equipe = equipes[indice];

            if (!modalidade.equalsIgnoreCase(equipe.getModalidade())) {
                JOptionPane.showMessageDialog(null,
                        "Modalidade incompatível!");
                return;
            }

            if (equipe.getTecnico() == null) {
                JOptionPane.showMessageDialog(null,
                        "Esta equipe não possui técnico!");
                return;
            }

            SolicitacaoEquipe solicitacao =
                    new SolicitacaoEquipe(this, equipe);

            equipe.getTecnico().receberSolicitacao(solicitacao);
        }
    }



    @Override
    public void exibirDados() {
        super.exibirDados();

        JOptionPane.showMessageDialog(null,
                "Matrícula: " + matricula
                        + "\nCurso: " + curso
                        + "\nModalidade: " + modalidade);
    }

    @Override
    public void exibirMenu() {
        // Switch será implementado posteriormente.
    }

    public String getMatricula() {
        return matricula;
    }

    public String getCurso() {
        return curso;
    }

    public String getModalidade() {
        return modalidade;
    }
}
