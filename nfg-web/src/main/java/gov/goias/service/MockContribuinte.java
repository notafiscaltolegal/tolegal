package gov.goias.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.goias.dtos.DTOCnaeAutorizado;
import gov.goias.dtos.DTOContribuinte;
import gov.goias.entidades.CCEContribuinte;
import gov.goias.entidades.GENPessoa;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.GENPessoaJuridica;

public class MockContribuinte {

	public static List<DTOContribuinte> listDTOContriuinte() {
		List<DTOContribuinte> listContribuintes = new ArrayList<>();
		
		DTOContribuinte dt = new DTOContribuinte();
		
		dt.setDataCredenciamento(new Date());
		dt.setDataEfetivaParticipacao(new Date());
		dt.setDataObrigatoriedadeCnae(new Date());
		dt.setIdPessoa(1);
		dt.setIndiResponsavelAdesao('S');
		dt.setIsEmissorEFD(true);
		dt.setIsEmpresaParticipante(true);
		dt.setListaCnaeAutorizado(getListCnaeAutorizado());
		dt.setNomeEmpresa("Empresa mock");
		dt.setNumeroCnpj("12345678912345");
		dt.setNumeroInscricao(14234567);
		dt.setQtdMensagens(10);
		dt.setQtdReclamacoes(123);
		
		listContribuintes.add(dt);
		return listContribuintes;
	}
	
	public static ArrayList<DTOCnaeAutorizado> getListCnaeAutorizado() {
		
		ArrayList<DTOCnaeAutorizado> list = new ArrayList<>();
		
		DTOCnaeAutorizado dt = new DTOCnaeAutorizado();
		
		dt.setId(1L);
		dt.setIdSubClasseCnae(2L);
		dt.setIsCnaeAutorizado(true);
		dt.setIsCnaeObrigatorio(true);
		
		list.add(dt);
		
		return list;
	}

	public static CCEContribuinte getCCEContribuinte() {
		
		CCEContribuinte cce = new CCEContribuinte();
		
		cce.setDataCadastramento(new Date());
		cce.setDataEmissaoExtratoCadastral(new Date());
		cce.setDataFinalContratoContador(new Date());
		cce.setDataInicioContratoContador(new Date());
		cce.setDataInicioPrecariedade(new Date());
		cce.setDataPrazoCerto(new Date());
		cce.setIdContador(123L);
		cce.setIndicacaoSituacaoCadastral('A');
		cce.setInfoContribuinte("Contribuinte Mock");
		cce.setNumeroExtratoCadastral(12345L);
		cce.setNumeroInscricao(1234567);
		cce.setNumeroValidadorExtratoCadastro(12354);
		cce.setTipoContratoContador("Mock");
		cce.setPessoa(getPessoa());
		
		return cce;
	}

	public static GENPessoa getPessoa() {
		
		GENPessoa pessoa = new GENPessoa();
		
		pessoa.setIdPessoa(123);
		pessoa.setPessoaFisica(getPessoaFisica());
		pessoa.setPessoaJuridica(getPesssoaJuridica());
		pessoa.setTipoPessoa('J');
		
		return pessoa;
	}

	public static GENPessoaJuridica getPesssoaJuridica() {
		
		GENPessoaJuridica genPessoaJuridica = new GENPessoaJuridica();
		
		genPessoaJuridica.setCodigoNire(new BigInteger("12345"));
		genPessoaJuridica.setIdPessoa(123);
		genPessoaJuridica.setIndiHomologacaoCadastro('S');
		genPessoaJuridica.setNomeFantasia("Empresa Mock");
		genPessoaJuridica.setNumeroCnpj("12345678912345");
		genPessoaJuridica.setNumeroInscricaoMunicipal(new BigInteger("12345678"));
		genPessoaJuridica.setPessoaFisica(getPessoaFisica());
		genPessoaJuridica.setTipoPessoa('J');
		genPessoaJuridica.setTipoPessoaJuridica('S');
		
		return genPessoaJuridica;
	}

	public static GENPessoaFisica getPessoaFisica() {
		return MockCidadao.getPessoaParticipante("12345678912").getGenPessoaFisica();
	}

}
