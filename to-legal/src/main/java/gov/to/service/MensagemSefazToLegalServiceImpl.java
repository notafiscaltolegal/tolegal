package gov.to.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.to.dominio.DestinatarioEnum;
import gov.to.dominio.SituacaoMensagem;
import gov.to.entidade.MensagemSefazToLegal;
import gov.to.entidade.MensagemVisualizadaCidadaoToLegal;
import gov.to.entidade.MensagemVisualizadaEmpresaToLegal;
import gov.to.filtro.FiltroMensagemSefazToLegalDTO;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class MensagemSefazToLegalServiceImpl extends ConsultasDaoJpa<MensagemSefazToLegal> implements MensagemSefazToLegalService{
	
	@EJB
	private MensagemVisuCidadaoToLegalService mensagemVisuCidadaoService;
	
	@EJB
	private GenericService<MensagemVisualizadaCidadaoToLegal, Long> serviceCidadaoMsg;
	
	@EJB
	private MensagemVisuEmpresaToLegalService mensagemVisuEmpresaToLegalService;
	
	@EJB
	private GenericService<MensagemVisualizadaEmpresaToLegal, Long> serviceEmpresaMsg;
	
	@EJB
	private GenericService<MensagemSefazToLegal, Long> serviceSefazMsg;
	
	@Override
	public List<MensagemSefazToLegal> pesquisar(FiltroMensagemSefazToLegalDTO filtro, String... hbInitialize) {
		return filtrarPesquisa(filtro, MensagemSefazToLegal.class, hbInitialize);
	}

	@Override
	public Integer qntMensagemNaoLidaCidadao(String cpf) {
		
		atualizaMensagemCidadaoComSefaz(cpf);
		
		Long qnt = mensagemVisuCidadaoService.qntMensagemAguardandoLeitura(cpf);
		
		if (qnt == null || qnt == BigInteger.ZERO.intValue()){
			return null;
		}
		
		return qnt.intValue();
	}
	
	public void atualizaMensagemCidadaoComSefaz(String cpf) {
		
		Criteria criteria = getSession().createCriteria(MensagemSefazToLegal.class);
		
		Criteria criteriaCidadao = getSession().createCriteria(MensagemVisualizadaCidadaoToLegal.class);
		
		@SuppressWarnings("unchecked")
		List<Long> ids = (List<Long>) criteria
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("situacao", SituacaoMensagem.ENVIADA))
				.add(Restrictions.eq("destinatario", DestinatarioEnum.CIDADAO))
				.list();
		
		if (ids.isEmpty()){
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<MensagemVisualizadaCidadaoToLegal> idsMsgCidadao = (List<MensagemVisualizadaCidadaoToLegal>) criteriaCidadao
				.createAlias("msgSefazToLegal", "msgSefazToLegal")
				.add(Restrictions.in("msgSefazToLegal.id", ids))
				.list();
		
		List<Long> idsCidadao = new ArrayList<>();
		
		for (MensagemVisualizadaCidadaoToLegal msgCidadao : idsMsgCidadao){
			idsCidadao.add(msgCidadao.getId());
		}
		
		for (Long idMsgSefaz : ids){
			
			if (!idsCidadao.contains(idMsgSefaz)){
				
				MensagemVisualizadaCidadaoToLegal msg = new MensagemVisualizadaCidadaoToLegal();
				MensagemSefazToLegal msgSefaz = serviceSefazMsg.getById(MensagemSefazToLegal.class, idMsgSefaz);
				
				msg.setCpf(cpf);
				msg.setDataLeitura(msgSefaz.getDataEnvio());
				msg.setMensagem(msgSefaz.getMensagem());
				msg.setTitulo(msgSefaz.getTitulo());
				msg.setSituacao(SituacaoMensagem.AGUARDANDO_LEITURA);
				
				serviceCidadaoMsg.salvar(msg);
			}
		}
	}
	
	public void atualizaMensagemEmpresaComSefaz(String inscricaoEstadual) {
		
		Criteria criteria = getSession().createCriteria(MensagemSefazToLegal.class);
		
		Criteria criteriaCidadao = getSession().createCriteria(MensagemVisualizadaEmpresaToLegal.class);
		
		@SuppressWarnings("unchecked")
		List<Long> ids = (List<Long>) criteria
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("situacao", SituacaoMensagem.ENVIADA))
				.add(Restrictions.eq("destinatario", DestinatarioEnum.EMPRESA))
				.list();
		
		if (ids.isEmpty()){
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<MensagemVisualizadaEmpresaToLegal> idsMsgEmpresa = (List<MensagemVisualizadaEmpresaToLegal>) criteriaCidadao
				.createAlias("msgSefazToLegal", "msgSefazToLegal")
				.add(Restrictions.in("msgSefazToLegal.id", ids))
				.list();
		
		List<Long> idsEmpresa = new ArrayList<>();
		
		for (MensagemVisualizadaEmpresaToLegal msgCidadao : idsMsgEmpresa){
			idsEmpresa.add(msgCidadao.getId());
		}
		
		for (Long idMsgSefaz : ids){
			
			if (!idsEmpresa.contains(idMsgSefaz)){
				
				MensagemVisualizadaEmpresaToLegal msg = new MensagemVisualizadaEmpresaToLegal();
				MensagemSefazToLegal msgSefaz = serviceSefazMsg.getById(MensagemSefazToLegal.class, idMsgSefaz);
				
				msg.setInscricaoEstadual(inscricaoEstadual);
				msg.setDataLeitura(msgSefaz.getDataEnvio());
				msg.setMensagem(msgSefaz.getMensagem());
				msg.setTitulo(msgSefaz.getTitulo());
				msg.setSituacao(SituacaoMensagem.AGUARDANDO_LEITURA);
				
				this.serviceEmpresaMsg.salvar(msg);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void gravarLeituraDasMensagens(String cpf) {
		
		Criteria criteria = getSession().createCriteria(MensagemVisualizadaCidadaoToLegal.class);
		
		List<MensagemVisualizadaCidadaoToLegal> msgs = (List<MensagemVisualizadaCidadaoToLegal>) criteria
				.add(Restrictions.eq("situacao", SituacaoMensagem.AGUARDANDO_LEITURA))
				.list();
		
		for (MensagemVisualizadaCidadaoToLegal msg : msgs){
			
			msg.setSituacao(SituacaoMensagem.LIDA);
			
			this.serviceCidadaoMsg.merge(msg);
		}
	}

	@Override
	public Integer qntMensagemNaoLidaEmpresa(String inscricaoEstadual) {
		
		atualizaMensagemEmpresaComSefaz(inscricaoEstadual);
		
		Long qnt = mensagemVisuEmpresaToLegalService.qntMensagemAguardandoLeitura(inscricaoEstadual);
		
		if (qnt == null || qnt == BigInteger.ZERO.intValue()){
			return null;
		}
		
		return qnt.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void gravarLeituraDasMensagensEmpresa(String inscricaoEstadual) {
		
		Criteria criteria = getSession().createCriteria(MensagemVisualizadaEmpresaToLegal.class);
		
		List<MensagemVisualizadaEmpresaToLegal> msgs = (List<MensagemVisualizadaEmpresaToLegal>) criteria
				.add(Restrictions.eq("situacao", SituacaoMensagem.AGUARDANDO_LEITURA))
				.list();
		
		for (MensagemVisualizadaEmpresaToLegal msg : msgs){
			
			msg.setSituacao(SituacaoMensagem.LIDA);
			
			this.serviceEmpresaMsg.merge(msg);
		}
	}
}