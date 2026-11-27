package trabalhofinal.exception;

public class PermissaoException extends RuntimeException {
    public PermissaoException(String mensagem) {
        super(mensagem);
    }

    public PermissaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
