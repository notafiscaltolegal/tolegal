package gov.to.service;

import java.util.Date;

import javax.ejb.Local;

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
	
}
