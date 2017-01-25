package gov.to.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import gov.to.entidade.GanhadorSorteioToLegal;
import gov.to.entidade.SorteioToLegal;
import gov.to.filtro.FiltroGanhadorToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class GanhadorToLegalServiceImpl extends ConsultasDaoJpa<GanhadorSorteioToLegal> implements GanhadorToLegalService{
	
	@EJB
	private GenericService<SorteioToLegal, Long> genericServiceSorteio;
	
	@EJB
	private GenericService<GanhadorSorteioToLegal, Long> genericGanhadorService;
	
	@EJB
	private BilheteToLegalService bilheteService;
	
	@EJB
	private PessoaFisicaToLegalService pessoaFisicaToLegalService;
	
	@EJB
	private SorteioToLegalService sorteioToLegalService;

	@Override
	public List<GanhadorSorteioToLegal> pesquisar(FiltroGanhadorToLegal filtroDoc, String... strings) {
		
		return filtrarPesquisa(filtroDoc, GanhadorSorteioToLegal.class);
	}

	@Override
	public List<GanhadorSorteioToLegal> ganhadorPorSorteio(Integer idSorteio, String cpf) {
		
		Integer numSorteio = null;
		
		if (idSorteio == null){
			
			numSorteio = sorteioToLegalService.sorteioAtual().getNumeroSorteio();
		}else{
			
			numSorteio = genericServiceSorteio.getById(SorteioToLegal.class, idSorteio.longValue()).getNumeroSorteio();
		}
		
		FiltroGanhadorToLegal filtro = new FiltroGanhadorToLegal();
		
		filtro.setNumeroSorteio(numSorteio);
		filtro.setCpf(cpf);
		
		return this.pesquisar(filtro);
	}

}