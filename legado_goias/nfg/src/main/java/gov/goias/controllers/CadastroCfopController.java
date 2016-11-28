package gov.goias.controllers;

import gov.goias.entidades.*;
import gov.goias.exceptions.NFGException;
import gov.goias.persistencia.historico.Historico;
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

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by thiago-mb on 18/07/2014.
 */
@Controller
@RequestMapping("/portal/cfop")
public class CadastroCfopController extends BaseController {

    @Autowired
    private GENCfop genCfop;

    @Autowired
    private CfopAutorizado cfopAutorizado;

    @RequestMapping(value = "/cadastro")
    public  ModelAndView cadastro(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("/cfop/cadastro");
        List<GENCfop> listaCfops;
        listaCfops = genCfop.list();
        Collections.sort(listaCfops ,new Comparator<GENCfop>() {
            @Override
            public int compare(GENCfop o1, GENCfop o2) {
                return o1.getCodigoCFOP().compareTo(o2.getCodigoCFOP());
            }
        });
        modelAndView.addObject("listaCfop", listaCfops);
        return modelAndView;
    }

    @RequestMapping(value = "/list-cfops-autorizados/{page}")
    public @ResponseBody Map listarCfops(@PathVariable(value="page") Integer page,  String codigoCFOP, String descricaoCFOP)
    {
        Map<String, Object> params = new HashMap<String, Object>();

        String startParam = request.getParameter("start");
        Integer start = StringUtils.isEmpty(startParam) ? 0 : Integer.parseInt(startParam);
        params.put("start", start);

        String maxParam = request.getParameter("max");
        Integer max = StringUtils.isEmpty(maxParam) ? 10 : Integer.parseInt(maxParam);
        params.put("max", max);

        List<CfopAutorizado> cfopAutorizados = null;
        Long countCfopsAutorizadosNaoExcluidos = null;

        String descricaoParam = StringUtils.isEmpty(
                descricaoCFOP) ? descricaoCFOP : descricaoCFOP.toUpperCase();

        cfopAutorizados = cfopAutorizado.list(codigoCFOP, descricaoParam);

        countCfopsAutorizadosNaoExcluidos = cfopAutorizado.countCfopsAutorizadosNaoExcluidos();

        Map resposta = new HashMap();
        resposta.put("cfopsAutorizados", cfopAutorizados);
        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", countCfopsAutorizadosNaoExcluidos);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cadastrar-cfop")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Historico
    public @ResponseBody Map cadastrarCfop(Integer codigo) throws Exception
    {
        Map resposta = new HashMap();
        try
        {
            GENCfop cfopCadastrar = new GENCfop();
            cfopCadastrar.setCodigoCFOP(codigo);
            CfopAutorizado cfopAutorizadoCad = new CfopAutorizado();
            cfopAutorizadoCad.setGenCfop(cfopCadastrar);
            cfopAutorizadoCad.setDataInclusaoCfop(new Date());

            if(cfopAutorizadoCad.list(String.valueOf(codigo),null).isEmpty())
            {
                cfopAutorizadoCad.save();

                resposta.put("success", true);
                resposta.put("message", "Cfop incluído!");
            }
            else
            {
                resposta.put("success", false);
                resposta.put("message", "Este Cfop já foi incluído!");
            }

        }
        catch (Exception e)
        {
            resposta.put("success", false);
            resposta.put("message", "Não foi possível incluir este CFOP!");
        }
        return resposta;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/excluir-cfop")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Historico
    public @ResponseBody Map excluirCfop(Integer idCfopAutorizado) throws Exception
    {
        Map resposta = new HashMap();
        try
        {
            CfopAutorizado cfopFiscal = new CfopAutorizado();
            //buscando no banco o id informado para setar sua data de exclusão(exclusão lógica)
            cfopFiscal = cfopFiscal.get(idCfopAutorizado);
            cfopFiscal.setDataExclusaoCfop(new Date());
            cfopFiscal.update();

            resposta.put("success", true);
            resposta.put("message", "Removido!");
        }
        catch (Exception e)
        {
            resposta.put("success", false);
            resposta.put("message", "Não foi possível remover este CFOP!");
        }
        return resposta;
    }
}
