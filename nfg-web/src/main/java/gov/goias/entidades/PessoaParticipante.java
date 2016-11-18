package gov.goias.entidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PessoaParticipante implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Character participaSorteio;

	private Character recebeEmail;
	
	private boolean participaSorteioBol;

	private boolean recebeEmailBol;

	private Date dataCadastro;

	public String dataCadastroStr;

	public String emailPreCadastro;
	public String telefonePreCadastro;
	public String nomeMaePreCadastro;

	public GENPessoaFisica genPessoaFisica;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Character getRecebeEmail() {
		return recebeEmail;
	}

	public void setRecebeEmail(Character recebeEmail) {
		this.recebeEmail = recebeEmail;
	}

	public Character getParticipaSorteio() {
		return participaSorteio;
	}

	public String getParticipaSorteioString() {
		if (participaSorteio != null) {
			if (participaSorteio.toString().equals("S")) {
				return "Sim";
			} else {
				return "Nao";
			}
		} else {
			return null;
		}
	}

	public String getNomeMaePreCadastro() {
		return nomeMaePreCadastro;
	}

	public void setNomeMaePreCadastro(String nomeMaePreCadastro) {
		this.nomeMaePreCadastro = nomeMaePreCadastro;
	}

	public String getRecebeEmailString() {
		if (recebeEmail != null) {

			if (recebeEmail.toString().equals("S")) {
				return "Sim";
			} else {
				return "Nao";
			}
		} else {
			return null;
		}
	}

	public void setParticipaSorteio(Character participaSorteio) {
		this.participaSorteio = participaSorteio;
	}

	public GENPessoaFisica getGenPessoaFisica() {
		return genPessoaFisica;
	}

	public void setGenPessoaFisica(GENPessoaFisica genPessoaFisica) {
		this.genPessoaFisica = genPessoaFisica;
	}

	public String getEmailPreCadastro() {
		return emailPreCadastro;
	}

	public void setEmailPreCadastro(String emailPreCadastro) {
		this.emailPreCadastro = emailPreCadastro;
	}

	public String getTelefonePreCadastro() {
		return telefonePreCadastro;
	}

	public void setTelefonePreCadastro(String telefonePreCadastro) {
		this.telefonePreCadastro = telefonePreCadastro;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getDataCadastroStr() {
		String dataFormat = null;
		
		if (this.getDataCadastro() != null){
			
			dataFormat = formataData(this.getDataCadastro(),"dd/MM/yyyy");
		}
		
		return dataFormat;
	}
	
	private String formataData(Date date, String pattern) {
		String dataUltimaAlteracaoFormat;
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		
		dataUltimaAlteracaoFormat = dateFormat.format(date);
		return dataUltimaAlteracaoFormat;
	}

	public void setDataCadastroStr(String dataCadastroStr) {
		this.dataCadastroStr = dataCadastroStr;
	}

	public boolean getExcluidoDosSorteios() {
		return false;
	}

	public boolean isParticipaSorteioBol() {
		return participaSorteioBol;
	}

	public void setParticipaSorteioBol(boolean participaSorteioBol) {
		this.participaSorteioBol = participaSorteioBol;
	}

	public boolean isRecebeEmailBol() {
		return recebeEmailBol;
	}

	public void setRecebeEmailBol(boolean recebeEmailBol) {
		this.recebeEmailBol = recebeEmailBol;
	}
}
