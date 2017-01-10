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

import gov.goias.dtos.DTOBilhetePessoa;
import gov.goias.entidades.BilhetePessoa;
import gov.goias.exceptions.NFGException;
import gov.to.dominio.SituacaoBilhete;
import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.entidade.BilheteToLegal;
import gov.to.entidade.PontuacaoBonusToLegal;
import gov.to.entidade.PontuacaoToLegal;
import gov.to.entidade.SorteioToLegal;
import gov.to.filtro.FiltroBilheteToLegal;
import gov.to.filtro.FiltroPontuacaoBonusToLegal;
import gov.to.persistencia.ConsultasDaoJpa;
import gov.to.persistencia.GenericPersistence;
import gov.to.properties.SorteioProperties;

@Stateless
public class BilheteToLegalServiceImpl extends ConsultasDaoJpa<BilheteToLegal> implements BilheteToLegalService{
	
	@EJB
	private SorteioToLegalService sorteioToLegalService;
	
	@EJB
	private ConsultasDaoJpa<PontuacaoBonusToLegal> reposirotyPontuacaoBonus; 
	
	@EJB
	private GenericPersistence<BilheteToLegal, Long> bilhetePersistence;
	
	@EJB
	private GenericPersistence<PontuacaoBonusToLegal, Long> pontuacaoBonusPersistence;
	
	@EJB
	private GenericPersistence<PontuacaoToLegal, Long> pontuacaoPersistence;
	
	@Override
	public List<BilhetePessoa> listBilhetes(String cpf, Integer idSorteio) {
		
		FiltroBilheteToLegal filtro = new FiltroBilheteToLegal();
		
		filtro.setCpf(cpf);
		filtro.setIdSorteio(idSorteio.longValue());
		
		List<BilheteToLegal> listBilheteToLegal = super.filtrarPesquisa(filtro, BilheteToLegal.class);
		
		return converteBilhetePessoa(listBilheteToLegal);
	}

	private List<BilhetePessoa> converteBilhetePessoa(List<BilheteToLegal> listBilheteToLegal) {
		
		if (listBilheteToLegal == null){
			return null;
		}
		
		List<BilhetePessoa> listBilhetePessoa = new ArrayList<>();
		
		for (BilheteToLegal bilhete : listBilheteToLegal){
			
			BilhetePessoa bilhetePessoa = new BilhetePessoa();
			
			bilhetePessoa.setId(bilhete.getId().intValue());
			bilhetePessoa.setNumeroSequencial(bilhete.getNumeroSeqBilhete());
			
			listBilhetePessoa.add(bilhetePessoa);
		}
		
		return listBilhetePessoa;
	}

	@Override
	public Long totalBilheteSorteioPorCpf(String cpf, Integer idSorteio) {
		
		Criteria criteria = getSession().createCriteria(BilheteToLegal.class);
		
		Long count = (Long) criteria
				.setProjection(Projections.count("id"))
				.createAlias("sorteioToLegal", "sorteioToLegal")
				.add(Restrictions.eq("cpf", cpf)) 
				.add(Restrictions.eq("sorteioToLegal.id", idSorteio.longValue()))
				.uniqueResult();
		
		return count;
	}

	@Override
	public List<Map<String,DTOBilhetePessoa>> listBilhetePaginado(String cpf, Integer idSorteio, Integer max, Integer page) {
	     try{
	            List<Map<String,DTOBilhetePessoa>> listaDeMapBilhetes = new ArrayList<>();

	            int nrColunas = 3;
				List<DTOBilhetePessoa> bilhetesPremiados = listarBilhetesPremiados(max, page, nrColunas , idSorteio, cpf);

	            for (int i=0; i<bilhetesPremiados.size() ; i+=nrColunas){
	            	
	                Map<String,DTOBilhetePessoa> mapDeBilhetes = new HashMap<String, DTOBilhetePessoa>();
	                
	                if (i <= bilhetesPremiados.size()-1)
	                    mapDeBilhetes.put("bilhete1col",(DTOBilhetePessoa) bilhetesPremiados.get(i));

	                if (i+1 <= bilhetesPremiados.size()-1)
	                    mapDeBilhetes.put("bilhete2col",(DTOBilhetePessoa) bilhetesPremiados.get(i+1));

	                if (i+2 <= bilhetesPremiados.size()-1)
	                    mapDeBilhetes.put("bilhete3col",(DTOBilhetePessoa) bilhetesPremiados.get(i+2));

	                listaDeMapBilhetes.add(mapDeBilhetes);
	            }

	            return  listaDeMapBilhetes;
	        }catch (Exception e){
	            throw new NFGException("Algo de errado ocorreu ao tentar listar os bilhetes");
	        }
	}

