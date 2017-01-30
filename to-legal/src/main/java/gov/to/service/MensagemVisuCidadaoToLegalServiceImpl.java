package gov.to.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.goias.entidades.Mensagem;
import gov.goias.service.PaginacaoDTO;
import gov.to.dominio.SituacaoMensagem;
import gov.to.entidade.MensagemVisualizadaCidadaoToLegal;
import gov.to.entidade.NotaEmpresaToLegal;
import gov.to.filtro.FiltroMensagemVisuCidadaoToLegalDTO;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class MensagemVisuCidadaoToLegalServiceImpl extends ConsultasDaoJpa<MensagemVisualizadaCidadaoToLegal> implements MensagemVisuCidadaoToLegalService{
	
	@EJB
	private GenericService<MensagemVisualizadaCidadaoToLegal, Long> servicoMensagem;
	
	@Override
	public List<MensagemVisualizadaCidadaoToLegal> pesquisar(FiltroMensagemVisuCidadaoToLegalDTO filtro, String... hbInitialize) {
		return filtrarPesquisa(filtro, MensagemVisualizadaCidadaoToLegal.class, hbInitialize);
	}

	@Override
	public Long qntMensagemAguardandoLeitura(String cpf) {
		
		Criteria criteria = getSession().createCriteria(MensagemVisualizadaCidadaoToLegal.class);
		
		Long ids = (Long) criteria
				.setProjection(Projections.count("id"))
				.add(Restrictions.eq("cpf", cpf))
				.add(Restrictions.eq("situacao", SituacaoMensagem.AGUARDANDO_LEITURA))
				.uniqueResult();
		
		return ids;
	}

	@Override
	public PaginacaoDTO<Mensagem> findMensagensCaixaDeEntrada(Integer max, Integer page, String cpf) {
		
		FiltroMensagemVisuCidadaoToLegalDTO filtro = new FiltroMensagemVisuCidadaoToLegalDTO();
		filtro.setCpf(cpf);
		
		Criteria criteria = criteriaPesquisa(filtro, MensagemVisualizadaCidadaoToLegal.class);
		
		criteria.addOrder(Order.desc("dataLeitura"));
		
		@SuppressWarnings("unchecked")
		List<MensagemVisualizadaCidadaoToLegal> msgs = (List<MensagemVisualizadaCidadaoToLegal>) criteria.list();
		
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
			
			MensagemVisualizadaCidadaoToLegal msgCidadao = msgs.get(i);
			
			msgDTO.setData(msgCidadao.getDataLeitura());
			msgDTO.setId(msgCidadao.getId().intValue());
			msgDTO.setTexto(msgCidadao.getMensagem());
			msgDTO.setTitulo(msgCidadao.getTitulo());
			msgDTO.setTipoDestinatario('C');
			
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
	public void cadastrarMensagem(Date dataLeitura, String titulo, String mensagem, String cpf, String cnpj) {		
		MensagemVisualizadaCidadaoToLegal msg=new MensagemVisualizadaCidadaoToLegal();
	    msg.setCpf(cpf);
	    msg.setMensagem(mensagem);
	    msg.setTitulo(titulo);
	    msg.setDataLeitura(dataLeitura);
	    msg.setSituacao(SituacaoMensagem.AGUARDANDO_LEITURA);
	    
		servicoMensagem.salvar(msg);
	}
}