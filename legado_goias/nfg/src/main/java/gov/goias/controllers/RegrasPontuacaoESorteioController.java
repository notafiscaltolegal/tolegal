package gov.goias.controllers;

import gov.goias.entidades.*;
import gov.goias.persistencia.historico.HistoricoNFG;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * Created by lucas-mp on 10/10/14.
 */
@Controller
@RequestMapping("/regras") //teste
public class RegrasPontuacaoESorteioController
        extends BaseController
{

    @Autowired
    PessoaParticipante pessoaParticipanteRepository;

    @Autowired
    RegraSorteio regraSorteioRepository;
    private Logger logger = Logger.getLogger(RegrasPontuacaoESorteioController.class);

    @RequestMapping(value = "/pontuacao/bonus/put")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody String putRegraPontuacaoBonus(@RequestBody Map<String,String> regraPontuacaoExtraMap) {

        String descricao = regraPontuacaoExtraMap.get("descricao");
        try{
            logger.info("Put Regra pontuacao extra descricao " + descricao);
            Integer qtdePontos = regraPontuacaoExtraMap.get("qtdePontos") != null ? Integer.parseInt(regraPontuacaoExtraMap.get("qtdePontos")) : null;
            Date dIni = regraPontuacaoExtraMap.get("dataInicio") != null ? new Date(Long.parseLong(regraPontuacaoExtraMap.get("dataInicio"))) : null;
            Date dFin = regraPontuacaoExtraMap.get("dataFinal") != null ? new Date(Long.parseLong(regraPontuacaoExtraMap.get("dataFinal"))) : null;

            if(regraSorteioRepository.adicionaRegraPontuacaoExtra(descricao,qtdePontos,dIni,dFin)){
                logger.info("Regra pontuacao extra sucesso descricao "+descricao);
                return "Registro devidamente gravado";
            }else{
                logger.error("Regra pontuacao extra falha em adicionaRegraPontuacaoExtra descricao " + descricao);
                return "Falha ao gravar registro";
            }
        }catch (Exception e){
            logger.error("Regra pontuacao extra falha descricao " + descricao + " Error: " + e.getMessage());
            return "Falha ao gravar registro";
        }
    }

    @RequestMapping(value = "/pontuacao/notas/put")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody String putRegraPontuacaoNotas(@RequestBody Map<String,String> regraPontuacaoNotasMap) {

        try{
            Integer numrMaximoPontoDocumento = regraPontuacaoNotasMap.get("numrMaximoPontoDocumento") != null ? Integer.parseInt(regraPontuacaoNotasMap.get("numrMaximoPontoDocumento")) : null;
            Date dIni = regraPontuacaoNotasMap.get("dataInicio") != null ? new Date(Long.parseLong(regraPontuacaoNotasMap.get("dataInicio"))) : null;
            Date dFin = regraPontuacaoNotasMap.get("dataFinal") != null ? new Date(Long.parseLong(regraPontuacaoNotasMap.get("dataFinal"))) : null;
            Integer numrMaximoEstabRef = regraPontuacaoNotasMap.get("numrMaximoEstabRef") != null ? Integer.parseInt(regraPontuacaoNotasMap.get("numrMaximoEstabRef")) : null;
            Integer numrMaximoDocRef = regraPontuacaoNotasMap.get("numrMaximoDocRef") != null ? Integer.parseInt(regraPontuacaoNotasMap.get("numrMaximoDocRef")) : null;
            Integer numrMaximoPontoRef = regraPontuacaoNotasMap.get("numrMaximoPontoRef") != null ? Integer.parseInt(regraPontuacaoNotasMap.get("numrMaximoPontoRef")) : null;
            Double valrFatorConversao = regraPontuacaoNotasMap.get("valrFatorConversao") != null ? Double.parseDouble(regraPontuacaoNotasMap.get("valrFatorConversao")) :null;

            if(regraSorteioRepository.adicionaRegraPontuacaoNotas(numrMaximoDocRef, numrMaximoEstabRef, numrMaximoPontoDocumento, numrMaximoPontoRef, valrFatorConversao, dIni, dFin)){
                return "Registro devidamente gravado";
            }else{
                return "Falha ao gravar registro";
            }
        }catch (Exception e){
            return "Falha ao gravar registro";
        }
    }

    @RequestMapping(value = "/sorteio/put")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody String putRegraSorteio(@RequestBody Map<String,String> regraSorteioMap) {
        try{
            String infoSorteio = regraSorteioMap.get("infoSorteio");
            Date dataRealizacao = regraSorteioMap.get("dataRealizacao") != null ? new Date(Long.parseLong(regraSorteioMap.get("dataRealizacao"))) : null;
            Integer statProcesmSorteio = regraSorteioMap.get("statProcesmSorteio") != null ? Integer.parseInt(regraSorteioMap.get("statProcesmSorteio")) : null;
            Date dataCadastro = regraSorteioMap.get("dataCadastro") != null ? new Date(Long.parseLong(regraSorteioMap.get("dataCadastro"))) : null;
            Integer tipoSorteio = regraSorteioMap.get("tipoSorteio") != null ? Integer.parseInt(regraSorteioMap.get("tipoSorteio")) : null;
            Double numrConversaoPontoBilhete = regraSorteioMap.get("numrConversaoPontoBilhete") != null ? Double.parseDouble(regraSorteioMap.get("numrConversaoPontoBilhete")) :null;
            Character indiDivulgaSite = regraSorteioMap.get("indiDivulgaSite") != null ?  regraSorteioMap.get("indiDivulgaSite").charAt(0) : null;
            Date dataExtracaoLoteriaFederal = regraSorteioMap.get("dataExtracaoLoteriaFederal") != null ? new Date(Long.parseLong(regraSorteioMap.get("dataExtracaoLoteriaFederal"))) : null;
            Integer numrExtracaoLoteriaFederal = regraSorteioMap.get("numrExtracaoLoteriaFederal") != null ? Integer.parseInt(regraSorteioMap.get("numrExtracaoLoteriaFederal")) : null;
            Character indiSorteioRealizado = regraSorteioMap.get("indiSorteioRealizado") != null ?  regraSorteioMap.get("indiSorteioRealizado").charAt(0) : null;

            if(regraSorteioRepository.adicionaRegraSorteio(infoSorteio,dataRealizacao,statProcesmSorteio,dataCadastro,tipoSorteio,numrConversaoPontoBilhete,indiDivulgaSite,dataExtracaoLoteriaFederal,numrExtracaoLoteriaFederal,indiSorteioRealizado)){
                return "Registro devidamente gravado";
            }else{
                return "Falha ao gravar registro";
            }
        }catch (Exception e){
            return "Falha ao gravar registro";
        }
    }

}









