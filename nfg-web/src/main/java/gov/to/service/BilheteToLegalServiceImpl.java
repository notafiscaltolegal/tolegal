package gov.to.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.goias.dtos.DTOBilhetePessoa;
import gov.goias.entidades.BilhetePessoa;
import gov.goias.exceptions.NFGException;
import gov.to.entidade.BilheteToLegal;
import gov.to.filtro.FiltroBilheteToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class BilheteToLegalServiceImpl extends ConsultasDaoJpa<BilheteToLegal> implements BilheteToLegalService{
	
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
}