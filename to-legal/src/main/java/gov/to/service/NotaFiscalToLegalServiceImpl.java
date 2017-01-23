package gov.to.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.time.DateFormatUtils;
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
		
		Criteria criteriaNotaEmpresa = reposiroty.getSession().createCriteria(PontuacaoToLegal.class);
		
		Long countNotaEmpresa = (Long) criteriaNotaEmpresa
				.setProjection(Projections.count("id"))
				.createAlias("sorteioToLegal", "sorteioToLegal")
				.createAlias("notaFiscalEmpresaToLegal", "notaFiscalEmpresaToLegal")
				.add(Restrictions.eq("sorteioToLegal.id", Long.valueOf(idSorteio))) 
				.add(Restrictions.eq("notaFiscalEmpresaToLegal.cpfDestinatario", cpf)) 
				.uniqueResult();
		
		if (count == null){
			count = BigInteger.ZERO.longValue();
		}
		
		if (countNotaEmpresa == null){
			countNotaEmpresa = BigInteger.ZERO.longValue();
		}
		
		
		return count.intValue() + countNotaEmpresa.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> chaveAcessoPorDataEmissao(Date dataInicio) {
		
		String dtInicio = DateFormatUtils.format(dataInicio, "dd/MM/yyyy");
		
		Query qr = reposiroty.getSession().createSQLQuery("SELECT chave_acesso FROM TB_NOTA_LEGAL WHERE data_emissao >= to_date('"+dtInicio+"','dd/mm/yyyy') ");
		
		qr.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		List<Map<String, Object>> src = qr.list();
		List<String> list = new ArrayList<String>();
		
		for (Map<String, Object> map : src) {
			list.add(map.get("CHAVE_ACESSO").toString());
		}
		
		return list;
	}
}