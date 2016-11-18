package gov.goias.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import gov.goias.dtos.DTODescadastramentoUsuario;
import gov.goias.dtos.DTOValidacaoTrocaCnae;

/**
 * Created by diogo-rs on 7/23/2014.
 */
@Controller
@RequestMapping("/sefaz")
public class WebserviceCadastro {

    private static Logger logger = Logger.getLogger(WebserviceCadastro.class);

    @RequestMapping("/valida-troca-de-cnae/{inscricao}/{cnae}")
    @ModelAttribute("validacao")
    public DTOValidacaoTrocaCnae cnaeParticipanteDoNFG(@PathVariable Integer inscricao, @PathVariable String cnae){
       return new DTOValidacaoTrocaCnae("teste");
    }

    @RequestMapping("/remover-cadastro/{inscricao}")
    @ModelAttribute("descadastramento")
    public DTODescadastramentoUsuario removerCadastro(@PathVariable Integer inscricao){
        return new DTODescadastramentoUsuario("teste");
    }
}
