package gov.goias.dtos;

public class DTOUsuarioPortalAsp {

    private String matricula;

	private String senha;
	
	private DTOFuncionarioAsp funcionarioXML;

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public DTOFuncionarioAsp getFuncionarioXML() {
		return funcionarioXML;
	}

	public void setFuncionarioXML(DTOFuncionarioAsp funcionarioXML) {
		this.funcionarioXML = funcionarioXML;
	}

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
