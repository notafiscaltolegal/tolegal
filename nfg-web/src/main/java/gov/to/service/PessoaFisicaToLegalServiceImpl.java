package gov.to.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import gov.to.entidade.EnderecoToLegal;
import gov.to.entidade.PessoaFisicaToLegal;
import gov.to.filtro.FiltroPessoaFisicaToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class PessoaFisicaToLegalServiceImpl implements PessoaFisicaToLegalService{
	
	@EJB
	private ConsultasDaoJpa<PessoaFisicaToLegal> reposiroty;

	@Override
	public List<PessoaFisicaToLegal> pesquisar(FiltroPessoaFisicaToLegal filtro, String... hbInitialize) {
		return reposiroty.filtrarPesquisa(filtro, PessoaFisicaToLegal.class, hbInitialize);
	}

	@Override
	public EnderecoToLegal enderecoPorCpf(String cpf) {
		
		FiltroPessoaFisicaToLegal filtro = new FiltroPessoaFisicaToLegal();
		
		filtro.setCpf(cpf);
		
		PessoaFisicaToLegal pessoa = reposiroty.primeiroRegistroPorFiltro(filtro, PessoaFisicaToLegal.class, "endereco","endereco.municipio");
		
		if (pessoa == null){
			return null;
		}
		
		return pessoa.getEndereco();
	}

	@Override
	public PessoaFisicaToLegal primeiroRegistro(FiltroPessoaFisicaToLegal filtro, String string, String string2) {
		return reposiroty.primeiroRegistroPorFiltro(filtro, PessoaFisicaToLegal.class, "endereco","endereco.municipio");
	}
}