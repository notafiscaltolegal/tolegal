package gov.goias.service;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.goias.bindings.PessoaPerfil3;
import gov.goias.dtos.DTOMinhasNotas;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.exceptions.NFGException;
import gov.goias.util.Encrypter;
import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.dominio.SituacaoUsuario;
import gov.to.dto.PontuacaoDTO;
import gov.to.dto.RespostaReceitaFederalDTO;
import gov.to.entidade.ContribuinteToLegal;
import gov.to.entidade.EnderecoToLegal;
import gov.to.entidade.NotaEmpresaToLegal;
import gov.to.entidade.NotaFiscalToLegal;
import gov.to.entidade.PessoaFisicaToLegal;
import gov.to.entidade.PontuacaoBonusToLegal;
import gov.to.entidade.PontuacaoToLegal;
import gov.to.entidade.UsuarioToLegal;
import gov.to.filtro.FiltroContribuinteToLegal;
import gov.to.filtro.FiltroNotaEmpresaToLegal;
import gov.to.filtro.FiltroNotaFiscalToLegal;
import gov.to.filtro.FiltroPontuacaoToLegal;
import gov.to.filtro.FiltroUsuarioToLegal;
import gov.to.persistencia.DataFiltroBetween;
import gov.to.properties.SorteioProperties;
import gov.to.service.BilheteToLegalService;
import gov.to.service.GenericService;
import gov.to.service.NotaEmpresaService;
import gov.to.service.NotaFiscalToLegalService;
import gov.to.service.PessoaFisicaToLegalService;
import gov.to.service.PontuacaoToLegalService;
import gov.to.service.SorteioToLegalService;
import gov.to.service.UsuarioToLegalService;

@Service
public class CidadaoServiceImpl implements CidadaoService{
	
	@Autowired
	private PessoaFisicaToLegalService servicePessoaFisica;
	
	@Autowired
	private UsuarioToLegalService serviceUsuario;
	
	@Autowired
	private SorteioToLegalService sorteioToLegalService;
	
	@Autowired
	private NotaFiscalToLegalService notaFiscalToLegalService;
	
	@Autowired
	private BilheteToLegalService bilheteToLegalService;
	
	@Autowired
	private PontuacaoToLegalService pontuacaoToLegalService;
	
	@Autowired
	private GenericService<UsuarioToLegal, Long> genericService;
	
	@Autowired
	private GenericService<PontuacaoBonusToLegal, Long> genericServicePontuacaoBonus;
	
	@Autowired
	private NotaEmpresaService notaEmpresaService;
	
	@Autowired
	private GenericService<ContribuinteToLegal, String> genericServiceContribuinte;
	
	public PessoaParticipanteDTO consultaWebSeviceReceita(String cpf){
		
//		 Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//	     marshaller.setContextPath("gov.goias.bindings");
//
//	     InfoConvService infoConvService = new InfoConvService();
//	     infoConvService.setDefaultUri("https://infoconv.receita.fazenda.gov.br/ws/cpf/ConsultarCPF.asmx");
//	     infoConvService.setMarshaller(marshaller);
//	     infoConvService.setUnmarshaller(marshaller);
//
	     PessoaPerfil3 pessoaReceitaFederal = new PessoaPerfil3();
	     pessoaReceitaFederal.setCPF("123455678912");

	     try{
	    	 
//	         pessoaReceitaFederal = infoConvService.consultaPorCpf(cpf);
	         
	         return converteParaPesssoaParticipanteDTO(pessoaReceitaFederal);
	         
	     }catch (Exception e){
	         e.printStackTrace();
	     }
	     
	     return null;
	}
	
	private PessoaParticipanteDTO converteParaPesssoaParticipanteDTO(PessoaPerfil3 pessoaReceitaFederal) {
		
		
		PessoaParticipanteDTO pessoaParticipanteDTO = new PessoaParticipanteDTO();
		
		PessoaParticipante pessoaParticipante = MockCidadao.getPessoaParticipante(pessoaReceitaFederal.getCPF());
		
		pessoaParticipanteDTO.setPessoaParticipante(pessoaParticipante);
		pessoaParticipanteDTO.setPossuiCredencial(true);
		pessoaParticipanteDTO.setErro(null);
		
		return pessoaParticipanteDTO;
	}

