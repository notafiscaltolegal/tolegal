package gov.to.controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import gov.goias.dtos.DTOMinhasNotas;
import gov.goias.entidades.BilhetePessoa;
import gov.goias.entidades.ComplSituacaoReclamacao;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.RegraSorteio;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.exceptions.NFGException;
import gov.goias.service.CidadaoService;
import gov.goias.service.EmpresaService;
import gov.goias.service.EnderecoDTO;
import gov.goias.service.EnderecoService;
import gov.goias.service.PaginacaoDTO;
import gov.goias.service.PessoaParticipanteDTO;
import gov.goias.service.ReclamacaoService;
import gov.goias.service.SorteioCidadaoDTO;
import gov.goias.util.Captcha;
import gov.goias.util.CdBarrasUtil;
import gov.goias.util.ImageUtils;
import gov.goias.util.TextUtils;
import gov.goias.util.UtilReflexao;
import gov.sefaz.util.Base64;
import gov.to.dto.PontuacaoDTO;
import gov.to.dto.RespostaReceitaFederalDTO;
import gov.to.entidade.EnderecoToLegal;
import gov.to.entidade.GanhadorSorteioToLegal;
import gov.to.entidade.MunicipioToLegal;
import gov.to.entidade.UsuarioToLegal;
import gov.to.goias.DocumentoFiscalReclamadoToLegal;
import gov.to.goias.ReclamacaoLogDTO;
import gov.to.service.BilheteToLegalService;
import gov.to.service.GanhadorToLegalService;
import gov.to.service.GenericService;
import gov.to.service.PontuacaoToLegalService;
import gov.to.service.ReclamacaoLogToLegalService;
import gov.to.service.ReclamacaoToLegalService;
import gov.to.service.SorteioToLegalService;
import gov.to.service.UsuarioToLegalService;

@Controller
@RequestMapping("/cidadao")
public class CidadaoController extends BaseController {
	
	@Autowired
	private SorteioToLegalService sorteioService;
	
	@Autowired
	private CidadaoService cidadaoService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private ReclamacaoService reclamacaoService;
	
	@Autowired
	private ReclamacaoToLegalService reclamacaoToLegalService;
	
	@Autowired
	private ReclamacaoLogToLegalService reclamacaoLogToLegalService;
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
    private Captcha captcha;
	
	@Autowired
	private BilheteToLegalService bilheteService;
	
	@Autowired
	private PontuacaoToLegalService pontuacaoToLegalService;
	
	@Autowired
	private UsuarioToLegalService usuarioToLegalService;
	
	@Autowired
	private GanhadorToLegalService ganhadorToLegalService;
	
	@Autowired
	private GenericService<MunicipioToLegal, Long> genericMunicipioService;
	
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    

    @RequestMapping("voltar")
    public String voltar() {
        return "redirect:login";
    }

	@RequestMapping("telaCidadao")
    public ModelAndView telaCidadao() throws Exception {
        ModelAndView m = new ModelAndView("cidadao/telaCidadao");
        return m;
    }

    @RequestMapping("cadastrar")
    public ModelAndView viewCadastroCidadao() throws Exception {
        ModelAndView m = new ModelAndView("cidadao/cadastro");
        
        m.addObject("ufs", enderecoService.listUF());
        m.addObject("tiposLogradouro", enderecoService.listTipoLogradouro());
        m.addObject("isCadastroNormal", true);
        return m;
    }
    
    @RequestMapping("carregaCidadesPorUf")
    public @ResponseBody Map carregaCidadesPorUf(String uf) {

        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("municipios", enderecoService.listMunicipioPorUf(uf));
        return resposta;
    }
    
