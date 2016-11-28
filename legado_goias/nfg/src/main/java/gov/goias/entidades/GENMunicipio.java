package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import gov.goias.persistencia.MunicipioRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author henrique-rh
 * @since 16/12/2014.
 */
@Entity
@Table(name = "GEN_MUNICIPIO")
@Repository
@Cacheable(true)
public class GENMunicipio extends MunicipioRepository implements Serializable {

    private static final long serialVersionUID = -2781204310518429162L;
    @Id
    @Column(name = "CODG_MUNICIPIO")
    private Long codigoMunicipio;

    @Column(name = "CODG_CORREIO")
    private Long codigoCorreio;

    @Column(name = "DATA_CRIACAO")
    private Date dataCriacao;

    @Column(name = "CODG_SERPRO")
    private Long codigoSerpro;

    @Column(name = "NUMR_LEI_CRIACAO")
    private String numeroLeiCriacao;

    @Column(name = "CODG_CEP_GENERICO")
    private String cepGenerico;

    @Column(name = "CODG_ORIGEM_INFORMACAO")
    private Long codigoOrigemInformacao;

    @Column(name = "NOME_MUNICIPIO")
    private String nome;

    @Column(name = "DATA_ATUALIZA")
    private Date dataAtualizacao;

    @Column(name = "MATR_FUNC")
    private String matriculaFuncionario;

    @Column(name = "CODG_MUNICIPIO_DISTRITO")
    private String codigoMunicipioDistrito;

    @Column(name = "CODG_UF")
    private String uf;

    @Column(name = "CODG_MUNICIPIO_COMARCA")
    private String codigoMunicipioComarca;

    @Column(name = "CODG_UNIDADE_OPER")
    private String codigoUnidadeOperacional;

    @Column(name = "NUMR_SIMPL_MUNICIPIO")
    private String numeroSimplificadoMunicipio;

    @Column(name = "CODG_IBGE")
    private String codigoIbge;

    @Column(name = "TIPO_LOCALIDADE")
    private String tipoLocalidade;

    @Column(name = "NUMR_CNPJ_PREFEITURA")
    private String numeroCnpjPrefeitura;

    @Column(name = "CODG_CELG")
    private String codigoCelg;

    @Column(name = "INDI_CEP_ESPECIFICO")
    private Character indiCepEspecifico;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Long codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public Long getCodigoCorreio() {
        return codigoCorreio;
    }

    public void setCodigoCorreio(Long codigoCorreio) {
        this.codigoCorreio = codigoCorreio;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getCodigoSerpro() {
        return codigoSerpro;
    }

    public void setCodigoSerpro(Long codigoSerpro) {
        this.codigoSerpro = codigoSerpro;
    }

    public String getNumeroLeiCriacao() {
        return numeroLeiCriacao;
    }

    public void setNumeroLeiCriacao(String numeroLeiCriacao) {
        this.numeroLeiCriacao = numeroLeiCriacao;
    }

    public String getCepGenerico() {
        return cepGenerico;
    }

    public void setCepGenerico(String cepGenerico) {
        this.cepGenerico = cepGenerico;
    }

    public Long getCodigoOrigemInformacao() {
        return codigoOrigemInformacao;
    }

    public void setCodigoOrigemInformacao(Long codigoOrigemInformacao) {
        this.codigoOrigemInformacao = codigoOrigemInformacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getMatriculaFuncionario() {
        return matriculaFuncionario;
    }

    public void setMatriculaFuncionario(String matriculaFuncionario) {
        this.matriculaFuncionario = matriculaFuncionario;
    }

    public String getCodigoMunicipioDistrito() {
        return codigoMunicipioDistrito;
    }

    public void setCodigoMunicipioDistrito(String codigoMunicipioDistrito) {
        this.codigoMunicipioDistrito = codigoMunicipioDistrito;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCodigoMunicipioComarca() {
        return codigoMunicipioComarca;
    }

    public void setCodigoMunicipioComarca(String codigoMunicipioComarca) {
        this.codigoMunicipioComarca = codigoMunicipioComarca;
    }

    public String getCodigoUnidadeOperacional() {
        return codigoUnidadeOperacional;
    }

    public void setCodigoUnidadeOperacional(String codigoUnidadeOperacional) {
        this.codigoUnidadeOperacional = codigoUnidadeOperacional;
    }

    public String getNumeroSimplificadoMunicipio() {
        return numeroSimplificadoMunicipio;
    }

    public void setNumeroSimplificadoMunicipio(String numeroSimplificadoMunicipio) {
        this.numeroSimplificadoMunicipio = numeroSimplificadoMunicipio;
    }

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getTipoLocalidade() {
        return tipoLocalidade;
    }

    public void setTipoLocalidade(String tipoLocalidade) {
        this.tipoLocalidade = tipoLocalidade;
    }

    public String getNumeroCnpjPrefeitura() {
        return numeroCnpjPrefeitura;
    }

    public void setNumeroCnpjPrefeitura(String numeroCnpjPrefeitura) {
        this.numeroCnpjPrefeitura = numeroCnpjPrefeitura;
    }

    public String getCodigoCelg() {
        return codigoCelg;
    }

    public void setCodigoCelg(String codigoCelg) {
        this.codigoCelg = codigoCelg;
    }

    public Character getIndiCepEspecifico() {
        return indiCepEspecifico;
    }

    public void setIndiCepEspecifico(Character indiCepEspecifico) {
        this.indiCepEspecifico = indiCepEspecifico;
    }
}
