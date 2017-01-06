package gov.goias.controllers;

import java.net.BindException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.DocumentoFiscalDigitado;
import gov.goias.entidades.DocumentoFiscalParticipante;
import gov.goias.entidades.enums.StatusProcessamentoDocumentoFiscal;
import gov.goias.exceptions.NFGException;
import gov.goias.service.EmpresaService;
import gov.goias.service.NotaService;
import gov.goias.util.ValidacaoDeCpf;
import gov.to.goias.DocumentoFiscalDigitadoToLegal;
import gov.to.service.NotaEmpresaService;

@Controller
@RequestMapping(value = { "/contador/nota", "/contribuinte/nota" })
public class NotaController extends BaseController {
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private NotaService notaService;
	
	@Autowired
	private NotaEmpresaService notaEmpresaService;
	
	@RequestMapping("/cadastro/{inscricao}")
	public ModelAndView cadastro(@PathVariable(value = "inscricao") Integer inscricao) {
		verificaCompatibilidadeInscricao(inscricao);

		ModelAndView modelAndView = new ModelAndView("/nota/cadastro");

		Map<Object, Object> tipoDocumento = new HashMap<Object, Object>();
		Map<Object, Object> serie = new HashMap<Object, Object>();

		tipoDocumento.put("Modelo 1 - 1/A", DocumentoFiscalDigitado.TIPO_NOTA_MODELO_1);
		tipoDocumento.put("Modelo 2 - (D-1)", DocumentoFiscalDigitado.TIPO_NOTA_MODELO_2);
		tipoDocumento.put("ECF - Antigo", DocumentoFiscalDigitado.TIPO_NOTA_ECF_ANTIGO);

		serie.put("D", DocumentoFiscalDigitado.SERIE_NOTA_D);
		serie.put("D - Única", DocumentoFiscalDigitado.SERIE_NOTA_D_UNICA);
		serie.put("Única", DocumentoFiscalDigitado.SERIE_NOTA_UNICA);

		String nomeFantasia = empresaService.nomeFantasiaPelaInscricao(inscricao);
		String urlRetorno = "";

		if (getContadorLogado() != null)
			urlRetorno = "/to-legal/contador/contribuintes/cadastro";
		if (getEmpresaLogada() != null)
			urlRetorno = "/to-legal/contribuinte/cadastro";

		modelAndView.addObject("tipoDocumento", tipoDocumento);
		modelAndView.addObject("serie", serie);
		modelAndView.addObject("inscricaoEstadual", inscricao);
		modelAndView.addObject("urlRetorno", urlRetorno);
		modelAndView.addObject("nomeFantasia", nomeFantasia);
		modelAndView.addObject("urlBase", "/contribuinte/nota/");
		return modelAndView;
	}
	
	public void verificaCompatibilidadeInscricao(Integer inscricao){
        boolean inscricaoCompativel = false;
        inscricaoCompativel |= empresaService.inscricaoCompativelContador(getContadorLogado(), inscricao);
        inscricaoCompativel |= empresaService.inscricaoCompativelContribuinte(getEmpresaLogada(), inscricao);
        String urlRedirect = request.getRequestURI().contains("contador") ? "/contador/contribuintes/cadastro" : "/contribuinte/cadastro";
        
        if (!inscricaoCompativel) 
        	throw new NFGException("Nenhum usuário logado ou inscriç&#225;o n&#225;o condizente com nenhum usuário logado!", urlRedirect);
    }

