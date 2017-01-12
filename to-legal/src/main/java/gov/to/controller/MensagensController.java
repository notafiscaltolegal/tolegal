package gov.to.controller;

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
import gov.goias.exceptions.NFGException;
import gov.goias.service.MensagemService;
import gov.goias.service.PaginacaoDTO;
import gov.to.service.MensagemSefazToLegalService;
import gov.to.service.MensagemVisuCidadaoToLegalService;

/**
 * @author lucas-mp
 * @since 31/08/15.
 */
@Controller
@RequestMapping("/mensagens")
public class MensagensController extends BaseController{
    
	@Autowired
	private MensagemService mensagemService;
	
	@Autowired
	private MensagemSefazToLegalService mensagemSefazToLegalService;
	
	@Autowired
	private MensagemVisuCidadaoToLegalService mensagemVisuCidadaoToLegalService;
	
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

        PaginacaoDTO<Mensagem> mensagemPaginate = mensagemVisuCidadaoToLegalService.findMensagensCaixaDeEntrada(max, page, cidadao.getGenPessoaFisica().getCpf());

        resposta.put("mensagens", mensagemPaginate.getList());

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", mensagemPaginate.getCount());
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("/gravarLeituraDasMensagens")
    public @ResponseBody Map<String, Object> gravarLeituraDasMensagens(BindException bind) throws ParseException {
    	 
    	Map<String, Object> resposta = new HashMap<String, Object>();
         PessoaParticipante cidadaoLogado = getCidadaoLogado();
         
         try{
        	 mensagemSefazToLegalService.gravarLeituraDasMensagens(cidadaoLogado.getGenPessoaFisica().getCpf());
             resposta.put("sucesso",Boolean.TRUE);
         }catch(Exception ex){
        	 ex.printStackTrace();
        	 resposta.put("sucesso",Boolean.FALSE);
         }
        
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
    	
    	PessoaParticipante usuarioLogado = getCidadaoLogado();
    	
    	if (usuarioLogado == null){
    		return null;
    	}
    	
        resposta.put("nrMensagensNovas",mensagemSefazToLegalService.qntMensagemNaoLidaCidadao(usuarioLogado.getGenPessoaFisica().getCpf()));
        
        return resposta;
    }

}
