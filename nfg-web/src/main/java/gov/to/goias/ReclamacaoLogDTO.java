package gov.to.goias;

import java.util.Date;

public class ReclamacaoLogDTO {

	private String perfilDescricao;

	private Date dataCadastroSituacao;

	private String complSituacaoReclamacao;

	public String getPerfilDescricao() {
		return perfilDescricao;
	}

	public void setPerfilDescricao(String perfilDescricao) {
		this.perfilDescricao = perfilDescricao;
	}

	public Date getDataCadastroSituacao() {
		return dataCadastroSituacao;
	}

	public void setDataCadastroSituacao(Date dataCadastroSituacao) {
		this.dataCadastroSituacao = dataCadastroSituacao;
	}

	public String getComplSituacaoReclamacao() {
		return complSituacaoReclamacao;
	}

	public void setComplSituacaoReclamacao(String complSituacaoReclamacao) {
		this.complSituacaoReclamacao = complSituacaoReclamacao;
	}

}
