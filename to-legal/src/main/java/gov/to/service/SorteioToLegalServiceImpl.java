package gov.to.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import gov.goias.entidades.RegraSorteio;
import gov.goias.service.SorteioCidadaoDTO;
import gov.to.dominio.SituacaoSorteio;
import gov.to.entidade.SorteioToLegal;
import gov.to.filtro.FiltroSorteioToLegal;
import gov.to.persistencia.ConsultasDaoJpa;
import gov.to.persistencia.GenericPersistence;

@Stateless
public class SorteioToLegalServiceImpl extends ConsultasDaoJpa<SorteioToLegal> implements SorteioToLegalService{
	
	@EJB
	private GenericPersistence<SorteioToLegal, Long> genericPersistence;
	
	@EJB
	private BilheteToLegalService bilheteService;
	
	@EJB
	private NotaFiscalToLegalService notaFiscalToLegalService;
	
	@EJB
	private PontuacaoToLegalService pontuacaoToLegalService;

	@Override
	public List<RegraSorteio> listRegraSorteioDivulgaResultado() {
		
		List<SorteioToLegal> listSorteio = genericPersistence.listarTodos(SorteioToLegal.class);
		List<RegraSorteio> list = new ArrayList<>();
		
		for (SorteioToLegal stl : listSorteio){
			
			if (SituacaoSorteio.INATIVO.equals(stl.getSituacao())){
				continue;
			}
			
			RegraSorteio rs = new RegraSorteio();
			
			rs.setId(stl.getId().intValue());
			rs.setInformacao(stl.getNumeroSorteio().toString());
			
			list.add(rs);
		}

		return list ;
	}

	@Override
	public SorteioCidadaoDTO carregaDadosDoSorteioParaCidadao(Integer idSorteio, String cpf) {
		
		SorteioCidadaoDTO sorteioDTO = new SorteioCidadaoDTO();
		
		RegraSorteio regraSorteio = regraSorteioPorIdSorteioToLegal(idSorteio);
		
		sorteioDTO.setTotalBilhetes(bilheteService.totalBilheteSorteioPorCpf(cpf, idSorteio));
		sorteioDTO.setTotalDocs(notaFiscalToLegalService.totalNotasPorCpf(cpf,idSorteio));
		sorteioDTO.setTotalPontos(pontuacaoToLegalService.totalPontosSorteioPorCpf(cpf, idSorteio));
		sorteioDTO.setSorteio(regraSorteio);
		
		return sorteioDTO;
	}

	private RegraSorteio regraSorteioPorIdSorteioToLegal(Integer idSorteio) {
		SorteioToLegal sorteioToLegal = genericPersistence.getById(SorteioToLegal.class, idSorteio.longValue());
		
		RegraSorteio regraSorteio = new RegraSorteio();
		
		//Regra do sistema de goiás nao utilizado no to legal
		//1 - aguardando geracao bilhete
		//2 - gerando bilhete
		//3 - Mostra os pontos
		
		regraSorteio.setStatus(3);
		regraSorteio.setNumeroLoteria(sorteioToLegal.getNumeroExtracao());
		regraSorteio.setRealizadoBoolean(sorteioToLegal.isSorteioRealizado());
		regraSorteio.setDataExtracaoLoteria(sorteioToLegal.getDataExtracaoLoteria());
		regraSorteio.setDataRealizacao(sorteioToLegal.getDataSorteio());
		regraSorteio.setInformacao(sorteioToLegal.getNumeroSorteio().toString());
		
		return regraSorteio;
	}

	@Override
	public Integer ultimoSorteio() {
		Criteria criteria = getSession().createCriteria(SorteioToLegal.class);
		
		Integer max = (Integer) criteria
				.setProjection(Projections.max("numeroSorteio"))
				.uniqueResult();
		
		if (max == null)
			max = BigInteger.ZERO.intValue();
		
		return max;
	}

	@Override
	public RegraSorteio sorteioPorId(Integer idSorteio) {
		return regraSorteioPorIdSorteioToLegal(idSorteio);
	}

	@Override
	public SorteioToLegal sorteioAtual() {

		int numSorteio = this.ultimoSorteio();
		
		if (numSorteio == BigInteger.ZERO.intValue()){
			return null;
		}
		
		FiltroSorteioToLegal filtro = new FiltroSorteioToLegal();
		
		filtro.setNumeroSorteio(numSorteio);
		
		return primeiroRegistroPorFiltro(filtro, SorteioToLegal.class);
	}
}