package gov.goias.service;

import java.util.List;

import gov.goias.entidades.MFDArquivoMemoriaFiscal;

public interface MDFService {

	List<MFDArquivoMemoriaFiscal> findMDF(Integer inscricaoEstadual, Integer intInicio, Integer intFim, Integer max,
			Integer page);

	Integer findCountMDF(Integer inscricaoEstadual, Integer intInicio, Integer intFim);

	List<MFDArquivoMemoriaFiscal> findMDF(Integer inscricaoEstadual, Integer max, Integer page);

	Integer findCountMDF(Integer inscricaoEstadual);


}