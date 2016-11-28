package gov.goias.controllers;

import gov.goias.dtos.DTOCnaeCadastro;
import gov.goias.entidades.*;
import gov.goias.exceptions.NFGException;
import gov.goias.persistencia.historico.Historico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by thiago-mb on 18/07/2014.
 */
@Controller
@RequestMapping("/portal/cnae")
public class CadastroCnaeController extends BaseController {

    @Autowired
    private CCESecaoCnae cceSecaoCnae;
    @Autowired
    private CCEDivisaoCnae cceDivisaoCnae;
    @Autowired
    private CCEGrupoCnae cceGrupoCnae;
    @Autowired
    private CCEClasseCnae cceClasseCnae;
    @Autowired
    private CCESubClasseCnae cceSubClasseCnae;
    @Autowired
    private CnaeAutorizado cnaeAutorizado;


    @RequestMapping(value = "/cadastro")
    public  ModelAndView listSecao(HttpServletRequest request){
        //request.setAttribute("listaNfgCnaeAutorizado", cceCnaeFiscal.listCnaesAutorizadosNaoExcluidos());
        return new ModelAndView("/cadastro-cnae");
    }

    @RequestMapping(value = "/list-cnaes-autorizados")
    public @ResponseBody Map listCnaes(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        String startParam = request.getParameter("start");
        Integer start = StringUtils.isEmpty(startParam) ? 0 : Integer.parseInt(startParam);
        params.put("start", start);

        String maxParam = request.getParameter("max");
        Integer max = StringUtils.isEmpty(maxParam) ? 10 : Integer.parseInt(maxParam);
        params.put("max", max);

        String query = request.getParameter("query");

        List<CnaeAutorizado> cnaeAutorizados = null;
        Long countCnaesAutorizadosNaoExcluidos = null;
        if(StringUtils.isEmpty(query)) {
            cnaeAutorizados = cnaeAutorizado.listCnaesAutorizadosNaoExcluidos(params);
            countCnaesAutorizadosNaoExcluidos = cnaeAutorizado.countCnaesAutorizadosNaoExcluidos();
        } else{
            cnaeAutorizados = cnaeAutorizado.listCnaesAutorizadosNaoExcluidos(query, params);
            countCnaesAutorizadosNaoExcluidos = cnaeAutorizado.countCnaesAutorizadosNaoExcluidos(query);
        }

        Collections.sort(cnaeAutorizados, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CnaeAutorizado cnaeAutorizado1 = (CnaeAutorizado) o1;
                CnaeAutorizado cnaeAutorizado2 = (CnaeAutorizado) o2;
                return cnaeAutorizado1.getSubClasseCnae().getCodSubClasseCnae().compareTo(cnaeAutorizado2.getSubClasseCnae().getCodSubClasseCnae());
            }
        });

        Map resposta = new HashMap();
        resposta.put("list", cnaeAutorizados);
        resposta.put("total", countCnaesAutorizadosNaoExcluidos);
        resposta.put("start", params.get("start"));
        resposta.put("max", params.get("max"));

        return resposta;
    }

    @RequestMapping(value = "/list-secao")
    public @ResponseBody Map listSecao(){
        List listaSecao = cceSecaoCnae.list();
        Collections.sort(listaSecao, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CCESecaoCnae cceSecaoCnae1 = (CCESecaoCnae) o1;
                CCESecaoCnae cceSecaoCnae2 = (CCESecaoCnae) o2;
                return cceSecaoCnae1.getCodSecaoCnae().compareTo(cceSecaoCnae2.getCodSecaoCnae());
            }
        });
        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("listaSecao", listaSecao);
        return resposta;
    }

    @RequestMapping(value = "/list-divisao")
    public @ResponseBody Map listDivisao(String idSecao){
        List<CCEDivisaoCnae> listaDivisao = cceDivisaoCnae.findCodDivisaoByIdSecao(Integer.valueOf(idSecao));
        Collections.sort(listaDivisao, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CCEDivisaoCnae cceDivisaoCnae1 = (CCEDivisaoCnae) o1;
                CCEDivisaoCnae cceDivisaoCnae2 = (CCEDivisaoCnae) o2;
                return cceDivisaoCnae1.getCodDivisaoCnae().compareTo(cceDivisaoCnae2.getCodDivisaoCnae());
            }
        });
        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("listaDivisao", listaDivisao);
        return resposta;
    }

    @RequestMapping(value = "/list-grupo")
    public @ResponseBody Map listGrupo(String idDivisao){
        List<CCEGrupoCnae> listaGrupo = cceGrupoCnae.findCodGrupoByIdDivisao(Integer.valueOf(idDivisao));
        Collections.sort(listaGrupo, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CCEGrupoCnae cceGrupoCnae1 = (CCEGrupoCnae) o1;
                CCEGrupoCnae cceGrupoCnae2 = (CCEGrupoCnae) o2;
                return cceGrupoCnae1.getCodGrupoCnae().compareTo(cceGrupoCnae2.getCodGrupoCnae());
            }
        });
        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("listaGrupo", listaGrupo);
        return resposta;
    }
    @RequestMapping(value = "/list-classe")
    public @ResponseBody Map listClasse(String idGrupo){
        List<CCEClasseCnae> listaClasse = new ArrayList<CCEClasseCnae>();
        String[] listaIdGrupoString = idGrupo.split(";");
        //
        for (String idGrupoString : listaIdGrupoString){
            listaClasse.addAll(cceClasseCnae.findCodClasseByIdGrupo(Integer.valueOf(Integer.parseInt(idGrupoString))));
        }

        Collections.sort(listaClasse, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CCEClasseCnae cceClasseCnae1 = (CCEClasseCnae) o1;
                CCEClasseCnae cceClasseCnae2 = (CCEClasseCnae) o2;
                return cceClasseCnae1.getCodClasseCnae().compareTo(cceClasseCnae2.getCodClasseCnae());
            }
        });

        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("listaClasse", listaClasse);
        return resposta;
    }
    @RequestMapping(value = "/list-subclasse")
    public @ResponseBody Map listSubClasse(String idClasse){
        List<CCESubClasseCnae> listaSubClasse = new ArrayList<CCESubClasseCnae>();
        String[] listaIdClasseString = idClasse.split(";");
        for (String idClasseString : listaIdClasseString ) {
            listaSubClasse.addAll(cceSubClasseCnae.findCodSubClasseByIdClasse(Integer.valueOf(idClasseString)));
        }

        Collections.sort(listaSubClasse, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CCESubClasseCnae cceSubClasseCnae1 = (CCESubClasseCnae) o1;
                CCESubClasseCnae cceSubClasseCnae2 = (CCESubClasseCnae) o2;
                return cceSubClasseCnae1.getCodSubClasseCnae().compareTo(cceSubClasseCnae2.getCodSubClasseCnae());
            }
        });

        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("listaSubClasse", listaSubClasse);
        return resposta;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cadastrar-cnae")
    @Historico
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ModelAndView cadastrarCnae(DTOCnaeCadastro dtoCnaeCadastro, HttpServletRequest request) throws Exception {

        try {
            cnaeAutorizado.inserir(dtoCnaeCadastro);
            ModelAndView modelAndView = new ModelAndView("/cadastro-cnae");
            modelAndView.addObject("message", "CNAE incluído");
            return modelAndView;
        } catch(Exception e){
            throw new NFGException("Erro ao salvar.", new ModelAndView("/cadastro-cnae"));
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/excluir-cnae")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Historico
    public @ResponseBody Map excluirCnae(Integer idCnaeAutorizadoDel, HttpServletRequest request) throws Exception {
        CnaeAutorizado cnaeFiscal = new CnaeAutorizado();
        //buscando no banco o id informado para setar sua data de exclusão(exclusão lógica)
        cnaeFiscal = cnaeFiscal.get(idCnaeAutorizadoDel);
        cnaeFiscal.setDataExclusaoCnae(new Date());
        cnaeFiscal.update();

        Map resposta = new HashMap();

        resposta.put("success", true);
        return resposta;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Historico
    public @ResponseBody Map update(Integer cnae, String data, HttpServletRequest request) throws Exception {
        Map resposta = new HashMap();

        try{
            CnaeAutorizado cnaeFiscal = cnaeAutorizado.get(cnae);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            cnaeFiscal.setDataObrigatoriedade(format.parse(data));
            cnaeFiscal.update();

            resposta.put("success", true);
        } catch(Exception e){
            e.printStackTrace();
            resposta.put("success", false);
        }

        return resposta;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ha-contribuintes-cadastrados")
    public @ResponseBody Map validarNovaData(Long cnae, HttpServletRequest request) throws Exception {
        Map resposta = new HashMap();

        Boolean jaPossuiContribuintes = cnaeAutorizado.possuiContribuintesCadastrados(cnae);
        resposta.put("success", true);
        resposta.put("possuiContribuintes", jaPossuiContribuintes);

        return resposta;
    }
}