	private List<DTOBilhetePessoa> listarBilhetesPremiados(Integer max, Integer page, int nrColunas, Integer idSorteio, String cpf) {
		
		FiltroBilheteToLegal filtro = new FiltroBilheteToLegal();
		
		filtro.setCpf(cpf);
		filtro.setIdSorteio(idSorteio.longValue());
		
		List<BilheteToLegal> listBilheteToLegal = super.filtrarPesquisa(filtro, BilheteToLegal.class);
		List<DTOBilhetePessoa> list = new ArrayList<>();
		
		int inicio = calcInicio(page, max);
	    int fim = calcPagFim(page, max);
		
		for (int i=inicio; i <= fim; i++){
			
			DTOBilhetePessoa bilhetePessoa = new DTOBilhetePessoa();
			
			if (i == listBilheteToLegal.size()){
				break;
			}
			
			BilheteToLegal bilheteToLegal = listBilheteToLegal.get(i);
			
			bilhetePessoa.setBilheteDefinitivo("S");
			bilhetePessoa.setIdBilhete(bilheteToLegal.getId().intValue());
			bilhetePessoa.setIdSorteio(bilheteToLegal.getSorteioToLegal().getId().intValue());
			bilhetePessoa.setNumero(bilheteToLegal.getNumeroSeqBilhete());
			bilhetePessoa.setPremiado(bilheteToLegal.getPremiadoFormat());
			
			list.add(bilhetePessoa);
		}
		
		return list;
	}

	private static int calcPagFim(Integer page, Integer max) {
		
		return (calcInicio(page, max) + max) -1;
	}

	private static int calcInicio(Integer page, Integer max) {
		
		return (page * max);
	}

	@Override
	public void processaBilheteBonusPontuacao(String cpf) {
		
		SorteioToLegal sorteioToLegal = sorteioToLegalService.sorteioAtual();
		
		FiltroPontuacaoBonusToLegal filtro = new FiltroPontuacaoBonusToLegal();
		filtro.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO);
		filtro.setIdSorteio(sorteioToLegal.getId());
		filtro.setCpf(cpf);
		List<PontuacaoBonusToLegal> listPontuacaoBonus = reposirotyPontuacaoBonus.filtrarPesquisa(filtro, PontuacaoBonusToLegal.class);
		
		for (PontuacaoBonusToLegal pontuacaoBonus : listPontuacaoBonus){
			
			Integer qntBilhetes = pontuacaoBonus.getQntPonto() / SorteioProperties.getValue(SorteioProperties.QNT_PONTOS_POR_BILHETE);
			
			for (int i=BigInteger.ZERO.intValue(); i < qntBilhetes; i++){
				
				BilheteToLegal bilheteToLegal = new BilheteToLegal();
				bilheteToLegal.setSorteioToLegal(sorteioToLegal);
				bilheteToLegal.setNumeroSeqBilhete(geraNumeroBilhete());
				bilheteToLegal.setStBilhete(SituacaoBilhete.VALIDO);
				bilheteToLegal.setCpf(pontuacaoBonus.getCpf());
				bilheteToLegal.setPontuacaoBonusToLegal(pontuacaoBonus);
				
				bilhetePersistence.salvar(bilheteToLegal);
			}
			
			pontuacaoBonus.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.PONTUADO);
			pontuacaoBonusPersistence.merge(pontuacaoBonus);
		}
	}
	
	@Override
	public Integer geraNumeroBilhete() {
		
		Criteria criteria = getSession().createCriteria(BilheteToLegal.class);
		
		criteria.setProjection(Projections.max("numeroSeqBilhete"));
		Integer max = (Integer)criteria.uniqueResult();
		
		if (max == null){
			return BigInteger.TEN.intValue();
		}
		
		return ++max;
	}

	@Override
	public void processaBilhetePorPontuacao(SorteioToLegal sorteioToLegal, PontuacaoToLegal pontuacao) {
			
		Integer qntBilhetes = pontuacao.getQntPonto() / SorteioProperties.getValue(SorteioProperties.QNT_PONTOS_POR_BILHETE);
		
		for (int i=BigInteger.ZERO.intValue(); i < qntBilhetes; i++){
			
			BilheteToLegal bilheteToLegal = new BilheteToLegal();
			bilheteToLegal.setSorteioToLegal(sorteioToLegal);
			bilheteToLegal.setNumeroSeqBilhete(geraNumeroBilhete());
			bilheteToLegal.setStBilhete(SituacaoBilhete.VALIDO);
			bilheteToLegal.setCpf(pontuacao.getNotaFiscalToLegal().getCpf());
			bilheteToLegal.setPontuacaoToLegal(pontuacao);
			
			bilhetePersistence.salvar(bilheteToLegal);
		}
		
		pontuacao.setSituacaoPontuacao(SituacaoPontuacaoNota.PONTUADO);
		pontuacaoPersistence.merge(pontuacao);
	}
	
	@Override
	public void processaBilhetePorPontuacaoBonus(SorteioToLegal sorteioToLegal, PontuacaoBonusToLegal pontuacaoBonus) {
			
		Integer qntBilhetes = pontuacaoBonus.getQntPonto() / SorteioProperties.getValue(SorteioProperties.QNT_PONTOS_POR_BILHETE);
		
		for (int i=BigInteger.ZERO.intValue(); i < qntBilhetes; i++){
			
			BilheteToLegal bilheteToLegal = new BilheteToLegal();
			bilheteToLegal.setSorteioToLegal(sorteioToLegal);
			bilheteToLegal.setNumeroSeqBilhete(geraNumeroBilhete());
			bilheteToLegal.setStBilhete(SituacaoBilhete.VALIDO);
			bilheteToLegal.setCpf(pontuacaoBonus.getCpf());
			bilheteToLegal.setPontuacaoBonusToLegal(pontuacaoBonus);
			
			bilhetePersistence.salvar(bilheteToLegal);
		}
		
		pontuacaoBonus.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.PONTUADO);
		pontuacaoBonusPersistence.merge(pontuacaoBonus);
	}
}