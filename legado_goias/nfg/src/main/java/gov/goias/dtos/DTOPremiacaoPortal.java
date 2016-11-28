package gov.goias.dtos;

import java.io.Serializable;

/**
 * Created by bruno-cff on 23/06/2015.
 */
public class DTOPremiacaoPortal implements Serializable{

    private String nome;
    private String cpf;
    private String sorteio;
    private Integer numeroBilhete;
    private Integer valor;
    private String codigoAgencia;
    private String nomeAgencia;
    private String nomeBanco;
    private Integer codigoBanco;
    private Integer tipoConta;
    private Integer numeroConta;
    private String digito;
    private Integer idPremioBilhete;

    public DTOPremiacaoPortal() {
    }

    public DTOPremiacaoPortal(String nome, String cpf, String sorteio, Integer numeroBilhete, Integer valor,
                              String codigoAgencia, String nomeAgencia, String nomeBanco, Integer codigoBanco,
                              Integer tipoConta, Integer numeroConta, String digito, Integer idPremioBilhete) {
        this.nome = nome;
        this.cpf = cpf;
        this.sorteio = sorteio;
        this.numeroBilhete = numeroBilhete;
        this.valor = valor;
        this.codigoAgencia = codigoAgencia;
        this.nomeAgencia = nomeAgencia;
        this.nomeBanco = nomeBanco;
        this.codigoBanco = codigoBanco;
        this.tipoConta = tipoConta;
        this.numeroConta = numeroConta;
        this.digito = digito;
        this.idPremioBilhete = idPremioBilhete;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getNomeAgencia() {
        return nomeAgencia;
    }

    public void setNomeAgencia(String nomeAgencia) {
        this.nomeAgencia = nomeAgencia;
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
}
