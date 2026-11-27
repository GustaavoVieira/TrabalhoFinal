package trabalhofinal.interfaces;

import trabalhofinal.model.*;

/**
 * Interface que define o contrato para objetos que podem receber notificações.
 * Implementada por Usuario e suas subclasses.
 * Demonstra o conceito de POO: Interfaces.
 */
public interface Notificavel {

    /**
     * Recebe e exibe uma notificação ao usuário.
     *
     * @param titulo   título/assunto da notificação
     * @param mensagem corpo da notificação
     */
    void receberNotificacao(String titulo, String mensagem);

    /**
     * Recebe uma solicitação de equipe. Apenas usuários que gerenciam
     * equipes (Tecnico) precisam implementar com lógica; os demais
     * podem fornecer implementação vazia (default).
     */
    default void receberSolicitacao(SolicitacaoEquipe solicitacao) {
        // comportamento padrão: ignorar
    }
}