    @RequestMapping("carregaNomeAgencia")
    public
    @ResponseBody
    Map  carregaAgencias(Integer idBanco, String numeroAgencia) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        return resposta;
    }

    @RequestMapping("carregaAgencias")
    public
    @ResponseBody
    Map carregaAgencias(Integer id) {

        Map<String, Object> resposta = new HashMap<String, Object>();
        return resposta;
    }

    @RequestMapping("carregaBancosPorCodigo")
    public
    @ResponseBody
    Map carregaBancosPorCodigo() {

        Map<String, Object> resposta = new HashMap<String, Object>();
        return resposta;
    }

    @RequestMapping("verificaEndereco")
    public @ResponseBody Map verificaEndereco(String cep) {
    	
        Map<String, Object> resposta = new HashMap<String, Object>();
    	
    	EnderecoDTO endereco = enderecoService.consularEnderecoPorCEP(cep);

    	resposta.put("cep", endereco.getCep());
    	resposta.put("nomeLogradouro", endereco.getNomeLogradouro());
    	resposta.put("numero", endereco.getNumero());
    	resposta.put("nomeBairro", endereco.getNomeBairro());
    	resposta.put("complemento", endereco.getComplemento());

//    	resposta.put("nomeTipoLogradouro", endereco.getTipoLogradouro());
        resposta.put("nomeUf", endereco.getNomeUf());
        resposta.put("nomeMunicipio", endereco.getNomeMunicipio());
        resposta.put("ibgeMunicipio", endereco.getCodgIbgeMunicipio());
        resposta.put("enderecoHomolog", endereco.getEnderecoHomolog());
        resposta.put("cepInvalido", endereco.getCepInvalido());
        
        return resposta;
    }

    @RequestMapping("validardadoscidadao")
    public
    @ResponseBody
    Map preValidaDadosCidadao(@Valid GENPessoaFisica genPessoaFisica,
                              HttpServletResponse response,
                              @RequestParam("challenge") String challenge,
                              @RequestParam("captchaResponse") String captchaResponse,
                              boolean ehCertificado,
                              String emailPreCadastro,
                              String nomePreCadastro,
                              String telefonePreCadastro
    ) throws ParseException, IOException {
    	
    	captcha.validarCaptchaSeAtivo(request, challenge, captchaResponse);
    	
        RespostaReceitaFederalDTO receitaFederalDTO = cidadaoService.validaPessoaReceitaFederal(genPessoaFisica.getCpf());
        
        if (StringUtils.isNotBlank(receitaFederalDTO.getMensagemErro())) {
        	
            Map<String, Object> erroCadastro = new HashMap<>();
            erroCadastro.put("message",receitaFederalDTO.getMensagemErro());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return erroCadastro;
        }
        
        if (cidadaoService.pessoaParticipantePorCPF(genPessoaFisica.getCpf()) == null){
        	
        	Map<String, Object> resposta = new HashMap<>();
        	resposta.put("nomePreCadastro", receitaFederalDTO.getNomePessoaFisica());
        	resposta.put("semCredencial",true);
        	resposta.put("urlRedirect", "/cidadao/conclusaoDadosPerfil");
            	 
            resposta.put("ativarCaptcha", Captcha.verificarAtivacaoCaptcha("numeroDeTentativas", request));
            resposta.put("enderecoCadastrado", cidadaoService.consultaCepCadastrado(genPessoaFisica.getCpf()));
            return resposta;
            
        }else{
        	
        	throw new NFGException("Já existe um cadastro com este CPF!");
        }
    }

	@RequestMapping("gravarResgate")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public
    @ResponseBody
    Map gravarResgate(String tipoResgate, String vlrTributo, String vlrTaxas, Integer selectBanco, String numeroAgencia, Integer selectConta, Integer contaBancaria, String digito, Integer idPremioBilhete){

        return new HashMap<>();
    }

    @RequestMapping("efetuarcadastro")
    public @ResponseBody Map efetuarCadastroDeCidadao(GENPessoaFisica genPessoaFisica,
                                                      String email, String telefone, String senha,
                                                      String senhaAtual,
                                                      String cep,
                                                      Integer tipoLogradouro,
                                                      String nomeLogradouro,
                                                      String nomeBairro,
                                                      String numero,
                                                      String complemento,
                                                      Integer municipio,
                                                      String endHomologado,
                                                      Boolean participaSortreio,
                                                      Boolean recebeEmail
    ) {
    	
    	Integer dddTelefone, nrTelefone;
    	
        try {
            dddTelefone = Integer.parseInt(telefone.split(" ")[0]);
            nrTelefone = Integer.parseInt(telefone.split(" ")[1]);
        } catch (Exception e) {
            throw new NFGException("Campo Telefone em formato errado!");
        }

        if (numero.length() > 10) {
            throw new NFGException("O campo número deve conter no máximo 10 dígitos.");
        }

        if (nomeLogradouro != null && nomeLogradouro.length() > 60) {
            throw new NFGException("O campo logradouro deve conter no máximo 60 caracteres.");
        }

        if (cidadaoService.emailCadastrado(email)) {
            throw new NFGException("Já existe um cadastro com este endereço de e-mail.");
        }
        
        MunicipioToLegal muni = new MunicipioToLegal();
        
        muni.setId(municipio.longValue());

        EnderecoToLegal endereco = new EnderecoToLegal();
        
        endereco.setCep(cep);
        endereco.setLogradouro(nomeLogradouro);
        endereco.setBairro(nomeBairro);
        endereco.setNumero(numero);
        endereco.setComplemento(complemento);
        endereco.setMunicipio(muni);
        
        PessoaParticipante pessoaParticipante = new PessoaParticipante();
        pessoaParticipante.setGenPessoaFisica(genPessoaFisica);
        pessoaParticipante.setParticipaSorteio(participaSortreio ? 'S' : 'N');
        pessoaParticipante.setRecebeEmail(recebeEmail ? 'S' : 'N');
        pessoaParticipante.setRecebeEmailBol(recebeEmail);
        pessoaParticipante.setParticipaSorteioBol(participaSortreio);
        pessoaParticipante.setDataCadastro(new Date());
        pessoaParticipante.setEmailPreCadastro(email);
        pessoaParticipante.setTelefonePreCadastro("("+dddTelefone+") "+ nrTelefone);
        
        Map<String, Object> resposta = new HashMap<String, Object>();
        
        try{
        	
        	cidadaoService.preCadastroCidadao(pessoaParticipante, endereco, senha);
        	setSuccessMessage("Foi enviado um e-mail para "+email+". Acesse e realize a confirmação do cadastro.");
        	
        }catch(NFGException ex){
        	
        	ex.printStackTrace();
        	setErrorMessage(ex.getMessage());
        	
        }finally{
        	
        	resposta.put("urlRedirect", "/cidadao/login");
        }
        
        return resposta;
    }

    @RequestMapping("loginSite")
    public ModelAndView viewLoginCidadaoFrame(){
    	
    	ModelAndView modelAndView;

//        PessoaParticipante cidadaoLogado = getCidadaoLogado();
//
//        if(cidadaoLogado != null){
//            modelAndView = new ModelAndView("cidadao/frameUsuarioLogado");
//            modelAndView.addObject("mensagemBemVindo", cidadaoLogado.getGenPessoaFisica().getNome());
//            return modelAndView;
//        }
        modelAndView = new ModelAndView("cidadao/frameUsuarioNaoLogado");

        return modelAndView;
    }
    
    @RequestMapping("login")
    public ModelAndView viewLoginCidadao(@RequestParam(required=false, value="hash") String hash) {
    	
    	 request.getSession().setAttribute(SESSION_CIDADAO_LOGADO, null);
    	
    	 ModelAndView mav = new ModelAndView("cidadao/login");
    	
    	if (StringUtils.isNotBlank(hash)){
    		
    		String decriptHash = Base64.decodeToString(hash);
    		
    		String cpf = null;
    		String email = null;
    		
    		try{
    			
    			cpf = StringUtils.split(decriptHash, File.separator)[0];
	    		email = StringUtils.split(decriptHash, File.separator)[1];
    			
    		}catch(Exception ex) {
    			ex.printStackTrace();
    			mav.addObject("contaInativada", "Hash de ativação inválido.");
    			return mav;
    		}
    		
    		try{
    			
    			cidadaoService.ativarCadastro(cpf, email);
    			mav.addObject("contaAtivada", "Sua conta foi ativada com sucesso, realizar o login abaixo!");
    			
    		}catch(NFGException ex){
    			ex.printStackTrace();
    			mav.addObject("contaInativada", ex.getMessage());
    			return mav;
    		}
    	}
    	
    	return mav;
    }
    
    @RequestMapping("esqueciSenha")
    public ModelAndView viewEsqueciSenha() {
    	
    	//Enviar e-mail 
    	setSuccessMessage("Uma nova senha foi gerada e enviada para seu e-mail.");
    	
        return new ModelAndView("cidadao/esqueciSenha");
    }

    @RequestMapping("efetuarlogin")
    public @ResponseBody Map<?,?> efetuarLoginCidadao(String cpf, String senha,String challenge,String captchaResponse) throws BindException {
    	
    	captcha.validarCaptchaSeAtivo(request, challenge, captchaResponse);
    	
    	 Map<String, Object> resposta = new HashMap<String, Object>();
    	 
    	 PessoaParticipanteDTO retornoAutenticacao = cidadaoService.autenticaCidadao(cpf, senha);
    	
    	if (retornoAutenticacao.isContador()){
    		
    		setSuccessMessage("Bem-vindo(a) ao To Legal!");
            resposta.put("semCredencial", false);
            resposta.put("urlRedirect", "/login-contador");

            request.getSession().setAttribute(SESSION_CIDADAO_LOGADO, retornoAutenticacao.getPessoaParticipante());
            return resposta;
    	}

        if (StringUtils.isNotBlank(retornoAutenticacao.getErro())){
            resposta.put("loginInvalido", retornoAutenticacao.getErro());
        }else {

            PessoaParticipante cidadaoLogando = retornoAutenticacao.getPessoaParticipante();
            
            if(!retornoAutenticacao.isPossuiCredencial()){
                resposta.put("semCredencial", true);
                resposta.put("urlRedirect", "/cidadao/conclusaoDadosPerfil");
                request.getSession().setAttribute(SESSION_ID_CIDADAO_SEM_CREDENC, cidadaoLogando.getId());
            } else {
                setSuccessMessage("Bem-vindo(a) ao To Legal!");
                resposta.put("semCredencial", false);
                resposta.put("urlRedirect", "/cidadao/inicioSite");

                request.getSession().setAttribute(SESSION_CIDADAO_LOGADO, cidadaoLogando);
            }
        }
        
        resposta.put("ativarCaptchaLogin", Captcha.verificarAtivacaoCaptcha("numeroDeTentativasLogin", request));
        
        return resposta;
    }

	@RequestMapping("recuperarsenha")
    public @ResponseBody Map enviarEmailRecuperacaoSenhaCidadao(String cpf) {
		
		Map<String, Object> resposta = new HashMap<String, Object>();

        PessoaParticipante pessoaExistente = cidadaoService.pessoaParticipantePorCPF(cpf);
        
        if (pessoaExistente == null) {
        	throw new NFGException("CPF sem cadastro no programa.");
        }

        String emailDeEnvio = cidadaoService.enviaEmailRecuperacaoSenha(cpf);
        if (emailDeEnvio!=null) {
            resposta.put("status", emailDeEnvio+", verifique sua caixa de entrada!");
        } else {
            throw new NFGException("Falha no envio de e-mail.");
        }
        return resposta;
    }


    @RequestMapping("verificaInconsistencia")
    public @ResponseBody Map verificaInconsistencia(String nomePainel,String nomeTopo) {
       return new HashMap<>();
    }


    @RequestMapping("inicioSite")
    public ModelAndView viewPaginaInicialCidadaoSite() throws ParseException {
    	ModelAndView modelAndView;
        PessoaParticipante cidadao = getCidadaoLogado();

        modelAndView = new ModelAndView("cidadao/telaInicialSite");
        EnderecoToLegal enderecoCadastrado = cidadaoService.consultaCepCadastrado(cidadao.getGenPessoaFisica().getCpf());
        modelAndView.addObject("cidadao", cidadao);
        List<RegraSorteio> sorteios = sorteioService.listRegraSorteioDivulgaResultado();

        modelAndView.addObject("sorteios", sorteios);
        
        EnderecoDTO enderecoDto = converteEndereco(enderecoCadastrado);
        
        modelAndView.addObject("endereco", UtilReflexao.obterAtributos(enderecoDto));
        return modelAndView;
    }

	private EnderecoDTO converteEndereco(EnderecoToLegal enderecoCadastrado) {
		EnderecoDTO endDto = new EnderecoDTO();
        
        endDto.setNomeBairro(enderecoCadastrado.getBairro());
        endDto.setCep(enderecoCadastrado.getCep());
        endDto.setComplemento(enderecoCadastrado.getComplemento());
        endDto.setNomeLogradouro(enderecoCadastrado.getLogradouro());
        endDto.setNumero(enderecoCadastrado.getNumero());
        
        if (enderecoCadastrado.getMunicipio() != null){

            endDto.setNomeMunicipio(enderecoCadastrado.getMunicipio().getMunNome());
            endDto.setNomeUf(enderecoCadastrado.getMunicipio().getEstado().name());	
        }
        
        return endDto;
	}

	@RequestMapping("conclusaoDadosPerfil")
    public ModelAndView concluirPerfil() throws Exception {

		Integer idCidadao = (Integer) request.getSession().getAttribute(SESSION_ID_CIDADAO_SEM_CREDENC);

        PessoaParticipante cidadao = cidadaoService.pessoaParticipantePorId(idCidadao);

        ModelAndView modelAndView = new ModelAndView("cidadao/conclusaoDadosPerfil");
        EnderecoToLegal enderecoCadastradoa = cidadaoService.consultaCepCadastrado(cidadao.getGenPessoaFisica().getCpf());
        
        EnderecoDTO enderecoDTO = converteEndereco(enderecoCadastradoa);
        
        modelAndView.addObject("cep", enderecoDTO.getCep());
        modelAndView.addObject("nomeLogradouro", enderecoDTO.getNomeLogradouro());
        modelAndView.addObject("numero", enderecoDTO.getNumero());
        modelAndView.addObject("nomeBairro", enderecoDTO.getNomeBairro());
        modelAndView.addObject("complemento", enderecoDTO.getComplemento());

        modelAndView.addObject("nomeTipoLogradouro", enderecoDTO.getTipoLogradouro());
        modelAndView.addObject("nomeUf", enderecoDTO.getNomeUf());
        modelAndView.addObject("nomeMunicipio", enderecoDTO.getNomeMunicipio());
        modelAndView.addObject("enderecoHomolog", enderecoDTO.getEnderecoHomolog());

        modelAndView.addObject("cidadao", cidadao);
        modelAndView.addObject("ufs", enderecoService.listUF());
        modelAndView.addObject("tiposLogradouro", enderecoService.listTipoLogradouro());
        modelAndView.addObject("participaSorteio", cidadao.getParticipaSorteio().toString().equals("S"));
        modelAndView.addObject("recebeEmail", cidadao.getRecebeEmail().toString().equals("S"));
        
        return modelAndView;
    }

    @RequestMapping("atualizaPerfil")
    public ModelAndView viewAtualizaPerfilCidadao() throws Exception {
    	
    	PessoaParticipante cidadao = getCidadaoLogado();
        ModelAndView modelAndView = new ModelAndView("cidadao/perfil");
        EnderecoToLegal enderecoCadastrado  = cidadaoService.consultaCepCadastrado(cidadao.getGenPessoaFisica().getCpf());
        
        EnderecoDTO enderecoDTO = converteEndereco(enderecoCadastrado);
        
        modelAndView.addObject("cep", enderecoDTO.getCep());
        modelAndView.addObject("nomeLogradouro", enderecoDTO.getNomeLogradouro());
        modelAndView.addObject("numero", enderecoDTO.getNumero());
        modelAndView.addObject("nomeBairro", enderecoDTO.getNomeBairro());
        modelAndView.addObject("complemento", enderecoDTO.getComplemento());

        modelAndView.addObject("nomeUf", enderecoDTO.getNomeUf());
        modelAndView.addObject("nomeMunicipio", enderecoDTO.getNomeMunicipio());
        modelAndView.addObject("enderecoHomolog", enderecoDTO.getEnderecoHomolog());

        modelAndView.addObject("cidadao", cidadao);
        modelAndView.addObject("ufs", enderecoService.listUF());
        modelAndView.addObject("tiposLogradouro", enderecoService.listTipoLogradouro());
        modelAndView.addObject("participaSorteio", cidadao.getParticipaSorteio().toString().equals("S"));
        modelAndView.addObject("recebeEmail", cidadao.getRecebeEmail().toString().equals("S"));
        modelAndView.addObject("dataCadastro", cidadao.getDataCadastro());
        
        return modelAndView;
    }

    @RequestMapping("novaReclamacao")
    public ModelAndView novaReclamacao() throws Exception {
    	PessoaParticipante cidadao = getCidadaoLogado();
        ModelAndView modelAndView = new ModelAndView("cidadao/novaReclamacao");

        modelAndView.addObject("cidadao", cidadao);

        return modelAndView;
    }

    @RequestMapping("resgatar")
    public ModelAndView resgatarPremiacao() throws Exception{
        ModelAndView modelAndView = new ModelAndView("cidadao/premiacao");
        return modelAndView;
    }

    /**diferenciando do metodo "grava perfil" por uma questao de permissao de acesso no CidadaoInterceptor*/
    @RequestMapping("gravaConclusaoPerfil")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map gravaConclusaoPerfil(String cpf, String email, String telefone, Boolean participaSortreio, Boolean recebeEmail,
                                                  Integer cep,
                                                  Integer tipoLogradouro,
                                                  String nomeLogradouro,
                                                  String nomeBairro,
                                                  String numero,
                                                  String complemento,
                                                  Integer municipio,
                                                  String endHomologado,
                                                  String senha
    ) {
        return new HashMap<>();
    }

    @RequestMapping("gravaPerfil")
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map atualizarPerfilCidadao(String cpf, String email, String telefone, Boolean participaSortreio, Boolean recebeEmail,
                                                    String cep,
                                                    Integer tipoLogradouro,
                                                    String nomeLogradouro,
                                                    String nomeBairro,
                                                    String numero,
                                                    String complemento,
                                                    Integer municipio,
                                                    String endHomologado
    ) {
        return dadosCidadao(cpf, email, telefone, participaSortreio, recebeEmail, cep, tipoLogradouro, nomeLogradouro, nomeBairro, numero, complemento, municipio, endHomologado, null);
    }

	public Map dadosCidadao(String cpf, String email, String telefone, Boolean participaSortreio, Boolean recebeEmail,
			String cep, Integer tipoLogradouro, String nomeLogradouro, String nomeBairro, String numero,
			String complemento, Integer municipio, String endHomologado, String senha) {
		
		Map<String, Object> resposta = new HashMap<String, Object>();
		
		if (cidadaoService.emailCadastrado(email, cpf)) {
			throw new NFGException("Já existe um cadastro com este endereço de e-mail.", true);
		}
		
		UsuarioToLegal usuarioToLegal = usuarioToLegalService.usuarioPorCpf(cpf);
		
		usuarioToLegal.setEmail(email);
		usuarioToLegal.setTelefone(telefone);
		usuarioToLegal.setRecebeEmail(recebeEmail);
		usuarioToLegal.setParticipaSorteio(participaSortreio);
		
		EnderecoToLegal endereco = usuarioToLegal.getPessoaFisica().getEndereco();
		endereco.setBairro(nomeBairro);
		endereco.setCep(cep);
		endereco.setNumero(numero);
		endereco.setLogradouro(nomeLogradouro);
		endereco.setComplemento(complemento);
		
		if (municipio != null && !endereco.getMunicipio().getId().equals(municipio.longValue())){
			
			endereco.setMunicipio(genericMunicipioService.getById(MunicipioToLegal.class, municipio.longValue()));
		}
		
		usuarioToLegal.getPessoaFisica().setEndereco(endereco);
		
		try{
			
			PessoaParticipante pessoaPerfilAtualizado = usuarioToLegalService.atualizaPerfil(usuarioToLegal);
			
			request.getSession().setAttribute(SESSION_CIDADAO_LOGADO, pessoaPerfilAtualizado);
			setSuccessMessage("Perfil atualizado com sucesso!");
			resposta.put("urlRedirect", "/cidadao/inicioSite");
			return resposta;
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new NFGException("Falha ao atualizar o perfil!", true);
		}
	}

    @RequestMapping("alterarSenhaPerfil")
    public ModelAndView viewAlterarSenhaDoPerfil() {
    	PessoaParticipante cidadao = getCidadaoLogado();
        ModelAndView modelAndView = new ModelAndView("cidadao/alterarSenhaPerfil");
        modelAndView.addObject("cidadao", cidadao);
        return modelAndView;
    }

    @RequestMapping("gravaNovaSenhaPerfil")
    public @ResponseBody Map alterarSenhaDoPerfil(String cpf, String senhaPerfilAntiga, String senhaPerfilNova, String senhaPerfilConfirm) {
    	
    	Map<String, Object> resposta = new HashMap<String, Object>();
    	resposta.put("urlRedirect", "/cidadao/inicioSite");
    	
    	if (request.getSession().getAttribute("alterarSenha") == null){
    		
    		request.getSession().setAttribute("alterarSenha", File.separator);
    		
    	}else{
    		
    		request.getSession().setAttribute("alterarSenha", null);
    		return resposta;
    	}
    	
    	try{
    		cidadaoService.alterarSenha(cpf, senhaPerfilAntiga, senhaPerfilNova, senhaPerfilConfirm);
    		setSuccessMessage("Nova senha gravada com sucesso!");
    		
    	}catch(NFGException ex ){
    		
    		setMensagemDeErro(ex.getMessage());
    		return resposta;
    	}
        
        return resposta;
    }

    @RequestMapping("efetuarlogout")
    public String efetuarLogoutCidadao() {
        return "redirect:login";
    }

    @RequestMapping("efetuarlogoutSite")
    public ModelAndView efetuarLogoutCidadaoSite() {
    	request.getSession().setAttribute(BaseController.SESSION_CIDADAO_LOGADO, null);
    	request.getSession().invalidate();
    	return new ModelAndView(new RedirectView("/to-legal"));
    }

    @RequestMapping("redirecionarHome")
    public ModelAndView redirecionarHome() {
    	return new ModelAndView(new RedirectView("/to-legal"));
    }

    @RequestMapping("listarPremiacao/{page}")
    public @ResponseBody Map<String, Object> listarPremiacao(@PathVariable(value = "page") Integer page, Integer idSorteio, BindException bind) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();
        
        PessoaParticipante cidadaoLogado = getCidadaoLogado();
        
        List<GanhadorSorteioToLegal> ganhador = ganhadorToLegalService.ganhadorPorSorteio(idSorteio, cidadaoLogado.getGenPessoaFisica().getCpf());
        
        resposta.put("resultadoGanhadorSorteio", ganhador);
        
        return resposta;
    }


    @RequestMapping("listarNotasCidadao/{page}")
    public @ResponseBody Map<String, Object> listarNotasCidadao(@PathVariable(value = "page") Integer page, String cpfFiltro, BindException bind) throws ParseException {

//    	List<DTOMinhasNotas> docsFiscais = cidadaoService.documentosFiscaisPorCpf(cpfFiltro);
//
//        Map<String, Object> resposta = new HashMap<String, Object>();
//        resposta.put("minhasNotas", docsFiscais);
        
        return listarNotasCidadaoEmDetalhe(page, cpfFiltro, null, null, bind);
    }

    @RequestMapping("viewMinhasNotasEmDetalhe")
    public ModelAndView viewMinhasNotasEmDetalhe() {
        PessoaParticipante cidadao = getCidadaoLogado();
        ModelAndView modelAndView = new ModelAndView("cidadao/minhasNotasDetalhe");
        modelAndView.addObject("cidadao", cidadao);
        return modelAndView;
    }


    @RequestMapping("listarNotasCidadaoEmDetalhe/{page}")
    public @ResponseBody Map<String, Object> listarNotasCidadaoEmDetalhe(@PathVariable(value = "page") Integer page, String cpfFiltro, String referenciaInicial, String referenciaFinal, BindException bind) throws ParseException {

    	Integer max = 10;
        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();
        
        Date dataInicial = referenciaInicial != null && referenciaInicial.length() > 0 ? simpleDateFormat.parse(referenciaInicial) : null;
        Date dataFinal = referenciaFinal != null && referenciaFinal.length() > 0 ? simpleDateFormat.parse(referenciaFinal) : null;

        PaginacaoDTO<DTOMinhasNotas> docsFiscais = cidadaoService.documentosFiscaisPorCpf(cpfFiltro, dataInicial, dataFinal, max, page);


        pagination.put("total", docsFiscais.getCount());
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("pagination", pagination);
        resposta.put("minhasNotas", docsFiscais.getList());

        return resposta;
    }

    @RequestMapping("geraCartao")
    public @ResponseBody HttpServletResponse geraCartao(HttpServletResponse response) throws IOException {
    	
        GENPessoaFisica cidadaoLogado = getCidadaoLogado().getGenPessoaFisica();

        InputStream streamTemplateCartao = servletContext.getResourceAsStream("/resources/images/cartao_cidadao_novo_to.jpg");
        File imagemCdBarras = CdBarrasUtil.geraCodigoDeBarras(response, cidadaoLogado.getCpf());

        BufferedImage  templateImage = ImageIO.read(streamTemplateCartao);
        BufferedImage cdBarrasImage = ImageIO.read(imagemCdBarras);
        BufferedImage  nomeImage = ImageUtils.textToBufferedImage(cidadaoLogado.getNome());
        BufferedImage  cpfImage = ImageUtils.textToBufferedImage(TextUtils.format("###.###.###-##", cidadaoLogado.getCpf()));
        BufferedImage combined = new BufferedImage(templateImage.getWidth(), templateImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        g.drawImage(templateImage, 0, 0, null);
        g.drawImage(cdBarrasImage, 615, 110, null);
        g.drawImage(nomeImage, 100, 192, null);
        g.drawImage(cpfImage, 100, 143, null);

        response.setContentType("image/x-png");
        OutputStream outputStream = response.getOutputStream();
        ImageIO.write(combined, "png", outputStream);
        outputStream.close();
		return response;
    }


    @RequestMapping("detalheBilhetes/{idSorteio}")
    public ModelAndView viewBilhetesEmDetalhe(@PathVariable(value = "idSorteio") Integer idSorteio){

        PessoaParticipante cidadao = getCidadaoLogado();
        Integer totalDePontos = null;
        String cpf = cidadao.getGenPessoaFisica().getCpf();

        List<BilhetePessoa> bilhetesCidadao = bilheteService.listBilhetes(cpf, idSorteio);
        int totalBilhetes = bilhetesCidadao.size();

        RegraSorteio sorteio = sorteioService.sorteioPorId(idSorteio);
        totalDePontos = pontuacaoToLegalService.totalNotasSorteioPorCpf(cpf,idSorteio);
        
        if (bilhetesCidadao.isEmpty()){
        	bilhetesCidadao = null;
        }

        ModelAndView modelAndView = new ModelAndView("cidadao/bilhetes");
        modelAndView.addObject("sorteio", sorteio);
        modelAndView.addObject("bilhetes", bilhetesCidadao);
        modelAndView.addObject("total", totalBilhetes);
        modelAndView.addObject("totalDePontos", totalDePontos);

        return modelAndView;
    }

    @RequestMapping("detalhePontos/{idSorteio}")
    public ModelAndView viewPontosEmDetalhe(@PathVariable(value = "idSorteio") Integer idSorteio){
        PessoaParticipante cidadao = getCidadaoLogado();
        RegraSorteio sorteio;
        String cpf = cidadao.getGenPessoaFisica().getCpf();
        Integer totalDePontos = null;
        Long totalBilhetes = null;
        
        try{
            sorteio = sorteioService.sorteioPorId(idSorteio);
        }catch (Exception e){
            throw new NFGException("Ops, parece que esse sorteio não existe. Tente novamente.",new ModelAndView("cidadao/telainicial"));
        }

        try{
            totalDePontos = pontuacaoToLegalService.totalPontosSorteioPorCpf(cpf, idSorteio);
            totalBilhetes = bilheteService.totalBilheteSorteioPorCpf(cpf, idSorteio);
        }catch (Exception e){
            e.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView("cidadao/pontos");
        modelAndView.addObject("sorteio", sorteio);
        modelAndView.addObject("totalDePontos", totalDePontos);
        modelAndView.addObject("totalDeBilhetes", totalBilhetes);
        return modelAndView;
    }


    @RequestMapping("carregaDadosDaPremiacao")
    public @ResponseBody Map<String, Object>
    carregaDadosDaPremiacao(Integer idSorteio){
    	Map<String, Object> resposta = new HashMap<>();
        PessoaParticipante cidadaoLogado = getCidadaoLogado();
        
        List<GanhadorSorteioToLegal> ganhador = ganhadorToLegalService.ganhadorPorSorteio(idSorteio, cidadaoLogado.getGenPessoaFisica().getCpf());
        
        resposta.put("isUsuarioPremiado", !ganhador.isEmpty());
        
        return resposta;
    }

    @RequestMapping("carregaDadosDoSorteioParaCidadao")
    public @ResponseBody Map<?, ?> carregaDadosDoSorteio(Integer idSorteio, BindException bind) throws ParseException {
    	 PessoaParticipante cidadao = getCidadaoLogado();
    	 
    	 SorteioCidadaoDTO sorteioCidadaoDTO = sorteioService.carregaDadosDoSorteioParaCidadao(idSorteio, cidadao.getGenPessoaFisica().getCpf());
    	 
    	 Map<String, Object> resposta = new HashMap<String, Object>();

         resposta.put("sorteio", sorteioCidadaoDTO.getSorteio());
         resposta.put("totalDocs", sorteioCidadaoDTO.getTotalDocs());
         resposta.put("totalPontos", sorteioCidadaoDTO.getTotalPontos());
         resposta.put("totalBilhetes", sorteioCidadaoDTO.getTotalBilhetes());
         
         return resposta;
    }

    @RequestMapping("listarBilhetes/{page}")
    public @ResponseBody Map<String, Object> listarBilhetes(@PathVariable(value = "page") Integer page, Integer idSorteio, BindException bind){

    	Integer max = 6;
        Long count = 0L;
        PessoaParticipante cidadao = getCidadaoLogado();

        List<?> dadosBilhetes = bilheteService.listBilhetePaginado(cidadao.getGenPessoaFisica().getCpf(), idSorteio, max, page);
        
        if(dadosBilhetes.size() > 0){
            count = bilheteService.totalBilheteSorteioPorCpf(cidadao.getGenPessoaFisica().getCpf(), idSorteio);
        }

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count.intValue());
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("dadosBilhetes", dadosBilhetes);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("listarPontosDasNotas/{page}")
    public @ResponseBody Map<String, Object> listarPontosDasNotas(@PathVariable(value = "page") Integer page, Integer idSorteio, BindException bind) throws UnsupportedEncodingException {
    	 Integer max = 10;
         String cpf = getCidadaoLogado().getGenPessoaFisica().getCpf();

         PaginacaoDTO<PontuacaoDTO> dadosPontuacaoNotas = pontuacaoToLegalService.consultaPontuacaoDocsFiscaisPorSorteio(idSorteio, cpf, max, page);
         
         Map<String, Object> resposta = new HashMap<String, Object>();
         Map<String, Object> pagination = new HashMap<String, Object>();

         pagination.put("total", dadosPontuacaoNotas.getCount());
         pagination.put("page", ++page);
         pagination.put("max", max);

         resposta.put("dadosPontuacaoNotas", dadosPontuacaoNotas.getList());
         resposta.put("pagination", pagination);

         return resposta;
    }

    @RequestMapping("listarPontosBonus/{page}")
    public @ResponseBody Map<String, Object> listarPontosBonus(@PathVariable(value = "page") Integer page, Integer idSorteio, BindException bind)   {
        
    	Integer max = 3;
        Integer count = 0;
        String cpf = getCidadaoLogado().getGenPessoaFisica().getCpf();
        
        List<Map<String, Object>> dadosPontuacaoBonus = pontuacaoToLegalService.pontuacaoBonus(idSorteio, cpf, max, page);
        count = dadosPontuacaoBonus.size();
        
        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("dadosPontuacaoBonus", dadosPontuacaoBonus);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("verificaSenha")
    public ModelAndView viewNovaSenhaCidadao(@RequestParam(value = "token") String token, @RequestParam(value = "cpf") String cpf) {
            ModelAndView m = new ModelAndView("cidadao/novasenha");
            m.addObject("token", token);
            m.addObject("cpf", cpf);
            return m;
    }

    @RequestMapping("gravaNovaSenha")
    public @ResponseBody Map gravaNovaSenhaCidadao(String cpf, String tokenSenha, String novaSenha, String novaSenhaConfirm) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        setSuccessMessage("Nova senha gravada com sucesso!");
        
        resposta.put("urlRedirect", "/cidadao/login");
        return resposta;
    }

    @RequestMapping("certificado")
    public ModelAndView certificadoDigitalDeCidadao() throws Exception {
    	
    	ModelAndView modelAndView = new ModelAndView(new RedirectView("/to-legal/contribuinte/login"));
		modelAndView .addObject("isCertificado", true);
    	return modelAndView;
    }

    @RequestMapping("verificaPreCadastro")
    public ModelAndView viewConclusaoCadastro(@RequestParam(value = "token") String token, @RequestParam(value = "cpf") String cpf) throws Exception {
        ModelAndView modelAndView = new ModelAndView("cidadao/login");
        throw new NFGException("O período de pré-cadastro está encerrado. Faça o cadastro convencional clicando em 'Primeiro Acesso'.",modelAndView);
    }

    @RequestMapping("listarReclamacoes/{page}")
    public @ResponseBody Map<String, Object> listarReclamacoes(@PathVariable(value = "page") Integer page, BindException bind) throws ParseException {
    	 Integer max = 5;

         PessoaParticipante cidadao = getCidadaoLogado();
         PaginacaoDTO<DocumentoFiscalReclamadoToLegal> reclamacoesPaginate = reclamacaoToLegalService.findReclamacoesDoCidadao(cidadao, page, max);

         Map<String, Object> resposta = new HashMap<String, Object>();
         Map<String, Object> pagination = new HashMap<String, Object>();

         pagination.put("total", reclamacoesPaginate.getCount());
         pagination.put("page", ++page);
         pagination.put("max", max);

         resposta.put("cidadao", cidadao);
         resposta.put("reclamacoes", reclamacoesPaginate.getList());
         resposta.put("pagination", pagination);
         
         return resposta;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("salvarNovaReclamacao")
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map salvarNovaReclamacao(
            Integer tipoDocFiscalReclamacao,
            Integer codgMotivo,
            Date dataEmissaoDocFiscal,
            Integer numeroReclamacao,
            Integer iEReclamacao,
            Double valorReclamacao,
            boolean dataDentroDoPrazo,
            Integer problemaEmpresaReclamacao,
            @RequestParam("file") MultipartFile fileReclamacao
    ) {

    	PessoaParticipante cidadao = getCidadaoLogado();
        Map retorno = new HashMap();
        
    	try{   		
    		
    		reclamacaoToLegalService.cadastraNovaReclamacao(tipoDocFiscalReclamacao, codgMotivo, dataEmissaoDocFiscal, numeroReclamacao, iEReclamacao, valorReclamacao, fileReclamacao, cidadao,dataDentroDoPrazo, problemaEmpresaReclamacao);
    		
    		setSuccessMessage("A sua reclamação foi registrada com sucesso e a empresa será notificada. Acompanhe o processo pelo painel Minhas Reclamações.");
            retorno.put("sucesso",Boolean.TRUE);
    	}catch (NFGException nx)
    	{
    		nx.printStackTrace();
    		retorno.put("sucesso",Boolean.FALSE);
    		retorno.put("msg_erro", nx.getMessage());
    	}
    	
    	catch (Exception ex){
    		ex.printStackTrace();
    		retorno.put("sucesso",Boolean.FALSE);
    	}

    	return retorno;
    }

    @RequestMapping("alterarSituacaoReclamacao")
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map alterarSituacaoReclamacao(Integer idReclamacao,Integer novoCodgTipoCompl, String infoReclamacao ) {
       
        Boolean sucesso = reclamacaoToLegalService.alterarStatusReclamacao(idReclamacao, novoCodgTipoCompl);

        Map<String, Boolean> retorno = new HashMap<>();
        retorno.put("sucesso",sucesso);
        
        return retorno;
    }

	@RequestMapping("selectAcoesDisponiveis")
	public @ResponseBody Map selectAcoesDisponiveis(Integer idReclamacao) {

		DocumentoFiscalReclamadoToLegal reclamacao = reclamacaoService.reclamacaoPorId(idReclamacao);
		Map retorno = new HashMap<>();
		List<ComplSituacaoReclamacao> acoesDisponiveis = reclamacaoService
				.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CIDADAO, reclamacao);
		retorno.put("acoesDisponiveis", acoesDisponiveis);

		return retorno;
	}

    @RequestMapping("verReclamacaoDetalhe/{idReclamacao}")
    public ModelAndView verReclamacaoDetalhe(@PathVariable("idReclamacao") Integer idReclamacao){
    	ModelAndView modelAndView;
        modelAndView = new ModelAndView("cidadao/reclamacaoDetalhe");

        DocumentoFiscalReclamadoToLegal reclamacao = reclamacaoService.reclamacaoPorId(idReclamacao);

        List<ComplSituacaoReclamacao> statusDisponiveis = reclamacaoService.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CIDADAO,reclamacao);

        modelAndView.addObject("reclamacao", reclamacao);
        modelAndView.addObject("statusDisponiveis", statusDisponiveis);
        modelAndView.addObject("nomeFantasia", reclamacao.getNomeFantasiaEmpresa());
        modelAndView.addObject("dataEmissaoStr", simpleDateFormat.format(reclamacao.getDataDocumentoFiscal()));
        modelAndView.addObject("dataReclamacaoStr", simpleDateFormat.format(reclamacao.getDataReclamacao()));
        
        

        return modelAndView;
    }

	@RequestMapping("listarAndamentoReclamacao/{page}")
    public @ResponseBody Map<String, Object> listarAndamentoReclamacao(@PathVariable(value = "page") Integer page, Integer idReclamacao,BindException bind) throws ParseException {
    	Integer max = 5;

    	 //PessoaParticipante cidadao = getCidadaoLogado();
        PaginacaoDTO<ReclamacaoLogDTO> reclamacoesPaginate = reclamacaoLogToLegalService.logReclamacaoPorIdReclamacao(idReclamacao, page, max);

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", reclamacoesPaginate.getCount());
        pagination.put("page", ++page);
        pagination.put("max", max);

        //resposta.put("cidadao", cidadao);
        resposta.put("situacoesReclamacao", reclamacoesPaginate.getList());
        resposta.put("pagination", pagination);

        return resposta;
    }

	@RequestMapping("buscarEmpresaPorIe")
	public @ResponseBody Map buscarEmpresaPorIe(Integer inscricao) {
		Map<String, Object> resposta = new HashMap<String, Object>();

        String nomeFantasia = empresaService.nomeFantasiaPelaInscricao(inscricao);
        resposta.put("nomeFantasia", nomeFantasia);
        
        return resposta;
	}
}