	@RequestMapping(method = RequestMethod.POST, value = "/cadastrar")
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// Exception.class)
	public @ResponseBody Map<String, Object> salvar(Integer inscricaoEstadual, Integer numeroDocumentoFiscal,
			Integer serieNotaFiscal, String subSerieNotaFiscal, Date dataEmissao, String cpf, Double valorTotal,
			Integer tipoDocumentoFiscal) throws Exception {
		
        Map<String,Object> resposta = new HashMap<String,Object>();
        
        if(!empresaService.inscricaoEstadualValida(inscricaoEstadual))
            throw new NFGException("Inscriç&#225;o Estadual Inválida!");
        if(dataEmissao!= null && dataEmissao.after(new Date()))
            throw new NFGException("Data de Emiss&#225;o Inválida!");
        if(!new ValidacaoDeCpf(cpf).isCpfValido())
            throw new NFGException("CPF Inválido!");

        try {
            DocumentoFiscalDigitadoToLegal ultimaNotaValida = notaEmpresaService.ultimaNotaValida(
                    inscricaoEstadual, numeroDocumentoFiscal,
                    serieNotaFiscal, subSerieNotaFiscal,
                    dataEmissao, tipoDocumentoFiscal
            );

            if(ultimaNotaValida == null) {

                notaEmpresaService.cadastrarNota(
                        numeroDocumentoFiscal, serieNotaFiscal, subSerieNotaFiscal,
                        dataEmissao, cpf, valorTotal, tipoDocumentoFiscal, 
                        new Date(), inscricaoEstadual
                );
                resposta.put("message","Nota Salva!");
                resposta.put("success",true);
            } else {
            	
                if (notaEmpresaService.existePontuacaoParaODocumento(ultimaNotaValida)){
                    resposta.put("success",false);
                    resposta.put("message","Este documento não pode ser alterado, pois já está vinculado á pontuação!!");
                }else{
                	notaEmpresaService.atualizarNota(
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/get-layout-params")
	public @ResponseBody Map<String, Object> obterParametrosLayout(Integer inscricaoEstadual,
			Integer numeroDocumentoFiscal, Date dataEmissao, Integer serieNotaFiscal, String subSerieNotaFiscal,
			Integer tipoDocumentoFiscal) {

		Map<String,Object> resposta = new HashMap<String,Object>();

        DocumentoFiscalDigitadoToLegal ultimaNotaNaoRemovida = notaEmpresaService.ultimaNotaValida(
                inscricaoEstadual, numeroDocumentoFiscal,
                serieNotaFiscal, subSerieNotaFiscal,
                dataEmissao, tipoDocumentoFiscal
        );

        resposta.put("salvar",false);

        //todo retirar mock
        if (!empresaService.inscricaoEstadualValida(inscricaoEstadual)) {
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
            
            resposta.put("subserie", ultimaNotaNaoRemovida.getSubSerieNotaFiscal());
            
        }
		return resposta;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/excluir-nota")
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// Exception.class)
	public @ResponseBody Map<String, Object> excluirNota(Integer idDocumentoFiscalDigital) throws Exception {
		
		Map<String,Object> resposta = new HashMap<String,Object>();
        DocumentoFiscalDigitadoToLegal documento = notaEmpresaService.documentoFiscalPorId(idDocumentoFiscalDigital);

        try {
        	
            if (notaEmpresaService.existePontuacaoParaODocumento(documento)){
            	
                resposta.put("success", false);
                resposta.put("message", "Este documento n&#225;o pode ser excluído, pois já está vinculado á pontuaç&#225;o!");
            
            }else{
            	
                DocumentoFiscalParticipante docParticipanteList = notaService.documentoFiscalParticipantePorIdDocumentoFiscalDigital(documento.getId());
                
                if (docParticipanteList != null && docParticipanteList.getId() != null){
                	docParticipanteList.setStatusProcessamento(StatusProcessamentoDocumentoFiscal.CANCELADO.getValue());
                	notaService.alterarDocumentoFiscalParticipante(docParticipanteList);
                }

                documento.setDataCancelDocumentoFiscal(new Date());
                notaEmpresaService.alterar(documento);

                resposta.put("success", true);
                resposta.put("message", "Removido!");
            }
        } catch (Exception e) {
            e.printStackTrace();

            resposta.put("success", false);
            resposta.put("message", "N&#225;o foi possível remover esta nota!");
        }
        
        return resposta;
	}

	@RequestMapping(value = "/buscar/{page}")
	public @ResponseBody Map<String, Object> buscarNotas(@PathVariable(value = "page") Integer page, Integer ieFiltro,
			String cpfFiltro, Integer nrDocFiltro, String dataEmissaoFiltro, BindException bind) {
		Integer max = 10;
        String dataEmissao = dataEmissaoFiltro != null ? dataEmissaoFiltro : null;

        List<DocumentoFiscalDigitado> ultimasNotasInseridas = notaService.ultimasNotasInseridas(ieFiltro,nrDocFiltro, dataEmissao,cpfFiltro,page * max, max);

        int notasInseridas = notaService.countUltimosInseridos(ieFiltro,nrDocFiltro,dataEmissao,cpfFiltro);

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total",  notasInseridas);
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("ultimasNotasInseridas",ultimasNotasInseridas);
        resposta.put("pagination", pagination);

        return resposta;
	}
}
