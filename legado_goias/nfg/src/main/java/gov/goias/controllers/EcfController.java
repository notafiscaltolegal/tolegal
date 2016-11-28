package gov.goias.controllers;

import gov.goias.persistencia.CCEContribuinteRepository;
import gov.goias.persistencia.EcfRepository;
import gov.goias.util.FileManagement;
import gov.goias.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Letícia Álvares on 31/05/2016.
 */
@Controller
@RequestMapping(value = {"/contribuinte/ecf"})
//todo retirar mock
//@RequestMapping("/nota")
public class EcfController extends BaseController
{
    @Autowired
    private EcfRepository ecfRep;

    @Autowired
    CCEContribuinteRepository cceContribuinteRepository;

    private static final Logger logger = Logger.getLogger(EcfController.class);

    @RequestMapping("/importar/{inscricao}")
    public ModelAndView cadastro(@PathVariable(value="inscricao")Integer inscricao)
    {

        //todo retirar mock
//        inscricao = 100038280;

        ModelAndView modelAndView = new ModelAndView("/ecf/importar");

        String nomeFantasia = cceContribuinteRepository.getFodendoNomeFantasiaPelaInscricao(inscricao);
        String urlRetorno="";

        urlRetorno = "/nfg/ecf/importar";

        modelAndView.addObject("inscricaoEstadual",inscricao);
        modelAndView.addObject("urlRetorno",urlRetorno);
        modelAndView.addObject("nomeFantasia",nomeFantasia);
        modelAndView.addObject("urlBase", getUrlBase());

        if (ecfRep.isRetificadora())
            modelAndView.addObject("retificadora", "Retificadora");
        else modelAndView.addObject("retificadora", "Original");

        return modelAndView;
    }

    private String getUrlBase() {
        String urlBase = request.getRequestURI();
        urlBase = urlBase.substring(urlBase.indexOf("/nfg") + 4, urlBase.length());
        urlBase = urlBase.replace("/cadastro", "");
        urlBase = urlBase.replaceAll("\\d*", "");
        return  urlBase;
    }



    @RequestMapping(value = "/upload/{inscricao}", method = RequestMethod.POST)
    public @ResponseBody Map uploadTxtResultado(@RequestParam("file") MultipartFile file,@PathVariable(value = "inscricao") Integer inscricao) {
        HashMap<String, Object> resposta = new HashMap<>();

        ecfRep.setIsRetificadora(false);

        if (!file.isEmpty()){

            //Para contagem do tempo
            long inicio = System.currentTimeMillis();

            //Abrindo o arquivo
            FileManagement fMng = new FileManagement();
            fMng.salvarArquivo(file);
            ecfRep.setCaminhoCompleto(fMng.unzip(fMng.getCaminhoArquivosECF() + '/' + file.getOriginalFilename(), fMng.getCaminhoArquivosECF()));

            //Variáveis necessárias para validação
            ArrayList<String> linhasArquivo = new ArrayList<String>();
            ArrayList<String> resultadoValidacaoLayout = new ArrayList<String>();
            ArrayList<String> resultadoRejeita = new ArrayList<String>();
            ArrayList<String> resultadoAlerta = new ArrayList<String>();
            TextUtils tu = new TextUtils();

            //Validação
            ecfRep.setInscricao(inscricao);
            linhasArquivo = ecfRep.leArquivoParaArray(ecfRep.getCaminhoCompleto());
            resultadoValidacaoLayout = ecfRep.validarLayoutArquivoECF(linhasArquivo);

            if (resultadoValidacaoLayout.size() !=0){
                resposta.put("mensagensErro", tu.transformaArrayStringEmMap(resultadoValidacaoLayout));
                return resposta;
            }

            if (!ecfRep.bTodosSemCPFCNPJ){
                resultadoAlerta.add("Não consta nenhum CPF/CNPJ nos documentos fiscais informados no arquivo.");
            }

            if (!ecfRep.isbTemCupom()){
                resultadoAlerta.add("Não consta nenhum cupom fiscal no arquivo. Arquivo SEM MOVIMENTO?");
            }

            resultadoRejeita = ecfRep.validaEstruturaRejeitaECF(linhasArquivo);

            //mock pra passar sem erros
            //resultadoRejeita = new ArrayList<String>();

            if (resultadoRejeita.size() > 0 ){
                resposta.put("mensagensErro", tu.transformaArrayStringEmMap(resultadoRejeita));
                File arq = new File(ecfRep.getCaminhoCompleto());
                arq.delete();
                arq = new File(fMng.getCaminhoArquivosECF() + '/' + file.getOriginalFilename());
                arq.delete();
                return resposta;
            }

            resultadoAlerta = ecfRep.validaEstruturaAlertaECF(linhasArquivo);
            if (resultadoAlerta.size() > 0 ){
                resposta.put("mensagensAlerta", tu.transformaArrayStringEmMap(resultadoAlerta));
            }

            resposta.put("retificadora", ecfRep.isRetificadora());

            ecfRep.gravarFinalizacao();

            //Finalizando a contagem do tempo
            long fim  = System.currentTimeMillis();
            long result = fim - inicio;
            long segundos = ( result / 1000 ) % 60;      // se não precisar de segundos, basta remover esta linha.
            long minutos  = ( result / 60000 ) % 60;     // 60000   = 60 * 1000
            long horas    = result / 3600000;            // 3600000 = 60 * 60 * 1000
            String duracaoExecucao = String.format ("%02d:%02d:%02d", horas, minutos, segundos);
            logger.info(duracaoExecucao);

            return resposta;

        }else{
            resposta.put("error","Não foi possível localizar o arquivo.");
        }
        return resposta;
    }

    @RequestMapping(value = "/finalizaUpload/{inscricao}", method = RequestMethod.POST)
    public @ResponseBody Map finalizaUpload(@RequestParam("file") MultipartFile file,@PathVariable(value = "inscricao") Integer inscricao) {
        HashMap<String, Object> resposta = new HashMap<>();

        ecfRep.finalizar(ecfRep.getCaminhoCompleto());

        return resposta;
    }


}
