package gov.goias.service;

import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.GENEmpresa;
import gov.goias.entidades.GENPessoaFisica;

public interface EmpresaService {

	String nomeFantasiaPelaInscricao(Integer inscricao);

	String nomeFantasiaPeloCnpj(String numeroCnpjEmpresa);

	GENEmpresa findByCnpj(String pjCnpj);

	boolean inscricaoCompativelContador(GENPessoaFisica contadorLogado, Integer inscricao);

	boolean inscricaoCompativelContribuinte(GENEmpresa empresaLogada, Integer inscricao);

	boolean inscricaoEstadualValida(Integer inscricaoEstadual);

	CCEContribuinte contribuintePorInstricaoEstadual(Integer inscricaoEstadual);

}