	@Override
	public PessoaParticipanteDTO autenticaCidadao(String cpf, String senha) {
		
		FiltroUsuarioToLegal filtro = new FiltroUsuarioToLegal();
		
		filtro.setCpf(cpf);
		
		UsuarioToLegal usuario = serviceUsuario.primeiroRegistro(filtro,"pessoaFisica","pessoaFisica.endereco");
		
		return converteUsuarioToLegalFromPessoaParticipanteDTO(usuario, senha);
	}


	private PessoaParticipanteDTO converteUsuarioToLegalFromPessoaParticipanteDTO(UsuarioToLegal usuario, String senha) {

		PessoaParticipanteDTO pessoaParticipanteDTO = new PessoaParticipanteDTO();
		
		if (usuario == null){
			
			pessoaParticipanteDTO.setErro("Usuário não encontrado.");
			
		}else if (!usuario.getSenha().equals(Encrypter.encryptMD5(senha))){
			
			pessoaParticipanteDTO.setErro("Senha incorreta.");
			
		}else if (usuario.getSituacao().equals(SituacaoUsuario.INATIVO)){
			
			pessoaParticipanteDTO.setErro("Usuário inativo, favor realizar a ativação no e-mail cadastrado.");
			
		}else{
			
			PessoaParticipante pess = new PessoaParticipante();
			
			pess.setDataCadastro(usuario.getDataCadastro());
			pess.setEmailPreCadastro(usuario.getEmail());
			pess.setNomeMaePreCadastro(usuario.getPessoaFisica().getNomeMae());
			pess.setGenPessoaFisica(convertePessoaFisicaToLegal(usuario));
			pess.setId(usuario.getId().intValue());
			pess.setParticipaSorteio(booleanToChar(usuario.getParticipaSorteio()));
			pess.setRecebeEmail(booleanToChar(usuario.getRecebeEmail()));
			pess.setTelefonePreCadastro(usuario.getTelefone());
			
			pessoaParticipanteDTO.setPessoaParticipante(pess);
			pessoaParticipanteDTO.setUsuarioToLegal(usuario);
		}
		
		return pessoaParticipanteDTO;
	}

	private char booleanToChar(Boolean bol) {
		return bol ? 'S' : 'N';
	}

	private GENPessoaFisica convertePessoaFisicaToLegal(UsuarioToLegal usuarioToLegal) {
		
		GENPessoaFisica gen = new GENPessoaFisica();
		
		gen.setCpf(usuarioToLegal.getPessoaFisica().getCpf());
		gen.setDataDeNascimento(usuarioToLegal.getPessoaFisica().getDataNascimento());
		gen.setIdPessoa(usuarioToLegal.getPessoaFisica().getId().intValue());
		gen.setNome(usuarioToLegal.getPessoaFisica().getNome());
		gen.setNomeDaMae(usuarioToLegal.getPessoaFisica().getNomeMae());
		gen.setEmail(usuarioToLegal.getEmail());
		gen.setTelefone(usuarioToLegal.getTelefone());
		
		return gen;
	}

	@Override
	public PessoaParticipante pessoaParticipantePorId(Integer idCidadao) {
		return MockCidadao.getPessoaParticipante("00000000000");
	}

	@Override
	public EnderecoToLegal consultaCepCadastrado(String cpf) {
		
		return servicePessoaFisica.enderecoPorCpf(cpf);
	}

	@Override
	public boolean usuarioPremiado(Integer id) {
		return false;
	}

	@Override
	public List<DTOMinhasNotas> documentosFiscaisPorCpf(String cpfFiltro) {
		
		FiltroNotaFiscalToLegal filtro = new FiltroNotaFiscalToLegal();
		filtro.setCpf(cpfFiltro);
		
		FiltroPontuacaoToLegal filtroPontuacao = new FiltroPontuacaoToLegal();
		filtroPontuacao.setCpf(cpfFiltro);
		
		List<NotaFiscalToLegal> listNotaToLegal = notaFiscalToLegalService.pesquisar(filtro);
		List<PontuacaoToLegal> listNotaToLegalPontuada = pontuacaoToLegalService.pesquisar(filtroPontuacao,"notaFiscalToLegal","sorteioToLegal","notaFiscalEmpresaToLegal");
		
		Integer ultimoSorteio = sorteioToLegalService.ultimoSorteio();
		
		List<DTOMinhasNotas> list = converteParaDtoNota(listNotaToLegal, listNotaToLegalPontuada,ultimoSorteio);
		
		list.addAll(concerteNotaEmpresaParaDTO(cpfFiltro,listNotaToLegalPontuada,ultimoSorteio));
		
		return list;
	}

