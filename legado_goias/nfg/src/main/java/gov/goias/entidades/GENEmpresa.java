package gov.goias.entidades;

import gov.goias.persistencia.GENEmpresaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GEN_EMPRESA")
@Repository
@Cacheable(true)
public class GENEmpresa extends GENEmpresaRepository implements Serializable {

	private static final long serialVersionUID = -2781204310518429162L;

    @Id
    @Column(name = "NUMR_CNPJ_BASE", nullable = false)
	private String numeroCnpjBase;

    @Column(name = "NOME_EMPRESAR", nullable = false)
    private String nomeEmpresa;

    @Column(name = "DATA_CONSTIT_EMPRESA", nullable = true)
    private Date dataConstituicao;

    @Column(name = "VALR_CAPITAL_SOCIAL", nullable = true)
    private Double valorCapitalSocial;

    @Column(name = "CODG_NIRE_EMPRESA", nullable = true)
    private Long codigoNireEmpresa;

    @Column(name = "CODG_NATUREZA_JURIDICA", nullable = true)
    private Integer codigoNaturezaJuridica;

    @Column(name = "INDI_HOMOLOG_CADASTRO", nullable = true)
    private Character indiHomologacaoCadastro;

    @Column(name = "INDI_SITUACAO_EMPRESA_JUCEG", nullable = true)
    private Integer indiSituacaoEmpresaJuceg;

    @Column(name = "INDI_ORIGEM_EMPRESA", nullable = false)
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