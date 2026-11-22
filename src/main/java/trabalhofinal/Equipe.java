
package trabalhofinal;

import javax.swing.JOptionPane;

public class Equipe {

    private String nome;
    private String modalidade;
    private Tecnico tecnico;
    private Atleta[] atletas;
    private int qtdAtletas;

    public Equipe(String nome, String modalidade, Tecnico tecnico) {
        this.nome = nome;
        this.modalidade = modalidade;
        this.tecnico = tecnico;
        this.atletas = new Atleta[50];
        this.qtdAtletas = 0;
    }

    public boolean adicionarAtleta(Atleta atleta) {

        if (atleta == null) {
            JOptionPane.showMessageDialog(null,
                    "Atleta inválido!");
            return false;
        }

        for (int i = 0; i < qtdAtletas; i++) {

            if (atletas[i].getMatricula().equals(atleta.getMatricula())) {
                JOptionPane.showMessageDialog(null,
                        "Atleta já adicionado!");
                return false;
            }
        }

        if (qtdAtletas < atletas.length) {

            atletas[qtdAtletas] = atleta;
            qtdAtletas++;

            JOptionPane.showMessageDialog(null,
                    "Atleta adicionado com sucesso!");
            return true;

        } else {
            JOptionPane.showMessageDialog(null,
                    "Equipe cheia!");
            return false;
        }

    }

    public void removerAtleta(String matricula) {

        for (int i = 0; i < qtdAtletas; i++) {

            if (atletas[i].getMatricula().equals(matricula)) {

                for (int j = i; j < qtdAtletas - 1; j++) {
                    atletas[j] = atletas[j + 1];
                }

                atletas[qtdAtletas - 1] = null;
                qtdAtletas--;

                JOptionPane.showMessageDialog(null,
                        "Atleta removido da equipe com sucesso!");

                return;
            }
        }

        JOptionPane.showMessageDialog(null,
                "Atleta não encontrado nesta equipe!");
    }

    public void listarAtletas() {

        String lista = "Equipe: " + nome
                + "\nModalidade: " + modalidade
                + "\n\nAtletas cadastrados:\n";

        if (qtdAtletas == 0) {
            lista += "Nenhum atleta cadastrado.";
        } else {

            for (int i = 0; i < qtdAtletas; i++) {
                lista += "\n" + atletas[i].getNome();
            }
        }

        JOptionPane.showMessageDialog(null, lista);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }
}
