package trabalhofinal.repository;

import trabalhofinal.model.*;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDados {

    public static final int LIMITE_USUARIOS    = 100;
    public static final int LIMITE_CAMPEONATOS = 100;
    public static final int LIMITE_ESPACOS     = 100;
    public static final int LIMITE_RESERVAS    = 500;

    private static final List<Usuario>         usuarios    = new ArrayList<>();
    private static final List<Equipe>          equipes     = new ArrayList<>();
    private static final List<Campeonato>      campeonatos = new ArrayList<>();
    private static final List<EspacoEsportivo> espacos     = new ArrayList<>();
    private static final List<Reserva>         reservas    = new ArrayList<>();

    public static List<Usuario> getUsuarios() { return usuarios; }

    public static boolean emailJaCadastrado(String email) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public static boolean matriculaJaCadastrada(String matricula) {
        return usuarios.stream()
                .filter(u -> u instanceof Atleta)
                .anyMatch(u -> ((Atleta) u).getMatricula().equalsIgnoreCase(matricula));
    }

    public static List<Atleta> getAtletasPorModalidade(String modalidade) {
        List<Atleta> resultado = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Atleta a && a.getModalidade().equalsIgnoreCase(modalidade)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public static List<Equipe> getEquipes() { return equipes; }

    public static boolean equipeJaCadastrada(String nome, String modalidade) {
        return equipes.stream().anyMatch(e ->
                e.getNome().equalsIgnoreCase(nome) && e.getModalidade().equalsIgnoreCase(modalidade));
    }

    public static boolean atletaJaEmEquipeDaModalidade(String matricula, String modalidade) {
        for (Equipe e : equipes) {
            if (e.possuiAtletaPorModalidade(matricula, modalidade)) return true;
        }
        return false;
    }

    public static List<Campeonato> getCampeonatos() { return campeonatos; }

    public static boolean campeonatoJaCadastrado(String nome, String modalidade) {
        return campeonatos.stream().anyMatch(c ->
                c.getNome().equalsIgnoreCase(nome) && c.getModalidade().equalsIgnoreCase(modalidade));
    }

    public static List<EspacoEsportivo> getEspacos() { return espacos; }

    public static boolean espacoJaCadastrado(String nome) {
        return espacos.stream().anyMatch(e -> e.getNome().equalsIgnoreCase(nome));
    }

    public static List<Reserva> getReservas() { return reservas; }
}
