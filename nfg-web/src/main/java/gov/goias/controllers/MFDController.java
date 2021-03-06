package gov.goias.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import gov.goias.entidades.MFDArquivoMemoriaFiscal;
import gov.goias.exceptions.NFGException;
import gov.goias.service.EmpresaService;
import gov.goias.service.MDFService;

/**
 * Created by bruno-cff on 30/06/2015.
 */
@Controller
@RequestMapping(value = {"/contador/mfd" , "contribuinte/mfd"})
public class MFDController extends BaseController {
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private MDFService mdfService;

    @RequestMapping("/consultar/{inscricaoEstadual}")
    public ModelAndView consultar(@PathVariable(value="inscricaoEstadual")Integer inscricaoEstadual) {
        verificaCompatibilidadeInscricao(inscricaoEstadual);

        ModelAndView modelAndView = new ModelAndView("/mfd/consultar");

        modelAndView.addObject("inscricaoEstadual", inscricaoEstadual);
        modelAndView.addObject("urlBase", getUrlBase());

        return modelAndView;
    }

    @RequestMapping("/listar/{page}")
    public @ResponseBody
    Map<String, Object> listar(@PathVariable(value = "page") Integer page, Integer inscricaoEstadual, String referenciaInicio, String referenciaFim) {
        Integer max = 12;
        Integer count = 0;
        String anoInicio = "";
        String anoFim = "";
        String mesInicio = "";
        String mesFim = "";

        List<MFDArquivoMemoriaFiscal> listArquivoMemoriaFiscal = new ArrayList<>();
        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        if(inscricaoEstadual != null){
            if(referenciaInicio != null && referenciaFim != null){
                anoInicio = referenciaInicio.substring(3, 7);
                anoFim = referenciaFim.substring(3, 7);
                mesInicio = referenciaInicio.substring(0, 2);
                mesFim = referenciaFim.substring(0, 2);

                String dataFim = anoFim + mesFim;
                String dataInicio = anoInicio + mesInicio;

                Integer intFim = Integer.parseInt(dataFim);
                Integer intInicio = Integer.parseInt(dataInicio);

                listArquivoMemoriaFiscal = mdfService.findMDF(inscricaoEstadual, intInicio, intFim, max, page);
                count = mdfService.findCountMDF(inscricaoEstadual, intInicio, intFim);
            }else{
                listArquivoMemoriaFiscal = (List<MFDArquivoMemoriaFiscal>) mdfService.findMDF(inscricaoEstadual, max, page);
                count = mdfService.findCountMDF(inscricaoEstadual);
            }
        }

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("listArquivoMemoriaFiscal", listArquivoMemoriaFiscal);
        resposta.put("pagination", pagination);

        return resposta;
    }


    public void verificaCompatibilidadeInscricao(Integer inscricao){
        boolean inscricaoCompativel = false;
        inscricaoCompativel |= empresaService.inscricaoCompativelContador(contadorLogado, inscricao);
        inscricaoCompativel |= empresaService.inscricaoCompativelContribuinte(empresaLogada, inscricao);
        String urlRedirect = getUrlBase().contains("/contador") ? "/contador/contribuintes/cadastro" : "/contribuinte/cadastro";
        if (!inscricaoCompativel) throw new NFGException("Nenhum usu�rio logado ou inscri�&#225;o n&#225;o condizente com nenhum usu�rio logado!", urlRedirect);
    }

    private String getUrlBase() {
    	
    	String contexto = "/nfg-web";
    	
        String urlBase = request.getRequestURI();
        urlBase = urlBase.substring(urlBase.indexOf(contexto)+contexto.length(),urlBase.length());
        urlBase = urlBase.replace("/consultar", "");
        urlBase = urlBase.replaceAll("\\d*", "");
        return  urlBase;
    }
}
