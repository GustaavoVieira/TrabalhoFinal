package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

import javax.swing.JOptionPane;

/**
 * Representa uma reserva de espaço esportivo.
 * Suporta reserva por usuário e vinculação opcional a equipes.
 * Implementa controle de status (PENDENTE, APROVADA, REJEITADA).
 * Requisito 5 (Reserva de Espaços).
 */
public class Reserva {

    public static final String STATUS_PENDENTE  = "PENDENTE";
    public static final String STATUS_APROVADA  = "APROVADA";
    public static final String STATUS_REJEITADA = "REJEITADA";

    private EspacoEsportivo espaco;
    private Usuario responsavel;
    private Equipe equipe;
    private String data;
    private String horario;
    private String status;

    public Reserva(EspacoEsportivo espaco, Usuario responsavel,
                   String data, String horario) {
        this(espaco, responsavel, null, data, horario, STATUS_APROVADA);
    }

    public Reserva(EspacoEsportivo espaco, Usuario responsavel, Equipe equipe,
                   String data, String horario, String status) {

        if (espaco == null) {
            throw new IllegalArgumentException("O espaço esportivo é obrigatório!");
        }
        if (responsavel == null) {
            throw new IllegalArgumentException("O responsável pela reserva é obrigatório!");
        }
        if (data == null || data.trim().isEmpty()
                || horario == null || horario.trim().isEmpty()) {
            throw new IllegalArgumentException("Data e horário são obrigatórios!");
        }

        this.espaco = espaco;
        this.responsavel = responsavel;
        this.equipe = equipe;
        this.data = data.trim();
        this.horario = horario.trim();
        this.status = (status != null) ? status : STATUS_PENDENTE;
    }

    public void aprovar() {
        this.status = STATUS_APROVADA;
        responsavel.receberNotificacao("Aprovação de Reserva",
                "Sua solicitação de reserva para o espaço '" + espaco.getNome()
                        + "' no dia " + data + " às " + horario + " foi APROVADA!");
    }

    public void rejeitar(String motivo) {
        this.status = STATUS_REJEITADA;
        responsavel.receberNotificacao("Reserva Rejeitada",
                "Sua solicitação de reserva para o espaço '" + espaco.getNome()
                        + "' no dia " + data + " às " + horario + " foi rejeitada. Motivo: " + motivo);
    }

    public void exibirDados() {
        JOptionPane.showMessageDialog(null,
                "Espaço: " + espaco.getNome()
                        + "\nResponsável: " + responsavel.getNome()
                        + (equipe != null ? "\nEquipe: " + equipe.getNome() : "")
                        + "\nData: " + data
                        + "\nHorário: " + horario
                        + "\nStatus: " + status);
    }

    public EspacoEsportivo getEspaco()     { return espaco; }
    public Usuario getResponsavel()         { return responsavel; }
    public Equipe getEquipe()              { return equipe; }
    public String getData()                 { return data; }
    public String getHorario()              { return horario; }
    public String getStatus()               { return status; }
    public void setStatus(String status)    { this.status = status; }
}
