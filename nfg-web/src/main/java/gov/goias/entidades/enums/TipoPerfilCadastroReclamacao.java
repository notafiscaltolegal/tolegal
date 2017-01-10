package gov.goias.entidades.enums;

/**
 * Created by lucas-mp on 15/10/15.
*/
public enum TipoPerfilCadastroReclamacao {
    SISTEMA(1,"Sistema"),
    CIDADAO(2,"Cidad&#225;o"),
    CONTRIBUINTE(3,"Empresa"),
    GESTOR(4,"Gest&#225;o"),
    CONTADOR(5,"Contador da empresa");

    private int value;
    private String descricao;

    TipoPerfilCadastroReclamacao(int value, String descricao) {
        this.descricao = descricao;
        this.value = value;
    }

    public static TipoPerfilCadastroReclamacao get(int value) {
        for (TipoPerfilCadastroReclamacao status : TipoPerfilCadastroReclamacao.values()) {
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