	private  List<DTOMinhasNotas> concerteNotaEmpresaParaDTO(String cpfFiltro, List<PontuacaoToLegal> listNotaToLegalPontuada, Integer ultimoSorteio) {
		
		List<DTOMinhasNotas> list = new ArrayList<>();
		
		FiltroNotaEmpresaToLegal filtroNotaEmpresa = new FiltroNotaEmpresaToLegal();
		filtroNotaEmpresa.setCpfDestinatario(cpfFiltro);
		
		List<NotaEmpresaToLegal> listNotaEmpresa = notaEmpresaService.pesquisar(filtroNotaEmpresa);
		
		for (NotaEmpresaToLegal nto : listNotaEmpresa){
			
			ContribuinteToLegal contribuinte = genericServiceContribuinte.getById(ContribuinteToLegal.class, FiltroContribuinteToLegal.inscricaoEstadualFormat(Integer.valueOf(nto.getInscricaoEstadual())));
			
			DTOMinhasNotas dto = new DTOMinhasNotas();
			PontuacaoToLegal pontuacao = pontuacaoPorIdNotaEmpresa(listNotaToLegalPontuada, nto.getId());
			
			dto.setCnpj(contribuinte.getCnpj());
			dto.setNumero(nto.getNumeroDocumento());
			dto.setValor(nto.getValor());
			dto.setEstabelecimento(contribuinte.getRazaoSocial());
			dto.setRegistro(nto.getDataEmissao());
			dto.setEmissao(nto.getDataEmissao());
			
			if (pontuacao == null){
				
				dto.setStatusPontuacao(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO.getValor());
				dto.setQtdePontos(BigInteger.ZERO.intValue());
				dto.setInfoSorteioParticipado(String.valueOf(ultimoSorteio));
			
			}else{
				
				dto.setStatusPontuacao(SituacaoPontuacaoNota.PONTUADO.getValor());
				dto.setQtdePontos(pontuacao.getQntPonto());
				dto.setInfoSorteioParticipado(pontuacao.getSorteioToLegal().getNumeroSorteio().toString());
			}
			
			list.add(dto);
		}
		
		return list;
	}

	private List<DTOMinhasNotas> converteParaDtoNota(List<NotaFiscalToLegal> listNotaToLegal, List<PontuacaoToLegal> listNotaToLegalPontuada, Integer ultimoSorteio) {

		List<DTOMinhasNotas> list = new ArrayList<>();
		
		if (listNotaToLegal == null){
			return new ArrayList<>();
		}
		
		for (NotaFiscalToLegal nto : listNotaToLegal){
			
			DTOMinhasNotas dto = new DTOMinhasNotas();
			PontuacaoToLegal pontuacao = pontuacaoPorIdNota(listNotaToLegalPontuada, nto.getId());
			
			dto.setCnpj(nto.getCnpj());
			dto.setNumero(nto.getNumNota());
			dto.setValor(nto.getValor());
			dto.setEstabelecimento(nto.getRazaoSocial());
			dto.setRegistro(nto.getDataEmissao());
			dto.setEmissao(nto.getDataEmissao());
			
			if (pontuacao == null){
				
				dto.setStatusPontuacao(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO.getValor());
				dto.setQtdePontos(BigInteger.ZERO.intValue());
				dto.setInfoSorteioParticipado(String.valueOf(ultimoSorteio));
			
			}else{
				
				dto.setStatusPontuacao(SituacaoPontuacaoNota.PONTUADO.getValor());
				dto.setQtdePontos(pontuacao.getQntPonto());
				dto.setInfoSorteioParticipado(pontuacao.getSorteioToLegal().getNumeroSorteio().toString());
			}
			
			list.add(dto);
		}
		
		return list;
	}
	
