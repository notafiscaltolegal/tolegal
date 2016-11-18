package gov.goias.service;

import java.util.Date;
import java.util.List;

import entidade.endereco.Endereco;
import gov.goias.dtos.DTOMinhasNotas;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.exceptions.NFGException;
import gov.to.dto.RespostaReceitaFederalDTO;
import gov.to.entidade.EnderecoToLegal;

public interface CidadaoService {

	PessoaParticipanteDTO autenticaCidadao(String cpf, String senha);

	PessoaParticipante pessoaParticipantePorId(Integer idCidadao);

	EnderecoToLegal consultaCepCadastrado(String cpf);

	boolean usuarioPremiado(Integer id);

	List<DTOMinhasNotas> documentosFiscaisPorCpf(String cpfFiltro, Date dataInicial, Date dataFinal, Integer max, Integer page);

	PessoaParticipante pessoaParticipantePorCPF(String cpf);

	Integer numeroDeNotasPorCpf(String cpf);

	List<DTOMinhasNotas> documentosFiscaisPorCpf(String cpfFiltro);

	boolean emailCadastrado(String email, String cpf);

	PessoaParticipante atualizaPerfil(PessoaParticipante pessoaParticipante);

	void gravaNovaSenha(String cpf, String senha);

	String enviaEmailRecuperacaoSenha(String cpf);

	RespostaReceitaFederalDTO validaPessoaReceitaFederal(String cpf);

	boolean emailCadastrado(String email);

	void preCadastroCidadao(PessoaParticipante pessoaParticipante, Endereco endereco, String senha) throws NFGException;

	void ativarCadastro(String cpf, String email);

	void alterarSenha(String cpf, String senhaPerfilAntiga, String senhaPerfilNova, String senhaPerfilConfirm);
}
