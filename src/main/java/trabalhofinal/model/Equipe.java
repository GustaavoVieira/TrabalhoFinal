package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Equipe extends EntidadeBase implements Relatorio {

    private String modalidade;
    private Tecnico tecnico;
    private int limiteAtletas = 15;
    private final List<Atleta> atletas = new ArrayList<>();
    private final List<String> historicoCampeonatos = new ArrayList<>();

    public Equipe(String nome, String modalidade, Tecnico tecnico) {
        super(nome);
        setModalidade(modalidade);
        setTecnico(tecnico);
    }

    public boolean adicionarAtleta(Atleta atleta) {
        if (atleta == null) {
            throw new IllegalArgumentException("Atleta inválido.");
        }

        if (atleta.isSuspenso()) {
            throw new AtletaSuspensException(
                    "O atleta " + atleta.getNome() + " está suspenso e não pode ser adicionado.");
        }

        for (Atleta a : atletas) {
            if (a.getMatricula().equalsIgnoreCase(atleta.getMatricula())) {
                throw new AtletaJaEmEquipeException(
                        "O atleta " + atleta.getNome() + " já está nesta equipe.");
            }
        }

        atletas.add(atleta);
        atleta.adicionarHistorico("Ingressou na equipe '" + getNome() + "' (" + modalidade + ")");
        JOptionPane.showMessageDialog(null, "Atleta " + atleta.getNome() + " adicionado com sucesso!");
        return true;
    }

    public void removerAtleta(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Matrícula inválida!");
            return;
        }
        Atleta aRemover = null;
        for (Atleta a : atletas) {
            if (a.getMatricula().equalsIgnoreCase(matricula.trim())) {
                aRemover = a;
                break;
            }
        }

        if (aRemover != null) {
            atletas.remove(aRemover);
            aRemover.adicionarHistorico("Desvinculou-se da equipe '" + getNome() + "'");
            JOptionPane.showMessageDialog(null, "Atleta removido da equipe com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Atleta não encontrado nesta equipe!");
        }
    }

    public void listarAtletas() {
        StringBuilder lista = new StringBuilder(
                "Equipe: " + getNome()
                + "\nModalidade: " + modalidade
                + "\nTécnico: " + (tecnico != null ? tecnico.getNome() : "Sem técnico")
                + "\nVagas: " + atletas.size() + "/" + limiteAtletas
                + "\n\nElenco:\n");
        if (atletas.isEmpty()) {
            lista.append("Nenhum atleta cadastrado.");
        } else {
            for (Atleta a : atletas) {
                lista.append("• ").append(a.getNome())
                     .append(" [Matrícula: ").append(a.getMatricula()).append("]")
                     .append(a.isSuspenso() ? " ⚠ SUSPENSO" : "").append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, lista.toString(), "Elenco da Equipe", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean possuiAtletaPorModalidade(String matricula, String modalidadeAlvo) {
        if (!this.modalidade.equalsIgnoreCase(modalidadeAlvo)) return false;
        for (Atleta a : atletas) {
            if (a.getMatricula().equalsIgnoreCase(matricula)) return true;
        }
        return false;
    }

    public boolean precisaReforcos() {
        return atletas.size() < limiteAtletas;
    }

    public void adicionarCampeonato(String nomeCampeonato) {
        historicoCampeonatos.add(nomeCampeonato);
    }

    public void listarHistoricoCampeonatos() {
        if (historicoCampeonatos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum campeonato no histórico desta equipe.");
            return;
        }
        StringBuilder sb = new StringBuilder("Histórico de campeonatos da equipe " + getNome() + ":\n\n");
        for (String c : historicoCampeonatos) sb.append("• ").append(c).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    @Override
    public void gerarRelatorio() {
        listarAtletas();
        listarHistoricoCampeonatos();
    }

    @Override
    public String resumo() {
        return getNome() + " [" + modalidade + "] – Elenco: " + atletas.size() + "/" + limiteAtletas
                + " | Técnico: " + (tecnico != null ? tecnico.getNome() : "Nenhum");
    }

    public String getModalidade() { return modalidade; }
    public void setModalidade(String modalidade) {
        if (modalidade == null || modalidade.trim().isEmpty()) {
            throw new IllegalArgumentException("A modalidade não pode ficar vazia.");
        }
        this.modalidade = modalidade.trim();
    }

    public Tecnico getTecnico() { return tecnico; }
    public void setTecnico(Tecnico tecnico) {
        if (tecnico == null) {
            throw new IllegalArgumentException("A equipe precisa possuir um técnico.");
        }
        this.tecnico = tecnico;
    }

    public int getLimiteAtletas()       { return limiteAtletas; }
    public void setLimiteAtletas(int lim){ this.limiteAtletas = lim; }

    public int getQtdAtletas()          { return atletas.size(); }
    public List<Atleta> getAtletas()    { return new ArrayList<>(atletas); }
}
