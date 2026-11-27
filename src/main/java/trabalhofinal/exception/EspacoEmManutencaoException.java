package trabalhofinal.exception;

/**
 * Lançada quando se tenta reservar um espaço esportivo que está
 * em manutenção.
 * Regra de Negócio 5.
 */
public class EspacoEmManutencaoException extends NegocioException {
    public EspacoEmManutencaoException(String mensagem) {
        super(mensagem);
    }
}
