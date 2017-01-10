package gov.goias.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.exceptions.NFGException;
import gov.goias.interceptors.ContadorInterceptor;
import gov.goias.service.ContribuinteService;
import gov.goias.service.MensagemService;

/**
 * Created by diogo-rs on 7/15/2014.
 */
@Controller
@RequestMapping("/contador/contribuintes")
public class CadastroDeContribuintesController extends BaseController {
	
	@Autowired
	private ContribuinteService contribuinteService;

	@Autowired
	private MensagemService mensagemService;
	
	@RequestMapping("/list/{page}")
	public @ResponseBody Map list(@PathVariable(value = "page") Integer page) {
		// page--; // Page 1 == 0
		Integer max = 10;
		String inscricao = request.getParameter("inscricao");
		inscricao = inscricao != null ? inscricao.replaceAll("[^0-9]", "") : null;
		Integer numrInscricao = StringUtils.isEmpty(inscricao) ? null : Integer.parseInt(inscricao);

		String cnpj = request.getParameter("cnpj");
		cnpj = cnpj != null ? cnpj.replaceAll("[^0-9]", "") : null;
		String nome = request.getParameter("nome");

		GENPessoaFisica contador = getContadorLogado();

		List<DTOContribuinte> resultado = contribuinteService.findContribuintesParaContador(page, max, contador, numrInscricao, cnpj, nome);
		Long count = contribuinteService.countContribuintesParaContador(contador, numrInscricao);
		List<DTOContribuinte> contribuintes = mensagemService.retornaQtdMensagensEmpresas(resultado);

		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("contribuintes", contribuintes);
		retorno.put("urlBase", "/nfg-web/contador");
		Map<String, Object> pagination = new HashMap<String, Object>();
		pagination.put("total", count);
		pagination.put("page", ++page);
		pagination.put("max", max);
		retorno.put("pagination", pagination);
		return retorno;
	}

	@RequestMapping(value = "/cadastro")
	public ModelAndView listContribuintes() {
		ModelAndView model = new ModelAndView("contribuinte/list");
		model.addObject("urlCadastrar", "/contador/contribuintes/cadastrar");
		model.addObject("urlAlterar", "/contador/contribuintes/alterar");
		model.addObject("urlListar", "/contador/contribuintes/list");
		return model;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/cadastrar")
//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ModelAndView cadastrarContribuinteAoNFG(Integer inscricao, String dataInicio, String termoDeAcordo)
			throws NFGException {
		String redirectUrl = "/contador/contribuintes/cadastro";
		contribuinteService.cadastrarPeloContador(contadorLogado, inscricao, dataInicio, termoDeAcordo, redirectUrl);

		setMessage("Cadastro alterado com sucesso. A nova data de início da sua vigência é: " + dataInicio);
		RedirectView redirectView = new RedirectView(redirectUrl, true);
		redirectView.setExposeModelAttributes(false);
		return new ModelAndView(redirectView);
	}

	@RequestMapping(value = "/alterar", method = RequestMethod.POST)
//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ModelAndView alterar(Integer inscricao, String dataInicio) throws NFGException {
		String redirectUrl = "/contador/contribuintes/cadastro";

		contribuinteService.alterarPeloContador(contadorLogado, inscricao, dataInicio, redirectUrl);

		setMessage("Cadastro alterado com sucesso. A nova data de início da sua vigência é: " + dataInicio);
		RedirectView redirectView = new RedirectView(redirectUrl, true);
		redirectView.setExposeModelAttributes(false);
		return new ModelAndView(redirectView);
	}

	@RequestMapping("efetuarlogoutSite")
	public ModelAndView efetuarLogout() {
		request.getSession().setAttribute(ContadorInterceptor.SESSION_CONTADOR_LOGADO, null);
		return new ModelAndView(new RedirectView("/nfg-web"));
	}

}
