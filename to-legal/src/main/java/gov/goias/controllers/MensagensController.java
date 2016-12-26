package gov.goias.controllers;

import java.net.BindException;
import java.text.ParseException;
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

import gov.goias.entidades.Mensagem;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.service.MensagemService;
import gov.goias.service.PaginacaoDTO;

/**
 * @author lucas-mp
 * @since 31/08/15.
 */
@Controller
@RequestMapping("/mensagens")
public class MensagensController extends BaseController{
    
	@Autowired
	private MensagemService mensagemService;
	
    @RequestMapping("viewMensagens")
    public ModelAndView viewMensagens(){
        ModelAndView modelAndView = new ModelAndView("mensagens/viewMensagens");
        return modelAndView;
    }

    @RequestMapping("/listarMensagens/{page}")
    public @ResponseBody Map<String, Object> listarMensagens(@PathVariable(value = "page") Integer page, BindException bind) throws ParseException {
    	Map<String, Object> resposta = new HashMap<String, Object>();
        Integer max = 5;

        PessoaParticipante cidadao = getCidadaoLogado();
        Integer idPessoa = cidadao.getGenPessoaFisica().getIdPessoa();

        PaginacaoDTO<Mensagem> mensagemPaginate = new PaginacaoDTO<>(); //mensagemService.findMensagensCaixaDeEntrada(max, page, idPessoa);

        resposta.put("mensagens", mensagemPaginate.getList());

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", mensagemPaginate.getCount());
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("/gravarLeituraDasMensagens")
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map<String, Object> gravarLeituraDasMensagens(BindException bind) throws ParseException {
    	 Map<String, Object> resposta = new HashMap<String, Object>();
         PessoaParticipante cidadaoLogado = (PessoaParticipante) request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO);
         boolean sucesso = mensagemService.gravarLeituraDasMensagens(cidadaoLogado);
         resposta.put("sucesso",sucesso);
         return resposta;
    }

    @RequestMapping("/gravarLeituraDasMensagensEmpresas")
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map<String, Object> gravarLeituraDasMensagensEmpresas(Integer idPessoa) throws ParseException {
    	Map<String, Object> resposta = new HashMap<String, Object>();
        boolean sucesso = mensagemService.gravarLeituraDasMensagensEmpresas(idPessoa);
        resposta.put("sucesso",sucesso);
        return resposta;
    }

    @RequestMapping("/listarMensagensEmpresas/{page}")
    public @ResponseBody Map<String, Object> listarMensagensEmpresas(@PathVariable(value = "page") Integer page, Integer idPessoa ) throws ParseException {
    	Map<String, Object> resposta = new HashMap<String, Object>();
        Integer max = 5;
        Integer count;

        List<Mensagem> mensagens = new ArrayList<>();
        mensagens = mensagemService.findMensagensCaixaDeEntradaEmpresas(max, page, idPessoa);
        count = mensagemService.countMensagensEmpresas(idPessoa);

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
//        PessoaParticipante cidadao = (PessoaParticipante) request.getSession().getAttribute(BaseController.SESSION_CIDADAO_LOGADO);
//        if (cidadao != null && cidadao.getGenPessoaFisica() != null) {
//            Integer numeroDeMensagensNaoLidasPeloCidadao = mensagemService.findNumeroDeMensagensNaoLidasPeloCidadao(cidadao);
//            if(numeroDeMensagensNaoLidasPeloCidadao>0){
                resposta.put("nrMensagensNovas",null);
//            }
//        }
        return resposta;
    }

}
