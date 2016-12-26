package gov.goias.service;

import java.util.Date;
import java.util.List;

import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.DocumentoFiscalDigitado;
import gov.goias.entidades.DocumentoFiscalParticipante;

public interface NotaService {

	DocumentoFiscalDigitado ultimaNotaValida(Integer inscricaoEstadual, Integer numeroDocumentoFiscal,
			Integer serieNotaFiscal, String subSerieNotaFiscal, Date dataEmissao, Integer tipoDocumentoFiscal);

	void cadastrarNota(Integer numeroDocumentoFiscal, Integer serieNotaFiscal, String subSerieNotaFiscal,
			Date dataEmissao, String cpf, Double valorTotal, Integer tipoDocumentoFiscal, CCEContribuinte contribuinte,
			Date date, Integer inscricaoEstadual);

	void atualizarNota(DocumentoFiscalDigitado ultimaNotaValida, Integer numeroDocumentoFiscal, Integer serieNotaFiscal,
			String subSerieNotaFiscal, Date dataEmissao, String cpf, Double valorTotal, Integer tipoDocumentoFiscal,
			Integer inscricaoEstadual);

	List<DocumentoFiscalDigitado> ultimasNotasInseridas(Integer ieFiltro, Integer nrDocFiltro, String dataEmissao,
			String cpfFiltro, int offset, Integer max);

	int countUltimosInseridos(Integer ieFiltro, Integer nrDocFiltro, String dataEmissao, String cpfFiltro);

	DocumentoFiscalDigitado documentoFiscalPorId(Integer idDocumentoFiscalDigital);

	void alterar(DocumentoFiscalDigitado documento) throws Exception;

	DocumentoFiscalParticipante documentoFiscalParticipantePorIdDocumentoFiscalDigital(Integer id);

	void alterarDocumentoFiscalParticipante(DocumentoFiscalParticipante docParticipanteList) throws Exception;

}
