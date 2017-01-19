package gov.to.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import gov.to.entidade.NotaFiscalToLegal;
import gov.to.entidade.PontuacaoToLegal;
import gov.to.filtro.FiltroNotaFiscalToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class NotaFiscalToLegalServiceImpl implements NotaFiscalToLegalService{
	
	@EJB
	private ConsultasDaoJpa<NotaFiscalToLegal> reposiroty;
	
	@Override
	public List<NotaFiscalToLegal> pesquisar(FiltroNotaFiscalToLegal filtro, String... hbInitialize) {
		return reposiroty.filtrarPesquisa(filtro, NotaFiscalToLegal.class, hbInitialize);
	}

	@Override
	public NotaFiscalToLegal primeiroRegistro(FiltroNotaFiscalToLegal filtro, String... hbInitialize) {
		return reposiroty.primeiroRegistroPorFiltro(filtro, NotaFiscalToLegal.class, hbInitialize);
	}

	@Override
	public int totalNotasPorCpf(String cpf, Integer idSorteio) {
		
		Criteria criteria = reposiroty.getSession().createCriteria(PontuacaoToLegal.class);
		
		Long count = (Long) criteria
				.setProjection(Projections.count("id"))
				.createAlias("sorteioToLegal", "sorteioToLegal")
				.createAlias("notaFiscalToLegal", "notaFiscalToLegal")
				.add(Restrictions.eq("sorteioToLegal.id", Long.valueOf(idSorteio))) 
				.add(Restrictions.eq("notaFiscalToLegal.cpf", cpf)) 
				.uniqueResult();
		
		return count.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> chaveAcessoPorDataEmissao(Date dataInicio, Date dataFim) {
		
		SimpleDateFormat sp = new SimpleDateFormat("dd/mm/yyyy");
		
		String dtInicio = sp.format(dataInicio);
		String dtFim = sp.format(dataFim);
		
		Query qr = reposiroty.getSession().createSQLQuery("SELECT chave_acesso FROM TB_NOTA_LEGAL WHERE data_emissao >= to_date('"+dtInicio+"','dd/mm/yyyy') AND data_emissao <= to_date('"+dtFim+"','dd/mm/yyyy') ");
		
		qr.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		List<Map<String, Object>> src = qr.list();
		List<String> list = new ArrayList<String>();
		
		for (Map<String, Object> map : src) {
			list.add(map.get("CHAVE_ACESSO").toString());
		}
		
		return list;
	}
}