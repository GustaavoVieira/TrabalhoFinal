package trabalhofinal.exception;

/**
 * Lançada quando uma reserva ou jogo conflita com outro já existente
 * no mesmo espaço esportivo, data e horário.
 * Regra de Negócio 2.
 */
public class ReservaConflitoException extends NegocioException {
    public ReservaConflitoException(String mensagem) {
        super(mensagem);
    }
}
