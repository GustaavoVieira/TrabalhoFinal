# Sistema de Gerenciamento Esportivo Universitário (Trabalho Final de POO)

Sistema desenvolvido em Java com interface gráfica baseada em `javax.swing.JOptionPane` para gerenciar usuários (Atletas, Técnicos e Coordenadores), equipes esportivas, campeonatos, jogos, reservas de espaços, notificações e relatórios estatísticos, incluindo um módulo inteligente de sugestão de equipes.

---

## 🏗️ Arquitetura e Organização em Pacotes

O projeto segue o princípio de **Responsabilidade Única (SRP)** e separação em camadas:

- **`trabalhofinal`**: Ponto de entrada (`Main.java`).
- **`trabalhofinal.model`**: Entidades de domínio (`EntidadeBase`, `Usuario`, `Atleta`, `Tecnico`, `Coordenador`, `Equipe`, `Campeonato`, `Jogo`, `EspacoEsportivo`, `Reserva`, `SolicitacaoEquipe`, `Notificacao`).
- **`trabalhofinal.interfaces`**: Contratos de comportamento (`Notificavel`, `Relatorio`).
- **`trabalhofinal.exception`**: Hierarquia de exceções de regras de negócio (`NegocioException`, `PermissaoException`, `ReservaConflitoException`, `AtletaJaEmEquipeException`, `EspacoEmManutencaoException`, `CampeonatoEncerradoException`, `AtletaSuspensException`).
- **`trabalhofinal.repository`**: Persistência em memória (`RepositorioDados`).
- **`trabalhofinal.service`**: Regras de negócio e fluxos (`GerenciadorUsuarios`, `GerenciadorCampeonatos`, `GerenciadorEspacosReservas`, `GerenciadorRelatorios`, `SistemaFormacaoEquipes`).
- **`trabalhofinal.util`**: Auxiliares de entrada e seleção de dados (`InputHelper`).

---

## 💡 Conceitos de POO Aplicados

- **Classes Abstratas**: `EntidadeBase` (geração automática de ID com atributo estático) e `Usuario` (polimorfismo via `exibirMenu()`).
- **Herança e Polimorfismo**: `Atleta`, `Tecnico` e `Coordenador` herdam de `Usuario`, cada um implementando seu próprio menu e comportamento.
- **Interfaces**: `Notificavel` e `Relatorio` implementadas pelas entidades centrais.
- **Encapsulamento e Pacotes**: Atributos privados/protegidos, getters/setters com validação e organização modular em 7 pacotes.
- **Tratamento de Exceções**: Exceções customizadas estendendo `NegocioException` (`RuntimeException`) para todas as regras de negócio.
- **Módulo Extra (+2 pts)**: `SistemaFormacaoEquipes` calcula compatibilidade entre atletas e equipes com base em modalidade, histórico, nível de desempenho, horários e necessidade de reforços.

---

## 🚀 Como Executar

1. Certifique-se de ter o **JDK 25+** e o **Maven** instalados.
2. Compile o projeto:
   ```bash
   mvn compile
   ```
3. Execute a classe principal `trabalhofinal.Main` pela sua IDE (IntelliJ IDEA, Eclipse ou VS Code).

---

## 👤 Autoria

Desenvolvimento integral (modelagem, regras de negócio, interface e testes) realizado individualmente pelo aluno autor do repositório.
