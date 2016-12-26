package gov.goias.service;

import org.springframework.stereotype.Service;

import gov.goias.entidades.GENPessoaFisica;

@Service
public class PessoaFisicaServiceImpl implements PessoaFisicaService{

	@Override
	public GENPessoaFisica findByCpf(String cpf) {
		
		return MockCidadao.getPessoaParticipante("03066547175").getGenPessoaFisica();
	}

}