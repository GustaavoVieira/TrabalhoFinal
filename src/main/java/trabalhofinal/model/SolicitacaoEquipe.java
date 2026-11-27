package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

public class SolicitacaoEquipe {
    private Atleta atleta;
    private Equipe equipe;
    private String situacao;

    public SolicitacaoEquipe(Atleta atleta, Equipe equipe) {
        this.atleta = atleta;
        this.equipe = equipe;
        this.situacao = "PENDENTE";
    }

    public Atleta getAtleta() {
        return atleta;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
