package gov.goias.entidades;

import java.util.Date;

import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;

/**
 * Created by lucas-mp.
 */
public class SituacaoDocumentoFiscalReclamado  {

    private Integer id;
    private Date dataCadastroSituacao;
    private String info;
    private Integer tipoPerfil;
    private TipoPerfilCadastroReclamacao perfilCadastro;
    private DocumentoFiscalReclamado documentoFiscalReclamado;
    private ComplSituacaoReclamacao complSituacaoReclamacao;
    private GENPessoa pessoaResponsavelCadastro;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataCadastroSituacao() {
        return dataCadastroSituacao;
    }

    public void setDataCadastroSituacao(Date dataCadastroSituacao) {
        this.dataCadastroSituacao = dataCadastroSituacao;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public DocumentoFiscalReclamado getDocumentoFiscalReclamado() {
        return documentoFiscalReclamado;
    }

    public void setDocumentoFiscalReclamado(DocumentoFiscalReclamado documentoFiscalReclamado) {
        this.documentoFiscalReclamado = documentoFiscalReclamado;
    }

    public ComplSituacaoReclamacao getComplSituacaoReclamacao() {
        return complSituacaoReclamacao;
    }

    public void setComplSituacaoReclamacao(ComplSituacaoReclamacao complSituacaoReclamacao) {
        this.complSituacaoReclamacao = complSituacaoReclamacao;
    }

    public Integer getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(Integer tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public GENPessoa getPessoaResponsavelCadastro() {
        return pessoaResponsavelCadastro;
    }

    public void setPessoaResponsavelCadastro(GENPessoa pessoaResponsavelCadastro) {
        this.pessoaResponsavelCadastro = pessoaResponsavelCadastro;
    }

    public String getPerfilDescricao(){
        return TipoPerfilCadastroReclamacao.get(getTipoPerfil()).getDescricao();
    }

    public TipoPerfilCadastroReclamacao getPerfilCadastro() {
        return perfilCadastro;
    }

    public void setPerfilCadastro(TipoPerfilCadastroReclamacao perfilCadastro) {
        this.perfilCadastro = perfilCadastro;
        this.tipoPerfil=perfilCadastro.getValue();
    }
}