package gov.goias.entidades.enums;

/**
 * Created by lucas-mp on 15/10/15.
*/
public enum TipoMotivoReclamacao {
    DOCUMENTO_NAO_CONSTA_NO_PORTAL(1,"Documento com CPF n&#225;o consta no painel Minhas Notas do Portal do Cidadão"),
    EMPRESA_NAO_PERGUNTOU(2,"Empresa n&#225;o perguntou se eu desejava incluir o CPF na nota/cupom fiscal"),
    EMPRESA_EXIGIU_CADASTRO(3,"Empresa exigiu a elaboração de um cadastro para a colocação do CPF"),
    EMPRESA_ALEGOU_PROBLEMA(4,"Empresa alegou problema com equipamento ou sistema"),
    EMPRESA_DIFICULTOU(5,"Empresa dificultou o fornecimento do documento fiscal com CPF"),
    EMPRESA_ALEGOU_NAO_PARTICIPAR(6,"Empresa alegou n&#225;o participar do programa NFGoiana");

    public int value;
    public String descricao;

    TipoMotivoReclamacao(int value, String descricao) {
        this.value = value;
        this.descricao = descricao;
    }

    public static TipoMotivoReclamacao get(int value) {
        for (TipoMotivoReclamacao status : TipoMotivoReclamacao.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getDescricao() {
        return descricao;
    }
}
