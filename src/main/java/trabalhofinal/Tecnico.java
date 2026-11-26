
package trabalhofinal;

import javax.swing.JOptionPane;

public class Tecnico extends Usuario {

    private SolicitacaoEquipe[] solicitacoes;
    private int qtdSolicitacoes;
    private Equipe[] equipes;
    private int qtdEquipes;

    public Tecnico(String nome, String email, String senha) {
        super(nome, email, senha);

        this.solicitacoes = new SolicitacaoEquipe[100];
        this.qtdSolicitacoes = 0;

        this.equipes = new Equipe[50];
        this.qtdEquipes = 0;
    }




    public void criarEquipe() {

        if (qtdEquipes >= equipes.length) {
            JOptionPane.showMessageDialog(null,
                    "Limite de equipes atingido!");
            return;
        }

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

        if (Main.equipeJaCadastrada(nome, modalidade)) {
            JOptionPane.showMessageDialog(null,
                    "Já existe uma equipe com esse nome e modalidade!");
            return;
        }

        Equipe equipe = new Equipe(nome, modalidade, this);

        if (Main.adicionarEquipe(equipe)) {
            equipes[qtdEquipes] = equipe;
            qtdEquipes++;

            JOptionPane.showMessageDialog(null,
                    "Equipe criada com sucesso!");
        }
    }


    public void consultarEquipes() {

        if (qtdEquipes == 0) {
            JOptionPane.showMessageDialog(null, "Nenhuma equipe cadastrada!");
            return;
        }

        String lista = "EQUIPES CADASTRADAS:\n\n";

        for (int i = 0; i < qtdEquipes; i++) {
            lista += "Nome: " + equipes[i].getNome()
                    + "\nModalidade: " + equipes[i].getModalidade()
                    + "\n\n";
        }

        JOptionPane.showMessageDialog(null, lista);
    }

    public Equipe selecionarEquipe() {

        if (qtdEquipes == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhuma equipe cadastrada!");
            return null;
        }

        String[] opcoes = new String[qtdEquipes];

        for (int i = 0; i < qtdEquipes; i++) {
            opcoes[i] = equipes[i].getNome()
                    + " - " + equipes[i].getModalidade();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Selecione uma equipe:",
                "Equipes cadastradas",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) {
            return null;
        }

        for (int i = 0; i < qtdEquipes; i++) {
            if (opcoes[i].equals(escolha)) {
                return equipes[i];
            }
        }

        return null;
    }


    public void receberSolicitacao(SolicitacaoEquipe solicitacao) {

        if (solicitacao == null
                || solicitacao.getAtleta() == null
                || solicitacao.getEquipe() == null) {

            JOptionPane.showMessageDialog(null,
                    "Solicitação inválida!");
            return;
        }

        if (solicitacao.getEquipe().getTecnico() != this) {
            JOptionPane.showMessageDialog(null,
                    "Esta equipe não pertence a este técnico!");
            return;
        }

        for (int i = 0; i < qtdSolicitacoes; i++) {

            if (solicitacoes[i].getAtleta().getMatricula()
                    .equalsIgnoreCase(
                            solicitacao.getAtleta().getMatricula())
                    && solicitacoes[i].getEquipe()
                    == solicitacao.getEquipe()
                    && (solicitacoes[i].getSituacao().equals("PENDENTE")
                    || solicitacoes[i].getSituacao().equals("ACEITA"))) {

                JOptionPane.showMessageDialog(null,
                        "Este atleta já possui uma solicitação pendente "
                                + "ou já foi aceito nesta equipe!");
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

            if (solicitacao.getSituacao().equals("PENDENTE")
                    && solicitacao.getEquipe().getTecnico() == this) {

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

                    if (equipe.adicionarAtleta(atleta)) {
                        solicitacao.setSituacao("ACEITA");
                    }

                } else if (resposta == JOptionPane.NO_OPTION) {

                    solicitacao.setSituacao("RECUSADA");

                    JOptionPane.showMessageDialog(null,
                            "Solicitação recusada!");
                }
            }
        }

        if (!encontrou) {
            JOptionPane.showMessageDialog(null,
                    "Não existem solicitações pendentes!");
        }
    }

    public void consultarIntegrantes() {

        Equipe equipe = selecionarEquipe();

        if (equipe != null) {
            equipe.listarAtletas();
        }
    }

    public void removerAtletaEquipe() {

        Equipe equipe = selecionarEquipe();

        if (equipe == null) {
            return;
        }

        String matricula = JOptionPane.showInputDialog(
                "Digite a matrícula do atleta que deseja remover:");

        if (matricula == null || matricula.trim().isEmpty()) {
            return;
        }

        equipe.removerAtleta(matricula);
    }


    @Override
    public void exibirMenu() {

        int opcao;

        do {

            String[] opcoes = {
                    "Criar equipe",
                    "Consultar equipes",
                    "Gerenciar solicitações",
                    "Consultar integrantes",
                    "Remover atleta",
                    "Sair"
            };

            opcao = JOptionPane.showOptionDialog(
                    null,
                    "Bem-vindo, Técnico!\nEscolha uma opção:",
                    "Menu do Técnico",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (opcao) {

                case 0:
                    criarEquipe();
                    break;

                case 1:
                    consultarEquipes();
                    break;

                case 2:
                    gerenciarEquipe();
                    break;

                case 3:
                    consultarIntegrantes();
                    break;

                case 4:
                    removerAtletaEquipe();
                    break;

                case 5:
                case -1:
                    JOptionPane.showMessageDialog(null,
                            "Saindo do menu do técnico...");
                    break;
            }

        } while (opcao != 5 && opcao != -1);
    }

    public Equipe[] getEquipes() {
        return equipes;
    }

    public int getQtdEquipes() {
        return qtdEquipes;
    }

}
