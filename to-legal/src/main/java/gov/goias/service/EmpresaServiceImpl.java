package gov.goias.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.GENEmpresa;
import gov.goias.entidades.GENPessoaFisica;

@Service
public class EmpresaServiceImpl implements EmpresaService{

	@Override
	public String nomeFantasiaPelaInscricao(Integer inscricao) {
		return "Lojas Americanas Mock";
	}

	@Override
	public String nomeFantasiaPeloCnpj(String numeroCnpjEmpresa) {
		return "Posto de Gasolina Mock";
	}

	@Override
	public GENEmpresa findByCnpj(String pjCnpj) {
		
		GENEmpresa genEmpresa = new GENEmpresa();
		
		genEmpresa.setCodigoNaturezaJuridica(123);
		genEmpresa.setCodigoNireEmpresa(123L);
		genEmpresa.setDataConstituicao(new Date());
		genEmpresa.setIndiHomologacaoCadastro('N');
		genEmpresa.setIndiOrigemEmpresa('A');
		genEmpresa.setIndiSituacaoEmpresaJuceg(213);
		genEmpresa.setNomeEmpresa("Empresa Mock");
		genEmpresa.setNumeroCnpjBase("71408353000173");
		genEmpresa.setValorCapitalSocial(123456.78);
		
		return genEmpresa;
	}

	@Override
	public boolean inscricaoCompativelContador(GENPessoaFisica contadorLogado, Integer inscricao) {
		return false;
	}

	@Override
	public boolean inscricaoCompativelContribuinte(GENEmpresa empresaLogada, Integer inscricao) {
		return true;
	}

	@Override
	public boolean inscricaoEstadualValida(Integer inscricaoEstadual) {
		return true;
	}

	@Override
	public CCEContribuinte contribuintePorInstricaoEstadual(Integer inscricaoEstadual) {
		
		return MockContribuinte.getCCEContribuinte();
	}
}