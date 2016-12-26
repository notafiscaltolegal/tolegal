package gov.goias.entidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import gov.goias.entidades.enums.StatusProcessamentoSorteio;


/**
 * Created by bruno-cff on 06/05/2015.
 */
public class RegraSorteio implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String informacao;

    private Date dataRealizacao;

    private Integer status;

    private Date dataExtracaoLoteria;

    private Date dataLimiteCadastroPessoa;

    private Date dataCadastro;

    private Integer tipo;

    private Double numeroConversao;

    private String divulgaSorteio;

    private Character realizado;

    private Integer numeroLoteria;

    private Integer numeroMaxDocFisc;
    
    private Boolean realizadoBoolean;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public String getDivulgaSorteio() {
        return divulgaSorteio;
    }

    public void setDivulgaSorteio(String divulgaSorteio) {
        this.divulgaSorteio = divulgaSorteio;
    }

    public Double getNumeroConversao() {
        return numeroConversao;
    }

    public void setNumeroConversao(Double numeroConversao) {
        this.numeroConversao = numeroConversao;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Integer getStatus() {

        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(Date dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataRealizacaoStr(){
    	
    	if (this.getDataRealizacao() == null){
    		return null;
    	}
    	
        return simpleDateFormat.format(this.getDataRealizacao());
    }
    public String getDataLimiteCadastroPessoaStr(){
        if (this.getDataLimiteCadastroPessoa()!=null){
            return simpleDateFormat.format(this.getDataLimiteCadastroPessoa());
        }else{
            return "N&#225;o informado";
        }
    }

    public Date getDataExtracaoLoteria() {
        return dataExtracaoLoteria;
    }

    public void setDataExtracaoLoteria(Date dataExtracaoLoteria) {
        this.dataExtracaoLoteria = dataExtracaoLoteria;
    }

    public String getDataExtracaoLoteriaStr() {
    	
    	if (this.getDataExtracaoLoteria() == null){
    		return null;
    	}
    	
        return simpleDateFormat.format(this.getDataExtracaoLoteria());
    }

    public String getDataCadastroStr() {
    	
    	if (this.getDataCadastro() == null){
    		return null;
    	}
    	
        return simpleDateFormat.format(this.getDataCadastro());
    }

    public Character getRealizado() {
        return realizado;
    }

    public void setRealizado(Character realizado) {
        this.realizado = realizado;
    }

    public boolean isRealizadoBoolean() {
    	
    	if (getRealizado() == null){
    		return Boolean.FALSE;
    	}
    	
        return getRealizado()=='S';
    }

    public Integer getNumeroLoteria() { return numeroLoteria; }

    public void setNumeroLoteria(Integer numeroLoteria) { this.numeroLoteria = numeroLoteria; }

    public Date getDataLimiteCadastroPessoa() {
        return dataLimiteCadastroPessoa;
    }

    public void setDataLimiteCadastroPessoa(Date dataLimiteCadastroPessoa) {
        this.dataLimiteCadastroPessoa = dataLimiteCadastroPessoa;
    }

    public Integer getNumeroMaxDocFisc() {
        return numeroMaxDocFisc;
    }

    public void setNumeroMaxDocFisc(Integer numeroMaxDocFisc) {
        this.numeroMaxDocFisc = numeroMaxDocFisc;
    }

    public String getStatusStr() {
        return (StatusProcessamentoSorteio.parse(getStatus())).getDescricao();
    }

	public Boolean getRealizadoBoolean() {
		return realizadoBoolean;
	}

	public void setRealizadoBoolean(Boolean realizadoBoolean) {
		this.realizadoBoolean = realizadoBoolean;
	}
}