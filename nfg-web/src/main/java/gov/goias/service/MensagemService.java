package gov.goias.service;

import java.util.List;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.entidades.Mensagem;
import gov.goias.entidades.PessoaParticipante;

public interface MensagemService {

	PaginacaoDTO<Mensagem> findMensagensCaixaDeEntrada(Integer max, Integer page, Integer idPessoa);

	boolean gravarLeituraDasMensagens(PessoaParticipante cidadaoLogado);

	List<DTOContribuinte> retornaQtdMensagensEmpresas(List<DTOContribuinte> resultado);

	boolean gravarLeituraDasMensagensEmpresas(Integer idPessoa);

	List<Mensagem> findMensagensCaixaDeEntradaEmpresas(Integer max, Integer page, Integer idPessoa);

	Integer countMensagensEmpresas(Integer idPessoa);

	Integer findNumeroDeMensagensNaoLidasPeloCidadao(PessoaParticipante cidadao);

}