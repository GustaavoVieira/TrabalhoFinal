
package trabalhofinal;

import javax.swing.JOptionPane;

public class Jogo {

    private Equipe equipe1;
    private Equipe equipe2;
    private EspacoEsportivo espaco;
    private String data;
    private String horario;
    private int placarEquipe1;
    private int placarEquipe2;
    private boolean realizado;

    public Jogo(Equipe equipe1, Equipe equipe2,
                EspacoEsportivo espaco, String data, String horario) {

        if (equipe1 == null || equipe2 == null) {
            throw new IllegalArgumentException(
                    "As equipes não podem ser nulas!");
        }

        if (equipe1 == equipe2) {
            throw new IllegalArgumentException(
                    "Uma equipe não pode jogar contra ela mesma!");
        }

        if (espaco == null) {
            throw new IllegalArgumentException(
                    "O jogo precisa possuir um espaço esportivo!");
        }

        if (data == null || data.trim().isEmpty()
                || horario == null || horario.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Data e horário são obrigatórios!");
        }

        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.espaco = espaco;
        this.data = data;
        this.horario = horario;
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

        int placar1;
        int placar2;

        while (true) {

            String resultado1 = JOptionPane.showInputDialog(
                    "Digite o placar da equipe "
                            + equipe1.getNome() + ":");

            if (resultado1 == null) return;

            try {
                placar1 = Integer.parseInt(resultado1.trim());

                if (placar1 < 0) {
                    JOptionPane.showMessageDialog(null,
                            "O placar não pode ser negativo!");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Digite apenas números inteiros!");
            }
        }

        while (true) {

            String resultado2 = JOptionPane.showInputDialog(
                    "Digite o placar da equipe "
                            + equipe2.getNome() + ":");

            if (resultado2 == null) return;

            try {
                placar2 = Integer.parseInt(resultado2.trim());

                if (placar2 < 0) {
                    JOptionPane.showMessageDialog(null,
                            "O placar não pode ser negativo!");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Digite apenas números inteiros!");
            }
        }

        placarEquipe1 = placar1;
        placarEquipe2 = placar2;
        realizado = true;

        JOptionPane.showMessageDialog(null,
                "Resultado registrado com sucesso!");
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
                        + "\nEspaço: " + espaco.getNome()
                        + "\nData: " + data
                        + "\nHorário: " + horario
                        + "\nResultado: " + resultado);
    }

    public Equipe getEquipe1() {
        return equipe1;
    }

    public Equipe getEquipe2() {
        return equipe2;
    }

    public EspacoEsportivo getEspaco() {
        return espaco;
    }

    public String getData() {
        return data;
    }

    public String getHorario() {
        return horario;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public int getPlacarEquipe1() {
        return placarEquipe1;
    }

    public int getPlacarEquipe2() {
        return placarEquipe2;
    }
}
