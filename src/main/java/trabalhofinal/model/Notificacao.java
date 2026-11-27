package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.List;

public class Notificacao {

    private static final List<Notificacao> historicoGlobal = new ArrayList<>();

    private final String titulo;
    private final String mensagem;
    private final String destinatario;
    private boolean lida;

    public Notificacao(String titulo, String mensagem, String destinatario) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
        this.lida = false;
        historicoGlobal.add(this);
    }

    public String getTitulo()       { return titulo; }
    public String getMensagem()     { return mensagem; }
    public String getDestinatario() { return destinatario; }
    public boolean isLida()         { return lida; }
    public void marcarComoLida()    { this.lida = true; }

    public static List<Notificacao> getHistoricoGlobal() {
        return historicoGlobal;
    }

    public static List<Notificacao> getNotificacoesPara(String emailDestinatario) {
        List<Notificacao> resultado = new ArrayList<>();
        for (Notificacao n : historicoGlobal) {
            if (n.destinatario.equalsIgnoreCase(emailDestinatario)) {
                resultado.add(n);
            }
        }
        return resultado;
    }

    @Override
    public String toString() {
        return "[" + (lida ? "LIDA" : "NÃO LIDA") + "] " + titulo + ": " + mensagem;
    }
}
