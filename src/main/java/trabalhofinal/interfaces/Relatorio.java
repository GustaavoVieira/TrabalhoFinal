package trabalhofinal.interfaces;

import trabalhofinal.model.*;

/**
 * Interface que define o contrato para geração de relatórios.
 * Implementada por classes que fornecem estatísticas/relatórios do sistema.
 * Demonstra o conceito de POO: Interfaces.
 */
public interface Relatorio {

    /**
     * Gera e exibe o relatório completo do objeto.
     */
    void gerarRelatorio();

    /**
     * Retorna um resumo textual do objeto para uso em listagens.
     *
     * @return String com as informações principais
     */
    String resumo();
}
