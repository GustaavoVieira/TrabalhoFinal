package trabalhofinal.exception;

/**
 * Lançada quando se tenta adicionar equipes ou jogos a um campeonato
 * que já foi encerrado.
 * Regra de Negócio 6.
 */
public class CampeonatoEncerradoException extends NegocioException {
    public CampeonatoEncerradoException(String mensagem) {
        super(mensagem);
    }
}
