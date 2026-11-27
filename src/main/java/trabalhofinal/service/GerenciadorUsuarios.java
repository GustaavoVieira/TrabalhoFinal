package trabalhofinal.service;

import trabalhofinal.exception.*;
import trabalhofinal.model.*;
import trabalhofinal.repository.*;
import trabalhofinal.util.*;

import java.util.List;
import javax.swing.JOptionPane;

public class GerenciadorUsuarios {

    public static void login() {
        String email = JOptionPane.showInputDialog("Digite seu e-mail:");
        if (email == null || email.trim().isEmpty()) return;
        String senha = JOptionPane.showInputDialog("Digite sua senha:");
        if (senha == null || senha.isEmpty()) return;

        for (Usuario u : RepositorioDados.getUsuarios()) {
            if (u.getEmail().equalsIgnoreCase(email.trim()) && u.verificarSenha(senha)) {
                JOptionPane.showMessageDialog(null, "Login realizado com sucesso! Bem-vindo(a), " + u.getNome() + ".");
                u.exibirMenu();
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "E-mail ou senha incorretos!", "Erro de Login", JOptionPane.ERROR_MESSAGE);
    }

    public static void cadastrarPrimeiroCoordenador() {
        String nome  = JOptionPane.showInputDialog("Nome do Coordenador:");
        if (nome == null || nome.trim().isEmpty()) return;
        String email = JOptionPane.showInputDialog("E-mail do Coordenador:");
        if (email == null || email.trim().isEmpty()) return;
        String senha = JOptionPane.showInputDialog("Senha de Acesso:");
        if (senha == null || senha.isEmpty()) return;
        adicionarUsuario(new Coordenador(nome.trim(), email.trim(), senha));
        JOptionPane.showMessageDialog(null, "Primeiro coordenador cadastrado com sucesso!");
    }

    public static void adicionarUsuario(Usuario usuario) {
        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Usuário inválido!");
            return;
        }
        RepositorioDados.getUsuarios().add(usuario);
    }

    public static void cadastrarUsuario() {
        List<Usuario> usuarios = RepositorioDados.getUsuarios();
        if (usuarios.size() >= RepositorioDados.LIMITE_USUARIOS) {
            JOptionPane.showMessageDialog(null, "Limite de usuários atingido!");
            return;
        }
        String[] tipos = {"Atleta", "Técnico", "Coordenador"};
        int tipo = JOptionPane.showOptionDialog(null, "Selecione o tipo de usuário:",
                "Cadastro de Usuário", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo == -1) return;

        String nome = JOptionPane.showInputDialog("Nome:");
        if (nome == null || nome.trim().isEmpty()) return;
        String email = JOptionPane.showInputDialog("E-mail:");
        if (email == null || email.trim().isEmpty()) return;
        if (RepositorioDados.emailJaCadastrado(email.trim())) {
            JOptionPane.showMessageDialog(null, "Este e-mail já está cadastrado!");
            return;
        }
        String senha = JOptionPane.showInputDialog("Senha:");
        if (senha == null || senha.isEmpty()) return;

        Usuario usuario = null;
        switch (tipo) {
            case 0 -> {
                String matricula = JOptionPane.showInputDialog("Matrícula do Atleta:");
                if (matricula == null || matricula.trim().isEmpty()) return;
                if (RepositorioDados.matriculaJaCadastrada(matricula.trim())) {
                    JOptionPane.showMessageDialog(null, "Matrícula já cadastrada!");
                    return;
                }
                String curso = JOptionPane.showInputDialog("Curso Universitário:");
                if (curso == null || curso.trim().isEmpty()) return;
                String modalidade = JOptionPane.showInputDialog("Modalidade esportiva:");
                if (modalidade == null || modalidade.trim().isEmpty()) return;
                usuario = new Atleta(nome.trim(), email.trim(), senha,
                        matricula.trim(), curso.trim(), modalidade.trim());
            }
            case 1 -> usuario = new Tecnico(nome.trim(), email.trim(), senha);
            case 2 -> usuario = new Coordenador(nome.trim(), email.trim(), senha);
        }
        adicionarUsuario(usuario);
        JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
    }

    public static void consultarUsuarios() {
        List<Usuario> usuarios = RepositorioDados.getUsuarios();
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum usuário cadastrado!");
            return;
        }
        StringBuilder sb = new StringBuilder("USUÁRIOS CADASTRADOS:\n\n");
        for (Usuario u : usuarios) {
            String tipo = u instanceof Atleta ? "Atleta"
                    : u instanceof Tecnico ? "Técnico" : "Coordenador";
            sb.append("• ").append(u.getNome())
              .append(" [").append(tipo).append("] – ").append(u.getEmail()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void suspenderAtleta() {
        Atleta a = InputHelper.selecionarAtleta("Selecione o atleta a suspender:");
        if (a == null) return;
        a.suspender();
        a.receberNotificacao("Suspensão Disciplinar", "Você foi temporariamente suspenso de atividades oficiais.");
        JOptionPane.showMessageDialog(null, "Atleta " + a.getNome() + " foi SUSPENSO com sucesso!");
    }

    public static void reativarAtleta() {
        Atleta a = InputHelper.selecionarAtleta("Selecione o atleta a reativar:");
        if (a == null) return;
        a.reativar();
        a.receberNotificacao("Reativação", "Sua suspensão foi encerrada. Você está apto para competir!");
        JOptionPane.showMessageDialog(null, "Atleta " + a.getNome() + " foi REATIVADO com sucesso!");
    }

    public static void enviarComunicadoGeral() {
        String titulo = JOptionPane.showInputDialog("Título do Comunicado Geral:");
        if (titulo == null || titulo.trim().isEmpty()) return;
        String mensagem = JOptionPane.showInputDialog("Mensagem do Comunicado Oficial:");
        if (mensagem == null || mensagem.trim().isEmpty()) return;

        List<Usuario> usuarios = RepositorioDados.getUsuarios();
        for (Usuario u : usuarios) {
            u.receberNotificacao("📢 COMUNICADO DA COORDENAÇÃO: " + titulo.trim(), mensagem.trim());
        }
        JOptionPane.showMessageDialog(null, "Comunicado geral enviado para todos os " + usuarios.size() + " usuários cadastrados!");
    }

    public static void consultarHistoricoNotificacoes() {
        List<Notificacao> global = Notificacao.getHistoricoGlobal();
        if (global.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma notificação registrada no sistema até o momento.");
            return;
        }
        StringBuilder sb = new StringBuilder("HISTÓRICO GERAL DE NOTIFICAÇÕES DO SISTEMA:\n\n");
        for (Notificacao n : global) {
            sb.append("• Para [").append(n.getDestinatario()).append("] - ")
              .append(n.getTitulo()).append(": ").append(n.getMensagem()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
