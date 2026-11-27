package trabalhofinal.interfaces;

import trabalhofinal.model.*;

public interface Notificavel {

    void receberNotificacao(String titulo, String mensagem);

    default void receberSolicitacao(SolicitacaoEquipe solicitacao) {

    }
}
