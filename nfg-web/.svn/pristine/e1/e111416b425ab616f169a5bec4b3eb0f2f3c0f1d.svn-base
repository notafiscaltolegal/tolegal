package gov.goias.controllers;

import gov.goias.entidades.*;
import gov.goias.persistencia.historico.HistoricoNFG;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.BindException;
import java.text.ParseException;
import java.util.*;

/**
 * @author lucas-mp
 * @since 31/08/15.
 */
@Controller
@RequestMapping("/mensagens")
public class MensagensController extends BaseController{
    private static final Logger logger = Logger.getLogger(MensagensController.class);

    @Autowired
    Mensagem mensagemRepository;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("viewMensagens")
    public ModelAndView viewMensagens(){
        ModelAndView modelAndView = new ModelAndView("mensagens/viewMensagens");
        return modelAndView;
    }

    @RequestMapping("/listarMensagens/{page}")
    public @ResponseBody Map<String, Object> listarMensagens(@PathVariable(value = "page") Integer page, BindException bind) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();
        Integer max = 5;

        PessoaParticipante cidadao = (PessoaParticipante) request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO);
        Integer idPessoa = cidadao.getGenPessoaFisica().getIdPessoa();

        Map mensagemPaginate = mensagemRepository.findMensagensCaixaDeEntrada(max, page, idPessoa);
        List<Mensagem> mensagens  = (List<Mensagem>) mensagemPaginate.get("list");
        Integer count = (Integer) mensagemPaginate.get("count");

        resposta.put("mensagens", mensagens);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("/gravarLeituraDasMensagens")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map<String, Object> gravarLeituraDasMensagens(BindException bind) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();
        PessoaParticipante cidadaoLogado = (PessoaParticipante) request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO);
        boolean sucesso = mensagemRepository.gravarLeituraDasMensagens(cidadaoLogado);
        resposta.put("sucesso",sucesso);
        return resposta;
    }

    @RequestMapping("/gravarLeituraDasMensagensEmpresas")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map<String, Object> gravarLeituraDasMensagensEmpresas(Integer idPessoa) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();
        boolean sucesso = mensagemRepository.gravarLeituraDasMensagensEmpresas(idPessoa);
        resposta.put("sucesso",sucesso);
        return resposta;
    }

    @RequestMapping("/listarMensagensEmpresas/{page}")
    public @ResponseBody Map<String, Object> listarMensagensEmpresas(@PathVariable(value = "page") Integer page, Integer idPessoa ) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();
        Integer max = 5;
        Integer count;

        List<Mensagem> mensagens = new ArrayList<>();
        mensagens = mensagemRepository.findMensagensCaixaDeEntradaEmpresas(max, page, idPessoa);
        count = mensagemRepository.countMensagensEmpresas(idPessoa);

        resposta.put("mensagens", mensagens);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("/mensagensNaoLidasCidadao")
    public @ResponseBody Map<String, Object> getMensagensNaoLidasCidadao() {
        Map<String, Object> resposta = new HashMap<String, Object>();
        PessoaParticipante cidadao = (PessoaParticipante) request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO);
        if (cidadao != null && cidadao.getGenPessoaFisica() != null) {
            Integer numeroDeMensagensNaoLidasPeloCidadao = mensagemRepository.findNumeroDeMensagensNaoLidasPeloCidadao(cidadao);
            if(numeroDeMensagensNaoLidasPeloCidadao>0){
                resposta.put("nrMensagensNovas",numeroDeMensagensNaoLidasPeloCidadao.toString());
            }
        }
        return resposta;
    }

}
