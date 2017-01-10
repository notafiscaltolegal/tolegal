package gov.to.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.transform.Transformers;

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
	public PaginacaoContribuinteDTO findContribuintes(Integer page, Integer max, String cnpjBase, Integer numrInscricao, String cnpj, String nome) {
		
		FiltroContribuinteToLegal filtro = new FiltroContribuinteToLegal();
		
		filtro.setCnpj("15.997.281/0001-76");
		//filtro.setInscricaoEstadual(numrInscricao.toString());
		//filtro.setRazaoSocial(nome);
		
		StringBuilder sql = new StringBuilder();
    	//String dataInicioSorteio = "01/01/2016";
    	//String dataFimSorteio = "08/11/2016";
    	
    	sql.append("SELECT CONINSEST AS INSCRICAO_ESTADUAL, CONRAZSOC AS RAZAO_SOCIAL, CONINSCNPJ AS CNPJ, CONDATINIA AS DATA_VIGENCIA  FROM EFCDCO WHERE ROWNUM <= 100");
        
        
        org.hibernate.Query query = reposiroty.getSession().createSQLQuery(sql.toString());
        
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		List<ContribuinteToLegal> list = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> src = query.list();
		
		String colunaInscEstadual = "INSCRICAO_ESTADUAL";
		String colunaRazaoSocial = "RAZAO_SOCIAL";
		String colunaCNPJ = "CNPJ";
		String colunaDataVigencia = "DATA_VIGENCIA";
		
		for (Map<String, Object> map : src) {

			ContribuinteToLegal cont = new ContribuinteToLegal();

			cont.setId(map.get(colunaInscEstadual).toString());
			cont.setRazaoSocial(map.get(colunaRazaoSocial).toString());
			cont.setCnpj(map.get(colunaCNPJ).toString());

			try {
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				cont.setDataVigencia(sd.parse(map.get(colunaDataVigencia).toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			list.add(cont);
		}

		return converteListaDTOContribuinte(list, page, max);
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
}