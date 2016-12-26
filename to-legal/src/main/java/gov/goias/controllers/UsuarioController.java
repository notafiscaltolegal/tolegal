package gov.goias.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import gov.goias.entidades.GENEmpresa;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.dominio.TipoUsuario;
import gov.goias.interceptors.CertificateInterceptor;
import gov.goias.interceptors.ContadorInterceptor;

/**
 * 
 * @author 03066547175
 *
 */
@ControllerAdvice
public class UsuarioController {

	@Autowired
	private HttpServletRequest request;

	@ModelAttribute(value = "nomeUsuario")
	public String getNomeUsuario() {

		PessoaParticipante cidadao = (PessoaParticipante) request.getSession()
				.getAttribute(BaseController.SESSION_CIDADAO_LOGADO);

		if (cidadao != null && cidadao.getGenPessoaFisica() != null) {
			return cidadao.getGenPessoaFisica().getNome();
		}

		GENPessoaFisica contador = (GENPessoaFisica) request.getSession()
				.getAttribute(ContadorInterceptor.SESSION_CONTADOR_LOGADO);
		if (contador != null) {
			return contador.getNome();
		}
		GENEmpresa empresa = (GENEmpresa) request.getSession()
				.getAttribute(CertificateInterceptor.SESSION_EMPRESA_LOGADA);
		if (empresa != null) {
			return empresa.getNomeEmpresa();
		}
		return null;
	}

	@ModelAttribute(value = "tipoUsuario")
	public Integer getTipoUsuario() {

		PessoaParticipante cidadao = (PessoaParticipante) request.getSession()
				.getAttribute(BaseController.SESSION_CIDADAO_LOGADO);

		if (cidadao != null && cidadao.getGenPessoaFisica() != null) {
			return TipoUsuario.CIDADAO.getTipo();
		}
		GENPessoaFisica contador = (GENPessoaFisica) request.getSession()
				.getAttribute(ContadorInterceptor.SESSION_CONTADOR_LOGADO);
		if (contador != null) {
			return TipoUsuario.CONTADOR.getTipo();
		}
		GENEmpresa empresa = (GENEmpresa) request.getSession()
				.getAttribute(CertificateInterceptor.SESSION_EMPRESA_LOGADA);
		if (empresa != null) {
			return TipoUsuario.EMPRESA.getTipo();
		}
		return null;
	}
}