	private static int calcPagFim(Integer page, Integer max) {
		
		return (calcInicio(page, max) + max) -1;
	}

	private static int calcInicio(Integer page, Integer max) {
		
		return (page * max);
	}
	
	private PontuacaoToLegal pontuacaoPorIdNotaEmpresa(List<PontuacaoToLegal> listNotaToLegalPontuada, Long idNotaEmpresa) {

		if (listNotaToLegalPontuada == null){
			return null;
		}
		
		for (PontuacaoToLegal pontuacao : listNotaToLegalPontuada){
			
			if (pontuacao.getNotaFiscalEmpresaToLegal() != null && pontuacao.getNotaFiscalEmpresaToLegal().getId().equals(idNotaEmpresa)){
				return pontuacao;
			}
		}
		
		return null;
	}

	private PontuacaoToLegal pontuacaoPorIdNota(List<PontuacaoToLegal> listNotaToLegalPontuada, Long idNota) {

		if (listNotaToLegalPontuada == null){
			return null;
		}
		
		for (PontuacaoToLegal pontuacao : listNotaToLegalPontuada){
			
			if (pontuacao.getNotaFiscalToLegal() != null && pontuacao.getNotaFiscalToLegal().getId().equals(idNota)){
				return pontuacao;
			}
		}
		
		return null;
	}

	@Override
	public PessoaParticipante pessoaParticipantePorCPF(String cpf) {
		
		FiltroUsuarioToLegal filtro = new FiltroUsuarioToLegal();
		
		filtro.setCpf(cpf);
		
		UsuarioToLegal usuario = this.serviceUsuario.primeiroRegistro(filtro, "pessoaFisica","pessoaFisica.endereco","endereco.municipio");
		
		if (usuario == null){
			return null;
		}
		
		PessoaParticipante pessoaParticipante = new PessoaParticipante();
		GENPessoaFisica genPessoaFisica = new GENPessoaFisica();
		
		genPessoaFisica.setCpf(usuario.getPessoaFisica().getCpf());
		genPessoaFisica.setDataDeNascimento(usuario.getPessoaFisica().getDataNascimento());
		genPessoaFisica.setIdPessoa(usuario.getPessoaFisica().getId().intValue());
		genPessoaFisica.setNome(usuario.getPessoaFisica().getNome());
		genPessoaFisica.setNomeDaMae(usuario.getPessoaFisica().getNomeMae());
		
		pessoaParticipante.setGenPessoaFisica(genPessoaFisica);
		pessoaParticipante.setEmailPreCadastro(usuario.getEmail());
		pessoaParticipante.setTelefonePreCadastro(usuario.getTelefone());
		pessoaParticipante.setId(usuario.getId().intValue());
		
		return pessoaParticipante;
	}

	@Override
	public Integer numeroDeNotasPorCpf(String cpf) {
		return 5342;
	}

	@Override
	public PaginacaoDTO<DTOMinhasNotas> documentosFiscaisPorCpf(String cpfFiltro, Date dataInicial, Date dataFinal, Integer max, Integer page) {
		
		DataFiltroBetween dataFiltro = new DataFiltroBetween();
		
		dataFiltro.setDataInicio(dataInicial);
		dataFiltro.setDataFim(dataFinal);
		
		FiltroNotaFiscalToLegal filtro = new FiltroNotaFiscalToLegal();
		
		filtro.setCpf(cpfFiltro);
		filtro.setDataFiltro(dataFiltro);
		
		FiltroPontuacaoToLegal filtroPontuacao = new FiltroPontuacaoToLegal();
		filtroPontuacao.setCpf(cpfFiltro);
		filtroPontuacao.setDataFiltro(dataFiltro);
		
		List<NotaFiscalToLegal> listNotaToLegal = notaFiscalToLegalService.pesquisar(filtro);
		List<PontuacaoToLegal> listNotaToLegalPontuada = pontuacaoToLegalService.pesquisar(filtroPontuacao,"notaFiscalToLegal","sorteioToLegal");
		
		Integer ultimoSorteio = sorteioToLegalService.ultimoSorteio();
		
		List<DTOMinhasNotas> list = converteParaDtoNota(listNotaToLegal, listNotaToLegalPontuada,ultimoSorteio);
		
		list.addAll(concerteNotaEmpresaParaDTO(cpfFiltro,listNotaToLegalPontuada,ultimoSorteio));
		
		int inicio = calcInicio(page, max);
	    int fim = calcPagFim(page, max);
	    
	    List<DTOMinhasNotas> listPg = new ArrayList<>();
        
	    for (int i=inicio; i <= fim; i++){
			
	    	 PontuacaoDTO pontDTO = new PontuacaoDTO();
			
			if (i == list.size()){
				break;
			}
			
			DTOMinhasNotas nota = list.get(i);
			
			listPg.add(nota);
	    }
	    
	    PaginacaoDTO<DTOMinhasNotas> pg = new PaginacaoDTO<>();
	    
	    pg.setCount(list.size());
	    pg.setList(listPg);
		
		return pg;
	}
	
