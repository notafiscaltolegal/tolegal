package gov.goias.controllers;

import gov.goias.dtos.DTOContatoGanhadores;
import gov.goias.dtos.DTOGENEmailPessoa;
import gov.goias.dtos.DTOGENTelefonePessoa;
import gov.goias.entidades.GENEmailPessoa;
import gov.goias.entidades.GENTelefonePessoa;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.RegraSorteio;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruno-cff on 02/12/2015.
 */
@Controller
@RequestMapping("/portal/contato-ganhadores/contatoGanhadores")
//@RequestMapping("/contato-ganhadores/contatoGanhadores")
public class ContatoGanhadoresController extends BaseController{

    private static final Logger logger = Logger.getLogger(ContatoGanhadoresController.class);

    @Autowired
    RegraSorteio regraSorteioRepository;

    @Autowired
    PessoaParticipante pessoaParticipanteRepository;

    @Autowired
    GENEmailPessoa genEmailPessoaRepository;

    @Autowired
    GENTelefonePessoa genTelefonePessoaRepository;

    @RequestMapping("/inicio")
    public ModelAndView viewContatos(){
        ModelAndView modelAndView = new ModelAndView("/contato-ganhadores/contatoGanhadoresPortal");

        List<RegraSorteio> sorteios = regraSorteioRepository.listSorteios();

        modelAndView.addObject("sorteios", sorteios);

        return modelAndView;
    }

    @RequestMapping(value = "/listarContatoDetalhe/{page}")
    public @ResponseBody
    Map listarContatoDetalhe(@PathVariable(value = "page") Integer page, Integer idPessoa){
        Map<String, Object> resposta = new HashMap<String, Object>();

        List<DTOGENEmailPessoa> listEmails = new ArrayList<>();
        List<DTOGENTelefonePessoa> listTelefones = new ArrayList<>();

        if(idPessoa != null && idPessoa.toString() != ""){
            listEmails = genEmailPessoaRepository.listarEmailsPorGanhador(idPessoa);
            listTelefones = genTelefonePessoaRepository.listarTelefoneGanhador(idPessoa);
        }

        resposta.put("listEmails", listEmails);
        resposta.put("listTelefones", listTelefones);

        return resposta;
    }

    @RequestMapping(value = "/listarContatosRel")
    public ModelAndView listarContatosRel(@RequestParam(value = "selectSorteios") Integer selectSorteios){
        ModelAndView modelAndView = new ModelAndView("/contato-ganhadores/relatorioGanhadores");

        modelAndView.addObject("idSorteio",selectSorteios);

        return modelAndView;
    }

    @RequestMapping(value = "/listarContatosRelatorio/{page}")
    public @ResponseBody
    Map listarContatosRelatorio(@PathVariable(value = "page") Integer page, Integer idSorteio){
        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();
        Integer max = 40;
        Integer count = 0;

        List<DTOContatoGanhadores> listContatosRelatorioTemp = new ArrayList<>();
        List<DTOContatoGanhadores> listContatosRelatorio = new ArrayList<>();

        if(idSorteio != null && idSorteio.toString() != ""){
            listContatosRelatorioTemp = (List<DTOContatoGanhadores>) pessoaParticipanteRepository.listarRelatorioGanhadores(idSorteio);

            for(DTOContatoGanhadores contatoGanhadores : listContatosRelatorioTemp){
                List<DTOGENEmailPessoa> listEmails = new ArrayList<>();
                List<DTOGENTelefonePessoa> listTelefones = new ArrayList<>();

                if(contatoGanhadores.getIdPessoa() != null && contatoGanhadores.getIdPessoa().toString() != ""){
                    listEmails = genEmailPessoaRepository.listarEmailsPorGanhador(contatoGanhadores.getIdPessoa());
                    listTelefones = genTelefonePessoaRepository.listarTelefoneGanhador(contatoGanhadores.getIdPessoa());
                    contatoGanhadores.setListEmails(listEmails);
                    contatoGanhadores.setListTelefones(listTelefones);
                }

                listContatosRelatorio.add(contatoGanhadores);
            }
        }

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("listContatos", listContatosRelatorio);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping(value = "/listarContatos/{page}")
    public @ResponseBody
    Map listarContatos(@PathVariable(value = "page") Integer page, Integer idSorteio){
        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();
        Integer max = 40;
        Integer count = 0;

        List<DTOContatoGanhadores> listContatos = new ArrayList<>();

        if(idSorteio != null && idSorteio.toString() != ""){
            listContatos = (List<DTOContatoGanhadores>) pessoaParticipanteRepository.listarGanhadores(idSorteio, max, page);
            count = ((List<Integer>)pessoaParticipanteRepository.retornaCountGanhadores(idSorteio)).get(0);
        }

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("listContatos", listContatos);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("/viewContatoDetalhe")
    public ModelAndView viewContatoDetalhe(){
        ModelAndView modelAndView = new ModelAndView("/contato-ganhadores/viewContatoDetalhe");

        return modelAndView;
    }
}
