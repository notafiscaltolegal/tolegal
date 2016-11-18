package gov.goias.controllers;

import java.io.IOException;
import java.net.BindException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lucas-mp
 * @since 31/08/15.
 */
@Controller
@RequestMapping("/portal/mensagens/usuario")
public class MensagensUsuarioController extends BaseController{

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

        resposta.put("mensagens", new ArrayList<>());

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", 0);
        pagination.put("page", 1);
        pagination.put("max", 2);
        resposta.put("pagination", pagination);

        return resposta;
    }


    @RequestMapping(value = "/efetuarcadastro",method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map<String, Object> efetuarCadastroMensagemUsuario(Character tipoDestinatario,Character mensagemPublica,String mapDestinatariosString,String titulo, String texto) throws IOException {
        Map<String, Object> resposta = new HashMap<String, Object>();

        return resposta;
    }


    @RequestMapping(value = "/efetuarAlteracaoMensagem",method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map<String, Object> efetuarAlteracaoMensagem(String texto,String titulo,Integer id) throws IOException {
        Map<String, Object> resposta = new HashMap<String, Object>();

        return resposta;
    }



    @RequestMapping(value = "/efetuarExclusaoMensagem",method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map<String, Object> efetuarExclusaoMensagem(Integer id, Character publica) throws IOException {
        Map<String, Object> resposta = new HashMap<String, Object>();

        return resposta;
    }

    @RequestMapping("/filtrarDestinatarioMensagemUsuario/{page}")
    public @ResponseBody Map<String, Object> filtrarDestinatarioMensagemUsuario(@PathVariable(value = "page") Integer page,String tipoDestinatario,String cpfDestinatario,String nomeDestinatario,String cnpjDestinatario,String nomeFantasiaDestinatario, BindException bind) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();

        resposta.put("destinatarios", new ArrayList<>());
        resposta.put("tipoDestinatario", "C");

        return resposta;
    }



}
