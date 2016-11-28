package gov.goias.entidades;

import gov.goias.entidades.enums.StatusProcessamentoSorteio;
import gov.goias.persistencia.RegraSorteioRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by bruno-cff on 06/05/2015.
 */
@Entity
@Table(name = "NFG_REGRA_SORTEIO")
@Repository
@Cacheable(true)
public class RegraSorteio extends RegraSorteioRepository implements Serializable{

    private static final long serialVersionUID = 1L;

    @Autowired
    @Transient
    SimpleDateFormat simpleDateFormat;

    @Id
    @Column(name = "ID_REGRA_SORTEIO")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "INFO_SORTEIO")
    private String informacao;

    @Column(name = "DATA_REALIZACAO_SORTEIO")
    private Date dataRealizacao;

    @Transient
    private String dataLimiteCadastroPessoaStr;

    @Transient
    private String dataExtracaoLoteriaStr;

    @Transient
    private String dataRealizacaoStr;

    @Transient
    private String statusStr;

    @Transient
    private String dataCadastroStr;

    @Column(name = "STAT_PROCESM_SORTEIO_NFG")
    private Integer status;

    @Column(name = "DATA_EXTRACAO_LOTERIA_FEDERAL")
    private Date dataExtracaoLoteria;

    @Column(name = "DATA_LIMITE_CADASTRO_PESSOA")
    private Date dataLimiteCadastroPessoa;

    @Column(name = "DATA_CADASTRO_SORTEIO")
    private Date dataCadastro;

    @Column(name = "TIPO_SORTEIO_NFG")
    private Integer tipo;

    @Column(name = "NUMR_CONVERSAO_PONTO_BILHETE")
    private Double numeroConversao;


    @Column(name = "INDI_DIVULGA_SORTEIO_SITE")
    private String divulgaSorteio;

    @Column(name = "INDI_SORTEIO_REALIZADO")
    private Character realizado;

    @Column(name = "NUMR_EXTRACAO_LOTERIA_FEDERAL")
    private Integer numeroLoteria;


    @Column(name = "NUMR_MAXIMO_PONTO_DOC_FISC")
    private Integer numeroMaxDocFisc;

    @Transient
    private boolean realizadoBoolean;

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

    public void setDataRealizacaoStr(String dataRealizacaoStr) {
        this.dataRealizacaoStr = dataRealizacaoStr;
    }

    public String getDataRealizacaoStr(){
        return simpleDateFormat.format(this.getDataRealizacao());
    }
    public String getDataLimiteCadastroPessoaStr(){
        if (this.getDataLimiteCadastroPessoa()!=null){
            return simpleDateFormat.format(this.getDataLimiteCadastroPessoa());
        }else{
            return "Não informado";
        }
    }

    public Date getDataExtracaoLoteria() {
        return dataExtracaoLoteria;
    }

    public void setDataExtracaoLoteria(Date dataExtracaoLoteria) {
        this.dataExtracaoLoteria = dataExtracaoLoteria;
    }

    public String getDataExtracaoLoteriaStr() {
        return simpleDateFormat.format(this.getDataExtracaoLoteria());
    }

    public void setDataExtracaoLoteriaStr(String dataExtracaoLoteriaStr) {
        this.dataExtracaoLoteriaStr = dataExtracaoLoteriaStr;
    }

    public String getDataCadastroStr() {
        return simpleDateFormat.format(this.getDataCadastro());
    }

    public void setDataCadastroStr(String dataCadastroStr) {
        this.dataCadastroStr = dataCadastroStr;
    }

    public Character getRealizado() {
        return realizado;
    }

    public void setRealizado(Character realizado) {
        this.realizado = realizado;
    }

    public boolean isRealizadoBoolean() {
        return getRealizado()=='S';
    }

    public void setRealizadoBoolean(boolean realizadoBoolean) {
        this.realizadoBoolean = realizadoBoolean;
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
}
