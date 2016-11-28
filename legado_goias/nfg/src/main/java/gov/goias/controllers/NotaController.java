package gov.goias.controllers;

import gov.goias.entidades.*;
import gov.goias.entidades.enums.StatusProcessamentoDocumentoFiscal;
import gov.goias.exceptions.NFGException;
import gov.goias.persistencia.historico.Historico;
import gov.goias.persistencia.historico.HistoricoNFG;
import gov.goias.services.NotaService;
import gov.goias.util.ValidacaoDeCpf;

import java.net.BindException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Remisson Silva on 30/09/2014.
 */
@Controller
@RequestMapping(value = {"/contador/nota", "/contribuinte/nota"})
//todo retirar mock
//@RequestMapping("/nota")
public class NotaController extends BaseController
{
    @Autowired
    CCEContribuinte cceContribuinteRepository;


    @Autowired
    DocumentoFiscalParticipante documentoFiscalParticipanteRepo;
    @Autowired

    DocumentoFiscalDigitado documentoFiscalDigitado;
    @Autowired
    PontuacaoDocumentoFiscalPessoa pontuacaoDocumentoFiscalPessoaRepo;
    @Autowired
    CCEContribuinte contrib;

    @Autowired
    NotaService notaService;

    @RequestMapping("/cadastro/{inscricao}")
    public ModelAndView cadastro(@PathVariable(value="inscricao")Integer inscricao)
    {

        //todo retirar mock
//        inscricao = 100038280;
        verificaCompatibilidadeInscricao(inscricao);

        ModelAndView modelAndView = new ModelAndView("/nota/cadastro");

        Map<Object,Object> tipoDocumento = new HashMap<Object,Object>();
        Map<Object,Object> serie = new HashMap<Object,Object>();

        tipoDocumento.put("Modelo 1 - 1/A",DocumentoFiscalDigitado.TIPO_NOTA_MODELO_1);
        tipoDocumento.put("Modelo 2 - (D-1)",DocumentoFiscalDigitado.TIPO_NOTA_MODELO_2);
        tipoDocumento.put("ECF - Antigo",DocumentoFiscalDigitado.TIPO_NOTA_ECF_ANTIGO);

        serie.put("D",DocumentoFiscalDigitado.SERIE_NOTA_D);
        serie.put("D - �nica",DocumentoFiscalDigitado.SERIE_NOTA_D_UNICA);
        serie.put("�nica",DocumentoFiscalDigitado.SERIE_NOTA_UNICA);

        String nomeFantasia = cceContribuinteRepository.getFodendoNomeFantasiaPelaInscricao(inscricao);
        String urlRetorno="";

        if(contadorLogado!=null)
            urlRetorno = "/nfg/contador/contribuintes/cadastro";
        if(empresaLogada!=null)
            urlRetorno = "/nfg/contribuinte/cadastro";

        modelAndView.addObject("tipoDocumento",tipoDocumento);
        modelAndView.addObject("serie",serie);
        modelAndView.addObject("inscricaoEstadual",inscricao);
        modelAndView.addObject("urlRetorno",urlRetorno);
        modelAndView.addObject("nomeFantasia",nomeFantasia);
        modelAndView.addObject("urlBase", getUrlBase());
        return modelAndView;
    }

    private String getUrlBase() {
        String urlBase = request.getRequestURI();
        urlBase = urlBase.substring(urlBase.indexOf("/nfg")+4,urlBase.length());
        urlBase = urlBase.replace("/cadastro", "");
        urlBase = urlBase.replaceAll("\\d*", "");
        return  urlBase;
    }



