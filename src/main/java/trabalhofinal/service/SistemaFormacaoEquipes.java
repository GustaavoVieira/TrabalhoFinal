package trabalhofinal.service;

import trabalhofinal.exception.*;
import trabalhofinal.model.*;
import trabalhofinal.repository.*;
import trabalhofinal.util.*;

import java.util.ArrayList;
import java.util.List;

public class SistemaFormacaoEquipes {

    public static class Sugestao {
        private final Equipe equipe;
        private final int pontuacaoCompatibilidade;
        private final String justificativa;

        public Sugestao(Equipe equipe, int pontuacaoCompatibilidade, String justificativa) {
            this.equipe = equipe;
            this.pontuacaoCompatibilidade = pontuacaoCompatibilidade;
            this.justificativa = justificativa;
        }

        public Equipe getEquipe() { return equipe; }
        public int getPontuacaoCompatibilidade() { return pontuacaoCompatibilidade; }
        public String getJustificativa() { return justificativa; }

        @Override
        public String toString() {
            return equipe.getNome() + " (" + equipe.getModalidade() + ") - Compatibilidade: "
                    + pontuacaoCompatibilidade + "%\n  Motivo: " + justificativa;
        }
    }

    public static List<Sugestao> sugerirEquipesParaAtleta(Atleta atleta, List<Equipe> todasEquipes) {
        List<Sugestao> sugestoes = new ArrayList<>();
        if (atleta == null || todasEquipes == null) return sugestoes;

        for (Equipe equipe : todasEquipes) {

            if (equipe.getAtletas().contains(atleta)) continue;

            int score = 0;
            List<String> razoes = new ArrayList<>();

            if (equipe.getModalidade().equalsIgnoreCase(atleta.getModalidade())) {
                score += 40;
                razoes.add("Modalidade compatível (" + atleta.getModalidade() + ")");
            } else {
                continue;
            }

            if (equipe.precisaReforcos()) {
                score += 25;
                razoes.add("Equipe precisa urgentemente de reforços (" + equipe.getQtdAtletas() + " atletas)");
            }

            int participacoes = atleta.getHistorico().size();
            if (participacoes > 0) {
                int bonusHistorico = Math.min(15, participacoes * 5);
                score += bonusHistorico;
                razoes.add("Atleta experiente (" + participacoes + " evento(s) no histórico)");
            } else {
                score += 10;
                razoes.add("Atleta iniciante com potencial de desenvolvimento");
            }

            if (atleta.getNivelDesempenho() >= 7) {
                score += 10;
                razoes.add("Alto nível de desempenho técnico (" + atleta.getNivelDesempenho() + "/10)");
            } else {
                score += 5;
            }

            if (atleta.getDisponibilidadeHorario() != null && !atleta.getDisponibilidadeHorario().isEmpty()) {
                score += 10;
                razoes.add("Disponibilidade de horário informada: " + atleta.getDisponibilidadeHorario());
            }

            sugestoes.add(new Sugestao(equipe, score, String.join(", ", razoes)));
        }

        sugestoes.sort((s1, s2) -> Integer.compare(s2.getPontuacaoCompatibilidade(), s1.getPontuacaoCompatibilidade()));
        return sugestoes;
    }

    public static List<Atleta> sugerirAtletasParaEquipe(Equipe equipe, List<Usuario> usuarios) {
        List<Atleta> atletasSugeridos = new ArrayList<>();
        if (equipe == null || usuarios == null) return atletasSugeridos;

        for (Usuario u : usuarios) {
            if (u instanceof Atleta) {
                Atleta a = (Atleta) u;
                if (!a.isSuspenso()
                        && a.getModalidade().equalsIgnoreCase(equipe.getModalidade())
                        && !equipe.getAtletas().contains(a)) {
                    atletasSugeridos.add(a);
                }
            }
        }

        atletasSugeridos.sort((a1, a2) -> Integer.compare(a2.getNivelDesempenho(), a1.getNivelDesempenho()));
        return atletasSugeridos;
    }
}
