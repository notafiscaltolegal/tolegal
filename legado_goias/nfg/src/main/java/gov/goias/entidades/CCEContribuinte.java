package gov.goias.entidades;

import gov.goias.cache.CCEContribuinteCache;
import gov.goias.persistencia.CCEContribuinteRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CCE_CONTRIBUINTE")
@Repository
public class CCEContribuinte extends CCEContribuinteCache implements Serializable {

	private static final long serialVersionUID = 919648990256000298L;

	@Id
	@Column(name = "NUMR_INSCRICAO")
	private Integer numeroInscricao;

	@Column(name = "INDI_SITUACAO_CADASTRAL")
	private Character indicacaoSituacaoCadastral;

	@Column(name = "DATA_INICIO_CONTRATO_CONTADOR")
	private Date dataInicioContratoContador;

	@Column(name = "DATA_FINAL_CONTRATO_CONTADOR")
	private Date dataFinalContratoContador;

	@Column(name = "INFO_CONTRIB")
	private String infoContribuinte;

	@Column(name = "TIPO_CONTRATO_CONTADOR")
	private String tipoContratoContador;

	@Column(name = "NUMR_EXTRATO_CADASTRAL")
	private Long numeroExtratoCadastral;

	@Column(name = "DATA_EMISSAO_EXTRATO_CADASTRAL")
	private Date dataEmissaoExtratoCadastral;

	@Column(name = "DATA_CADASTRAM")
	private Date dataCadastramento;

	@Column(name = "NUMR_VALIDADOR_EXTRATO_CADASTR")
	private Integer numeroValidadorExtratoCadastro;

	@Column(name = "DATA_PRAZO_CERTO")
	private Date dataPrazoCerto;

	@Column(name = "DATA_INICIO_PRECARIED")
	private Date dataInicioPrecariedade;

    @Column(name = "ID_PESSOA_CONTADOR")
    private Long idContador;

    @ManyToOne
    @JoinColumn(name="ID_PESSOA", referencedColumnName="ID_PESSOA")
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
}