    public void verificaCompatibilidadeInscricao(Integer inscricao){
        boolean inscricaoCompativel = false;
        inscricaoCompativel |= cceContribuinteRepository.inscricaoCompativelContador(contadorLogado, inscricao);
        inscricaoCompativel |= cceContribuinteRepository.inscricaoCompativelContribuinte(empresaLogada, inscricao);
        String urlRedirect = getUrlBase().contains("/contador") ? "/contador/contribuintes/cadastro" : "/contribuinte/cadastro";
        if (!inscricaoCompativel) throw new NFGException("Nenhum usuário logado ou inscrição não condizente com nenhum usuário logado!", urlRedirect);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cadastrar")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map<String,Object> salvar( Integer inscricaoEstadual, Integer numeroDocumentoFiscal, Integer serieNotaFiscal, String subSerieNotaFiscal,Date dataEmissao,String cpf, Double valorTotal,Integer tipoDocumentoFiscal) throws Exception {
        notaService = new NotaService();
        Map<String,Object> resposta = new HashMap<String,Object>();

        //todo retirar mock
        if(contrib.get(inscricaoEstadual)==null)
            throw new NFGException("Inscrição Estadual Inválida!");
        if(dataEmissao!= null && dataEmissao.after(new Date()))
            throw new NFGException("Data de Emissão Inválida!");
        if(!new ValidacaoDeCpf(cpf).isCpfValido())
            throw new NFGException("CPF Inválido!");

        try {
            DocumentoFiscalDigitado ultimaNotaValida = notaService.getUltimaNotaValida(
                    inscricaoEstadual, numeroDocumentoFiscal,
                    serieNotaFiscal, subSerieNotaFiscal,
                    dataEmissao, tipoDocumentoFiscal
            );

            if(ultimaNotaValida == null) {
                CCEContribuinte contribuinte = new CCEContribuinte();
                contribuinte = contribuinte.get(inscricaoEstadual);

                notaService.cadastrarNota(
                        numeroDocumentoFiscal, serieNotaFiscal, subSerieNotaFiscal,
                        dataEmissao, cpf, valorTotal, tipoDocumentoFiscal, contribuinte,
                        new Date(), inscricaoEstadual
                );
                resposta.put("message","Nota Salva!");
                resposta.put("success",true);
            } else {
                if (existePontuacaoParaODocumento(ultimaNotaValida)){
                    resposta.put("success",false);
                    resposta.put("message","Este documento não pode ser alterado, pois já está vinculado à Pontuação NFG!!");
                }else{
                    notaService.atualizarNota(
                            ultimaNotaValida, numeroDocumentoFiscal, serieNotaFiscal,
                            subSerieNotaFiscal, dataEmissao, cpf, valorTotal, tipoDocumentoFiscal,inscricaoEstadual
                    );
                    resposta.put("message","Nota Atualizada!");
                    resposta.put("success", true);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();

            resposta.put("success",false);
            resposta.put("message","Erro ao salvar Nota!");
        }
        return resposta;
    }

    private boolean existePontuacaoParaODocumento(DocumentoFiscalDigitado ultimaNotaValida) {
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("idDocFiscalDigitado", ultimaNotaValida.getId().toString());
        List<DocumentoFiscalParticipante> docParticipanteList = documentoFiscalParticipanteRepo.list(dados, "id");
        if (docParticipanteList.size()>0){
            DocumentoFiscalParticipante docParticipante = docParticipanteList.get(0);
            return pontuacaoDocumentoFiscalPessoaRepo.existePontuacaoParaODocumento(docParticipante);
        }
        return false;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-layout-params")
    public @ResponseBody Map<String,Object> obterParametrosLayout(
            Integer inscricaoEstadual,Integer numeroDocumentoFiscal,
            Date dataEmissao,Integer serieNotaFiscal,
            String subSerieNotaFiscal,Integer tipoDocumentoFiscal
    ){
        Map<String,Object> resposta = new HashMap<String,Object>();
        notaService = new NotaService();

        DocumentoFiscalDigitado ultimaNotaNaoRemovida = notaService.getUltimaNotaValida(
                inscricaoEstadual, numeroDocumentoFiscal,
                serieNotaFiscal, subSerieNotaFiscal,
                dataEmissao, tipoDocumentoFiscal
        );

        resposta.put("salvar",false);

        //todo retirar mock
        if (contrib.get(inscricaoEstadual)==null) {
            resposta.put("error",true);
            resposta.put("errorMessage","Inscrição Estadual Inválida!");
        }

        if (ultimaNotaNaoRemovida == null) {
            resposta.put("valorBotao", "Inserir");
            resposta.put("salvar", true);
            resposta.put("excluir",false);
            resposta.put("nota",false);
        } else {
            resposta.put("valorBotao","Alterar");
            resposta.put("salvar",true);
            resposta.put("excluir",true);

            resposta.put("nota",true);
            resposta.put("valorNota",ultimaNotaNaoRemovida.getId());

            resposta.put("cpf",true);
            resposta.put("valorCpf",ultimaNotaNaoRemovida.getCpf());

            resposta.put("valorTotal",true);
            resposta.put("paramValorTotal",ultimaNotaNaoRemovida.getValorTotal());
        }

        return resposta;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/excluir-nota")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map<String,Object> excluirNota(Integer idDocumentoFiscalDigital) throws Exception {
        Map<String,Object> resposta = new HashMap<String,Object>();
        DocumentoFiscalDigitado documento = documentoFiscalDigitado.get(idDocumentoFiscalDigital);

        try {
            if (existePontuacaoParaODocumento(documento)){
                resposta.put("success", false);
                resposta.put("message", "Este documento não pode ser excluído, pois já está vinculado à Pontuação NFG!");
            }else{
                HashMap<String, Object> dados = new HashMap<>();
                dados.put("idDocFiscalDigitado", documento.getId().toString());
                List<DocumentoFiscalParticipante> docParticipanteList = documentoFiscalParticipanteRepo.list(dados, "id");
                if (docParticipanteList.size()>0){
                    DocumentoFiscalParticipante docParticipante = docParticipanteList.get(0);
                    docParticipante.setStatusProcessamento(StatusProcessamentoDocumentoFiscal.CANCELADO.getValue());
                    docParticipante.update();
                }

                documento.setDataCancelDocumentoFiscal(new Date());
                documento.update();

                resposta.put("success", true);
                resposta.put("message", "Removido!");
            }
        } catch (Exception e) {
            e.printStackTrace();

            resposta.put("success", false);
            resposta.put("message", "N�o foi poss�vel remover esta nota!");
        }
        return resposta;
    }

    @RequestMapping(value = "/buscar/{page}")
    public @ResponseBody Map<String, Object> buscarNotas(@PathVariable(value="page") Integer page,Integer ieFiltro, String cpfFiltro,Integer nrDocFiltro, String dataEmissaoFiltro, BindException bind) {
    	Integer max = 10;
        String dataEmissao = dataEmissaoFiltro != null ? dataEmissaoFiltro : null;

        List<DocumentoFiscalDigitado> ultimasNotasInseridas =
                documentoFiscalDigitado.list(ieFiltro,null,nrDocFiltro, dataEmissao,null,null,cpfFiltro,page * max, max,true);


        Long notasInseridas =
                documentoFiscalDigitado.countUltimosInseridos(ieFiltro,nrDocFiltro,dataEmissao,cpfFiltro);

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total",  notasInseridas.intValue());
        pagination.put("page", ++page);
        pagination.put("max", max);


        resposta.put("ultimasNotasInseridas",ultimasNotasInseridas);
        resposta.put("pagination", pagination);


        return resposta;
    }
}
