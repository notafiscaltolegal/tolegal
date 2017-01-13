package gov.to.controller;

import java.net.BindException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.util.Captcha;
import gov.to.dto.PaginacaoContribuinteDTO;
import gov.to.entidade.ContribuinteToLegal;
import gov.to.service.ContribuinteToLegalService;

/**
 * @author henrique-rh
 * @since 16/07/2014
 */
@Controller
@RequestMapping("/contribuinte")
public class ContribuinteController extends BaseController {

	@Autowired
	private ContribuinteToLegalService contribuinteToLegalService;
	
	@Autowired
    private Captcha captcha;
	
	@RequestMapping("/login")
    public ModelAndView login() throws Exception {
        ModelAndView model = new ModelAndView("contribuinte/login");
        return model;
    }
	
	@RequestMapping("/inicioSite")
    public ModelAndView inicio() throws Exception {
		
		if (super.getEmpresaLogada() == null){
			return login();
		}
		
        ModelAndView model = new ModelAndView("contribuinte/inicioSiteEmpresa");
        return model;
    }
	
	 @RequestMapping("efetuarlogin")
	    public @ResponseBody Map<?,?> efetuarLoginEmpresa(String ie, String senha,String challenge,String captchaResponse) throws BindException {
	    	
	    	captcha.validarCaptchaSeAtivo(request, challenge, captchaResponse);
	    	
	    	 Map<String, Object> resposta = new HashMap<String, Object>();
	    	 
			try {
				ContribuinteToLegal retornoAutenticacao = contribuinteToLegalService.autenticaCidadao(ie, senha);
				
				if (retornoAutenticacao == null){
					resposta.put("loginInvalido", "Inscrição Estadual ou Senha incorretos.");
					return resposta;
				}
	
				setSuccessMessage("Bem-vindo(a) ao To Legal!");
				resposta.put("urlRedirect", "/contribuinte/inicioSite");

				request.getSession().setAttribute(SESSION_EMPRESA_LOGADO, retornoAutenticacao);
	
			} catch (Exception ex) {
				ex.printStackTrace();
				resposta.put("loginInvalido", "Falha na autenticação");
			}
	        
	        resposta.put("ativarCaptchaLogin", Captcha.verificarAtivacaoCaptcha("numeroDeTentativasLogin", request));
	        
	        return resposta;
	    }
	
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
         
         PaginacaoContribuinteDTO pagDTO = contribuinteToLegalService.findContribuintes(page, max, super.getEmpresaLogada().getId());

         List<DTOContribuinte> resultado = pagDTO.getListContribuinteDTO();
         
         Map<String, Object> pagination = new HashMap<String, Object>();
         
         pagination.put("total", pagDTO.getCountPaginacao());
         pagination.put("page", ++page);
         pagination.put("max", max);
       

         Map<String, Object> retorno = new HashMap<String, Object>();
         retorno.put("contribuintes", resultado);
         retorno.put("urlBase", "/to-legal/contribuinte");
         retorno.put("pagination", pagination);
         
         return retorno;
    }

    @RequestMapping("efetuarlogoutSite")
    public ModelAndView efetuarLogout() {
    	request.getSession().setAttribute(BaseController.SESSION_EMPRESA_LOGADO, null);
    	request.getSession().invalidate();
        return new ModelAndView(new RedirectView("/to-legal"));
    }

}
