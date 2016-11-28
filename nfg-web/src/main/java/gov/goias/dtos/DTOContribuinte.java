package gov.goias.dtos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Transient;

/**
 * @author henrique-rh
 * @since 16/07/2014
 */
public class DTOContribuinte implements Serializable {

    private static final long serialVersionUID = 1908567338267414343L;

    private Integer numeroInscricao;
    private String nomeEmpresa;
    private String numeroCnpj;
    private Date dataEfetivaParticipacao;
    private Date dataObrigatoriedadeCnae;
    private Date dataCredenciamento;
    private Boolean isEmpresaParticipante;
    private Boolean isEmissorEFD;
    private Character indiResponsavelAdesao;
    private Integer idPessoa;

    @Transient
    private Integer qtdMensagens;

    @Transient
    private Integer qtdReclamacoes;
    
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private ArrayList<DTOCnaeAutorizado> listaCnaeAutorizado;

    public DTOContribuinte() {
    }

    public DTOContribuinte(Integer numeroInscricao, String nomeEmpresa, String numeroCnpj) {
        this.numeroInscricao = numeroInscricao;
        this.nomeEmpresa = nomeEmpresa;
        this.numeroCnpj = numeroCnpj;
    }

    public DTOContribuinte(Integer numeroInscricao, String nomeEmpresa, String numeroCnpj, Date dataEfetivaParticipacao) {
        this(numeroInscricao, nomeEmpresa, numeroCnpj);
        this.dataEfetivaParticipacao = dataEfetivaParticipacao;
    }
    
    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Integer getNumeroInscricao() {
        return numeroInscricao;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public String getNumeroCnpj() {
        return numeroCnpj;
    }

    public Date getDataEfetivaParticipacao() {
        return dataEfetivaParticipacao;
    }

    public String getDataEfetivaParticipacaoFormatada() {
        if(dataEfetivaParticipacao == null) return "";

        return format.format(dataEfetivaParticipacao);
    }

    public Date getDataLimite() {
        return new Date();
    }

    public String getDataLimiteFormatada() {
        if(getDataLimite() == null) return "";

        return format.format(getDataLimite());
    }

    public Boolean getPodeAlterar() {
        return Boolean.TRUE;
    }

    public void setNumeroInscricao(Integer numeroInscricao) {
        this.numeroInscricao = numeroInscricao;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public void setNumeroCnpj(String numeroCnpj) {
        this.numeroCnpj = numeroCnpj;
    }

    public void setDataEfetivaParticipacao(Date dataEfetivaParticipacao) {
        this.dataEfetivaParticipacao = dataEfetivaParticipacao;
    }

    public Boolean getHasCnaeAutorizado() {
    	
    	if (listaCnaeAutorizado == null){
    		return Boolean.TRUE;
    	}
    	
        for (DTOCnaeAutorizado cnae : listaCnaeAutorizado) {
            if (cnae.getIsCnaeAutorizado()) {
                return true;
            }
        }
        return false;
    }

    public Boolean getIsEmpresaParticipante() {
        return isEmpresaParticipante;
    }

    public void setIsEmpresaParticipante(Boolean isEmpresaParticipante) {
        this.isEmpresaParticipante = isEmpresaParticipante;
    }

    public Boolean getIsEmissorEFD() {
        return isEmissorEFD;
    }

    public void setIsEmissorEFD(Boolean isEmissorEFD) {
        this.isEmissorEFD = isEmissorEFD;
    }

    public Boolean getHasCnaeObrigatorio() {
    	
    	if (listaCnaeAutorizado == null){
    		return Boolean.TRUE;
    	}
    	
        for (DTOCnaeAutorizado cnae : listaCnaeAutorizado) {
            if (cnae.getIsCnaeObrigatorio()) {
                return true;
            }
        }
        return false;
    }

    public Date getDataObrigatoriedadeCnae() {
        return dataObrigatoriedadeCnae;
    }

    public String getDataObrigatoriedadeCnaeFormatada() {
        if(dataObrigatoriedadeCnae == null) return "";

        return format.format(dataObrigatoriedadeCnae);
    }

    public void setDataObrigatoriedadeCnae(Date dataObrigatoriedadeCnae) {
        this.dataObrigatoriedadeCnae = dataObrigatoriedadeCnae;
    }

    public Character getIndiResponsavelAdesao() {
        return indiResponsavelAdesao;
    }

    public void setIndiResponsavelAdesao(Character indiResponsavelAdesao) {
        this.indiResponsavelAdesao = indiResponsavelAdesao;
    }

    public Date getDataCredenciamento() {
        return dataCredenciamento;
    }

    public void setDataCredenciamento(Date dataCredenciamento) {
        this.dataCredenciamento = dataCredenciamento;
    }

    public void setIndiResponsavelAdesaoString(String indiResponsavelAdesao) {
        if (indiResponsavelAdesao != null) {
            this.indiResponsavelAdesao = indiResponsavelAdesao.charAt(0);
        }
    }

    public ArrayList<DTOCnaeAutorizado> getListaCnaeAutorizado() {
        return listaCnaeAutorizado;
    }

    public void setListaCnaeAutorizado(ArrayList<DTOCnaeAutorizado> listaCnaeAutorizado) {
        this.listaCnaeAutorizado = listaCnaeAutorizado;
    }

    public Integer getQtdMensagens() {
        return qtdMensagens;
    }

    public void setQtdMensagens(Integer qtdMensagens) {
        this.qtdMensagens = qtdMensagens;
    }

    public Integer getQtdReclamacoes() {
        return qtdReclamacoes;
    }

    public void setQtdReclamacoes(Integer qtdReclamacoes) {
        this.qtdReclamacoes = qtdReclamacoes;
    }
}
