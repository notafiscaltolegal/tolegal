package gov.goias.controllers;

import java.net.BindException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;


@Controller
@RequestMapping("/portal/reclamacao/usuario")
public class ReclamacaoUsuarioController extends BaseController{

    private static final Logger logger = Logger.getLogger(ReclamacaoUsuarioController.class);

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("reclamacao/reclamacaoIndex");
        return modelAndView;
    }

    @RequestMapping("/filtroRelatorio")
    public ModelAndView filtroRelatorio() {
        ModelAndView modelAndView = new ModelAndView("reclamacao/filtroRelatorio");
        List<TipoComplSituacaoReclamacao> situacoes = TipoComplSituacaoReclamacao.getList();
        modelAndView.addObject("situacoes",situacoes);
        return modelAndView;
    }

    @RequestMapping("/gerarRelatorio")
    public ModelAndView gerarRelatorio(
            @RequestParam(value="data") String data,
            @RequestParam(value="dataFim") String dataFim,
                                       @RequestParam(value="situacao") Integer situacao,
                                       @RequestParam(value="motivoReclamacao") Integer motivoReclamacao,
                                       @RequestParam(value="tipoDocFiscalReclamacao") Integer tipoDocFiscalReclamacao,
                                       @RequestParam(value="recNoPrazo") Integer recNoPrazo) {
        ModelAndView modelAndView = new ModelAndView("reclamacao/relatorio");
        modelAndView.addObject("situacao",situacao);
        modelAndView.addObject("data",data);
        modelAndView.addObject("dataFim",dataFim);
        modelAndView.addObject("motivoReclamacao",motivoReclamacao);
        modelAndView.addObject("tipoDocFiscalReclamacao",tipoDocFiscalReclamacao);
        modelAndView.addObject("recNoPrazo",recNoPrazo);
        return modelAndView;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        //o parametro true faz com que o binder gere data nula para string vazia do js
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("/listarReclamacoesRelatorio/{page}")
    public @ResponseBody Map<?,?> listarReclamacoesRel(@PathVariable(value = "page") Integer page,
                                                  Date data, Date dataFim,Integer situacao, Integer motivo, Integer tipoDocFisc, Integer noPrazo) {
    	return new HashMap<>();
    }


    @RequestMapping("/listarReclamacoes/{page}")
    public @ResponseBody
    Map<String, Object> listarReclamacoes(@PathVariable(value = "page") Integer page){
        Integer max = 5;


        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", 0);
        pagination.put("page", 0);
        pagination.put("max", max);

        resposta.put("reclamacoes", new ArrayList<>());
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("/verReclamacaoDetalhe/{idReclamacao}")
    public ModelAndView verReclamacaoDetalhe(@PathVariable("idReclamacao") Integer idReclamacao){
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("reclamacao/reclamacaoDetalhe");

        modelAndView.addObject("reclamacao", 0);
        modelAndView.addObject("statusDisponiveis", new ArrayList<>());
        modelAndView.addObject("nomeFantasia", "ReclamacaoUsuarioController 108");
        modelAndView.addObject("dataEmissaoStr", new Date());
        modelAndView.addObject("dataReclamacaoStr", new Date());

        return modelAndView;
    }

    @RequestMapping("listarAndamentoReclamacao/{page}")
    public @ResponseBody Map<String, Object> listarAndamentoReclamacao(@PathVariable(value = "page") Integer page, Integer idReclamacao,BindException bind) throws ParseException {
        Integer max = 5;
        Integer count=0;

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("situacoesReclamacao", new ArrayList<>());
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("selectAcoesDisponiveis")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map selectAcoesDisponiveis(Integer idReclamacao) {
        Map retorno = new HashMap();
        retorno.put("acoesDisponiveis",new ArrayList<>());
        return retorno;
    }

    @RequestMapping("alterarSituacaoReclamacao")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map alterarSituacaoReclamacao(Integer idReclamacao,Integer novoCodgTipoCompl, String infoReclamacao ) {
        Map retorno = new HashMap();
        return retorno;
    }
}
