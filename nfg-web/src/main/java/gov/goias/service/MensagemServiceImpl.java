package gov.goias.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.entidades.Mensagem;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.TipoMensagem;

@Service
public class MensagemServiceImpl implements MensagemService {

	@Override
	public PaginacaoDTO<Mensagem> findMensagensCaixaDeEntrada(Integer max, Integer page, Integer idPessoa) {
		
		Mensagem mensagem = getMensagem();
		
		PaginacaoDTO<Mensagem> pgMs = new PaginacaoDTO<>();
		List<Mensagem> list = new ArrayList<>();
		list.add(mensagem);
		
		pgMs.setList(list);
		pgMs.setCount(list.size());
		
		return pgMs;
	}

	private Mensagem getMensagem() {
		Mensagem mensagem = new Mensagem();
		
		mensagem.setData(new Date());
		mensagem.setId(1);
		mensagem.setListaDestinatariosString("xxxxxx");
		mensagem.setMensagemPublica('S');
		TipoMensagem tipoMensagem = new TipoMensagem();
		tipoMensagem.setNome("Tipo Mock");
		mensagem.setTipoMensagem(tipoMensagem );
		mensagem.setTitulo("Mensagem Mock");
		return mensagem;
	}

	@Override
	public boolean gravarLeituraDasMensagens(PessoaParticipante cidadaoLogado) {
		return true;
	}

	@Override
	public List<DTOContribuinte> retornaQtdMensagensEmpresas(List<DTOContribuinte> resultado) {
		return MockContribuinte.listDTOContriuinte();
	}

	@Override
	public boolean gravarLeituraDasMensagensEmpresas(Integer idPessoa) {
		
		System.out.println("gravarLeituraDasMensagensEmpresas mock");
		return true;
	}

	@Override
	public List<Mensagem> findMensagensCaixaDeEntradaEmpresas(Integer max, Integer page, Integer idPessoa) {
		return getListMensagem();
	}

	private List<Mensagem> getListMensagem() {
		
		List<Mensagem> list = new ArrayList<>();
		
		list.add(getMensagem());
		
		return list;
	}

	@Override
	public Integer countMensagensEmpresas(Integer idPessoa) {
		return 123;
	}

	@Override
	public Integer findNumeroDeMensagensNaoLidasPeloCidadao(PessoaParticipante cidadao) {
		return 123;
	}

}