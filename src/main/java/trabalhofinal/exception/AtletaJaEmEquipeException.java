package trabalhofinal.exception;

/**
 * Lançada quando se tenta adicionar um atleta a uma equipe da mesma
 * modalidade em que ele já está cadastrado.
 * Regra de Negócio 1.
 */
public class AtletaJaEmEquipeException extends NegocioException {
    public AtletaJaEmEquipeException(String mensagem) {
        super(mensagem);
    }
}
