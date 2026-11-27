package trabalhofinal.util;

import trabalhofinal.model.*;
import trabalhofinal.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Classe utilitária para entrada e seleção de dados via JOptionPane.
 * Responsabilidade única: interação de UI reutilizável (datas, horários, seleções).
 */
public class InputHelper {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FORMATO_HORARIO =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public static String solicitarData() {
        while (true) {
            String data = JOptionPane.showInputDialog("Digite a data (DD/MM/AAAA):");
            if (data == null) return null;
            try {
                LocalDate d = LocalDate.parse(data.trim(), FORMATO_DATA);
                if (d.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(null, "Não é possível agendar em datas passadas!");
                    continue;
                }
                return FORMATO_DATA.format(d);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Data inválida! Use o formato DD/MM/AAAA.");
            }
        }
    }

    public static String solicitarHorario() {
        while (true) {
            String h = JOptionPane.showInputDialog("Digite o horário (HH:mm):");
            if (h == null) return null;
            try {
                LocalTime t = LocalTime.parse(h.trim(), FORMATO_HORARIO);
                return FORMATO_HORARIO.format(t);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Horário inválido! Use o formato HH:mm.");
            }
        }
    }

    public static Atleta selecionarAtleta(String msg) {
        List<Atleta> atletas = new ArrayList<>();
        for (Usuario u : RepositorioDados.getUsuarios()) {
            if (u instanceof Atleta a) atletas.add(a);
        }
        if (atletas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum atleta cadastrado!");
            return null;
        }
        String[] opcoes = atletas.stream()
                .map(a -> a.getNome() + " [" + a.getMatricula() + "]" + (a.isSuspenso() ? " ⚠ SUSPENSO" : ""))
                .toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null, msg,
                "Selecionar Atleta", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return null;
        for (Atleta a : atletas) {
            if ((a.getNome() + " [" + a.getMatricula() + "]" + (a.isSuspenso() ? " ⚠ SUSPENSO" : "")).equals(escolha)) {
                return a;
            }
        }
        return null;
    }

    public static Campeonato selecionarCampeonato() {
        List<Campeonato> campeonatos = RepositorioDados.getCampeonatos();
        if (campeonatos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum campeonato cadastrado!");
            return null;
        }
        String[] opcoes = campeonatos.stream()
                .map(c -> c.getNome() + " – " + c.getModalidade() + " [" + c.getStatus() + "]")
                .toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Selecione um campeonato:", "Campeonatos",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return null;
        return campeonatos.stream()
                .filter(c -> (c.getNome() + " – " + c.getModalidade() + " [" + c.getStatus() + "]").equals(escolha))
                .findFirst().orElse(null);
    }

    public static EspacoEsportivo selecionarEspaco() {
        List<EspacoEsportivo> espacos = RepositorioDados.getEspacos();
        if (espacos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum espaço esportivo cadastrado!");
            return null;
        }
        String[] opcoes = espacos.stream()
                .map(EspacoEsportivo::resumo).toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Selecione um espaço esportivo:", "Espaços Esportivos",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return null;
        return espacos.stream().filter(e -> e.resumo().equals(escolha)).findFirst().orElse(null);
    }

    public static Equipe selecionarEquipeJogo(Campeonato campeonato, String msg) {
        List<Equipe> eq = campeonato.getEquipes();
        String[] opcoes = eq.stream()
                .map(e -> e.getNome() + " – " + e.getModalidade())
                .toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null, msg,
                "Equipes do campeonato", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return null;
        return eq.stream()
                .filter(e -> (e.getNome() + " – " + e.getModalidade()).equals(escolha))
                .findFirst().orElse(null);
    }
}
