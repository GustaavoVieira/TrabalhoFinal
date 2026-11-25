package trabalhofinal;

import javax.swing.JOptionPane;

public class Jogo {

    private Equipe equipe1;
    private Equipe equipe2;
    private String data;
    private String horario;
    private String local;
    private int placarEquipe1;
    private int placarEquipe2;
    private boolean realizado;

    public Jogo(Equipe equipe1, Equipe equipe2,
                String data, String horario, String local) {

        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.data = data;
        this.horario = horario;
        this.local = local;
        this.placarEquipe1 = 0;
        this.placarEquipe2 = 0;
        this.realizado = false;
    }

    public void registrarResultado() {

        if (realizado) {
            JOptionPane.showMessageDialog(null,
                    "O resultado deste jogo já foi registrado!");
            return;
        }

        String resultado1 = JOptionPane.showInputDialog(
                "Digite o placar da equipe " + equipe1.getNome() + ":");

        if (resultado1 == null) return;

        String resultado2 = JOptionPane.showInputDialog(
                "Digite o placar da equipe " + equipe2.getNome() + ":");

        if (resultado2 == null) return;

        try {

            int placar1 = Integer.parseInt(resultado1);
            int placar2 = Integer.parseInt(resultado2);

            if (placar1 < 0 || placar2 < 0) {
                JOptionPane.showMessageDialog(null,
                        "Os placares não podem ser negativos!");
                return;
            }

            placarEquipe1 = placar1;
            placarEquipe2 = placar2;
            realizado = true;

            JOptionPane.showMessageDialog(null,
                    "Resultado registrado com sucesso!");

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(null,
                    "Digite apenas números inteiros!");
        }
    }

    public void exibirDados() {

        String resultado;

        if (realizado) {
            resultado = placarEquipe1 + " x " + placarEquipe2;
        } else {
            resultado = "Ainda não realizado";
        }

        JOptionPane.showMessageDialog(null,
                "EQUIPE 1: " + equipe1.getNome()
                        + "\nEQUIPE 2: " + equipe2.getNome()
                        + "\nData: " + data
                        + "\nHorário: " + horario
                        + "\nLocal: " + local
                        + "\nResultado: " + resultado);
    }

    public Equipe getEquipe1() {
        return equipe1;
    }

    public Equipe getEquipe2() {
        return equipe2;
    }

    public boolean isRealizado() {
        return realizado;
    }
}