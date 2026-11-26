package trabalhofinal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import javax.swing.*;

public class Main {

    private static Usuario[] usuarios = new Usuario[100];
    private static Equipe[] equipes = new Equipe[100];
    private static Campeonato[] campeonatos = new Campeonato[100];
    private static EspacoEsportivo[] espacos = new EspacoEsportivo[100];
    private static Reserva[] reservas = new Reserva[100];

    private static int qtdEspacos = 0;
    private static int qtdReservas = 0;
    private static int qtdUsuarios = 0;
    private static int qtdEquipes = 0;
    private static int qtdCampeonatos = 0;


    public static void main(String[] args) {
        menuPrincipal();
    }

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter FORMATO_HORARIO =
            DateTimeFormatter.ofPattern("HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);

    public static void login() {

        String email = JOptionPane.showInputDialog("Digite seu e-mail:");
        if (email == null || email.trim().isEmpty()) return;

        String senha = JOptionPane.showInputDialog("Digite sua senha:");
        if (senha == null || senha.isEmpty()) return;

        for (int i = 0; i < qtdUsuarios; i++) {

            Usuario usuario = usuarios[i];

            if (usuario.getEmail().equalsIgnoreCase(email.trim())
                    && usuario.verificarSenha(senha)) {

                JOptionPane.showMessageDialog(null,
                        "Login realizado com sucesso!");

                usuario.exibirMenu();
                return;
            }
        }

        JOptionPane.showMessageDialog(null,
                "E-mail ou senha incorretos!");
    }

    public static void menuPrincipal() {

        int opcao;

        do {
            String[] opcoes;

            if (qtdUsuarios == 0) {
                opcoes = new String[]{
                        "Cadastrar primeiro Coordenador",
                        "Sair"
                };
            } else {
                opcoes = new String[]{
                        "Fazer login",
                        "Sair"
                };
            }

            opcao = JOptionPane.showOptionDialog(
                    null,
                    "Sistema de Gerenciamento Esportivo",
                    "Menu Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (qtdUsuarios == 0) {
                if (opcao == 0) {
                    cadastrarPrimeiroCoordenador();
                }
            } else {
                if (opcao == 0) {
                    login();
                }
            }

        } while (opcao != 1 && opcao != -1);
    }

    public static void cadastrarPrimeiroCoordenador() {

        String nome = JOptionPane.showInputDialog("Digite o nome:");
        if (nome == null || nome.trim().isEmpty()) return;

        String email = JOptionPane.showInputDialog("Digite o e-mail:");
        if (email == null || email.trim().isEmpty()) return;

        String senha = JOptionPane.showInputDialog("Digite a senha:");
        if (senha == null || senha.isEmpty()) return;

        Coordenador coordenador = new Coordenador(nome, email, senha);

        adicionarUsuario(coordenador);

        JOptionPane.showMessageDialog(null,
                "Primeiro coordenador cadastrado com sucesso!");
    }




    public static void cadastrarUsuario() {

        if (qtdUsuarios >= usuarios.length) {
            JOptionPane.showMessageDialog(null,
                    "Limite de usuários atingido!");
            return;
        }

        String[] tipos = {
                "Atleta",
                "Técnico",
                "Coordenador"
        };

        int tipo = JOptionPane.showOptionDialog(
                null,
                "Selecione o tipo de usuário:",
                "Cadastro de usuário",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                tipos,
                tipos[0]
        );

        if (tipo == -1) {
            return;
        }

        String nome = JOptionPane.showInputDialog("Digite o nome:");
        if (nome == null || nome.trim().isEmpty()) return;

        String email = JOptionPane.showInputDialog("Digite o e-mail:");
        if (email == null || email.trim().isEmpty()) return;

        if (emailJaCadastrado(email.trim())) {
            JOptionPane.showMessageDialog(null,
                    "Este e-mail já está cadastrado!");
            return;
        }

        String senha = JOptionPane.showInputDialog("Digite a senha:");
        if (senha == null || senha.isEmpty()) return;

        Usuario usuario = null;

        switch (tipo) {

            case 0:
                String matricula = JOptionPane.showInputDialog(
                        "Digite a matrícula:");
                if (matricula == null || matricula.trim().isEmpty()) return;

                if (matriculaJaCadastrada(matricula.trim())) {
                    JOptionPane.showMessageDialog(null,
                            "Esta matrícula já está cadastrada!");
                    return;
                }

                String curso = JOptionPane.showInputDialog("Digite o curso:");
                if (curso == null || curso.trim().isEmpty()) return;

                String modalidade = JOptionPane.showInputDialog(
                        "Digite a modalidade:");
                if (modalidade == null || modalidade.trim().isEmpty()) return;

                usuario = new Atleta(
                        nome.trim(),
                        email.trim(),
                        senha,
                        matricula.trim(),
                        curso.trim(),
                        modalidade.trim()
                );
                break;

            case 1:
                usuario = new Tecnico(nome.trim(), email.trim(), senha);
                break;

            case 2:
                usuario = new Coordenador(nome.trim(), email.trim(), senha);
                break;
        }

        adicionarUsuario(usuario);

        JOptionPane.showMessageDialog(null,
                "Usuário cadastrado com sucesso!");
    }


    public static void cadastrarCampeonato() {

        if (qtdCampeonatos >= campeonatos.length) {
            JOptionPane.showMessageDialog(null,
                    "Limite de campeonatos atingido!");
            return;
        }

        String nome = JOptionPane.showInputDialog(
                "Digite o nome do campeonato:");

        if (nome == null || nome.trim().isEmpty()) return;

        String modalidade = JOptionPane.showInputDialog(
                "Digite a modalidade do campeonato:");

        if (modalidade == null || modalidade.trim().isEmpty()) return;

        if (campeonatoJaCadastrado(nome.trim(), modalidade.trim())) {
            JOptionPane.showMessageDialog(null,
                    "Este campeonato já está cadastrado!");
            return;
        }

        Campeonato campeonato = new Campeonato(nome, modalidade);

        campeonatos[qtdCampeonatos] = campeonato;
        qtdCampeonatos++;

        JOptionPane.showMessageDialog(null,
                "Campeonato cadastrado com sucesso!");
    }


    public static void adicionarUsuario(Usuario usuario) {

        if (usuario == null) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível cadastrar um usuário inválido!");
            return;
        }

        if (qtdUsuarios < usuarios.length) {

            usuarios[qtdUsuarios] = usuario;
            qtdUsuarios++;

        } else {

            JOptionPane.showMessageDialog(null,
                    "Limite de usuários atingido!");
        }
    }





