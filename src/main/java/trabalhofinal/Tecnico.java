
package trabalhofinal;

import javax.swing.JOptionPane;

public class Tecnico extends Usuario {

    private SolicitacaoEquipe[] solicitacoes;
    private int qtdSolicitacoes;

    public Tecnico(String nome, String email, String senha) {
        super(nome, email, senha);

        this.solicitacoes = new SolicitacaoEquipe[100];
        this.qtdSolicitacoes = 0;
    }

    public void criarEquipe(Equipe equipe) {

        String nome = JOptionPane.showInputDialog(
                "Digite o nome da equipe:");

        if (nome == null || nome.trim().isEmpty()) {
            return;
        }

        String modalidade = JOptionPane.showInputDialog(
                "Digite a modalidade da equipe:");

        if (modalidade == null || modalidade.trim().isEmpty()) {
            return;
        }

        equipe.setNome(nome);
        equipe.setModalidade(modalidade);
        equipe.setTecnico(this);

        JOptionPane.showMessageDialog(null,
                "Equipe criada com sucesso!");
    }

    public void receberSolicitacao(SolicitacaoEquipe solicitacao) {

        if (solicitacao == null) {
            return;
        }

        for (int i = 0; i < qtdSolicitacoes; i++) {

            if (solicitacoes[i].getAtleta().getMatricula()
                    .equals(solicitacao.getAtleta().getMatricula())
                    && solicitacoes[i].getEquipe()
                    == solicitacao.getEquipe()
                    && solicitacoes[i].getSituacao()
                    .equals("PENDENTE")) {

                JOptionPane.showMessageDialog(null,
                        "Você já possui uma solicitação pendente!");

                return;
            }
        }

        if (qtdSolicitacoes < solicitacoes.length) {

            solicitacoes[qtdSolicitacoes] = solicitacao;
            qtdSolicitacoes++;

            JOptionPane.showMessageDialog(null,
                    "Solicitação enviada com sucesso! "
                            + "Aguarde a análise do técnico.");

        } else {
            JOptionPane.showMessageDialog(null,
                    "Limite de solicitações atingido!");
        }
    }

    public void gerenciarEquipe() {

        boolean encontrou = false;

        for (int i = 0; i < qtdSolicitacoes; i++) {

            SolicitacaoEquipe solicitacao = solicitacoes[i];

            if (solicitacao.getSituacao().equals("PENDENTE")) {

                encontrou = true;

                Atleta atleta = solicitacao.getAtleta();
                Equipe equipe = solicitacao.getEquipe();

                int resposta = JOptionPane.showConfirmDialog(
                        null,
                        "Atleta: " + atleta.getNome()
                                + "\nMatrícula: " + atleta.getMatricula()
                                + "\nEquipe: " + equipe.getNome()
                                + "\n\nDeseja aceitar esta solicitação?",
                        "Analisar solicitação",
                        JOptionPane.YES_NO_OPTION
                );

                if (resposta == JOptionPane.YES_OPTION) {

                    equipe.adicionarAtleta(atleta);

                    solicitacao.setSituacao("ACEITA");

                } else if (resposta == JOptionPane.NO_OPTION) {

                    solicitacao.setSituacao("RECUSADA");

                    JOptionPane.showMessageDialog(null,
                            "Solicitação recusada!");

                } else {
                    // O técnico fechou a janela.
                    // A solicitação continua pendente.
                }
            }
        }

        if (!encontrou) {
            JOptionPane.showMessageDialog(null,
                    "Não existem solicitações pendentes.");
        }
    }

    @Override
    public void exibirMenu() {
        // Switch será implementado posteriormente.
    }
}
