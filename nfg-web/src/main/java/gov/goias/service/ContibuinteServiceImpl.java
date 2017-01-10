package gov.goias.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.entidades.GENEmpresa;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.GENPessoaJuridica;

@Service
public class ContibuinteServiceImpl implements ContribuinteService{
	
	@Override
	public Long countContribuintes(String cnpjBase, Integer numrInscricao) {
		return 123L;
	}

	@Override
	public List<DTOContribuinte> findContribuintes(Integer page, Integer max, String cnpjBase, Integer numrInscricao, String cnpj, String nome) {
		
		return null;
	}

	@Override
	public void alterarPelaEmpresa(GENEmpresa empresaLogada, Integer inscricao, String dataInicio, String redirectUrl) {
		
//		throw new NFGException("Contribuinte n&#225;o possui CNAE autorizado", onException);
//        throw new NFGException("Data de início vigência inválida", onException);
//        throw new NFGException("Data de início vigência inválida", onException);
		
		System.out.println("Alterado Pela Empresa mock");
	}

	@Override
	public void cadastrarPelaEmpresa(GENEmpresa empresaLogada, Integer inscricao, String dataInicio,
			String termoDeAcordo, String redirectUrl) {
	}

	@Override
	public GENPessoaJuridica pessoaJuridicaPorCNPJ(String cnpj) {
		return MockContribuinte.getPesssoaJuridica();
	}

	@Override
	public List<DTOContribuinte> findContribuintesParaContador(Integer page, Integer max, GENPessoaFisica contador, Integer numrInscricao, String cnpj, String nome) {
		return MockContribuinte.listDTOContriuinte();
	}

	@Override
	public Long countContribuintesParaContador(GENPessoaFisica contador, Integer numrInscricao) {
		return 123L;
	}

	@Override
	public void cadastrarPeloContador(GENPessoaFisica contadorLogado, Integer inscricao, String dataInicio, String termoDeAcordo, String redirectUrl) {
		
		System.out.println("cadastrarPeloContador mock");
	}

	@Override
	public void alterarPeloContador(GENPessoaFisica contadorLogado, Integer inscricao, String dataInicio, String redirectUrl) {
		
		System.out.println("alterarPeloContador mock");
	}
}