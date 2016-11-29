package gov.goias.dtos;

import gov.goias.entidades.JdbcMappable;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by bruno-cff on 11/06/2015.
 */
public class DTOPremiacao implements Serializable, JdbcMappable{

    private String sorteio;
    private Integer numeroBilhete;
    private Integer valor;

    private String nomeBanco;

    private Integer codigoBanco;
    private String codigoAgencia;
    private Integer numeroConta;
    private Integer tipoConta;
    private String digito;

    private Integer idPremioBilhete;

    private Date dataLimiteResgate;
    private Date dataResgate;
    private Date dataSolicitacaoResgate;

    private Double valorTributo;
    private Double valorTaxasBancarias;

    private Double valorTributoParam;
    private Double valorTaxasChequeParam;
    private Double valorTaxasTransfParam;

    private Double valorLiquido;

    private Character tipoResgatePremio;

    private Boolean estahNoPrazoParaResgate;

    private String cpfGanhador;
    private String nomeGanhador;
    private Integer numeroDoPremioNoSorteio;
    private String infoResgate;

    public Boolean getEstahNoPrazoParaResgate() {
        return estahNoPrazoParaResgate;
    }

    public void setEstahNoPrazoParaResgate(Boolean estahNoPrazoParaResgate) {
        this.estahNoPrazoParaResgate = estahNoPrazoParaResgate;
    }

    public String getCpfGanhador() {
        return cpfGanhador;
    }

    public void setCpfGanhador(String cpfGanhador) {
        this.cpfGanhador = cpfGanhador;
    }

    public String getNomeGanhador() {
        return nomeGanhador;
    }

    public void setNomeGanhador(String nomeGanhador) {
        this.nomeGanhador = nomeGanhador;
    }

    public Integer getNumeroDoPremioNoSorteio() {
        return numeroDoPremioNoSorteio;
    }

    public void setNumeroDoPremioNoSorteio(Integer numeroDoPremioNoSorteio) {
        this.numeroDoPremioNoSorteio = numeroDoPremioNoSorteio;
    }

    public String getInfoResgate() {
        return infoResgate;
    }

    public void setInfoResgate(String infoResgate) {
        this.infoResgate = infoResgate;
    }

    public String getSorteio() {
        return sorteio;
    }

    public void setSorteio(String sorteio) {
        this.sorteio = sorteio;
    }

    public Integer getNumeroBilhete() {
        return numeroBilhete;
    }

    public void setNumeroBilhete(Integer numeroBilhete) {
        this.numeroBilhete = numeroBilhete;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getCodigoAgencia() {
        return codigoAgencia;
    }

    public void setCodigoAgencia(String codigoAgencia) {
        this.codigoAgencia = codigoAgencia;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getDigito() {
        return digito;
    }

    public void setDigito(String digito) {
        this.digito = digito;
    }

    public Integer getIdBilhetePremiado() {
        return idPremioBilhete;
    }

    public void setIdBilhetePremiado(Integer idBilhetePremiado) {
        this.idPremioBilhete = idBilhetePremiado;
    }

    public Integer getIdPremioBilhete() {
        return idPremioBilhete;
    }

    public void setIdPremioBilhete(Integer idPremioBilhete) {
        this.idPremioBilhete = idPremioBilhete;
    }

    public Date getDataLimiteResgate() {
        return dataLimiteResgate;
    }

    public void setDataLimiteResgate(Date dataLimiteResgate) {
        this.dataLimiteResgate = dataLimiteResgate;
    }

    public Date getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(Date dataResgate) {
        this.dataResgate = dataResgate;
    }

    public Date getDataSolicitacaoResgate() {
        return dataSolicitacaoResgate;
    }

    public void setDataSolicitacaoResgate(Date dataSolicitacaoResgate) {
        this.dataSolicitacaoResgate = dataSolicitacaoResgate;
    }

    public Double getValorTributo() {
        return valorTributo;
    }

    public void setValorTributo(Double valorTributo) {
        this.valorTributo = valorTributo;
    }

    public Double getValorTaxasBancarias() {
        return valorTaxasBancarias;
    }

    public void setValorTaxasBancarias(Double valorTaxasBancarias) {
        this.valorTaxasBancarias = valorTaxasBancarias;
    }

    public Character getTipoResgatePremio() {
        return tipoResgatePremio;
    }

    public void setTipoResgatePremio(Character tipoResgatePremio) {
        this.tipoResgatePremio = tipoResgatePremio;
    }

    public Double getValorTributoParam() {
        return valorTributoParam;
    }

    public void setValorTributoParam(Double valorTributoParam) {
        this.valorTributoParam = valorTributoParam;
    }

    public Double getValorTaxasChequeParam() {
        return valorTaxasChequeParam;
    }

    public void setValorTaxasChequeParam(Double valorTaxasChequeParam) {
        this.valorTaxasChequeParam = valorTaxasChequeParam;
    }

    public Double getValorTaxasTransfParam() {
        return valorTaxasTransfParam;
    }

    public void setValorTaxasTransfParam(Double valorTaxasTransfParam) {
        this.valorTaxasTransfParam = valorTaxasTransfParam;
    }

    public Integer getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(Integer codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public Integer getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(Integer tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Double getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(Double valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    @Override
    public void populate(ResultSet resultSet) throws SQLException, ParseException {

        setCpfGanhador(resultSet.getString("NUMR_CPF"));
        setNomeGanhador(resultSet.getString("NOME_PESSOA"));
        setInfoResgate(resultSet.getString("INFO_RESGATE"));
        setNumeroDoPremioNoSorteio(resultSet.getInt("NUMR_PREMIO_PROGRAMA_SORTEIO"));

        setSorteio(resultSet.getString("INFO_SORTEIO"));
//        setNumeroBilhete(resultSet.getInt("NUMR_SEQUENCIAL_BILHETE"));
        setValor(resultSet.getInt("VALR_PREMIO"));
        setCodigoAgencia(resultSet.getString("CODG_AGENCIA"));
        setNomeBanco(resultSet.getString("NOME_BANCO"));
        setNumeroConta(resultSet.getInt("NUMR_CONTA_BANCARIA"));
        setCodigoBanco(resultSet.getInt("CODG_BANCO"));
        setTipoConta(resultSet.getInt("TIPO_CONTA_BANCARIA"));
        setDigito(resultSet.getString("NUMR_DIGITO_CONTA_BANCARIA"));
        setIdBilhetePremiado(resultSet.getInt("ID_PREMIO_BILHETE"));

        setDataLimiteResgate(resultSet.getTimestamp("data_limite_resgate"));
        setDataResgate(resultSet.getTimestamp("data_resgate_premio"));
        setDataSolicitacaoResgate(resultSet.getTimestamp("data_solict_resgate_premio"));

        double valrTaxaBancaria = resultSet.getDouble("valr_taxa_bancaria");
        setValorTaxasBancarias(resultSet.wasNull() ? null : valrTaxaBancaria);

        double valrTributo = resultSet.getDouble("valr_tributo");
        setValorTributo(resultSet.wasNull() ? null : valrTributo);

        setValorLiquido( getValor()-((getValorTaxasBancarias()!=null? getValorTaxasBancarias():0)+  (getValorTributo()!=null? getValorTributo():0) ));

        String tipoResgatePremio = resultSet.getString("tipo_resgate_premio");
        setTipoResgatePremio((tipoResgatePremio == null || resultSet.wasNull()) ? null : tipoResgatePremio.charAt(0));
    }
}