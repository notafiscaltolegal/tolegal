package gov.goias.entidades.enums;

/**
 * @author henrique-rh
 * @since 08/01/15.
 */
public enum StatusProcessamentoDocumentoFiscal {
    VALIDADO(1),
    AGUARDANDO_VALIDACAO(2),
    SUBSTITUIDO(3),
    INVALIDO(4),
    SUBSTITUIDO_FORA_DO_PRAZO(5),
    CANCELADO(6);

    private int value;

    StatusProcessamentoDocumentoFiscal(int value) {
        this.value = value;
    }

    public static StatusProcessamentoDocumentoFiscal get(int value) {
        for (StatusProcessamentoDocumentoFiscal status : StatusProcessamentoDocumentoFiscal.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
