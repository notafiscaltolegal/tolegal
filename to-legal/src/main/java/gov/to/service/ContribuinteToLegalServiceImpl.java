package gov.to.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import gov.goias.dtos.DTOContribuinte;
import gov.to.dto.PaginacaoContribuinteDTO;
import gov.to.entidade.ContribuinteToLegal;
import gov.to.filtro.FiltroContribuinteToLegal;
import gov.to.persistencia.ConsultasDaoJpa;
import gov.to.persistencia.GenericPersistence;

@Stateless
public class ContribuinteToLegalServiceImpl extends ConsultasDaoJpa<ContribuinteToLegal> implements ContribuinteToLegalService{
	
	@EJB
	private GenericPersistence<ContribuinteToLegal, String> genericPersistence;
	
	@EJB
	private ConsultasDaoJpa<ContribuinteToLegal> reposiroty;

	@Override
	public PaginacaoContribuinteDTO findContribuintes(Integer page, Integer max, String numrInscricao) {
		
		FiltroContribuinteToLegal filtro = new FiltroContribuinteToLegal();
		
		filtro.setInscricaoEstadual(numrInscricao);

		List<ContribuinteToLegal> list = filtrarPesquisa(filtro, ContribuinteToLegal.class);
		
		return converteListaDTOContribuinte(list , page, max);
	}
	
	private PaginacaoContribuinteDTO converteListaDTOContribuinte(List<ContribuinteToLegal> listaContribuintesBancoDados, Integer page, Integer max) {
		
		int inicio = calcInicio(page, max);
	    int fim = calcPagFim(page, max);
		
		List<DTOContribuinte> list = new ArrayList<>();
		PaginacaoContribuinteDTO pagDTO = new PaginacaoContribuinteDTO();
		
		if (listaContribuintesBancoDados != null){
			pagDTO.setCountPaginacao(listaContribuintesBancoDados.size());	
		}
		
		
		for (int i=inicio; i <= fim; i++){
			
			DTOContribuinte dto = new DTOContribuinte();
			
			if (i == listaContribuintesBancoDados.size()){
				break;
			}
			
			ContribuinteToLegal ctl = listaContribuintesBancoDados.get(i);
			
			dto.setNomeEmpresa(ctl.getRazaoSocial());
			String inscricaoAntiga=ctl.getId().replace(".", "").replace("-", "");
			dto.setNumeroInscricao(Integer.parseInt(inscricaoAntiga));
			dto.setNumeroCnpj(ctl.getCnpj());
			dto.setDataEfetivaParticipacao(ctl.getDataVigencia());
			
			list .add(dto);
		}
		
		pagDTO.setListContribuinteDTO(list);
		
		return pagDTO;
	}
	
	private static int calcPagFim(Integer page, Integer max) {
		
		return (calcInicio(page, max) + max) -1;
	}

	private static int calcInicio(Integer page, Integer max) {
		
		return (page * max);
	}

	@Override
	public ContribuinteToLegal findByInscricaoEstadual(Integer inscricaoEstadual) {

		FiltroContribuinteToLegal filtroContribuinteToLegal = new FiltroContribuinteToLegal();

		filtroContribuinteToLegal.setInscricaoEstadual(FiltroContribuinteToLegal.inscricaoEstadualFormat(inscricaoEstadual));

		return super.primeiroRegistroPorFiltro(filtroContribuinteToLegal, ContribuinteToLegal.class);
	}

	@Override
	public ContribuinteToLegal autenticaCidadao(String ie, String senha) {
		
		FiltroContribuinteToLegal filtroContribuinteToLegal = new FiltroContribuinteToLegal();

		filtroContribuinteToLegal.setInscricaoEstadual(FiltroContribuinteToLegal.inscricaoEstadualFormat(Integer.valueOf(ie)));
		filtroContribuinteToLegal.setSenha(senha);

		return super.primeiroRegistroPorFiltro(filtroContribuinteToLegal, ContribuinteToLegal.class);
	}
}