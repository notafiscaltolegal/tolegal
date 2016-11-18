package gov.goias.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.goias.dtos.DTOMinhasNotas;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.NFEIdentificacao;
import gov.goias.entidades.PessoaParticipante;

public class MockCidadao {

	public static PessoaParticipante getPessoaParticipante(String cpf) {
		PessoaParticipante pessoaParticipante = new PessoaParticipante();
		pessoaParticipante.setDataCadastro(new Date());
		pessoaParticipante.setDataCadastroStr("01/01/2001");
		pessoaParticipante.setEmailPreCadastro("email@email.com");
		pessoaParticipante.setId(1);
		pessoaParticipante.setNomeMaePreCadastro("XXXX XXXX XXXXXX");
		pessoaParticipante.setParticipaSorteio('S');
		pessoaParticipante.setRecebeEmail('S');
		pessoaParticipante.setTelefonePreCadastro("982484197");		
		
		GENPessoaFisica pessoaFisica = new GENPessoaFisica();
		
		pessoaFisica.setCpf(cpf);
		pessoaFisica.setDataDeNascimento(new Date());
		pessoaFisica.setEmail("xxx@xx.com");
		pessoaFisica.setIdPessoa(1);
		pessoaFisica.setNome("XXX XXXXXX XXX");
		pessoaFisica.setNomeDaMae("XXXXX XXXXXX XXX XXXXX");
		pessoaFisica.setNumeroPessoaBase(1234L);
		pessoaFisica.setTelefone("35145107");
		
		pessoaParticipante.setGenPessoaFisica(pessoaFisica);
		return pessoaParticipante;
	}
	
	public static List<DTOMinhasNotas> listDtoMinhasNotas() {
		NFEIdentificacao nfe = new NFEIdentificacao();
		
		nfe.setDataEmissao(new Date());
		nfe.setId(1L);
		nfe.setNomeEmpresa("Casas Bahia Mock");
		nfe.setNumeroNota(123456);
		nfe.setValorNota(1000.20);
		
		DTOMinhasNotas minhasNotas = new DTOMinhasNotas();
		
		minhasNotas.setCnpj("000000000000000");
		minhasNotas.setDetalhe("Detalhe da nota fiscal mock");
		minhasNotas.setEmissao(new Date());
		minhasNotas.setEmpresaCadastrada('S');
		minhasNotas.setEstabelecimento("S");
		minhasNotas.setFantasia("Nome fantasia mock");
		minhasNotas.setInfoSorteioParticipado("Sorteio mockado");
		minhasNotas.setNumero("123456");
		minhasNotas.setQtdePontos(321);
		minhasNotas.setRegistro(new Date());
		minhasNotas.setStatusPontuacao(1);
		minhasNotas.setValor(1234.56);
		
		DTOMinhasNotas minhasNotas2 = new DTOMinhasNotas();
		
		minhasNotas2.setCnpj("11111111111111");
		minhasNotas2.setDetalhe("Detalhe da nota fiscal mock");
		minhasNotas2.setEmissao(new Date());
		minhasNotas2.setEmpresaCadastrada('N');
		minhasNotas2.setEstabelecimento("N");
		minhasNotas2.setFantasia("Casas Bahia mock");
		minhasNotas2.setInfoSorteioParticipado("Sorteio Falso");
		minhasNotas2.setNumero("1234567");
		minhasNotas2.setQtdePontos(123);
		minhasNotas2.setRegistro(new Date());
		minhasNotas2.setStatusPontuacao(2);
		minhasNotas2.setValor(123.4);
		
		List<DTOMinhasNotas> list = new ArrayList<>();
		list.add(minhasNotas);
		list.add(minhasNotas2);
		return list;
	}
}
