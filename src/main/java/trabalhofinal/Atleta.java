package trabalhofinal;

public class Atleta extends Usuario{
    private String matricula;
    private String curso;
    private String modalidade;

    public Atleta(String nome, String email, String senha,   String matricula, String curso, String modalidade) {
        super(nome, email, senha);
        this.matricula = matricula;
        this.curso = curso;
        this.modalidade = modalidade;
    }


    public String getMatricula() {
        return matricula;
    }

    public String getCurso() {
        return curso;
    }

    public String getModalidade() {
        return modalidade;
    }

    @Override
    public void exibirDados(){
        super.exibirDados();
        System.out.println("Matricula: " + matricula);
        System.out.println("Curso: " + this.curso);
        System.out.println("Modalidade: " + this.modalidade);
    }

    @Override
    public void exibirMenu(){
        //irei realizar um switch quando todas as funcionalidades estiverem prontas
    }
}
