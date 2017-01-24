package gov.to.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.goias.entidades.Mensagem;
import gov.goias.service.PaginacaoDTO;
import gov.to.dominio.SituacaoMensagem;
import gov.to.entidade.MensagemVisualizadaEmpresaToLegal;
import gov.to.filtro.FiltroMensagemVisuEmpresaToLegalDTO;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class MensagemVisuEmpresaToLegalServiceImpl extends ConsultasDaoJpa<MensagemVisualizadaEmpresaToLegal> implements MensagemVisuEmpresaToLegalService{
	
	@Override
	public List<MensagemVisualizadaEmpresaToLegal> pesquisar(FiltroMensagemVisuEmpresaToLegalDTO filtro, String... hbInitialize) {
		return filtrarPesquisa(filtro, MensagemVisualizadaEmpresaToLegal.class, hbInitialize);
	}

	@Override
	public PaginacaoDTO<Mensagem> findMensagensCaixaDeEntrada(Integer max, Integer page, String inscricaoEstadual) {
		
		FiltroMensagemVisuEmpresaToLegalDTO filtro = new FiltroMensagemVisuEmpresaToLegalDTO();
		filtro.setInscricaoEstadual(inscricaoEstadual);
		
		Criteria criteria = criteriaPesquisa(filtro, MensagemVisualizadaEmpresaToLegal.class);
		
		criteria.addOrder(Order.desc("dataLeitura"));
		
		@SuppressWarnings("unchecked")
		List<MensagemVisualizadaEmpresaToLegal> msgs = (List<MensagemVisualizadaEmpresaToLegal>) criteria.list();
		
		PaginacaoDTO<Mensagem> pg = new PaginacaoDTO<>();
		pg.setCount(msgs.size());
		
		List<Mensagem> list = new ArrayList<>();
		
		int inicio = calcInicio(page, max);
	    int fim = calcPagFim(page, max);
		
		for (int i=inicio; i <= fim; i++){
			
			Mensagem msgDTO = new Mensagem();
			
			if (i == msgs.size()){
				break;
			}
			
			MensagemVisualizadaEmpresaToLegal msg = msgs.get(i);
			
			msgDTO.setData(msg.getDataLeitura());
			msgDTO.setId(msg.getId().intValue());
			msgDTO.setTexto(msg.getMensagem());
			msgDTO.setTitulo(msg.getTitulo());
			msgDTO.setTipoDestinatario('E');
			
			list.add(msgDTO);
		}
		
		pg.setList(list);
		
		return pg;
	}
	
	private static int calcPagFim(Integer page, Integer max) {
		
		return (calcInicio(page, max) + max) -1;
	}

	private static int calcInicio(Integer page, Integer max) {
		
		return (page * max);
	}

	@Override
	public Long qntMensagemAguardandoLeitura(String inscricaoEstadual) {
		
		Criteria criteria = getSession().createCriteria(MensagemVisualizadaEmpresaToLegal.class);
		
		Long ids = (Long) criteria
				.setProjection(Projections.count("id"))
				.add(Restrictions.eq("inscricaoEstadual", inscricaoEstadual))
				.add(Restrictions.eq("situacao", SituacaoMensagem.AGUARDANDO_LEITURA))
				.uniqueResult();
		
		return ids;
	}
}