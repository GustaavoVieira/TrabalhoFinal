
package trabalhofinal;

import javax.swing.JOptionPane;

public class Equipe {

    private String nome;
    private String modalidade;
    private Tecnico tecnico;
    private Atleta[] atletas;
    private int qtdAtletas;

    public Equipe(String nome, String modalidade, Tecnico tecnico) {
        setNome(nome);
        setModalidade(modalidade);
        setTecnico(tecnico);
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

            if (atletas[i].getMatricula().equalsIgnoreCase(
                    atleta.getMatricula())) {

                JOptionPane.showMessageDialog(null,
                        "Atleta já adicionado!");
                return false;
            }
        }

        if (qtdAtletas >= atletas.length) {
            JOptionPane.showMessageDialog(null,
                    "Equipe cheia!");
            return false;
        }

        atletas[qtdAtletas] = atleta;
        qtdAtletas++;

        JOptionPane.showMessageDialog(null,
                "Atleta adicionado com sucesso!");

        return true;
    }

    public void removerAtleta(String matricula) {

        if (matricula == null || matricula.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Matrícula inválida!");
            return;
        }

        for (int i = 0; i < qtdAtletas; i++) {

            if (atletas[i].getMatricula().equalsIgnoreCase(
                    matricula.trim())) {

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
                + "\nTécnico: " + tecnico.getNome()
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

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O nome da equipe não pode ficar vazio!");
        }

        this.nome = nome.trim();
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {

        if (modalidade == null || modalidade.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "A modalidade não pode ficar vazia!");
        }

        this.modalidade = modalidade.trim();
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {

        if (tecnico == null) {
            throw new IllegalArgumentException(
                    "A equipe precisa possuir um técnico!");
        }

        this.tecnico = tecnico;
    }

    public int getQtdAtletas() {
        return qtdAtletas;
    }
}