	@Override
	public boolean emailCadastrado(String email, String cpf) {
		return false;
	}

	@Override
	public void gravaNovaSenha(String cpf, String senha) {
		
		System.out.println("gerar nova senha mock");
	}

	@Override
	public String enviaEmailRecuperacaoSenha(String cpf) {
		return serviceUsuario.emailRecuperarSenha(cpf);
	}

	@Override
	public RespostaReceitaFederalDTO validaPessoaReceitaFederal(String cpf) {
		
		//consultaReceitaFederal
		
		RespostaReceitaFederalDTO receita = new RespostaReceitaFederalDTO();
		
		receita.setNomePessoaFisica("Pedro Augusto de Oliveira");
		
		return receita;
	}

	@Override
	public boolean emailCadastrado(String email) {
		return false;
	}

	@Override
	public void preCadastroCidadao(PessoaParticipante pessoaParticipante, EnderecoToLegal endereco, String senha) throws NFGException{
		
		FiltroUsuarioToLegal filtro = new FiltroUsuarioToLegal();
		
		filtro.setCpf(pessoaParticipante.getGenPessoaFisica().getCpf());
		
		UsuarioToLegal usuario = serviceUsuario.primeiroRegistro(filtro);
		
		if (usuario != null && SituacaoUsuario.ATIVO.equals(usuario.getSituacao())){
			
			throw new NFGException("Já existe um usuário ativo para esse cpf.");
			
		}else if (usuario != null && SituacaoUsuario.INATIVO.equals(usuario.getSituacao())){
			
			usuario.setDataCadastro(new Date());
			usuario.setHash(gerarHash(pessoaParticipante.getGenPessoaFisica().getCpf(), pessoaParticipante.getEmailPreCadastro()));
			usuario.setParticipaSorteio(pessoaParticipante.isParticipaSorteioBol());
			usuario.setRecebeEmail(pessoaParticipante.isRecebeEmailBol());
			usuario.setSituacao(SituacaoUsuario.INATIVO);
			usuario.setTelefone(pessoaParticipante.getTelefonePreCadastro());
			usuario.setEmail(pessoaParticipante.getEmailPreCadastro());			
			
			genericService.salvar(usuario);
			serviceUsuario.enviaEmailConfirmacaoCadastro(usuario);
			
		}else{
			
			UsuarioToLegal usuarioToLegal = new UsuarioToLegal();
			
			usuarioToLegal.setDataCadastro(new Date());
			usuarioToLegal.setHash(gerarHash(pessoaParticipante.getGenPessoaFisica().getCpf(), pessoaParticipante.getEmailPreCadastro()));
			usuarioToLegal.setParticipaSorteio(pessoaParticipante.isParticipaSorteioBol());
			usuarioToLegal.setPessoaFisica(converteParaPF(pessoaParticipante.getGenPessoaFisica(), endereco));
			usuarioToLegal.setRecebeEmail(pessoaParticipante.isRecebeEmailBol());
			usuarioToLegal.setSituacao(SituacaoUsuario.INATIVO);
			usuarioToLegal.setTelefone(pessoaParticipante.getTelefonePreCadastro());
			usuarioToLegal.setEmail(pessoaParticipante.getEmailPreCadastro());
			usuarioToLegal.setSenha(Encrypter.encryptMD5(senha));
			
			genericService.salvar(usuarioToLegal);
			serviceUsuario.enviaEmailConfirmacaoCadastro(usuarioToLegal);
		}
	}

