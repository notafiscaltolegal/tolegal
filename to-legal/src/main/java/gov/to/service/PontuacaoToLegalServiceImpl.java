package gov.to.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.goias.service.PaginacaoDTO;
import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.dto.PontuacaoDTO;
import gov.to.entidade.PontuacaoBonusToLegal;
import gov.to.entidade.PontuacaoToLegal;
import gov.to.filtro.FiltroPontuacaoBonusToLegal;
import gov.to.filtro.FiltroPontuacaoToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class PontuacaoToLegalServiceImpl implements PontuacaoToLegalService{
	
	@EJB
	private ConsultasDaoJpa<PontuacaoToLegal> reposiroty;
	
	@EJB
	private ConsultasDaoJpa<PontuacaoBonusToLegal> reposirotyPontuacaoBonus;
	
	@Override
	public List<PontuacaoToLegal> pesquisar(FiltroPontuacaoToLegal filtro, String... hbInitialize) {
		return reposiroty.filtrarPesquisa(filtro, PontuacaoToLegal.class, hbInitialize);
	}

	@Override
	public PontuacaoToLegal primeiroRegistro(FiltroPontuacaoToLegal filtro, String... hbInitialize) {
		return reposiroty.primeiroRegistroPorFiltro(filtro, PontuacaoToLegal.class, hbInitialize);
	}

	@Override
	public int totalPontosSorteioPorCpf(String cpf, Integer idSorteio) {
		Criteria criteria = reposiroty.getSession().createCriteria(PontuacaoToLegal.class);
		
		Long soma = (Long) criteria
				.setProjection(Projections.sum("qntPonto"))
				.createAlias("sorteioToLegal", "sorteioToLegal")
				.createAlias("notaFiscalToLegal", "notaFiscalToLegal")
				.add(Restrictions.eq("notaFiscalToLegal.cpf", cpf)) 
				.add(Restrictions.eq("sorteioToLegal.id", idSorteio.longValue())) 
				.uniqueResult();
		
		if (soma == null){
			soma = BigInteger.ZERO.longValue();
		}
		
		return soma.intValue() + totalBonus(cpf, idSorteio);
	}
	
	private int totalBonus(String cpf, Integer idSorteio) {
		
		Criteria criteria = reposiroty.getSession().createCriteria(PontuacaoBonusToLegal.class);
		
		Long soma = (Long) criteria
				.setProjection(Projections.sum("qntPonto"))
				.createAlias("sorteio", "sorteio")
				.add(Restrictions.eq("cpf", cpf)) 
				.add(Restrictions.eq("situacaoPontuacaoNota", SituacaoPontuacaoNota.PONTUADO))
				.add(Restrictions.eq("sorteio.id", idSorteio.longValue())) 
				.uniqueResult();
		
		if (soma == null){
			soma = BigInteger.ZERO.longValue();
		}
		
		return soma.intValue();
	}

	@Override
	public int totalNotasSorteioPorCpf(String cpf, Integer idSorteio) {
		
		Criteria criteria = reposiroty.getSession().createCriteria(PontuacaoToLegal.class);
		
		Long soma = (Long) criteria
				.setProjection(Projections.count("notaFiscalToLegal.id"))
				.createAlias("sorteioToLegal", "sorteioToLegal")
				.createAlias("notaFiscalToLegal", "notaFiscalToLegal")
				.add(Restrictions.eq("notaFiscalToLegal.cpf", cpf)) 
				.add(Restrictions.eq("sorteioToLegal.id", idSorteio.longValue())) 
				.uniqueResult();
		
		if (soma == null){
			soma = BigInteger.ZERO.longValue();
		}
		
		return soma.intValue();
	}

	@Override
	public PaginacaoDTO<PontuacaoDTO> consultaPontuacaoDocsFiscaisPorSorteio(Integer idSorteio, String cpf, Integer max, Integer page) {
		
		FiltroPontuacaoToLegal filtro = new FiltroPontuacaoToLegal();
		
		filtro.setCpf(cpf);
		filtro.setIdSorteio(idSorteio.longValue());
		
		List<PontuacaoToLegal> resultList = this.pesquisar(filtro, "notaFiscalToLegal");
		
		PaginacaoDTO<PontuacaoDTO> pag = new PaginacaoDTO<>();
		
		pag.setCount(resultList.size());
		
        List<PontuacaoDTO> listPontuacaoDTO = new ArrayList<>();
        
        int inicio = calcInicio(page, max);
	    int fim = calcPagFim(page, max);
        
	    for (int i=inicio; i <= fim; i++){
			
	    	 PontuacaoDTO pontDTO = new PontuacaoDTO();
			
			if (i == resultList.size()){
				break;
			}
			
			PontuacaoToLegal pontuacao = resultList.get(i);
			
			pontDTO.setCnpj(pontuacao.getNotaFiscalToLegal().getCnpj());
			pontDTO.setEstabelecimento(pontuacao.getNotaFiscalToLegal().getRazaoSocial());
			pontDTO.setNumero(pontuacao.getNotaFiscalToLegal().getNumNota());
			pontDTO.setEmissao(pontuacao.getNotaFiscalToLegal().getDataEmissao());
			pontDTO.setRegistro("");
			pontDTO.setValor(pontuacao.getNotaFiscalToLegal().getValor());
			pontDTO.setQtdePontos(pontuacao.getQntPonto());
            
            listPontuacaoDTO.add(pontDTO);
        }
	    
	    pag.setList(listPontuacaoDTO);
        
		return pag;
	}
	
	private static int calcPagFim(Integer page, Integer max) {
		
		return (calcInicio(page, max) + max) -1;
	}

	private static int calcInicio(Integer page, Integer max) {
		
		return (page * max);
	}

	@Override
	public List<Map<String, Object>> pontuacaoBonus(Integer idSorteio, String cpf, Integer max, Integer page) {
		
		FiltroPontuacaoBonusToLegal filtro = new FiltroPontuacaoBonusToLegal();
		
		filtro.setCpf(cpf);
		
		List<PontuacaoBonusToLegal> resultList = reposirotyPontuacaoBonus.filtrarPesquisa(filtro, PontuacaoBonusToLegal.class);
        List<Map<String, Object>> listOfMapResults = new ArrayList<Map<String, Object>>();
        
        for(PontuacaoBonusToLegal pont : resultList) {
        	
            Map<String, Object> mapResults = new HashMap<String, Object>();
            
            mapResults.put("data", pont.getDataPontuacao());
            mapResults.put("descricao",pont.getDescricao());
            mapResults.put("qtdePontos",pont.getQntPonto());
            
            listOfMapResults.add(mapResults);
        }

        return listOfMapResults;
	}

	@Override
	public boolean notaEmpresaPontuada(Integer idNotaEmpresa) {
		
		FiltroPontuacaoToLegal filtroPontuacaoToLegal = new FiltroPontuacaoToLegal();
		
		filtroPontuacaoToLegal.setIdNotaEmpresa(idNotaEmpresa.longValue());
		
		List<PontuacaoToLegal> resultList = reposiroty.filtrarPesquisa(filtroPontuacaoToLegal, PontuacaoToLegal.class);
		
		return resultList.isEmpty();
	}
}