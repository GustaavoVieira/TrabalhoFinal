/**
 * Classe base para os usuarios do sistema esportivo universitario.
 * Os dados podem ser recebidos futuramente por Scanner na classe Main
 * e enviados ao construtor desta classe.
 */
public abstract class Usuario {

    private String nome;
    private String email;
    private String senha;

    public Usuario(String nome, String email, String senha) {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome nao pode ficar vazio.");
        }
        this.nome = nome.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail nao pode ficar vazio.");
        }
        this.email = email.trim();
    }

    public void setSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("A senha nao pode ficar vazia.");
        }
        this.senha = senha;
    }

    public boolean verificarSenha(String senhaInformada) {
        return senha != null && senha.equals(senhaInformada);
    }

    public void exibirDados() {
        System.out.println("Nome: " + nome);
        System.out.println("E-mail: " + email);
    }

    // Cada tipo de usuario tera seu proprio menu.
    public abstract void exibirMenu();
}
