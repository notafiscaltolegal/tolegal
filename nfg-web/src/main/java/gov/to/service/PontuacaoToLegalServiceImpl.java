package gov.to.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.to.dominio.SituacaoBonusPontuacao;
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
		
		return soma.intValue() + totalBonus(cpf, idSorteio);
	}
	
	private int totalBonus(String cpf, Integer idSorteio) {
		
		Criteria criteria = reposiroty.getSession().createCriteria(PontuacaoBonusToLegal.class);
		
		Long soma = (Long) criteria
				.setProjection(Projections.sum("qntPonto"))
				.add(Restrictions.eq("cpf", cpf)) 
				.add(Restrictions.eq("situacaoBonusPontuacao", SituacaoBonusPontuacao.ATIVO))
				.uniqueResult();
		
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
		
		return soma.intValue();
	}

	@Override
	public List<Map<String, Object>> consultaPontuacaoDocsFiscaisPorSorteio(Integer idSorteio, String cpf, Integer max, Integer page) {
		
		FiltroPontuacaoToLegal filtro = new FiltroPontuacaoToLegal();
		
		filtro.setCpf(cpf);
		filtro.setIdSorteio(idSorteio.longValue());
		
		List<PontuacaoToLegal> resultList = this.pesquisar(filtro, "notaFiscalToLegal");
		
        List<Map<String, Object>> listOfMapResults = new ArrayList<>();
        
        for(PontuacaoToLegal pontuacao : resultList) {
        	
            Map<String, Object> mapResults = new HashMap<>();
            
            mapResults.put("cnpj", pontuacao.getNotaFiscalToLegal().getCnpj());
            mapResults.put("estabelecimento", pontuacao.getNotaFiscalToLegal().getRazaoSocial());
            mapResults.put("numero", pontuacao.getNotaFiscalToLegal().getNumNota());
            mapResults.put("emissao", pontuacao.getNotaFiscalToLegal().getDataEmissao());
            mapResults.put("registro", "");
            mapResults.put("valor", pontuacao.getNotaFiscalToLegal().getValor());
            mapResults.put("qtdePontos", pontuacao.getQntPonto());
            mapResults.put("status", "");
            mapResults.put("detalhe", "") ;
            listOfMapResults.add(mapResults);
        }
        
		return listOfMapResults;
	}

	@Override
	public List<Map<String, Object>> pontuacaoBonus(Integer idSorteio, String cpf, Integer max, Integer page) {
		
		FiltroPontuacaoBonusToLegal filtro = new FiltroPontuacaoBonusToLegal();
		
		filtro.setCpf(cpf);
		
		List<PontuacaoBonusToLegal> resultList = reposirotyPontuacaoBonus.filtrarPesquisa(filtro, PontuacaoBonusToLegal.class);
        List<Map<String, Object>> listOfMapResults = new ArrayList<Map<String, Object>>();
        
        for(PontuacaoBonusToLegal pont : resultList) {
        	
            Map<String, Object> mapResults = new HashMap<String, Object>();
            
            mapResults.put("data", pont.getDataPontuacaoFormat());
            mapResults.put("descricao",pont.getDescricao());
            mapResults.put("qtdePontos",pont.getQntPonto());
            
            listOfMapResults.add(mapResults);
        }

        return listOfMapResults;
	}
}