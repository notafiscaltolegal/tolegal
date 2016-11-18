package gov.goias.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.exceptions.NFGException;
import gov.goias.interceptors.CertificateInterceptor;
import gov.goias.service.ContribuinteService;
import gov.goias.service.MensagemService;

/**
 * @author henrique-rh
 * @since 16/07/2014
 */
@Controller
@RequestMapping("/contribuinte")
public class ContribuinteController extends BaseController {

	@Autowired
	private ContribuinteService contribuinteService;
	
	@Autowired
	private MensagemService mensagemService;

    @RequestMapping("/cadastro")
    public ModelAndView listContribuintes() throws Exception {
        ModelAndView model = new ModelAndView("contribuinte/list");
        model.addObject("urlCadastrar", "/contribuinte/cadastrar");
        model.addObject("urlAlterar", "/contribuinte/alterar");
        model.addObject("urlListar", "/contribuinte/list");
        return model;
    }

    @RequestMapping("/list/{page}")
    public @ResponseBody Map list(@PathVariable(value="page") Integer page) {

    	 Integer max = 10;
         String inscricao = request.getParameter("inscricao");
         inscricao = inscricao != null ? inscricao.replaceAll("[^0-9]", "") : null;
         Integer numrInscricao = StringUtils.isEmpty(inscricao) ? null : Integer.parseInt(inscricao);
         String cnpjBase = getEmpresaLogada().getNumeroCnpjBase();

         String cnpj = request.getParameter("cnpj");
         cnpj = cnpj != null ? cnpj.replaceAll("[^0-9]", "") : null;
         String nome = request.getParameter("nome");

         List<DTOContribuinte> resultado = contribuinteService.findContribuintes(page, max, cnpjBase, numrInscricao, cnpj, nome);
         List<DTOContribuinte> contribuintes =  mensagemService.retornaQtdMensagensEmpresas(resultado);
         Long count = contribuinteService.countContribuintes(cnpjBase, numrInscricao);

         Map<String, Object> retorno = new HashMap<String, Object>();
         retorno.put("contribuintes", contribuintes);
         retorno.put("urlBase", "/nfg-web/contribuinte");
         Map<String, Object> pagination = new HashMap<String, Object>();
         pagination.put("total", count);
         pagination.put("page", ++page);
         pagination.put("max", max);
         retorno.put("pagination", pagination);
         return retorno;
    }

    @RequestMapping(value = "/alterar", method = RequestMethod.POST)
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ModelAndView alterar(Integer inscricao, String dataInicio) throws NFGException {
    	String redirectUrl = "/contribuinte/cadastro";

        contribuinteService.alterarPelaEmpresa(getEmpresaLogada(), inscricao, dataInicio, redirectUrl);

        setMessage("Cadastro alterado com sucesso. A nova data de início da sua vigência é: " + dataInicio);
        RedirectView redirectView = new RedirectView(redirectUrl, true);
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ModelAndView cadastrar(Integer inscricao, String dataInicio, String termoDeAcordo) throws NFGException {
    	 String redirectUrl = "/contribuinte/cadastro";
         contribuinteService.cadastrarPelaEmpresa(getEmpresaLogada(), inscricao, dataInicio, termoDeAcordo, redirectUrl);

         setMessage("Cadastro efetuado com sucesso. A data de início da sua vigência é: " + dataInicio);
         RedirectView redirectView = new RedirectView(redirectUrl, true);
         redirectView.setExposeModelAttributes(false);
         return new ModelAndView(redirectView);
    }

    @RequestMapping("efetuarlogoutSite")
    public ModelAndView efetuarLogout() {
    	request.getSession().setAttribute(CertificateInterceptor.SESSION_EMPRESA_LOGADA, null);
        return new ModelAndView(new RedirectView("/nfg-web"));
    }

}
