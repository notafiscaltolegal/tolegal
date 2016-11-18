package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

public class CCEContribuinte implements Serializable {

	private static final long serialVersionUID = 919648990256000298L;

	private Integer numeroInscricao;

	private Character indicacaoSituacaoCadastral;

	private Date dataInicioContratoContador;

	private Date dataFinalContratoContador;

	private String infoContribuinte;

	private String tipoContratoContador;

	private Long numeroExtratoCadastral;

	private Date dataEmissaoExtratoCadastral;

	private Date dataCadastramento;

	private Integer numeroValidadorExtratoCadastro;

	private Date dataPrazoCerto;

	private Date dataInicioPrecariedade;

    private Long idContador;

    private GENPessoa pessoa;

    public CCEContribuinte() {

    }

	public CCEContribuinte(GENPessoaJuridica pessoaJuridica) {
		this.pessoa = new GENPessoa(pessoaJuridica);
	}

    public CCEContribuinte(Integer numeroInscricao, Character indicacaoSituacaoCadastral, Date dataInicioContratoContador, Date dataFinalContratoContador, String infoContribuinte, String tipoContratoContador, Long numeroExtratoCadastral, Date dataEmissaoExtratoCadastral, Date dataCadastramento, Integer numeroValidadorExtratoCadastro, Date dataPrazoCerto, Date dataInicioPrecariedade, Long idContador, GENPessoaJuridica pessoaJuridica) {
        this.numeroInscricao = numeroInscricao;
        this.indicacaoSituacaoCadastral = indicacaoSituacaoCadastral;
        this.dataInicioContratoContador = dataInicioContratoContador;
        this.dataFinalContratoContador = dataFinalContratoContador;
        this.infoContribuinte = infoContribuinte;
        this.tipoContratoContador = tipoContratoContador;
        this.numeroExtratoCadastral = numeroExtratoCadastral;
        this.dataEmissaoExtratoCadastral = dataEmissaoExtratoCadastral;
        this.dataCadastramento = dataCadastramento;
        this.numeroValidadorExtratoCadastro = numeroValidadorExtratoCadastro;
        this.dataPrazoCerto = dataPrazoCerto;
        this.dataInicioPrecariedade = dataInicioPrecariedade;
        this.idContador = idContador;
        this.pessoa = new GENPessoa(pessoaJuridica);
    }

    public Integer getNumeroInscricao() {
		return numeroInscricao;
	}

	public void setNumeroInscricao(Integer numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public Character getIndicacaoSituacaoCadastral() {
		return indicacaoSituacaoCadastral;
	}

	public void setIndicacaoSituacaoCadastral(
			Character indicacaoSituacaoCadastral) {
		this.indicacaoSituacaoCadastral = indicacaoSituacaoCadastral;
	}

	public Date getDataInicioContratoContador() {
		return dataInicioContratoContador;
	}

	public void setDataInicioContratoContador(Date dataInicioContratoContador) {
		this.dataInicioContratoContador = dataInicioContratoContador;
	}

	public Date getDataFinalContratoContador() {
		return dataFinalContratoContador;
	}

	public void setDataFinalContratoContador(Date dataFinalContratoContador) {
		this.dataFinalContratoContador = dataFinalContratoContador;
	}

	public String getInfoContribuinte() {
		return infoContribuinte;
	}

	public void setInfoContribuinte(String infoContribuinte) {
		this.infoContribuinte = infoContribuinte;
	}

	public String getTipoContratoContador() {
		return tipoContratoContador;
	}

	public void setTipoContratoContador(String tipoContratoContador) {
		this.tipoContratoContador = tipoContratoContador;
	}

	public Long getNumeroExtratoCadastral() {
		return numeroExtratoCadastral;
	}

	public void setNumeroExtratoCadastral(Long numeroExtratoCadastral) {
		this.numeroExtratoCadastral = numeroExtratoCadastral;
	}

	public Date getDataEmissaoExtratoCadastral() {
		return dataEmissaoExtratoCadastral;
	}

	public void setDataEmissaoExtratoCadastral(Date dataEmissaoExtratoCadastral) {
		this.dataEmissaoExtratoCadastral = dataEmissaoExtratoCadastral;
	}

	public Date getDataCadastramento() {
		return dataCadastramento;
	}

	public void setDataCadastramento(Date dataCadastramento) {
		this.dataCadastramento = dataCadastramento;
	}

	public Integer getNumeroValidadorExtratoCadastro() {
		return numeroValidadorExtratoCadastro;
	}

	public void setNumeroValidadorExtratoCadastro(
			Integer numeroValidadorExtratoCadastro) {
		this.numeroValidadorExtratoCadastro = numeroValidadorExtratoCadastro;
	}

	public Date getDataPrazoCerto() {
		return dataPrazoCerto;
	}

	public void setDataPrazoCerto(Date dataPrazoCerto) {
		this.dataPrazoCerto = dataPrazoCerto;
	}

	public Date getDataInicioPrecariedade() {
		return dataInicioPrecariedade;
	}

	public void setDataInicioPrecariedade(Date dataInicioPrecariedade) {
		this.dataInicioPrecariedade = dataInicioPrecariedade;
	}

    public Long getIdContador() {
        return idContador;
    }

    public void setIdContador(Long idContador) {
        this.idContador = idContador;
    }

    public GENPessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(GENPessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Boolean podeParticiparDoNFG() {
        return contribuintesPossuiCNAEValido(this.numeroInscricao);
    }

	private Boolean contribuintesPossuiCNAEValido(Integer numeroInscricao2) {
		System.out.println("CRIAR REGRA DE NEGOCIO PARA VALIDAR O CNAE DA EMPRESA! MOCK!!");
		return true;
	}
}
