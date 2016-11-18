package gov.goias.service;

import gov.goias.entidades.GENPessoaFisica;

public interface PessoaFisicaService {

	GENPessoaFisica findByCpf(String cpf);


}