	private PessoaFisicaToLegal converteParaPF(GENPessoaFisica genPessoaFisica, EnderecoToLegal enderecoNFG) {
		
		PessoaFisicaToLegal pessoaFisica = new PessoaFisicaToLegal();
		
		pessoaFisica.setCpf(genPessoaFisica.getCpf());
		pessoaFisica.setDataNascimento(genPessoaFisica.getDataDeNascimento());
		pessoaFisica.setNome(genPessoaFisica.getNome());
		pessoaFisica.setNomeMae(genPessoaFisica.getNomeDaMae());
		
		EnderecoToLegal enderecoToLegal = new EnderecoToLegal();
		
		enderecoToLegal.setBairro(enderecoNFG.getBairro());
		enderecoToLegal.setCep(enderecoNFG.getCep());
		enderecoToLegal.setComplemento(enderecoNFG.getComplemento());
		enderecoToLegal.setLogradouro(enderecoNFG.getLogradouro());
		enderecoToLegal.setNumero(enderecoNFG.getNumero());
		enderecoToLegal.setMunicipio(enderecoNFG.getMunicipio());
		
		pessoaFisica.setEndereco(enderecoToLegal);
		
		return pessoaFisica;
	}

	private String gerarHash(String cpf, String emailPreCadastro) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(cpf).append(File.separator).append(emailPreCadastro);
		
		String hash = Base64.encode(sb.toString().getBytes());
		
		return hash;
	}

	@Override
	public void ativarCadastro(String cpf, String email) {
		
		FiltroUsuarioToLegal filtro = new FiltroUsuarioToLegal();
		
		filtro.setCpf(cpf);
		
		UsuarioToLegal usuario = serviceUsuario.primeiroRegistro(filtro);
		
		if (usuario != null && SituacaoUsuario.INATIVO.equals(usuario.getSituacao())){
			
			usuario.setDataAtivacao(new Date());
			usuario.setSituacao(SituacaoUsuario.ATIVO);
			
			genericService.merge(usuario);
			
			PontuacaoBonusToLegal pontuacaoBonusCadastro = new PontuacaoBonusToLegal();
			final String DESCRICAO_PONTUACAO_CADASTRO = "CADASTRO TO LEGAL";
			
			pontuacaoBonusCadastro.setDescricao(DESCRICAO_PONTUACAO_CADASTRO);
			pontuacaoBonusCadastro.setQntPonto(SorteioProperties.getValue(SorteioProperties.QNT_PONTOS_BONUS_CADASTRO));
			pontuacaoBonusCadastro.setCpf(cpf);
			pontuacaoBonusCadastro.setDataPontuacao(new Date());
			pontuacaoBonusCadastro.setSorteio(sorteioToLegalService.sorteioAtual());
			
			genericServicePontuacaoBonus.salvar(pontuacaoBonusCadastro);
			
			bilheteToLegalService.processaBilheteBonusPontuacao(cpf);
			
		}else{
			throw new NFGException("Usuário já ativo ou e-mail de ativação inválido.");
		}
	}

	@Override
	public void alterarSenha(String cpf, String senhaPerfilAntiga, String senhaPerfilNova, String senhaPerfilConfirm) {
		
		FiltroUsuarioToLegal filtro = new FiltroUsuarioToLegal();
		
		filtro.setCpf(cpf);
		
		UsuarioToLegal usuario = serviceUsuario.primeiroRegistro(filtro);
		
		if (!Encrypter.encryptMD5(senhaPerfilAntiga).equalsIgnoreCase(usuario.getSenha())){
			throw new NFGException("Senha antiga incorreta.");
		}
		
		if (!senhaPerfilNova.equals(senhaPerfilConfirm)){
			throw new NFGException("Confirmação da nova senha incorreta.");
		}
		
		usuario.setSenha(Encrypter.encryptMD5(senhaPerfilConfirm));
		
		genericService.merge(usuario);
	}
}