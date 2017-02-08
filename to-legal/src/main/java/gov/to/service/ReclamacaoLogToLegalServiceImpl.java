package gov.to.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.time.DateFormatUtils;

import gov.goias.service.PaginacaoDTO;
import gov.to.dominio.PerfilGeralEnum;
import gov.to.dominio.ReclamacaoStatusEnum;
import gov.to.entidade.ReclamacaoLogToLegal;
import gov.to.filtro.FiltroReclamacaoLogToLegal;
import gov.to.goias.ReclamacaoLogDTO;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class ReclamacaoLogToLegalServiceImpl extends ConsultasDaoJpa<ReclamacaoLogToLegal>
		implements ReclamacaoLogToLegalService {
	
	@EJB
	private GenericService<ReclamacaoLogToLegal, Long> reclamacaoLogToLegalService;
	

	@Override
	public PaginacaoDTO<ReclamacaoLogDTO> logReclamacaoPorIdReclamacao(Integer idReclamacao, Integer page,
			Integer max) {

		PaginacaoDTO<ReclamacaoLogDTO> paginacaoDTO = new PaginacaoDTO<>();

		FiltroReclamacaoLogToLegal filtro = new FiltroReclamacaoLogToLegal();

		filtro.setIdReclamacao(idReclamacao.longValue());

		List<ReclamacaoLogToLegal> list = super.filtrarPesquisa(filtro, ReclamacaoLogToLegal.class);

		int inicio = calcInicio(page, max);
		int fim = calcPagFim(page, max);

		if (list != null) {
			paginacaoDTO.setCount(list.size());
		}

		List<ReclamacaoLogDTO> listsDocs = new ArrayList<>();

		for (int i = inicio; i <= fim; i++) {

			ReclamacaoLogDTO dto = new ReclamacaoLogDTO();

			if (i == list.size()) {
				break;
			}

			ReclamacaoLogToLegal rec = list.get(i);

			dto.setPerfilDescricao(rec.getPerfilGeral().getLabel());
			dto.setDataCadastroSituacao(DateFormatUtils.format(rec.getDataReclamacao(),"dd/MM/yyyy"));
			dto.setComplSituacaoReclamacao(rec.getStatusReclamacao().getLabel());
			listsDocs.add(dto);

		}

		paginacaoDTO.setList(listsDocs);

		return paginacaoDTO;
	}
	
	@Override
	public void cadastrarLog() {		
		ReclamacaoLogToLegal log=new ReclamacaoLogToLegal();
		Date d = new Date();
		//Calendar cal = new GregorianCalendar();				
		log.setDataReclamacao(d);
	    log.setStatusReclamacao(ReclamacaoStatusEnum.EMPRESA_NO_PRAZO);
	    //log.setReclamacaoToLegal(reclamacaoToLegal);
	    log.setPerfilGeral(PerfilGeralEnum.CIDADAO);
		
		reclamacaoLogToLegalService.salvar(log);
		
	}

	private static int calcPagFim(Integer page, Integer max) {

		return (calcInicio(page, max) + max) - 1;
	}

	private static int calcInicio(Integer page, Integer max) {

		return (page * max);
	}

}