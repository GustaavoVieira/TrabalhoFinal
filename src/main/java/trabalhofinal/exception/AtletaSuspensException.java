package trabalhofinal.exception;

/**
 * Lançada quando se tenta escalar um atleta que está suspenso.
 * Regra de Negócio 7.
 */
public class AtletaSuspensException extends NegocioException {
    public AtletaSuspensException(String mensagem) {
        super(mensagem);
    }
}
