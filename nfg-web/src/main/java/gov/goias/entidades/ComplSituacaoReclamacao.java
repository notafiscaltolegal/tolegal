package gov.goias.entidades;

import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;

/**
 * Created by lucas-mp.
 */
public class ComplSituacaoReclamacao {

    private Integer codigo;

    private String descricao;

    public ComplSituacaoReclamacao( ) {
    }

    public ComplSituacaoReclamacao(int codigo) {
        this.codigo = codigo;
    }

    public ComplSituacaoReclamacao(TipoComplSituacaoReclamacao tipoComplSituacaoReclamacao) {
        this.codigo = tipoComplSituacaoReclamacao.getValue();
    }


    public static ComplSituacaoReclamacao instanciarTipo(TipoComplSituacaoReclamacao tipoCompl){
        return new ComplSituacaoReclamacao(tipoCompl.getValue());
    }

    public TipoComplSituacaoReclamacao getTipoComplSituacaoReclamacao(){
        return TipoComplSituacaoReclamacao.get(this.codigo);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTipoSituacaoDescricao(){
        return TipoComplSituacaoReclamacao.get(getCodigo()).getDescricao();
    }

    public String getTipoSituacaoAcao(){
        return TipoComplSituacaoReclamacao.get(getCodigo()).getAcao();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
