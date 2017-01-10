package gov.goias.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

public class GENPessoaJuridica extends GENPessoa implements Serializable {

    private static final long serialVersionUID = -1902694338267414343L;

    @Column(name = "NUMR_CNPJ", nullable = false)
    private String numeroCnpj;

    @Column(name = "NOME_FANTASIA", nullable = true)
    private String nomeFantasia;

    @Column(name = "INDI_HOMOLOG_CADASTRO", nullable = true)
    private Character indiHomologacaoCadastro;

    @Column(name = "TIPO_PESSOA_JURIDICA", nullable = true)
    private Character tipoPessoaJuridica;

    @Column(name = "CODG_NIRE", nullable = true)
    private BigInteger codigoNire;

    @Column(name = "NUMR_INSCRICAO_MUNICIPAL", nullable = true)
    private BigInteger numeroInscricaoMunicipal;

    public GENPessoaJuridica() {
        super();
        super.setTipoPessoa(GENPessoa.PESSOA_JURIDICA);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public String getNumeroCnpj() {
        return numeroCnpj;
    }

    public void setNumeroCnpj(String numeroCnpj) {
        this.numeroCnpj = numeroCnpj;
    }


    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public Character getIndiHomologacaoCadastro() {
        return indiHomologacaoCadastro;
    }

    public void setIndiHomologacaoCadastro(Character indiHomologacaoCadastro) {
        this.indiHomologacaoCadastro = indiHomologacaoCadastro;
    }


    public Character getTipoPessoaJuridica() {
        return tipoPessoaJuridica;
    }

    public void setTipoPessoaJuridica(Character tipoPessoaJuridica) {
        this.tipoPessoaJuridica = tipoPessoaJuridica;
    }


    public BigInteger getCodigoNire() {
        return codigoNire;
    }

    public void setCodigoNire(BigInteger codigoNire) {
        this.codigoNire = codigoNire;
    }


    public BigInteger getNumeroInscricaoMunicipal() {
        return numeroInscricaoMunicipal;
    }

    public void setNumeroInscricaoMunicipal(BigInteger numeroInscricaoMunicipal) {
        this.numeroInscricaoMunicipal = numeroInscricaoMunicipal;
    }
}