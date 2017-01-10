package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

/**
 * @author henrique-rh
 * @since 16/12/2014.
 */
public class GENMunicipio implements Serializable {

    private static final long serialVersionUID = -2781204310518429162L;

    private Long codigoMunicipio;

    private Long codigoCorreio;

    private Date dataCriacao;

    private Long codigoSerpro;

    private String numeroLeiCriacao;

    private String cepGenerico;

    private Long codigoOrigemInformacao;

    private String nome;

    private Date dataAtualizacao;

    private String matriculaFuncionario;

    private String codigoMunicipioDistrito;

    private String uf;

    private String codigoMunicipioComarca;

    private String codigoUnidadeOperacional;

    private String numeroSimplificadoMunicipio;

    private String codigoIbge;

    private String tipoLocalidade;

    private String numeroCnpjPrefeitura;

    private String codigoCelg;

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
