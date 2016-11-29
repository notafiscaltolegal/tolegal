package gov.goias.controllers;

import gov.goias.entidades.*;
import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.persistencia.CCEContribuinteRepository;
import gov.goias.persistencia.historico.HistoricoNFG;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.BindException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruno-cff on 13/11/2015.
 */
@Controller
@RequestMapping("/portal/reclamacao/usuario")
//@RequestMapping("/reclamacao/usuario")
public class ReclamacaoUsuarioController extends BaseController{

    private static final Logger logger = Logger.getLogger(ReclamacaoUsuarioController.class);
    @Autowired
    DocumentoFiscalReclamado reclamacaoRepository;

    @Autowired
    SimpleDateFormat simpleDateFormat;

    @Autowired
    CCEContribuinteRepository cceRepository;

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
    public @ResponseBody Map listarReclamacoesRel(@PathVariable(value = "page") Integer page,
                                                  Date data, Date dataFim,Integer situacao, Integer motivo, Integer tipoDocFisc, Integer noPrazo) {
        Integer max = 10;

        logger.info("motivo:"+motivo+" tipoDoc:"+tipoDocFisc+" noPrazo"+noPrazo);
        Map reclamacoesPaginate = reclamacaoRepository.findReclamacoesUsuarioRelatorio(page,max,data,dataFim,situacao,motivo,tipoDocFisc,noPrazo);
        List<DocumentoFiscalReclamado> reclamacoes = (List<DocumentoFiscalReclamado>) reclamacoesPaginate.get("list");
        Integer count = (Integer) reclamacoesPaginate.get("count");

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("reclamacoes", reclamacoes);
        resposta.put("pagination", pagination);

        return resposta;
    }


    @RequestMapping("/listarReclamacoes/{page}")
    public @ResponseBody
    Map<String, Object> listarReclamacoes(@PathVariable(value = "page") Integer page){
        Integer max = 5;
        Integer count;

        Map reclamacoesPaginate = reclamacaoRepository.findReclamacoesUsuarioGestor(page, max);
        List<DocumentoFiscalReclamado> reclamacoes = (List<DocumentoFiscalReclamado>) reclamacoesPaginate.get("list");
        count = (Integer) reclamacoesPaginate.get("count");

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("reclamacoes", reclamacoes);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("/verReclamacaoDetalhe/{idReclamacao}")
    public ModelAndView verReclamacaoDetalhe(@PathVariable("idReclamacao") Integer idReclamacao){
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("reclamacao/reclamacaoDetalhe");

        DocumentoFiscalReclamado reclamacao = reclamacaoRepository.findReclamacaoPorId(idReclamacao);

        List<ComplSituacaoReclamacao> statusDisponiveis =
                reclamacaoRepository.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.GESTOR,reclamacao);

        modelAndView.addObject("reclamacao", reclamacao);
        modelAndView.addObject("statusDisponiveis", statusDisponiveis);
        String nomeFantasia = cceRepository.getFodendoNomeFantasiaPeloCnpj(reclamacao.getNumeroCnpjEmpresa());
        modelAndView.addObject("nomeFantasia", nomeFantasia != null ? nomeFantasia : "");
        modelAndView.addObject("dataEmissaoStr", simpleDateFormat.format(reclamacao.getDataDocumentoFiscal()));
        modelAndView.addObject("dataReclamacaoStr", simpleDateFormat.format(reclamacao.getDataReclamacao()));

        return modelAndView;
    }

    @RequestMapping("listarAndamentoReclamacao/{page}")
    public @ResponseBody Map<String, Object> listarAndamentoReclamacao(@PathVariable(value = "page") Integer page, Integer idReclamacao,BindException bind) throws ParseException {
        Integer max = 5;
        Integer count=0;

        Map andamentoReclamacoesPaginate = reclamacaoRepository.findAndamentoDaReclamacao(idReclamacao, page, max);
        List<SituacaoDocumentoFiscalReclamado> situacoesReclamacao = (List<SituacaoDocumentoFiscalReclamado>) andamentoReclamacoesPaginate.get("list");
        count = (Integer) andamentoReclamacoesPaginate.get("count");

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("situacoesReclamacao", situacoesReclamacao);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("selectAcoesDisponiveis")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map selectAcoesDisponiveis(Integer idReclamacao) {
        DocumentoFiscalReclamado reclamacao = reclamacaoRepository.findReclamacaoPorId(idReclamacao);
        Map retorno = new HashMap();
        List<ComplSituacaoReclamacao> acoesDisponiveis = reclamacaoRepository.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.GESTOR,reclamacao);
        retorno.put("acoesDisponiveis",acoesDisponiveis);
        return retorno;
    }

    @RequestMapping("alterarSituacaoReclamacao")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map alterarSituacaoReclamacao(Integer idReclamacao,Integer novoCodgTipoCompl, String infoReclamacao ) {
        Map retorno = new HashMap();
        GENPessoaFisica gestor = (GENPessoaFisica) request.getSession().getAttribute("portalLogado");
        DocumentoFiscalReclamado reclamacao = reclamacaoRepository.findReclamacaoPorId(idReclamacao);
        retorno = reclamacaoRepository.alteracaoDeSituacaoReclamacaoPorGestor(reclamacao, novoCodgTipoCompl, infoReclamacao, gestor);

        return retorno;
    }
}