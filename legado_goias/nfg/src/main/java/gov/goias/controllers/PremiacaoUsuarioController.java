package gov.goias.controllers;

import gov.goias.dtos.DTOPremiacao;
import gov.goias.entidades.*;
import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.persistencia.CCEContribuinteRepository;
import gov.goias.persistencia.PremioBilheteRepository;
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
import java.util.*;

/**
 * Created by bruno-cff on 13/11/2015.
 */
@Controller
@RequestMapping("/portal/premiacao/usuario")
//@RequestMapping("/premiacao/usuario")
public class PremiacaoUsuarioController extends BaseController{

    private static final Logger logger = Logger.getLogger(PremiacaoUsuarioController.class);

    @Autowired
    SimpleDateFormat simpleDateFormat;


    @Autowired
    RegraSorteio regraSorteioRepository;


    @Autowired
    PremioBilhete premioBilheteRepo;

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("premiacao/index");
        List<RegraSorteio> sorteios = regraSorteioRepository.listSorteios();
        modelAndView.addObject("sorteios", sorteios);
        return modelAndView;
    }



//    @RequestMapping("/gerarRelatorio")
//    public ModelAndView gerarRelatorio(
//            @RequestParam(value="data") String data,
//            @RequestParam(value="dataFim") String dataFim,
//                                       @RequestParam(value="situacao") Integer situacao,
//                                       @RequestParam(value="motivoReclamacao") Integer motivoReclamacao,
//                                       @RequestParam(value="tipoDocFiscalReclamacao") Integer tipoDocFiscalReclamacao,
//                                       @RequestParam(value="recNoPrazo") Integer recNoPrazo) {
//        ModelAndView modelAndView = new ModelAndView("reclamacao/relatorio");
//        modelAndView.addObject("situacao",situacao);
//        modelAndView.addObject("data",data);
//        modelAndView.addObject("dataFim",dataFim);
//        modelAndView.addObject("motivoReclamacao",motivoReclamacao);
//        modelAndView.addObject("tipoDocFiscalReclamacao",tipoDocFiscalReclamacao);
//        modelAndView.addObject("recNoPrazo",recNoPrazo);
//        return modelAndView;
//    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        //o parametro true faz com que o binder gere data nula para string vazia do js
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("/listarPremiacoes/{page}")
    public @ResponseBody Map listarReclamacoesRel(@PathVariable(value = "page") Integer page, Integer sorteio) {
        Integer max = 20;


        Map premiacoesPaginate = premioBilheteRepo.findPremiacoesDoSorteio(max,page,sorteio);
        List<DTOPremiacao> premiacoes = (List<DTOPremiacao>) premiacoesPaginate.get("list");
        Integer count = (Integer) premiacoesPaginate.get("count");

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        resposta.put("premiacoes", premiacoes);
        resposta.put("pagination", pagination);
        return resposta;
    }

    @RequestMapping("/editarPremiacao")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map editarPremiacao(String dataResgate, String infoResgate, Integer idPremioBilhete) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        HashMap<String, Object> dados = new HashMap<>();
        dados.put("id", idPremioBilhete);
        List<PremioBilhete> premioList =  premioBilheteRepo.list(dados, "id");

        PremioBilhete premioBilhete = null;

        if (premioList.size()>0){
            premioBilhete = premioList.get(0);
        }

        if (premioBilhete!=null){

            if (dataResgate!=null && !dataResgate.isEmpty()){
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    premioBilhete.setDataResgate(sdf.parse(dataResgate));
                } catch (ParseException e) {
                    resposta.put("erro","Data inválida! "+dataResgate);
                    logger.error("erro: Data inválida! " + dataResgate);
                }
            }

            if (infoResgate!=null && !infoResgate.isEmpty()){
                premioBilhete.setInfoResgate(infoResgate);
            }

            premioBilhete.save();
            resposta.put("sucesso",true);
        }else{
            resposta.put("erro","Premio bilhete nao encontrado pra este ID "+idPremioBilhete);
            logger.error("erro: Premio bilhete nao encontrado pra este ID "+idPremioBilhete);

        }


        return resposta;
    }


}
