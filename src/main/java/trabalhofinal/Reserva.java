
package trabalhofinal;

import javax.swing.JOptionPane;

public class Reserva {

    private EspacoEsportivo espaco;
    private Usuario responsavel;
    private String data;
    private String horario;

    public Reserva(EspacoEsportivo espaco, Usuario responsavel,
                   String data, String horario) {

        if (espaco == null) {
            throw new IllegalArgumentException(
                    "O espaço esportivo é obrigatório!");
        }

        if (responsavel == null) {
            throw new IllegalArgumentException(
                    "O responsável pela reserva é obrigatório!");
        }

        if (data == null || data.trim().isEmpty()
                || horario == null || horario.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Data e horário são obrigatórios!");
        }

        this.espaco = espaco;
        this.responsavel = responsavel;
        this.data = data;
        this.horario = horario;
    }

    public String getData() {
        return data;
    }

    public String getHorario() {
        return horario;
    }

    public EspacoEsportivo getEspaco() {
        return espaco;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void exibirDados() {
        JOptionPane.showMessageDialog(null,
                "Espaço: " + espaco.getNome()
                        + "\nResponsável: " + responsavel.getNome()
                        + "\nData: " + data
                        + "\nHorário: " + horario);
    }
}
