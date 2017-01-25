package gov.to.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import gov.goias.service.PaginacaoDTO;
import gov.to.entidade.NotaEmpresaToLegal;
import gov.to.filtro.FiltroNotaEmpresaToLegal;
import gov.to.goias.DocumentoFiscalDigitadoToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface NotaEmpresaService {

	
	DocumentoFiscalDigitadoToLegal ultimaNotaValida(Integer inscricaoEstadual, Integer numeroDocumentoFiscal,
			Integer serieNotaFiscal, String subSerieNotaFiscal, Date dataEmissao, Integer tipoDocumentoFiscal);
	
	void cadastrarNota(Integer numeroDocumentoFiscal, Integer serieNotaFiscal, String subSerieNotaFiscal,
			Date dataEmissao, String cpf, Double valorTotal, Integer tipoDocumentoFiscal,
			Date date, Integer inscricaoEstadual);

	void atualizarNota(DocumentoFiscalDigitadoToLegal ultimaNotaValida, Integer numeroDocumentoFiscal,
			Integer serieNotaFiscal, String subSerieNotaFiscal, Date dataEmissao, String cpf, Double valorTotal,
			Integer tipoDocumentoFiscal, Integer inscricaoEstadual);

	boolean existePontuacaoParaODocumento(DocumentoFiscalDigitadoToLegal ultimaNotaValida);

	DocumentoFiscalDigitadoToLegal documentoFiscalPorId(Integer idDocumentoFiscalDigital);

	void alterar(DocumentoFiscalDigitadoToLegal documento);

	PaginacaoDTO<DocumentoFiscalDigitadoToLegal> ultimasNotasInseridas(Integer ieFiltro, Integer nrDocFiltro, String dataEmissao,
			String cpfFiltro, int ig, Integer max);

	void excluir(NotaEmpresaToLegal nota);

	NotaEmpresaToLegal buscarNotaEmpresaToLegalPorId(Integer idDocumentoFiscalDigital);

	List<NotaEmpresaToLegal> pesquisar(FiltroNotaEmpresaToLegal filtroNotaEmpresa);
	
}
