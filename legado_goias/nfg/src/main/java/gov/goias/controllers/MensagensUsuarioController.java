package gov.goias.controllers;

import gov.goias.entidades.Mensagem;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.persistencia.historico.HistoricoNFG;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping("/portal/mensagens/usuario")
public class MensagensUsuarioController extends BaseController{

    @Autowired
    Mensagem mensagemRepository;

    @Autowired
    private HttpServletRequest request;


    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("mensagens/usuarioIndex");
        return modelAndView;
    }

    @RequestMapping("/viewAlterarMensagem")
    public ModelAndView viewAlterarMensagem() {
        ModelAndView modelAndView = new ModelAndView("mensagens/alterarMensagem");
        return modelAndView;
    }


    @RequestMapping("/cadastrar")
    public ModelAndView cadastrarMensagem() {
        ModelAndView modelAndView = new ModelAndView("mensagens/cadastrar");
        return modelAndView;
    }

    @RequestMapping("/adicionarDestinatario")
    public ModelAndView adicionarDestinatario() {
        ModelAndView modelAndView = new ModelAndView("mensagens/adicionarDestinatario");
        return modelAndView;
    }

    @RequestMapping("/listarMensagensCadastradasPeloUsuario/{page}")
    public @ResponseBody Map<String, Object> listarMensagensCadastradasPeloUsuario(@PathVariable(value = "page") Integer page, BindException bind) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();

        Integer max = 10;
        Integer count;

        List<Mensagem> mensagens = new ArrayList<>();
        mensagens = mensagemRepository.findMensagensCadastradasPeloUsuario(max,page);
        count = mensagemRepository.countMensagensCadastradasPeloUsuario();

        resposta.put("mensagens", mensagens);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("pagination", pagination);

        return resposta;
    }


    @RequestMapping(value = "/efetuarcadastro",method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map<String, Object> efetuarCadastroMensagemUsuario(Character tipoDestinatario,Character mensagemPublica,String mapDestinatariosString,String titulo, String texto) throws IOException {
        Map<String, Object> resposta = new HashMap<String, Object>();

        Map<String,LinkedHashMap> mapDestinatarios = null;
        if (!mapDestinatariosString.isEmpty()){
            mapDestinatarios = new ObjectMapper().readValue(mapDestinatariosString, HashMap.class);
        }

        if(mensagemRepository.cadastrarNovaMensagemUsuario(tipoDestinatario,mensagemPublica,titulo,texto,mapDestinatarios)){
            resposta.put("success",true);
            resposta.put("mensagem","A mensagem foi cadastrada com sucesso!");
        }else{
            resposta.put("success",false);
            resposta.put("mensagem","A mensagem não pôde ser cadastrada. Verifique os logs da aplicação.");
        }

        return resposta;
    }


    @RequestMapping(value = "/efetuarAlteracaoMensagem",method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map<String, Object> efetuarAlteracaoMensagem(String texto,String titulo,Integer id) throws IOException {
        Map<String, Object> resposta = new HashMap<String, Object>();

        if(mensagemRepository.alterarMensagemUsuario(titulo, texto, id)){
            resposta.put("success",true);
            resposta.put("mensagem","A mensagem foi alterada com sucesso!");
        }else{
            resposta.put("success",false);
            resposta.put("mensagem","A mensagem não pôde ser alterada. Verifique os logs da aplicação.");
        }

        return resposta;
    }



    @RequestMapping(value = "/efetuarExclusaoMensagem",method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map<String, Object> efetuarExclusaoMensagem(Integer id, Character publica) throws IOException {
        Map<String, Object> resposta = new HashMap<String, Object>();

        if(mensagemRepository.excluirMensagemUsuario(id,publica)){
            resposta.put("success",true);
            resposta.put("mensagem","A mensagem foi excluida com sucesso!");
        }else{
            resposta.put("success",false);
            resposta.put("mensagem","A mensagem não pôde ser excluida. Verifique os logs da aplicação.");
        }

        return resposta;
    }

    @RequestMapping("/filtrarDestinatarioMensagemUsuario/{page}")
    public @ResponseBody Map<String, Object> filtrarDestinatarioMensagemUsuario(@PathVariable(value = "page") Integer page,String tipoDestinatario,String cpfDestinatario,String nomeDestinatario,String cnpjDestinatario,String nomeFantasiaDestinatario, BindException bind) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();
        List destinatarios = new ArrayList<>();
        if (tipoDestinatario.equals("C")){
            destinatarios = mensagemRepository.findDestinatarioCpf(cpfDestinatario,nomeDestinatario);
        }else{
            destinatarios = mensagemRepository.findDestinatarioCnpj(cnpjDestinatario, nomeFantasiaDestinatario);
        }

        resposta.put("destinatarios", destinatarios);
        resposta.put("tipoDestinatario", tipoDestinatario);

        return resposta;
    }



}
