package gov.goias.entidades;

import java.io.Serializable;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Transient;

import org.springframework.dao.EmptyResultDataAccessException;

import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;

/**
 * Created by bruno-cff on 30/09/2015.
 */
public class DocumentoFiscalReclamado implements Serializable {

    private Integer id;

    private Date dataDocumentoFiscal;

    private Integer numero;

    private Integer tipoDocumentoFiscal;

    private Double valor;

    private Date dataReclamacao;

    private String reclamacaoResolvida;

    private Blob imgDocumentoFiscal;

    private Integer tipoExtensao;

    private MotivoReclamacao motivoReclamacao;

    private PessoaParticipante pessoaParticipante;

    private String numeroCnpjEmpresa;

    private String nomeFantasiaEmpresa;

    @Transient
    private String listaAndamentoStr;

    @Transient
    private String statusAndamentoStr;

    @Transient
    private String disableRadioBtn;

    @Transient
    private String razaoSocial;

    @Transient
    private String inscricaoEmpresa;
    
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataDocumentoFiscal() {
        return dataDocumentoFiscal;
    }


    public String getDataDocumentoFiscalStr() {
        if (dataDocumentoFiscal != null) {
            simpleDateFormat= (simpleDateFormat==null)? new SimpleDateFormat("dd/MM/yyyy"): simpleDateFormat;
            return simpleDateFormat.format(dataDocumentoFiscal);
        } else {
            return "";
        }

    }

    public String getInscricaoEmpresa() {
        return inscricaoEmpresa;
    }

    public void setInscricaoEmpresa(String inscricaoEmpresa) {
        this.inscricaoEmpresa = inscricaoEmpresa;
    }

    public void setDataDocumentoFiscal(Date dataDocumentoFiscal) {
        this.dataDocumentoFiscal = dataDocumentoFiscal;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getTipoDocumentoFiscal() {
        return tipoDocumentoFiscal;
    }

    public void setTipoDocumentoFiscal(Integer tipoDocumentoFiscal) {
        this.tipoDocumentoFiscal = tipoDocumentoFiscal;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataReclamacao() {
        return dataReclamacao;
    }

    public void setDataReclamacao(Date dataReclamacao) {
        this.dataReclamacao = dataReclamacao;
    }

    public String getReclamacaoResolvida() {
        return reclamacaoResolvida;
    }

    public void setReclamacaoResolvida(String reclamacaoResolvida) {
        this.reclamacaoResolvida = reclamacaoResolvida;
    }

    public Blob getImgDocumentoFiscal() {
        return imgDocumentoFiscal;
    }

    public void setImgDocumentoFiscal(Blob imgDocumentoFiscal) {
        this.imgDocumentoFiscal = imgDocumentoFiscal;
    }

    public Integer getTipoExtensao() {
        return tipoExtensao;
    }

    public void setTipoExtensao(Integer tipoExtensao) {
        this.tipoExtensao = tipoExtensao;
    }

    public MotivoReclamacao getMotivoReclamacao() {
        return motivoReclamacao;
    }

    public void setMotivoReclamacao(MotivoReclamacao motivoReclamacao) {
        this.motivoReclamacao = motivoReclamacao;
    }

    public PessoaParticipante getPessoaParticipante() {
        return pessoaParticipante;
    }

    public void setPessoaParticipante(PessoaParticipante pessoaParticipante) {
        this.pessoaParticipante = pessoaParticipante;
    }

    public String getNumeroCnpjEmpresa() {
        return numeroCnpjEmpresa;
    }

    public void setNumeroCnpjEmpresa(String numeroCnpjEmpresa) {
        this.numeroCnpjEmpresa = numeroCnpjEmpresa;
    }

    public String getNomeFantasiaEmpresa() {
        return nomeFantasiaEmpresa;
    }

    public void setNomeFantasiaEmpresa(String nomeFantasiaEmpresa) {
        this.nomeFantasiaEmpresa = nomeFantasiaEmpresa;
    }

    public String getDescUltimaSituacao() {
        Integer codgUltimaSituacao = getCodgUltimaSituacao();
        return "";
    }

    public TipoComplSituacaoReclamacao getSituacaoAtual() {
        if(getCodgUltimaSituacao() == null) return null;
        return TipoComplSituacaoReclamacao.get(getCodgUltimaSituacao());
    }

    public Integer getCodgUltimaSituacao() {
        if (getId() == null) return null;

        String sql = "select CODG_COMPL_SITUACAO_RECLAMACAO from" +
                " (SELECT CODG_COMPL_SITUACAO_RECLAMACAO FROM " +
                " NFG_SITUACAO_DOC_FISC_RECLAMA " +
                " where ID_DOCUMENTO_FISCAL_RECLAMADO = ? " +
                " ORDER BY DATA_CADASTRO_SITUACAO DESC, ID_SITUACAO_DOC_FISC_RECLAMA DESC) " +
                " where rownum < 2";
        try {
            return 161651651;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String getListaAndamentoStr() {
        Integer id = getId();
        if (id == null) return null;
        return "Lista de andamento reclamaç&#225;o Mock";
    }

    public void setListaAndamentoStr(String listaAndamentoStr) {
        this.listaAndamentoStr = listaAndamentoStr;
    }

    public String getStatusAndamentoStr() {
        Integer id = getId();
        if (id == null) return null;
        return "Status andamento Mock";
    }

    public String getDisableRadioBtn() {
        return disableRadioBtn;
    }

    public void setDisableRadioBtn(String disableRadioBtn) {
        this.disableRadioBtn = disableRadioBtn;
    }

    public void setStatusAndamentoStr(String statusAndamentoStr) {
        this.statusAndamentoStr = statusAndamentoStr;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
}