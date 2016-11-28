package gov.goias.dtos;

import java.util.Date;
import java.util.List;

/**
 * @author henrique-rh
 * @since 19/08/2014.
 */
public class DTOEmpresaParticipante {

    private Integer numeroInscricao;
    private String numeroCnpj;
    private String nomeEmpresa;
    private String nomeFantasia;
    private String dataCredenciamento;
    private String dataEfetivaParticipacao;
    private Character indiResponsavel;
    private String codgMunicipio;
    private String nomeMunicipio;
    private String nomeBairro;
//    private String descSubclasseCnae;
//    private Long idSubclasseCnae;
    private List<DTOSubclasseCnae> listaSubclasseCnae;

    public DTOEmpresaParticipante() {

    }
    public DTOEmpresaParticipante(Integer numeroInscricao, String numeroCnpj, String nomeEmpresa, String nomeFantasia, String dataCredenciamento, String dataEfetivaParticipacao) {
        this.numeroInscricao = numeroInscricao;
        this.numeroCnpj = numeroCnpj;
        this.nomeEmpresa = nomeEmpresa;
        this.nomeFantasia = nomeFantasia;
        this.dataCredenciamento = dataCredenciamento;
        this.dataEfetivaParticipacao = dataEfetivaParticipacao;
    }

    public Integer getNumeroInscricao() {
        return numeroInscricao;
    }

    public void setNumeroInscricao(Integer numeroInscricao) {
        this.numeroInscricao = numeroInscricao;
    }

    public String getNumeroCnpj() {
        return numeroCnpj;
    }

    public void setNumeroCnpj(String numeroCnpj) {
        this.numeroCnpj = numeroCnpj;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getDataCredenciamento() {
        return dataCredenciamento;
    }

    public void setDataCredenciamento(String dataCredenciamento) {
        this.dataCredenciamento = dataCredenciamento;
    }

    public String getDataEfetivaParticipacao() {
        return dataEfetivaParticipacao;
    }

    public void setDataEfetivaParticipacao(String dataEfetivaParticipacao) {
        this.dataEfetivaParticipacao = dataEfetivaParticipacao;
    }

    public Character getIndiResponsavel() {
        return indiResponsavel;
    }

    public void setIndiResponsavel(Character indiResponsavel) {
        this.indiResponsavel = indiResponsavel;
    }

    public String getCodgMunicipio() {
        return codgMunicipio;
    }

    public void setCodgMunicipio(String codgMunicipio) {
        this.codgMunicipio = codgMunicipio;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

//    public String getDescSubclasseCnae() {
//        return descSubclasseCnae;
//    }
//
//    public void setDescSubclasseCnae(String descSubclasseCnae) {
//        this.descSubclasseCnae = descSubclasseCnae;
//    }

    public String getNomeBairro() {
        return nomeBairro;
    }

    public void setNomeBairro(String nomeBairro) {
        this.nomeBairro = nomeBairro;
    }

//    public Long getIdSubclasseCnae() {
//        return idSubclasseCnae;
//    }
//
//    public void setIdSubclasseCnae(Long idSubclasseCnae) {
//        this.idSubclasseCnae = idSubclasseCnae;
//    }

    public void setIndiResponsavelString(String indiResponsavelAdesao) {
        if (indiResponsavelAdesao != null) {
            this.indiResponsavel = indiResponsavelAdesao.charAt(0);
        }
    }

    public List<DTOSubclasseCnae> getListaSubclasseCnae() {
        return listaSubclasseCnae;
    }

    public void setListaSubclasseCnae(List<DTOSubclasseCnae> listaSubclasseCnae) {
        this.listaSubclasseCnae = listaSubclasseCnae;
    }
}
