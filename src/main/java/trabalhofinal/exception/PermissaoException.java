package trabalhofinal.exception;

/**
 * Exceção lançada quando uma operação viola regras de permissão no sistema.
 * Por exemplo, ao tentar cadastrar um campeonato ou realizar uma reserva sem
 * ser um coordenador. Estende RuntimeException para que seja uma exceção não
 * checada, permitindo que o código simplesmente lance a exceção sem a necessidade
 * de tratá‑la obrigatoriamente.
 */
public class PermissaoException extends RuntimeException {
    /**
     * Construtor padrão que aceita apenas a mensagem de erro.
     *
     * @param mensagem descrição da causa da exceção
     */
    public PermissaoException(String mensagem) {
        super(mensagem);
    }

    /**
     * Construtor que aceita mensagem e causa subjacente.
     *
     * @param mensagem descrição da causa da exceção
     * @param causa    exceção que originou este erro
     */
    public PermissaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
