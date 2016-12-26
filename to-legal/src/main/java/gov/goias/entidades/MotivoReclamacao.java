package gov.goias.entidades;

import gov.goias.entidades.enums.TipoMotivoReclamacao;

/**
 * Created by lucas-mp.
 */
public class MotivoReclamacao {

    private Integer codigo;

    private String descricao;

    private TipoMotivoReclamacao tipoMotivo;

    public TipoMotivoReclamacao getTipoMotivo() {
        Integer codigo = getCodigo();
        return codigo !=null? TipoMotivoReclamacao.get(codigo) : null;

    }

    public String getTipoMotivoDescricao() {
        Integer codigo = getCodigo();
        return codigo !=null?(TipoMotivoReclamacao.get(getCodigo())).getDescricao(): null;
    }

    public void setTipoMotivo(TipoMotivoReclamacao tipoMotivo) {
        this.tipoMotivo = tipoMotivo;
        this.codigo = tipoMotivo.getValue();
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

