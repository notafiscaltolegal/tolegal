package gov.goias.controllers;

import gov.goias.dtos.DTOEmpresaParticipante;
import gov.goias.entidades.CnaeAutorizado;
import gov.goias.entidades.EmpresaParticipante;
import gov.goias.entidades.GENMunicipio;
import gov.goias.exceptions.NFGException;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author henrique-rh
 * @since 13/08/2014
 */
@Controller
@RequestMapping(value = {"/busca", "/portal/busca"})
public class BuscaController extends BaseController {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    EmpresaParticipante empresaParticipanteRepository;

    @Autowired
    GENMunicipio genMunicipioRepository;

    @Autowired
    CnaeAutorizado cnaeAutorizadoRepository;

    public static Boolean emManutencao = false;

    @RequestMapping("empresas")
    public ModelAndView viewEmpresas() {
        if (emManutencao) {
            throw new NFGException("", new ModelAndView("/cidadao/manutencao"));
        }
        ModelAndView modelAndView = new ModelAndView("busca/empresasParticipantes");
        List<CnaeAutorizado> listaCnaesAutorizados = cnaeAutorizadoRepository.listCnaesAutorizadosNaoExcluidos();
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("uf", "GO");
        List<GENMunicipio> listaMunicipios = genMunicipioRepository.list(dados,"nome");
        modelAndView.addObject("listaCnaesAutorizados",listaCnaesAutorizados);
        modelAndView.addObject("listaMunicipios",listaMunicipios);
        return modelAndView;
    }

    @RequestMapping("empresas/{page}")
    public @ResponseBody Map empresas(@PathVariable(value="page") Integer page, Long idSubClasseCnae, String nome, String cnpj, String codgMunicipio, String nomeBairro) {
        if (emManutencao) {
            throw new NFGException("", new ModelAndView("/cidadao/manutencao"));
        }
        Integer max = 10;
        List<DTOEmpresaParticipante> empresasParticipante = empresaParticipanteRepository.listEmpresasParticipantes(page , max, idSubClasseCnae, nome, cnpj, codgMunicipio, nomeBairro);
        Long count = empresaParticipanteRepository.countEmpresasParticipantes(idSubClasseCnae, nome, cnpj, codgMunicipio);
        Map<String, Object> retorno = new HashMap<String, Object>();
        retorno.put("empresas", empresasParticipante);
        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);
        retorno.put("pagination", pagination);
        return retorno;
    }

    @RequestMapping("empresa/{numrInscricao}")
    public ModelAndView viewEmpresa(@PathVariable("numrInscricao") String numrInscricao) {
        ModelAndView modelAndView = new ModelAndView("busca/detalheEmpresaParticipante");
        modelAndView.addObject("endereco","Av Pinheiro Chagas, 856, Jundiaí, Anápolis, Goiás");

        return modelAndView;
    }
}
