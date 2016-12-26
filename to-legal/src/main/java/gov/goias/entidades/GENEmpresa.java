package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

public class GENEmpresa implements Serializable {

	private static final long serialVersionUID = -2781204310518429162L;

	private String numeroCnpjBase;

    private String nomeEmpresa;

    private Date dataConstituicao;

    private Double valorCapitalSocial;

    private Long codigoNireEmpresa;

    private Integer codigoNaturezaJuridica;

    private Character indiHomologacaoCadastro;

    private Integer indiSituacaoEmpresaJuceg;

    private Character indiOrigemEmpresa;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNumeroCnpjBase() {
		return numeroCnpjBase;
	}

	public void setNumeroCnpjBase(String numeroCnpjBase) {
		this.numeroCnpjBase = numeroCnpjBase;
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

    public Date getDataConstituicao() {
		return dataConstituicao;
	}

	public void setDataConstituicao(Date dataConstituicao) {
		this.dataConstituicao = dataConstituicao;
	}

	public Double getValorCapitalSocial() {
		return valorCapitalSocial;
	}

	public void setValorCapitalSocial(Double valorCapitalSocial) {
		this.valorCapitalSocial = valorCapitalSocial;
	}

	public Long getCodigoNireEmpresa() {
		return codigoNireEmpresa;
	}

	public void setCodigoNireEmpresa(Long codigoNireEmpresa) {
		this.codigoNireEmpresa = codigoNireEmpresa;
	}

	public Integer getCodigoNaturezaJuridica() {
		return codigoNaturezaJuridica;
	}

	public void setCodigoNaturezaJuridica(Integer codigoNaturezaJuridica) {
		this.codigoNaturezaJuridica = codigoNaturezaJuridica;
	}

	public Character getIndiHomologacaoCadastro() {
		return indiHomologacaoCadastro;
	}

	public void setIndiHomologacaoCadastro(Character indiHomologacaoCadastro) {
		this.indiHomologacaoCadastro = indiHomologacaoCadastro;
	}

	public Integer getIndiSituacaoEmpresaJuceg() {
		return indiSituacaoEmpresaJuceg;
	}

	public void setIndiSituacaoEmpresaJuceg(Integer indiSituacaoEmpresaJuceg) {
		this.indiSituacaoEmpresaJuceg = indiSituacaoEmpresaJuceg;
	}

	public Character getIndiOrigemEmpresa() {
		return indiOrigemEmpresa;
	}

	public void setIndiOrigemEmpresa(Character indiOrigemEmpresa) {
		this.indiOrigemEmpresa = indiOrigemEmpresa;
	}
}