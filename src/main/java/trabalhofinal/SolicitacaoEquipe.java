package trabalhofinal;

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
