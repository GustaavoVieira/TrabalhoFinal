package trabalhofinal.model;

import trabalhofinal.exception.*;
import trabalhofinal.interfaces.*;
import trabalhofinal.repository.*;
import trabalhofinal.service.*;
import trabalhofinal.util.*;

import java.util.List;
import javax.swing.JOptionPane;

public abstract class Usuario extends EntidadeBase implements Notificavel {

    private String email;
    private String senha;

    public Usuario(String nome, String email, String senha) {
        super(nome);
        setEmail(email);
        setSenha(senha);
    }

    public boolean verificarSenha(String senhaInformada) {
        return senha != null && senha.equals(senhaInformada);
    }

    @Override
    public void receberNotificacao(String titulo, String mensagem) {
        new Notificacao(titulo, mensagem, getEmail());
        JOptionPane.showMessageDialog(null,
                "🔔 " + titulo + "\n\n" + mensagem, "Notificação",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void exibirDados() {
        JOptionPane.showMessageDialog(null,
                "Nome: "   + getNome()
                + "\nE-mail: " + email);
    }

    public abstract void exibirMenu();

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("O e-mail não pode ficar vazio.");
        this.email = email.trim();
    }

    public void setSenha(String senha) {
        if (senha == null || senha.isEmpty())
            throw new IllegalArgumentException("A senha não pode ficar vazia.");
        this.senha = senha;
    }
}
