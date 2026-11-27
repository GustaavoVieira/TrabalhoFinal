package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class EspacoEsportivo extends EntidadeBase implements Relatorio {

    public static final String STATUS_ATIVO      = "ATIVO";
    public static final String STATUS_MANUTENCAO = "MANUTENCAO";
    public static final String STATUS_INATIVO    = "INATIVO";

    private String tipo;
    private String localizacao;
    private int capacidade;
    private String statusFuncionamento;
    private final List<String> horariosDisponiveis = new ArrayList<>();

    public EspacoEsportivo(String nome, String tipo, String localizacao,
                           int capacidade) {
        super(nome);
        setTipo(tipo);
        setLocalizacao(localizacao);
        setCapacidade(capacidade);
        this.statusFuncionamento = STATUS_ATIVO;
    }

    public boolean estaEmManutencao() {
        return STATUS_MANUTENCAO.equalsIgnoreCase(statusFuncionamento);
    }

    public void adicionarHorario(String horario) {
        if (horario != null && !horario.trim().isEmpty()
                && !horariosDisponiveis.contains(horario.trim())) {
            horariosDisponiveis.add(horario.trim());
        }
    }

    public void removerHorario(String horario) {
        horariosDisponiveis.remove(horario);
    }

    public List<String> getHorariosDisponiveis() {
        return new ArrayList<>(horariosDisponiveis);
    }

    public void editar() {
        String novoNome = JOptionPane.showInputDialog(
                "Nome atual: " + getNome() + "\nNovo nome (deixe em branco para manter):");
        if (novoNome != null && !novoNome.trim().isEmpty()) setNome(novoNome.trim());

        String novoTipo = JOptionPane.showInputDialog(
                "Tipo atual: " + tipo + "\nNovo tipo (deixe em branco para manter):");
        if (novoTipo != null && !novoTipo.trim().isEmpty()) setTipo(novoTipo.trim());

        String novaLoc = JOptionPane.showInputDialog(
                "Localização atual: " + localizacao
                        + "\nNova localização (deixe em branco para manter):");
        if (novaLoc != null && !novaLoc.trim().isEmpty()) setLocalizacao(novaLoc.trim());

        String[] statusOpcoes = {STATUS_ATIVO, STATUS_MANUTENCAO, STATUS_INATIVO};
        String novoStatus = (String) JOptionPane.showInputDialog(
                null, "Selecione o status:", "Status do espaço",
                JOptionPane.QUESTION_MESSAGE, null,
                statusOpcoes, statusFuncionamento);
        if (novoStatus != null) setStatusFuncionamento(novoStatus);

        JOptionPane.showMessageDialog(null, "Espaço atualizado com sucesso!");
    }

    @Override
    public void gerarRelatorio() {
        exibirDados();
    }

    @Override
    public String resumo() {
        return getNome() + " [" + tipo + "] – " + statusFuncionamento
                + " | Cap.: " + capacidade;
    }

    public void exibirDados() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(getNome())
          .append("\nTipo: ").append(tipo)
          .append("\nLocalização: ").append(localizacao)
          .append("\nCapacidade: ").append(capacidade)
          .append("\nStatus: ").append(statusFuncionamento);
        if (!horariosDisponiveis.isEmpty()) {
            sb.append("\nHorários disponíveis: ");
            for (String h : horariosDisponiveis) sb.append(h).append("  ");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty())
            throw new IllegalArgumentException("O tipo não pode ser vazio.");
        this.tipo = tipo.trim();
    }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) {
        if (localizacao == null || localizacao.trim().isEmpty())
            throw new IllegalArgumentException("A localização não pode ser vazia.");
        this.localizacao = localizacao.trim();
    }

    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) {
        if (capacidade <= 0)
            throw new IllegalArgumentException("A capacidade deve ser maior que zero.");
        this.capacidade = capacidade;
    }

    public String getStatusFuncionamento() { return statusFuncionamento; }
    public void setStatusFuncionamento(String status) {
        if (status == null || status.trim().isEmpty())
            throw new IllegalArgumentException("O status não pode ser vazio.");
        this.statusFuncionamento = status.trim().toUpperCase();
    }
}
