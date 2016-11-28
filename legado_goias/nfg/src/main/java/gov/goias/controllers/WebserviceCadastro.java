package gov.goias.controllers;

import gov.goias.dtos.DTODescadastramentoUsuario;
import gov.goias.dtos.DTOValidacaoTrocaCnae;
import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.CnaeAutorizado;
import gov.goias.entidades.EmpresaParticipante;
import gov.goias.services.ContribuinteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by diogo-rs on 7/23/2014.
 */
@Controller
@RequestMapping("/sefaz")
public class WebserviceCadastro {

    private static Logger logger = Logger.getLogger(WebserviceCadastro.class);

    @Autowired
    private CCEContribuinte cceContribuinte;

    @Autowired
    private ContribuinteService contribuinteService;

    @Autowired
    private CnaeAutorizado cnaeAutorizado;

    @RequestMapping("/valida-troca-de-cnae/{inscricao}/{cnae}")
    @ModelAttribute("validacao")
    public DTOValidacaoTrocaCnae cnaeParticipanteDoNFG(@PathVariable Integer inscricao, @PathVariable String cnae){
        try{
            Boolean contribuinteEhParticipante = cceContribuinte.isParticipante(inscricao);
            Boolean cnaeEstaAutorizado = cnaeAutorizado.isAutorizado(cnae);
            return new DTOValidacaoTrocaCnae(contribuinteEhParticipante, cnaeEstaAutorizado);
        } catch (Exception e){
            logger.error("Erro ao consultar novo cnae(valida-troca-de-cnae)", e);
            return new DTOValidacaoTrocaCnae("Erro ao consultar cnae");
        }
    }

    @RequestMapping("/remover-cadastro/{inscricao}")
    @ModelAttribute("descadastramento")
    public DTODescadastramentoUsuario removerCadastro(@PathVariable Integer inscricao){
        try {
            CCEContribuinte contribuinte = cceContribuinte.get(inscricao);
            contribuinteService.descadastrarContribuinteDoNFG(contribuinte, "");
            return new DTODescadastramentoUsuario(true);
        }catch(Exception e){
            logger.error("Erro ao descadastrar contribuinte(remover-cadastro)", e);
            return new DTODescadastramentoUsuario("Erro ao descadastrar usuario");
        }
    }
}
