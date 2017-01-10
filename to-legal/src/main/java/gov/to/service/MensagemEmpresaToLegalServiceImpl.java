package gov.to.service;

import java.util.List;

import javax.ejb.Stateless;

import gov.to.entidade.MensagemEmpresaToLegal;
import gov.to.filtro.FiltroMensagemEmpresaToLegalDTO;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class MensagemEmpresaToLegalServiceImpl extends ConsultasDaoJpa<MensagemEmpresaToLegal> implements MensagemEmpresaToLegalService{
	
	@Override
	public List<MensagemEmpresaToLegal> pesquisar(FiltroMensagemEmpresaToLegalDTO filtro, String... hbInitialize) {
		return filtrarPesquisa(filtro, MensagemEmpresaToLegal.class, hbInitialize);
	}
}