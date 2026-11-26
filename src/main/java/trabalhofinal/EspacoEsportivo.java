
package trabalhofinal;

import javax.swing.JOptionPane;

public class EspacoEsportivo {

    private String nome;
    private String tipo;
    private int capacidade;

    public EspacoEsportivo(String nome, String tipo, int capacidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.capacidade = capacidade;
    }

    public void exibirDados() {
        JOptionPane.showMessageDialog(null,
                "Nome: " + nome
                        + "\nTipo: " + tipo
                        + "\nCapacidade: " + capacidade);
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCapacidade() {
        return capacidade;
    }
}
