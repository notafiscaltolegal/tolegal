package gov.to.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.PessoaParticipante;
import gov.to.email.EmailDTO;
import gov.to.email.EmailEnum;
import gov.to.email.EmailParametro;
import gov.to.email.EmailSingleton;
import gov.to.email.EmailUtils;
import gov.to.entidade.UsuarioToLegal;
import gov.to.filtro.FiltroUsuarioToLegal;
import gov.to.persistencia.ConsultasDaoJpa;
import gov.to.persistencia.GenericPersistence;

@Stateless
public class UsuarioToLegalServiceImpl implements UsuarioToLegalService{
	
	@EJB
	private ConsultasDaoJpa<UsuarioToLegal> reposiroty;
	
	@EJB
	private GenericPersistence<UsuarioToLegal, Long> genericPersistence;
	
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

	@Override
	public UsuarioToLegal usuarioPorCpf(String cpf) {
		
		FiltroUsuarioToLegal filtro = new FiltroUsuarioToLegal();
		
		filtro.setCpf(cpf);
		
		return primeiroRegistro(filtro, "pessoaFisica","pessoaFisica.endereco");
	}

	@Override
	public PessoaParticipante atualizaPerfil(UsuarioToLegal usuarioToLegal) {
		
		genericPersistence.merge(usuarioToLegal);
		
		return converteParaPessoaParticipante(usuarioToLegal);
	}

	private PessoaParticipante converteParaPessoaParticipante(UsuarioToLegal usuarioToLegal) {
		
		PessoaParticipante pessoaParticipante = new PessoaParticipante();
		pessoaParticipante.setDataCadastro(usuarioToLegal.getDataCadastro());
		pessoaParticipante.setEmailPreCadastro(usuarioToLegal.getEmail());
		pessoaParticipante.setId(usuarioToLegal.getId().intValue());
		pessoaParticipante.setNomeMaePreCadastro(usuarioToLegal.getPessoaFisica().getNomeMae());
		pessoaParticipante.setParticipaSorteio(usuarioToLegal.getParticipaSorteioFormat());
		pessoaParticipante.setRecebeEmail(usuarioToLegal.getRecebeEmailFormat());
		pessoaParticipante.setTelefonePreCadastro(usuarioToLegal.getTelefone());		
		
		GENPessoaFisica pessoaFisica = new GENPessoaFisica();
		
		pessoaFisica.setCpf(usuarioToLegal.getPessoaFisica().getCpf());
		pessoaFisica.setDataDeNascimento(usuarioToLegal.getPessoaFisica().getDataNascimento());
		pessoaFisica.setEmail(usuarioToLegal.getEmail());
		pessoaFisica.setIdPessoa(usuarioToLegal.getPessoaFisica().getId().intValue());
		pessoaFisica.setNome(usuarioToLegal.getPessoaFisica().getNome());
		pessoaFisica.setNomeDaMae(usuarioToLegal.getPessoaFisica().getNomeMae());
		pessoaFisica.setNumeroPessoaBase(usuarioToLegal.getPessoaFisica().getId());
		pessoaFisica.setTelefone(usuarioToLegal.getTelefone());
		
		pessoaParticipante.setGenPessoaFisica(pessoaFisica);
		return pessoaParticipante;
	}
}