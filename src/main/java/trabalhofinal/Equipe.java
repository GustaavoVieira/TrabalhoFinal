package trabalhofinal;

public class Equipe {

    private String nome;
    private String modalidade;
    private Tecnico tecnico;
    private Atleta[] atletas;
    private int qtdAtletas;

    public Equipe(String nome, String modalidade, Tecnico tecnico) {
        this.nome = nome;
        this.modalidade = modalidade;
        this.tecnico = tecnico;
        this.atletas = new Atleta[50];
        this.qtdAtletas = 0;
    }

    public void adicionarAtleta(Atleta atleta) {

        for  (int i = 0; i < this.atletas.length; i++) {
            if(this.atletas[i].getMatricula() == atletas[i].getMatricula()) {
                System.out.println("Atleta já adicionado");
            }
        }
        if (qtdAtletas < atletas.length) {
            atletas[qtdAtletas] = atleta;
            qtdAtletas++;

            System.out.println("Atleta adicionado com sucesso!");
        } else {
            System.out.println("Equipe cheia!");
        }
    }

    public void listarAtletas() {

        System.out.println("Equipe: " + nome);

        for (int i = 0; i < qtdAtletas; i++) {
            System.out.println(atletas[i].getNome());
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }
}
