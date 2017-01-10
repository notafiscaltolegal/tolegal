package gov.goias.entidades.enums;

/**
 * Enum representativo dos diferentes estados de processamento possíveis para dado sorteio.
 */
public enum StatusProcessamentoSorteio {
    AGUARDANDO_GERACAO_BILHETES                         ("Aguardando geração de bilhetes", 1),
    GERANDO_BILHETES                                    ("Gerando bilhetes", 2),
    BILHETES_GERADOS_AGUARDANDO_SORTEIO                 ("Bilhetes Gerados - Aguardando sorteio", 3),
    SORTEIO_REALIZADO_AGUARDANDO_DISTRIBUICAO_PREMIOS   ("Sorteio realizado - Aguardando distribuição de prêmios", 4),
    SORTEIO_FINALIZADO                                  ("Sorteio finalizado - Prêmios distribuídos", 9);

    private String descricao;
    private int valor;

    StatusProcessamentoSorteio(String descricao, int valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getValor() {
        return valor;
    }

    /**
     * Faz a conversão de um inteiro em sua respectiva entrada no enum, caso haja uma.
     *
     * @param valor o valor a ser convertido
     * @return      o valor do enum <c>StatusProcessamentoSorteio</c> correspondente ao valor informado
     */
    public static StatusProcessamentoSorteio parse(int valor) {
        StatusProcessamentoSorteio statusSorteio = null;
        for (StatusProcessamentoSorteio s : StatusProcessamentoSorteio.values()) {
            if (s.getValor() == valor) {
                statusSorteio = s;
                break;
            }
        }

        return statusSorteio;
    }

}
