package gov.goias.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.DocumentoFiscalDigitado;
import gov.goias.entidades.DocumentoFiscalParticipante;
import gov.goias.entidades.ProcesmLoteDocumentoFiscal;
import gov.goias.entidades.StatusProcessamento;

@Service
public class NotaServiceImpl implements NotaService{

	@Override
	public DocumentoFiscalDigitado ultimaNotaValida(Integer inscricaoEstadual, Integer numeroDocumentoFiscal,
			Integer serieNotaFiscal, String subSerieNotaFiscal, Date dataEmissao, Integer tipoDocumentoFiscal) {

		DocumentoFiscalDigitado documentoFiscalDigitado = getDocumentoFiscalDigital();
		
		return documentoFiscalDigitado;
	}

	private DocumentoFiscalDigitado getDocumentoFiscalDigital() {
		DocumentoFiscalDigitado documentoFiscalDigitado = new DocumentoFiscalDigitado();
		
		documentoFiscalDigitado.setContribuinte(MockContribuinte.getCCEContribuinte());
		documentoFiscalDigitado.setCpf("12345678912");
		documentoFiscalDigitado.setDataCancelDocumentoFiscal(new Date());
		documentoFiscalDigitado.setDataEmissao(new Date());
		documentoFiscalDigitado.setDataInclucaoDoctoFiscal(new Date());
		documentoFiscalDigitado.setId(123);
		documentoFiscalDigitado.setNumeroDocumentoFiscal(123545);
		documentoFiscalDigitado.setNumeroLote(123456);
		documentoFiscalDigitado.setSerieNotaFiscal(DocumentoFiscalDigitado.SERIE_NOTA_D);
		documentoFiscalDigitado.setSubSerieNotaFiscal("1236823");
		documentoFiscalDigitado.setTipoDocumentoFiscal(DocumentoFiscalDigitado.SERIE_NOTA_D);
		documentoFiscalDigitado.setValorTotal(12345.67);
		return documentoFiscalDigitado;
	}

	@Override
	public void cadastrarNota(Integer numeroDocumentoFiscal, Integer serieNotaFiscal, String subSerieNotaFiscal,
			Date dataEmissao, String cpf, Double valorTotal, Integer tipoDocumentoFiscal, CCEContribuinte contribuinte,
			Date date, Integer inscricaoEstadual) {
		
		System.out.println("Nota Cadastrada Mock");
	}

	@Override
	public void atualizarNota(DocumentoFiscalDigitado ultimaNotaValida, Integer numeroDocumentoFiscal,
			Integer serieNotaFiscal, String subSerieNotaFiscal, Date dataEmissao, String cpf, Double valorTotal,
			Integer tipoDocumentoFiscal, Integer inscricaoEstadual) {
		
		System.out.println("Nota Atualizada Mock");
	}

	@Override
	public List<DocumentoFiscalDigitado> ultimasNotasInseridas(Integer ieFiltro, Integer nrDocFiltro,
			String dataEmissao, String cpfFiltro, int offset, Integer max) {
		
		List<DocumentoFiscalDigitado> list = new ArrayList<>();
		list.add(getDocumentoFiscalDigital());
		
		return list;
	}

	@Override
	public int countUltimosInseridos(Integer ieFiltro, Integer nrDocFiltro, String dataEmissao, String cpfFiltro) {
		return 10;
	}

	@Override
	public DocumentoFiscalDigitado documentoFiscalPorId(Integer idDocumentoFiscalDigital) {
		return getDocumentoFiscalDigital();
	}

	@Override
	public void alterar(DocumentoFiscalDigitado documento) throws Exception {
		System.out.println("DocumentoFiscalDigital alerado MOck");
		
	}

	@Override
	public DocumentoFiscalParticipante documentoFiscalParticipantePorIdDocumentoFiscalDigital(Integer id) {
		return getDocumentoFiscalParticipante();
	}

	private DocumentoFiscalParticipante getDocumentoFiscalParticipante() {
		
		DocumentoFiscalParticipante documentoFiscalParticipante = new DocumentoFiscalParticipante();
		
		documentoFiscalParticipante.setDataEmissaoDocumento(new Date());
		documentoFiscalParticipante.setDataImportacao(new Date());
		documentoFiscalParticipante.setId(123);
		documentoFiscalParticipante.setIdDocFiscalDigitado(123);
		documentoFiscalParticipante.setIdEFD(123);
		documentoFiscalParticipante.setIdMFD(123);
		documentoFiscalParticipante.setIdNfe(123);
		documentoFiscalParticipante.setNumeroCnpjEmissor("1234567891234");
		documentoFiscalParticipante.setNumeroCpf("12345678912");
		documentoFiscalParticipante.setNumeroDocumentoFiscal(123);
		documentoFiscalParticipante.setProcesmLoteDocumentoFiscal(getProcessamentoLote());
		documentoFiscalParticipante.setStatusProcessamento(1);
		documentoFiscalParticipante.setValorDocumento(12234.2134);
		
		return documentoFiscalParticipante;
	}

	private ProcesmLoteDocumentoFiscal getProcessamentoLote() {

		ProcesmLoteDocumentoFiscal process = new ProcesmLoteDocumentoFiscal();
		
		process.setDataFimProcessamento(new Date());
		process.setDataInicioProcessamento(new Date());
		process.setId(123);
		process.setInfoMotivoErroProcesm("Erro mock");
		process.setNumrLote(123456);
		process.setOrigemDocumentoFiscal(123);
		process.setStatProcesmNfg(StatusProcessamento.P);
		
		return process;
	}

	@Override
	public void alterarDocumentoFiscalParticipante(DocumentoFiscalParticipante docParticipanteList) throws Exception {
		System.out.println("DocumentoFiscalParticipante alerado MOck");
	}

}
