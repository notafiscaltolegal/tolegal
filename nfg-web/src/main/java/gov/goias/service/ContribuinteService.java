package gov.goias.service;

import java.util.List;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.entidades.GENEmpresa;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.GENPessoaJuridica;

public interface ContribuinteService {

	Long countContribuintes(String cnpjBase, Integer numrInscricao);

	List<DTOContribuinte> findContribuintes(Integer page, Integer max, String cnpjBase, Integer numrInscricao,
			String cnpj, String nome);

	void alterarPelaEmpresa(GENEmpresa empresaLogada, Integer inscricao, String dataInicio, String redirectUrl);

	void cadastrarPelaEmpresa(GENEmpresa empresaLogada, Integer inscricao, String dataInicio, String termoDeAcordo,
			String redirectUrl);

	GENPessoaJuridica pessoaJuridicaPorCNPJ(String cnpj);

	List<DTOContribuinte> findContribuintesParaContador(Integer page, Integer max, GENPessoaFisica contador,
			Integer numrInscricao, String cnpj, String nome);

	Long countContribuintesParaContador(GENPessoaFisica contador, Integer numrInscricao);

	void cadastrarPeloContador(GENPessoaFisica contadorLogado, Integer inscricao, String dataInicio,
			String termoDeAcordo, String redirectUrl);

	void alterarPeloContador(GENPessoaFisica contadorLogado, Integer inscricao, String dataInicio, String redirectUrl);


}