    public static boolean adicionarEquipe(Equipe equipe) {

        if (equipe == null) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível cadastrar uma equipe inválida!");
            return false;
        }

        if (equipeJaCadastrada(
                equipe.getNome(), equipe.getModalidade())) {
            JOptionPane.showMessageDialog(null,
                    "Esta equipe já está cadastrada!");
            return false;
        }

        if (qtdEquipes < equipes.length) {
            equipes[qtdEquipes] = equipe;
            qtdEquipes++;
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "Limite de equipes atingido!");
            return false;
        }
    }

    public static Equipe selecionarEquipeJogo(
            Campeonato campeonato, String mensagem) {

        if (campeonato.getQtdEquipes() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Este campeonato não possui equipes!");
            return null;
        }

        String[] opcoes = new String[campeonato.getQtdEquipes()];

        for (int i = 0; i < campeonato.getQtdEquipes(); i++) {
            Equipe equipe = campeonato.getEquipe(i);

            opcoes[i] = equipe.getNome()
                    + " - " + equipe.getModalidade();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                mensagem,
                "Selecionar equipe",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return null;

        for (int i = 0; i < campeonato.getQtdEquipes(); i++) {
            if (opcoes[i].equals(escolha)) {
                return campeonato.getEquipe(i);
            }
        }

        return null;
    }


    public static void cadastrarJogo() {

        Campeonato campeonato = selecionarCampeonato();

        if (campeonato == null) return;

        if (campeonato.getQtdEquipes() < 2) {
            JOptionPane.showMessageDialog(null,
                    "O campeonato precisa ter pelo menos duas equipes para cadastrar um jogo!");
            return;
        }

        Equipe equipe1 = selecionarEquipeJogo(
                campeonato, "Selecione a primeira equipe:");

        if (equipe1 == null) return;

        Equipe equipe2 = selecionarEquipeJogo(
                campeonato, "Selecione a segunda equipe:");

        if (equipe2 == null) return;

        if (equipe1 == equipe2) {
            JOptionPane.showMessageDialog(null,
                    "Uma equipe não pode jogar contra ela mesma!");
            return;
        }

        EspacoEsportivo espaco = selecionarEspaco();

        if (espaco == null) return;

        String data = solicitarData();

        if (data == null) return;

        String horario = solicitarHorario();

        if (horario == null) return;

        if (!verificarDisponibilidade(espaco, data, horario)) {
            JOptionPane.showMessageDialog(null,
                    "Este espaço já está ocupado nessa data e horário!");
            return;
        }

        Jogo jogo = new Jogo(
                equipe1, equipe2, espaco, data, horario);

        campeonato.adicionarJogo(jogo);
    }


    public static void consultarUsuarios() {

        if (qtdUsuarios == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum usuário cadastrado!");
            return;
        }

        StringBuilder lista = new StringBuilder("USUÁRIOS CADASTRADOS:\n\n");

        for (int i = 0; i < qtdUsuarios; i++) {

            Usuario usuario = usuarios[i];

            lista.append("Nome: ").append(usuario.getNome()).append("\nE-mail: ").append(usuario.getEmail());

            if (usuario instanceof Atleta) {
                lista.append("\nTipo: Atleta");
            } else if (usuario instanceof Tecnico) {
                lista.append("\nTipo: Técnico");
            } else if (usuario instanceof Coordenador) {
                lista.append("\nTipo: Coordenador");
            }

            lista.append("\n\n");
        }

        JOptionPane.showMessageDialog(null, lista.toString());
    }

    public static void consultarEquipes() {

        if (qtdEquipes == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhuma equipe cadastrada!");
            return;
        }

        StringBuilder lista = new StringBuilder("EQUIPES CADASTRADAS:\n\n");

        for (int i = 0; i < qtdEquipes; i++) {

            lista.append("Nome: ").append(equipes[i].getNome()).append("\nModalidade: ").append(equipes[i].getModalidade()).append("\n\n");
        }

        JOptionPane.showMessageDialog(null, lista.toString());
    }

    public static void consultarCampeonatos() {

        if (qtdCampeonatos == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum campeonato cadastrado!");
            return;
        }

        for (int i = 0; i < qtdCampeonatos; i++) {
            campeonatos[i].listarEquipes();
            campeonatos[i].listarJogos();
        }
    }

    public static Campeonato selecionarCampeonato() {

        if (qtdCampeonatos == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum campeonato cadastrado!");
            return null;
        }

        String[] opcoes = new String[qtdCampeonatos];

        for (int i = 0; i < qtdCampeonatos; i++) {
            opcoes[i] = campeonatos[i].getNome()
                    + " - " + campeonatos[i].getModalidade();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Selecione um campeonato:",
                "Campeonatos cadastrados",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return null;

        for (int i = 0; i < qtdCampeonatos; i++) {
            if (opcoes[i].equals(escolha)) {
                return campeonatos[i];
            }
        }

        return null;
    }

    public static void adicionarEquipeCampeonato() {

        Campeonato campeonato = selecionarCampeonato();

        if (campeonato == null) return;

        if (qtdEquipes == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhuma equipe cadastrada no sistema!");
            return;
        }

        String[] opcoes = new String[qtdEquipes];

        for (int i = 0; i < qtdEquipes; i++) {
            opcoes[i] = equipes[i].getNome()
                    + " - " + equipes[i].getModalidade();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Selecione uma equipe:",
                "Adicionar equipe",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return;

        for (int i = 0; i < qtdEquipes; i++) {
            if (opcoes[i].equals(escolha)) {
                campeonato.adicionarEquipe(equipes[i]);
                return;
            }
        }
    }

    public static void registrarResultado() {

        Campeonato campeonato = selecionarCampeonato();

        if (campeonato == null) return;

        campeonato.registrarResultadoJogo();
    }

    public static void consultarResultados() {

        Campeonato campeonato = selecionarCampeonato();

        if (campeonato == null) return;

        campeonato.consultarResultados();
    }



    public static void cadastrarEspaco() {

        if (qtdEspacos >= espacos.length) {
            JOptionPane.showMessageDialog(null,
                    "Limite de espaços esportivos atingido!");
            return;
        }

        String nome = JOptionPane.showInputDialog(
                "Digite o nome do espaço esportivo:");

        if (nome == null || nome.trim().isEmpty()) return;

        if (espacoJaCadastrado(nome)) {
            JOptionPane.showMessageDialog(null,
                    "Já existe um espaço com esse nome!");
            return;
        }

        String tipo = JOptionPane.showInputDialog(
                "Digite o tipo do espaço (quadra, campo, ginásio etc.):");

        if (tipo == null || tipo.trim().isEmpty()) return;

        int capacidade;

        while (true) {

            String valorCapacidade = JOptionPane.showInputDialog(
                    "Digite a capacidade máxima de pessoas:");

            if (valorCapacidade == null) return;

            try {
                capacidade = Integer.parseInt(valorCapacidade.trim());

                if (capacidade <= 0) {
                    JOptionPane.showMessageDialog(null,
                            "A capacidade deve ser maior que zero!");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Digite uma capacidade válida!");
            }
        }

        EspacoEsportivo espaco = new EspacoEsportivo(
                nome.trim(),
                tipo.trim(),
                capacidade
        );

        espacos[qtdEspacos] = espaco;
        qtdEspacos++;

        JOptionPane.showMessageDialog(null,
                "Espaço esportivo cadastrado com sucesso!");
    }


    public static void consultarEspacos() {

        if (qtdEspacos == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum espaço esportivo cadastrado!");
            return;
        }

        StringBuilder lista = new StringBuilder("ESPAÇOS ESPORTIVOS CADASTRADOS:\n\n");

        for (int i = 0; i < qtdEspacos; i++) {

            lista.append("Nome: ").append(espacos[i].getNome()).append("\nTipo: ").append(espacos[i].getTipo()).append("\nCapacidade: ").append(espacos[i].getCapacidade()).append("\n\n");
        }

        JOptionPane.showMessageDialog(null, lista.toString());
    }


    public static EspacoEsportivo selecionarEspaco() {

        if (qtdEspacos == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum espaço esportivo cadastrado!");
            return null;
        }

        String[] opcoes = new String[qtdEspacos];

        for (int i = 0; i < qtdEspacos; i++) {
            opcoes[i] = espacos[i].getNome()
                    + " - " + espacos[i].getTipo();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Selecione um espaço esportivo:",
                "Espaços cadastrados",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return null;

        for (int i = 0; i < qtdEspacos; i++) {
            if (opcoes[i].equals(escolha)) {
                return espacos[i];
            }
        }

        return null;
    }


    public static void realizarReserva() {

        if (qtdReservas >= reservas.length) {
            JOptionPane.showMessageDialog(null,
                    "Limite de reservas atingido!");
            return;
        }

        EspacoEsportivo espaco = selecionarEspaco();

        if (espaco == null) return;

        if (qtdUsuarios == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhum usuário cadastrado!");
            return;
        }

        String[] opcoesUsuarios = new String[qtdUsuarios];

        for (int i = 0; i < qtdUsuarios; i++) {
            opcoesUsuarios[i] = usuarios[i].getNome()
                    + " - " + usuarios[i].getEmail();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Selecione o responsável pela reserva:",
                "Selecionar usuário",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoesUsuarios,
                opcoesUsuarios[0]
        );

        if (escolha == null) return;

        Usuario responsavel = null;

        for (int i = 0; i < qtdUsuarios; i++) {
            if (opcoesUsuarios[i].equals(escolha)) {
                responsavel = usuarios[i];
                break;
            }
        }

        if (responsavel == null) return;

        String data = solicitarData();

        if (data == null) return;

        String horario = solicitarHorario();

        if (horario == null) return;

        if (!verificarDisponibilidade(espaco, data, horario)) {

            JOptionPane.showMessageDialog(null,
                    "Este espaço já está ocupado nessa data e horário!");

            return;
        }

        Reserva reserva = new Reserva(
                espaco, responsavel, data.trim(), horario.trim());

        reservas[qtdReservas] = reserva;
        qtdReservas++;

        JOptionPane.showMessageDialog(null,
                "Reserva realizada com sucesso!");
    }


    public static void consultarReservas() {

        if (qtdReservas == 0) {
            JOptionPane.showMessageDialog(null,
                    "Nenhuma reserva cadastrada!");
            return;
        }

        for (int i = 0; i < qtdReservas; i++) {
            reservas[i].exibirDados();
        }
    }


    public static String solicitarData() {

        while (true) {

            String data = JOptionPane.showInputDialog(
                    "Digite a data (DD/MM/AAAA):");

            if (data == null) return null;

            try {
                LocalDate dataConvertida = LocalDate.parse(
                        data.trim(), FORMATO_DATA);

                if (dataConvertida.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(null,
                            "Não é possível realizar reservas e jogos em datas passadas!");
                    continue;
                }

                return FORMATO_DATA.format(dataConvertida);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Data inválida! Digite no formato DD/MM/AAAA.\n"
                                + "Exemplo: 10/12/2026");
            }
        }
    }


    public static String solicitarHorario() {

        while (true) {

            String horario = JOptionPane.showInputDialog(
                    "Digite o horário (HH:mm):");

            if (horario == null) return null;

            try {
                LocalTime horarioConvertido = LocalTime.parse(
                        horario.trim(), FORMATO_HORARIO);

                return FORMATO_HORARIO.format(horarioConvertido);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Horário inválido! Digite no formato HH:mm.\n"
                                + "Exemplo: 14:30");
            }
        }
    }


    public static boolean verificarDisponibilidade(
            EspacoEsportivo espaco, String data, String horario) {

        for (int i = 0; i < qtdReservas; i++) {

            if (reservas[i].getEspaco() == espaco
                    && reservas[i].getData().equals(data)
                    && reservas[i].getHorario().equals(horario)) {

                return false;
            }
        }

        for (int i = 0; i < qtdCampeonatos; i++) {

            Campeonato campeonato = campeonatos[i];

            for (int j = 0; j < campeonato.getQtdJogos(); j++) {

                Jogo jogo = campeonato.getJogo(j);

                if (jogo.getEspaco() == espaco
                        && jogo.getData().equals(data)
                        && jogo.getHorario().equals(horario)) {

                    return false;
                }
            }
        }

        return true;
    }


    public static void cancelarReserva() {

        if (qtdReservas == 0) {
            JOptionPane.showMessageDialog(null,
                    "Não existem reservas cadastradas!");
            return;
        }

        String[] opcoes = new String[qtdReservas];

        for (int i = 0; i < qtdReservas; i++) {
            opcoes[i] = reservas[i].getEspaco().getNome()
                    + " - " + reservas[i].getData()
                    + " - " + reservas[i].getHorario()
                    + " - " + reservas[i].getResponsavel().getNome();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Selecione a reserva que deseja cancelar:",
                "Cancelar reserva",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) return;

        for (int i = 0; i < qtdReservas; i++) {

            if (opcoes[i].equals(escolha)) {

                int resposta = JOptionPane.showConfirmDialog(
                        null,
                        "Deseja realmente cancelar esta reserva?",
                        "Confirmar cancelamento",
                        JOptionPane.YES_NO_OPTION
                );

                if (resposta == JOptionPane.YES_OPTION) {

                    for (int j = i; j < qtdReservas - 1; j++) {
                        reservas[j] = reservas[j + 1];
                    }

                    qtdReservas--;
                    reservas[qtdReservas] = null;

                    JOptionPane.showMessageDialog(null,
                            "Reserva cancelada com sucesso!");
                }

                return;
            }
        }
    }

    public static boolean emailJaCadastrado(String email) {

        for (int i = 0; i < qtdUsuarios; i++) {

            if (usuarios[i].getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }

    public static boolean matriculaJaCadastrada(String matricula) {

        for (int i = 0; i < qtdUsuarios; i++) {

            if (usuarios[i] instanceof Atleta) {

                Atleta atleta = (Atleta) usuarios[i];

                if (atleta.getMatricula().equalsIgnoreCase(matricula)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean espacoJaCadastrado(String nome) {

        for (int i = 0; i < qtdEspacos; i++) {

            if (espacos[i].getNome().equalsIgnoreCase(nome.trim())) {
                return true;
            }
        }

        return false;
    }

    public static boolean equipeJaCadastrada(String nome, String modalidade) {

        for (int i = 0; i < qtdEquipes; i++) {

            if (equipes[i].getNome().equalsIgnoreCase(nome.trim())
                    && equipes[i].getModalidade().equalsIgnoreCase(
                    modalidade.trim())) {
                return true;
            }
        }

        return false;
    }

    public static boolean campeonatoJaCadastrado(
            String nome, String modalidade) {

        for (int i = 0; i < qtdCampeonatos; i++) {

            if (campeonatos[i].getNome().equalsIgnoreCase(nome.trim())
                    && campeonatos[i].getModalidade().equalsIgnoreCase(
                    modalidade.trim())) {
                return true;
            }
        }

        return false;
    }

    public static Equipe[] getEquipes() {
        return equipes;
    }

    public static int getQtdEquipes() {
        return qtdEquipes;
    }
}