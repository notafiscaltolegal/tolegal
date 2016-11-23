package gov.to.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import gov.to.email.EmailDTO;
import gov.to.email.EmailEnum;
import gov.to.email.EmailParametro;
import gov.to.email.EmailSingleton;
import gov.to.email.EmailUtils;
import gov.to.entidade.UsuarioToLegal;
import gov.to.filtro.FiltroUsuarioToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class UsuarioToLegalServiceImpl implements UsuarioToLegalService{
	
	@EJB
	private ConsultasDaoJpa<UsuarioToLegal> reposiroty;
	
	@EJB
	private EmailSingleton emailService;
	
	@Override
	public List<UsuarioToLegal> pesquisar(FiltroUsuarioToLegal filtro, String... hbInitialize) {
		return reposiroty.filtrarPesquisa(filtro, UsuarioToLegal.class, hbInitialize);
	}

	@Override
	public UsuarioToLegal primeiroRegistro(FiltroUsuarioToLegal filtro, String... hbInitialize) {
		return reposiroty.primeiroRegistroPorFiltro(filtro, UsuarioToLegal.class, hbInitialize);
	}

	@Override
	public void enviaEmailConfirmacaoCadastro(UsuarioToLegal usuarioToLegal) {
		
		EmailDTO email = new EmailDTO();
		
		EmailParametro param = new EmailParametro();
		
		param.addParametro("{cpf}", usuarioToLegal.getPessoaFisica().getCpf());
		param.addParametro("{link}", "http://localhost:8080/nfg-web/cidadao/login?hash="+ usuarioToLegal.getHash());
		
		email.setAssunto("Confirmar cadastrado To Legal!");
		email.setMensagem(EmailUtils.formataEmail(EmailEnum.CONFIRMAR_CADASTRO, param));
		email.setPara(usuarioToLegal.getEmail());
		
		emailService.sendMail(email);
	}
	
	public void teste(){
		
	}
}