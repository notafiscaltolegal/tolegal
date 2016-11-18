package gov.goias.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import gov.goias.entidades.MFDArquivoMemoriaFiscal;

@Service
public class MDFServiceImpl implements MDFService{

	@Override
	public List<MFDArquivoMemoriaFiscal> findMDF(Integer inscricaoEstadual, Integer intInicio, Integer intFim, Integer max, Integer page) {
		return getListMFDArquivoMemoriaFiscal();
	}

	@Override
	public Integer findCountMDF(Integer inscricaoEstadual, Integer intInicio, Integer intFim) {
		return 31;
	}

	@Override
	public List<MFDArquivoMemoriaFiscal> findMDF(Integer inscricaoEstadual, Integer max, Integer page) {
		return getListMFDArquivoMemoriaFiscal();
	}

	private List<MFDArquivoMemoriaFiscal> getListMFDArquivoMemoriaFiscal() {
		
		List<MFDArquivoMemoriaFiscal> list = new ArrayList<>();
		
		MFDArquivoMemoriaFiscal arq = new MFDArquivoMemoriaFiscal();
		
		arq.setCnpjEstabelecimento("1234567891234");
		arq.setCodigo(123);
		arq.setCpfContribuinte("12345678912");
		arq.setDataCatalogacao(new Date());
		arq.setDataEntrega(new Date());
		arq.setDataEntregaSefaz(new Date());
		arq.setDataFimApuracao(new Date());
		arq.setDataFimProcessamento(new Date());
		arq.setDataGravacaoSoftware(new Date());
		arq.setDataInicioApuracao(new Date());
		arq.setDataInicioProcessamento(new Date());
		arq.setExportacaoNfg("Exportaç&#225;o Mock");
		arq.setId(123);
		arq.setInfoContexto("Contexto Mock");
		arq.setInfoDiretorio("/mock");
		arq.setInfoHash("hash mock");
		arq.setInfoMensagemLog("informaç&#225;o mensagem log");
		arq.setNome("Nome Mock");
		arq.setNumeroInscricao(123);
		arq.setNumeroOrdemUsuario(321);
		arq.setNumeroSequencial(123456);
		arq.setQuantidadeLinha(12345);
		arq.setReducaoZFim(12312);
		arq.setReducaoZInicio(321);
		arq.setReferencia(12);
		arq.setStatusProcessamento("Mock");
		arq.setTipoComando("Comando Mock");
		arq.setTipoFinalidade(21);
		arq.setTipoModelo("modelo mock");
		arq.setVersaoAtoCotepe("Vers&#225;o atocotepe mock");
		arq.setVersaoBiblioteca("Vers&#225;o biblioteca mock");
		arq.setVersaoSoftware("Vers&#225;o software mock");
		
		list.add(arq);
		
		return list;
	}

	@Override
	public Integer findCountMDF(Integer inscricaoEstadual) {
		return 12;
	}
}
