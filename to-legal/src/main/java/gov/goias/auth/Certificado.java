package gov.goias.auth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Certificado {

	public static final String REQUEST_ATTRIBUTE = "javax.servlet.request.X509Certificate";

	//Dados do proprio certificado
	private String state;
	private String locality;
	private String commonName;
	private String countryName;
	private String issuerCommonName;
	private String organizationName;
	private String organizationalUnit;
	private String serialNumber;

	//Informacoes de validade do certificado
	private Date dataInicioValidade;
	private Date dataFimValidade;

	//E-mail (RFC822Name)
	private String email;

	//Dados de certificados do tipo e-CNPJ
	private String pjCnpj;
	private String pjCnpjBase;
	private String pjNomeEmpresarial;
	private String pjInscricaoEstadual;
	private String pjInss;
	private String pjResponsavel;
	private String pjResponsavelCpf;
	private String pjResponsavelRg;
	private String pjResponsavelRgOrgaoExp;
	private String pjResponsavelNis;
	private Date pjResponsavelDataNascimento;

	//Dados de certificados do tipo e-CPF
	private String pfCpf;
	private String pfRg;
	private String pfRgOrgaoExp;
	private String pfPisPasep;
	private String pfNis;
	private String pfInss;
	private String pfTituloEleitoral;
	private String pfTituloEleitoralZona;
	private String pfTituloEleitoralSecao;
	private String pfTituloEleitoralMunicipio;
	private Date pfDataNascimento;
	private Character tipoCertificado;
	public static final Character E_CPF = 'F';
	public static final Character E_CNPJ = 'J';
	
	public boolean isCertificadoValido() {
		Date dataAtual = Calendar.getInstance().getTime();
		return !dataAtual.before(this.getDataInicioValidade()) && !dataAtual.after(this.getDataFimValidade());
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getLocality() {
		return locality;
	}
	
	public void setLocality(String locality) {
		this.locality = locality;
	}
	
	public String getCommonName() {
		return commonName;
	}
	
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getIssuerCommonName() {
		return issuerCommonName;
	}
	
	public void setIssuerCommonName(String issuerCommonName) {
		this.issuerCommonName = issuerCommonName;
	}
	
	public String getOrganizationName() {
		return organizationName;
	}
	
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	public String getOrganizationalUnit() {
		return organizationalUnit;
	}
	
	public void setOrganizationalUnit(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public Date getDataInicioValidade() {
		return dataInicioValidade;
	}

	public void setDataInicioValidade(Date dataInicioValidade) {
		this.dataInicioValidade = dataInicioValidade;
	}

	public Date getDataFimValidade() {
		return dataFimValidade;
	}

	public void setDataFimValidade(Date dataFimValidade) {
		this.dataFimValidade = dataFimValidade;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPjCnpj() {
		if(pjCnpj != null) {
			return pjCnpj.replaceAll("[^0-9]", "");
		} else {
			return pjCnpj;
		}
	}
	
	public void setPjCnpj(String pjCnpj) {
		this.pjCnpj = pjCnpj;
	}
	
	public String getPjCnpjBase() {
		if(pjCnpj != null) {
			pjCnpjBase = this.getPjCnpj().substring(0,8);
			return pjCnpjBase;
		} else {
			return null;
		}
	}

	public void setPjCnpjBase(String pjCnpjBase) {
		this.pjCnpjBase = pjCnpjBase;
	}

	public String getPjNomeEmpresarial() {
		return pjNomeEmpresarial;
	}
	
	public void setPjNomeEmpresarial(String pjNomeEmpresarial) {
		this.pjNomeEmpresarial = pjNomeEmpresarial;
	}
	
	public String getPjInscricaoEstadual() {
		return pjInscricaoEstadual;
	}
	
	public void setPjInscricaoEstadual(String pjInscricaoEstadual) {
		this.pjInscricaoEstadual = pjInscricaoEstadual;
	}
	
	public String getPjInss() {
		return pjInss;
	}
	
	public void setPjInss(String pjInss) {
		this.pjInss = pjInss;
	}
	
	public String getPjResponsavel() {
		return pjResponsavel;
	}
	
	public void setPjResponsavel(String pjResponsavel) {
		this.pjResponsavel = pjResponsavel;
	}
	
	public String getPjResponsavelCpf() {
		if(pjResponsavelCpf != null) {
			return pjResponsavelCpf.replaceAll("[^0-9]", "");
		} else {
			return pjResponsavelCpf;
		}
	}
	
	public void setPjResponsavelCpf(String pjResponsavelCpf) {
		this.pjResponsavelCpf = pjResponsavelCpf;
	}
	
	public String getPjResponsavelRg() {
		if(pjResponsavelRg != null) {
			return pjResponsavelRg.replaceAll("[^0-9]", "");
		} else {
			return pjResponsavelRg;
		}
	}
	
	public void setPjResponsavelRg(String pjResponsavelRg) {
		this.pjResponsavelRg = pjResponsavelRg;
	}
	
	public String getPjResponsavelRgOrgaoExp() {
		return pjResponsavelRgOrgaoExp;
	}
	
	public void setPjResponsavelRgOrgaoExp(String pjResponsavelRgOrgaoExp) {
		this.pjResponsavelRgOrgaoExp = pjResponsavelRgOrgaoExp;
	}
	
	public String getPjResponsavelNis() {
		return pjResponsavelNis;
	}
	
	public void setPjResponsavelNis(String pjResponsavelNis) {
		this.pjResponsavelNis = pjResponsavelNis;
	}
	
	public Date getPjResponsavelDataNascimento() {
		return pjResponsavelDataNascimento;
	}
	
	public void setPjResponsavelDataNascimento(Date pjResponsavelDataNascimento) {
		this.pjResponsavelDataNascimento = pjResponsavelDataNascimento;
	}
	
	public String getPfCpf() {
		if(pfCpf != null) {
			return pfCpf.replaceAll("[^0-9]", "");
		} else {
			return pfCpf;
		}
	}
	
	public void setPfCpf(String pfCpf) {
		this.pfCpf = pfCpf;
	}
	
	public String getPfRg() {
		if(pfRg != null) {
			return pfRg.replaceAll("[^0-9]", "");
		} else {
			return pfRg;
		}
	}
	
	public void setPfRg(String pfRg) {
		this.pfRg = pfRg;
	}
	
	public String getPfRgOrgaoExp() {
		return pfRgOrgaoExp;
	}
	
	public void setPfRgOrgaoExp(String pfRgOrgaoExp) {
		this.pfRgOrgaoExp = pfRgOrgaoExp;
	}
	
	public String getPfPisPasep() {
		return pfPisPasep;
	}
	
	public void setPfPisPasep(String pfPisPasep) {
		this.pfPisPasep = pfPisPasep;
	}
	
	public String getPfNis() {
		return pfNis;
	}
	
	public void setPfNis(String pfNis) {
		this.pfNis = pfNis;
	}
	
	public String getPfInss() {
		return pfInss;
	}
	
	public void setPfInss(String pfInss) {
		this.pfInss = pfInss;
	}
	
	public String getPfTituloEleitoral() {
		if(pfTituloEleitoral != null) {
			return pfTituloEleitoral.replaceAll("[^0-9]", "");
		} else {
			return pfTituloEleitoral;
		}
	}
	
	public void setPfTituloEleitoral(String pfTituloEleitoral) {
		this.pfTituloEleitoral = pfTituloEleitoral;
	}
	
	public String getPfTituloEleitoralZona() {
		return pfTituloEleitoralZona;
	}
	
	public void setPfTituloEleitoralZona(String pfTituloEleitoralZona) {
		this.pfTituloEleitoralZona = pfTituloEleitoralZona;
	}
	
	public String getPfTituloEleitoralSecao() {
		return pfTituloEleitoralSecao;
	}
	
	public void setPfTituloEleitoralSecao(String pfTituloEleitoralSecao) {
		this.pfTituloEleitoralSecao = pfTituloEleitoralSecao;
	}
	
	public String getPfTituloEleitoralMunicipio() {
		return pfTituloEleitoralMunicipio;
	}
	
	public void setPfTituloEleitoralMunicipio(String pfTituloEleitoralMunicipio) {
		this.pfTituloEleitoralMunicipio = pfTituloEleitoralMunicipio;
	}
	
	public Date getPfDataNascimento() {
		return pfDataNascimento;
	}
	public String getDataNascimentoString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return getPfDataNascimento() != null ?  sdf.format(getPfDataNascimento()) : "";
	}
	
	public void setPfDataNascimento(Date pfDataNascimento) {
		this.pfDataNascimento = pfDataNascimento;
	}
	
	public Character getTipoCertificado() {
		return tipoCertificado;
	}
	
	public void setTipoCertificado(Character tipoCertificado) {
		this.tipoCertificado = tipoCertificado;
	}
	
	public String retornarDadosCertificado() {
		return "Certificado: " + "State: " + state + " | " + "Locality: " + locality + " | " + "CommonName: " + commonName + " | " + "CountryName: " + countryName + " | " + "IssuerCommonName: " + issuerCommonName + " | " + "OrganizationName: " + organizationName + " | " + "OrganizationalUnit: " + organizationalUnit + " | " + "Email: " + email + " | " + "DataInicioValidade: " + dataInicioValidade + " | " + "DataFimValidade: " + dataFimValidade;
	}
	
	public String retornarDadosCertificadoPorTipo() {
		String retorno;
		if(tipoCertificado != null && tipoCertificado.equals(Certificado.E_CPF)) {
			retorno = "Certificado: " + "e-CPF" + " | " + "pfCpf: " + pfCpf + " | " + "pfRg: " + pfRg + " | " + "pfRgOrgaoExp: " + pfRgOrgaoExp + " | " + "pfPisPasep: " + pfPisPasep + " | " + "pfNis: " + pfNis + " | " + "pfInss: " + pfInss + " | " + "pfTituloEleitoral: " + pfTituloEleitoral + " | " + "pfTituloEleitoralZona: " + pfTituloEleitoralZona + " | " + "pfTituloEleitoralSecao: " + pfTituloEleitoralSecao + " | " + "pfTituloEleitoralMunicipio: " + pfTituloEleitoralMunicipio + " | " + "pfDataNascimento: " + pfDataNascimento;
		} else if(tipoCertificado != null && tipoCertificado.equals(Certificado.E_CNPJ)) {
			retorno = "Certificado: " + "e-CNPJ" + " | " + "pjCnpj: " + pjCnpj + " | " + "pjCnpjBase: " + this.getPjCnpjBase() + " | " + "pjNomeEmpresarial: " + pjNomeEmpresarial + " | " + "pjInscricaoEstadual: " + pjInscricaoEstadual + " | " + "pjInss: " + pjInss + " | " + "pfInss: " + pfInss + " | " + "pjResponsavel: " + pjResponsavel + " | " + "pjResponsavelCpf: " + pjResponsavelCpf + " | " + "pjResponsavelRg: " + pjResponsavelRg + " | " + "pjResponsavelRgOrgaoExp: " + pjResponsavelRgOrgaoExp + " | " + "pjResponsavelNis: " + pjResponsavelNis + " | " + "pjResponsavelDataNascimento: " + pjResponsavelDataNascimento;
		} else {
			retorno = "Certificado: Não foi possível extrair os dados do certificado apresentado";
		}
		return retorno;
	}
}