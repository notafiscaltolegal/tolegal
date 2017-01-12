package gov.to.service;

import java.math.BigInteger;
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
import gov.to.filtro.FiltroMensagemSefazToLegalDTO;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class MensagemSefazToLegalServiceImpl extends ConsultasDaoJpa<MensagemSefazToLegal> implements MensagemSefazToLegalService{
	
	@EJB
	private MensagemVisuCidadaoToLegalService mensagemVisuCidadaoService;
	
	@EJB
	private GenericService<MensagemVisualizadaCidadaoToLegal, Long> serviceCidadaoMsg;
	
	@EJB
	private GenericService<MensagemSefazToLegal, Long> serviceCidadaoSefazMsg;
	
	@Override
	public List<MensagemSefazToLegal> pesquisar(FiltroMensagemSefazToLegalDTO filtro, String... hbInitialize) {
		return filtrarPesquisa(filtro, MensagemSefazToLegal.class, hbInitialize);
	}

	@Override
	public Integer qntMensagemNaoLidaCidadao(String cpf) {
		
		List<Long> listMensagensCidadao = mensagemVisuCidadaoService.ids();
		List<Long> listMensagemCidadaoSefaz = idsCidadao();
		
		if (listMensagemCidadaoSefaz == null || listMensagemCidadaoSefaz.isEmpty()){
			return null;
		}
		
		if (listMensagensCidadao == null || listMensagensCidadao.isEmpty()){
			return listMensagemCidadaoSefaz.size();
		}
		
		if ((listMensagemCidadaoSefaz.size() - listMensagensCidadao.size()) == BigInteger.ZERO.intValue()){
			return null;
		}
		
		return listMensagemCidadaoSefaz.size() - listMensagensCidadao.size();
	}
	
	public List<Long> idsCidadao() {
		
		Criteria criteria = getSession().createCriteria(MensagemSefazToLegal.class);
		
		@SuppressWarnings("unchecked")
		List<Long> ids = (List<Long>) criteria
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("situacao", SituacaoMensagem.ENVIADA))
				.add(Restrictions.eq("destinatario", DestinatarioEnum.CIDADAO))
				.list();
		
		return ids;
	}

	@Override
	public void gravarLeituraDasMensagens(String cpf) {
		
		List<Long> listMensagensCidadao = mensagemVisuCidadaoService.ids();
		List<Long> listMensagemCidadaoSefaz = idsCidadao();
		
		if (listMensagemCidadaoSefaz == null || listMensagemCidadaoSefaz.isEmpty()){
			return;
		}
		
		for (Long id : listMensagemCidadaoSefaz){
			
			if (listMensagensCidadao != null && !listMensagensCidadao.contains(id)){
				
				MensagemVisualizadaCidadaoToLegal msg = new MensagemVisualizadaCidadaoToLegal();
				MensagemSefazToLegal msgSefaz = serviceCidadaoSefazMsg.getById(MensagemSefazToLegal.class, id);
				
				msg.setId(id);
				msg.setCpf(cpf);
				msg.setDataLeitura(msgSefaz.getDataEnvio());
				msg.setMensagem(msgSefaz.getMensagem());
				msg.setTitulo(msgSefaz.getTitulo());
				
				serviceCidadaoMsg.salvar(msg);
			}
		}
		
	}
}