package gov.goias.controllers;

import entidade.Credencial;
import gov.goias.dtos.DTOMinhasNotas;
import gov.goias.dtos.DTOPremiacao;
import gov.goias.entidades.*;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.exceptions.NFGException;


import gov.goias.interceptors.CertificateInterceptor;
import gov.goias.persistencia.CCEContribuinteRepository;
import gov.goias.persistencia.historico.HistoricoNFG;
import gov.goias.services.ContadorLoginService;
import gov.goias.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.BindException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by lucas-mp on 10/10/14.
 */
@Controller
@RequestMapping("/cidadao")
public class CidadaoController extends BaseController {
    private static final Logger logger = Logger.getLogger(CidadaoController.class);
    private @Autowired
    ServletContext servletContext;

    @Autowired
    SimpleDateFormat simpleDateFormat;

    @Autowired
    PessoaParticipante pessoaParticipanteRepository;


    @Autowired
    DocumentoFiscalReclamado reclamacaoRepository;

    @Autowired
    RegraSorteio regraSorteioRepository;

    @Autowired
    MensagemEmailEnvioService mensagemEmailEnvio;

    @Autowired
    GENUf ufRepository;

    @Autowired
    CCEContribuinteRepository cceRepository;

    @Autowired
    GENTipoLogradouro tipoLogradouroRepository;

    @Autowired
    GENPessoaFisica genPessoaRepository;

    @Autowired
    GENAgenciaBancaria genAgenciaBancariaRepository;

    @Autowired
    ContaBancariaPremio contaBancariaPremioRepository;

    @Autowired
    BilhetePessoa bilhetePessoaRepository;

    @Autowired
    Captcha captcha;

    @Autowired
    GENMunicipio genMunicipioRepository;

    @Autowired
    RegraPontuacaoDocumentoFiscal regraPontuacaoDocumentoFiscalRepository;



    @Autowired
    GENBanco genBancoRepository;

    @Autowired
    PremioBilhete premioBilheteRepository;

    @Value("${urlHome}")
    private String urlHome;

    private boolean notificaViaEmail=true;

    @RequestMapping("voltar")
    public String voltar() {
        HttpSession session = request.getSession();
        logger.info("cidadao/voltar: Setando NULL no cidadao da sessao: "+session.getId());
        session.setAttribute(SESSION_CIDADAO_LOGADO, null);
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
        m.addObject("ufs", ufRepository.listar("codgUf"));
        m.addObject("tiposLogradouro", tipoLogradouroRepository.listar("descTipoLogradouro"));
        m.addObject("isCadastroNormal", true);
        return m;
    }

    @RequestMapping("carregaCidadesPorUf")
    public @ResponseBody Map carregaCidadesPorUf(String uf) {

        HashMap<String, Object> dados = new HashMap<>();
        dados.put("uf", uf);
        logger.info("Tentando carregar os municipios para a uf " + uf);

        List<GENMunicipio> municipios = genMunicipioRepository.list(dados, "nome");
        logger.info(municipios.size() + " municipios carregados para a uf" + uf);



        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("municipios", municipios);
        return resposta;
    }

    @RequestMapping("carregaNomeAgencia")
    public
    @ResponseBody
    Map  carregaAgencias(Integer idBanco, String numeroAgencia) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        try{
            String agencia = genAgenciaBancariaRepository.listarNomeDaAgencia(idBanco, numeroAgencia);
            resposta.put("nomeAgencia", agencia);
        }catch (Exception e){
            logger.error(e);
            resposta.put("nomeAgencia", "Agencia Inexistente");
        }

        return resposta;
    }

    @RequestMapping("carregaAgencias")
    public
    @ResponseBody
    Map carregaAgencias(Integer id) {

        List<GENAgenciaBancaria> agenciasBancaria = genAgenciaBancariaRepository.listarAgenciaPorIdBanco(id);

        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("agenciasBancaria", agenciasBancaria);
        return resposta;
    }

    @RequestMapping("carregaBancosPorCodigo")
    public
    @ResponseBody
    Map carregaBancosPorCodigo() {

        List<GENBanco> bancos = genBancoRepository.listarBancos();

        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("bancos", bancos);
        return resposta;
    }

    @RequestMapping("verificaEndereco")
    public @ResponseBody Map verificaEndereco(Integer cep) {
        return pessoaParticipanteRepository.consultaCep(cep);
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

        PessoaParticipante pessoaExistente = pessoaParticipanteRepository.findByGenPessoaCPF(genPessoaFisica);
        if (pessoaExistente != null) {
            if (pessoaParticipanteRepository.pessoaComErroNoCadastro(pessoaExistente)){
                Map<String, Object> erroCadastro = new HashMap<>();
                erroCadastro.put("semCredencial",true);
                erroCadastro.put("urlRedirect", "/cidadao/conclusaoDadosPerfil");
                request.getSession().setAttribute(SESSION_ID_CIDADAO_SEM_CREDENC, pessoaExistente.getId());
                response.setStatus(500);
                return erroCadastro;
            }
            throw new NFGException("Já existe um cadastro com este CPF!");
        }

        Map<String, Object> resposta = pessoaParticipanteRepository.preConsultaDeCidadaoNasBases(
                genPessoaFisica.getCpf(), genPessoaFisica.getDataDeNascimento(), genPessoaFisica.getNomeDaMae(), response,
                ehCertificado);

        if (resposta.get("message") != null) {
            response.setStatus(500);
        }

        if (nomePreCadastro != null && nomePreCadastro.length() > 0) {
            resposta.put("nomePreCadastro", nomePreCadastro);
        }

        if (emailPreCadastro != null && emailPreCadastro.length() > 0) {
            resposta.put("emailPreCadastro", emailPreCadastro);
        }

        if (telefonePreCadastro != null && telefonePreCadastro.length() > 0) {
            resposta.put("telefonePreCadastro", telefonePreCadastro);
        }

        String senhaCadastrada = null;
        String senhaAtiva = null;

        Credencial credencial = pessoaParticipanteRepository.consultaSenhaPorCpf(genPessoaFisica.getCpf());

        if (credencial!=null){
            senhaCadastrada = credencial.getInfoSenha();
            senhaAtiva =  credencial.getStatusCredencial()!=null?credencial.getStatusCredencial().toString():null;
        }

        if (senhaCadastrada == null && (senhaAtiva!=null && senhaAtiva.equals("A"))){
            throw new NFGException("Você possui credencial ativa nas bases da Sefaz-GO mas ainda não realizou seu primeiro acesso. Ligue: (62) 3269-2368");
        }

        resposta.put("ativarCaptcha", Captcha.verificarAtivacaoCaptcha("numeroDeTentativas", request));
        resposta.put("senhaCadastrada", senhaCadastrada);
        resposta.put("senhaAtiva", senhaAtiva);

        resposta.put("enderecoCadastrado", pessoaParticipanteRepository.consultaCepCadastrado(genPessoaFisica.getCpf()));
        return resposta;
    }

    @RequestMapping("gravarResgate")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public
    @HistoricoNFG
    @ResponseBody
    Map gravarResgate(String tipoResgate, String vlrTributo, String vlrTaxas, Integer selectBanco, String numeroAgencia, Integer selectConta, Integer contaBancaria, String digito, Integer idPremioBilhete){
        Map<String, Object> resposta=null;
        try{
            resposta = new HashMap<String, Object>();
            premioBilheteRepository.gravaSolicitacaoDeResgate(tipoResgate, vlrTributo, vlrTaxas,selectBanco, numeroAgencia, selectConta, contaBancaria, digito, idPremioBilhete);
            resposta.put("urlRedirect", "/cidadao/inicioSite");
            setSuccessMessage("Solicitação de resgate gravada com sucesso!");
        }catch (Exception e){
            
            logger.error("Erro ao tentar gravar resgate do cidadao "+getCidadaoLogado().getId()+": "+e.getMessage());
        }

        return resposta;
    }

    @RequestMapping("efetuarcadastro")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map efetuarCadastroDeCidadao(GENPessoaFisica genPessoaFisica,
                                                      String email, String telefone, String senha,
                                                      String senhaAtual,
                                                      Integer cep,
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

        logger.info("Verificando senha servidor para o cpf " + genPessoaFisica.getCpf());

        Credencial credencial = pessoaParticipanteRepository.consultaSenhaPorCpf(genPessoaFisica.getCpf());

        String senhaServidor = null;
        String senhaAtiva =  null;

        if (credencial!=null){
            senhaServidor = credencial.getInfoSenha();
            senhaAtiva = credencial.getStatusCredencial()!=null?credencial.getStatusCredencial().toString():null;
        }


        if (senhaServidor != null && senhaServidor.length() > 0) {
            boolean senhaValida = Encrypter.encryptSHA512(senhaAtual.toUpperCase()).equalsIgnoreCase(senhaServidor);
            if (!senhaValida && (senhaAtiva!=null && senhaAtiva.equals("A")) ) {
                throw new NFGException("    Usuário possui acesso ao Portal de Sistemas da SEFAZ ou ao Portal do Contabilista. Deverá utilizar a mesma senha e, se não lembrar, solicitar inativação pelo e-mail nfgoiana@sefaz.go.gov.br informando o CPF.");
            }
        }

        if (numero.length() > 10) {
            throw new NFGException("O campo número deve conter no máximo 10 dígitos.");
        }

        if (nomeLogradouro != null && nomeLogradouro.length() > 60) {
            throw new NFGException("O campo logradouro deve conter no máximo 60 caracteres");
        }

        logger.info(genPessoaFisica.getCpf() + " passou da etapa de validacao de senha");

        if (pessoaParticipanteRepository.emailCadastrado(email, null)!=null) {
            throw new NFGException("Já existe um cadastro com este endereço de e-mail.");
        }

        logger.info(genPessoaFisica.getCpf() + " passou da etapa de validacao de email");

        PessoaParticipante novoParticipante = pessoaParticipanteRepository.efetuaCadastroPessoaParticipante(genPessoaFisica,
                email, dddTelefone, nrTelefone,
                senha, cep, tipoLogradouro,
                nomeLogradouro, nomeBairro, numero, participaSortreio, recebeEmail,
                complemento, municipio, endHomologado, senhaServidor, senhaAtiva
        );

        if (novoParticipante==null) {
            throw new NFGException("Erro ao efetuar cadastro");
        }else{
            regraSorteioRepository.adicionaPontuacaoBonusCadastro(novoParticipante);
        }

        logger.info(genPessoaFisica.getCpf() + " passou da etapa de cadastro");

        Map<String, Object> resposta = new HashMap<String, Object>();

        logger.info("Setando CIDADAO "+novoParticipante.getId()+" na SESSAO " +request.getSession().getId());


        request.getSession().setAttribute(SESSION_CIDADAO_LOGADO, novoParticipante);
        setSuccessMessage("Cadastro realizado com sucesso, bem-vindo(a) ao Nota Fiscal Goiana!");
        resposta.put("urlRedirect", "/cidadao/inicioSite");

        return resposta;
    }


    @RequestMapping("loginSite")
    public ModelAndView viewLoginCidadaoFrame(){
        ModelAndView modelAndView;

        PessoaParticipante cidadaoLogado = getCidadaoLogado();

        if(cidadaoLogado != null){
            modelAndView = new ModelAndView("cidadao/frameUsuarioLogado");
            modelAndView.addObject("mensagemBemVindo", cidadaoLogado.getGenPessoaFisica().getNome());
            return modelAndView;
        }
        modelAndView = new ModelAndView("cidadao/frameUsuarioNaoLogado");

        return modelAndView;
    }

    @RequestMapping("login")
    public ModelAndView viewLoginCidadao() {
        return new ModelAndView("cidadao/login");
    }

    @RequestMapping("esqueciSenha")
    public ModelAndView viewEsqueciSenha() {
        return new ModelAndView("cidadao/esqueciSenha");
    }

    @RequestMapping("efetuarloginSite")
    public @ResponseBody Map efetuarLoginCidadaoFrame(String cpf, String senha,String challenge,String captchaResponse) throws BindException {
        Map<String, Object> retornoAutenticacao = pessoaParticipanteRepository.autenticaCidadao(cpf, senha);
        Map<String, Object> resposta = new HashMap<String, Object>();

        if(retornoAutenticacao.get("erro")==null){
            PessoaParticipante cidadaoLogado = (PessoaParticipante)retornoAutenticacao.get("pessoaParticipante");
            if((boolean)retornoAutenticacao.get("semCredencial")){
                resposta.put("semCredencial", true);
                resposta.put("urlRedirect", "/cidadao/conclusaoDadosPerfil");
                request.getSession().setAttribute(SESSION_ID_CIDADAO_SEM_CREDENC, cidadaoLogado.getId());
            }else{
                logger.info("Setando CIDADAO "+cidadaoLogado.getId()+" na SESSAO " +request.getSession().getId());
                request.getSession().setAttribute(SESSION_CIDADAO_LOGADO, cidadaoLogado);
                setSuccessMessage("Bem-vindo(a) ao Nota Fiscal Goiana!");
                resposta.put("urlRedirect", "/cidadao/inicioSite");
            }
        }else{
            resposta.put("urlRedirect", "/cidadao/login");
            setMensagemDeErro((String) retornoAutenticacao.get("erro"));
        }

        resposta.put("ativarCaptchaLogin", Captcha.verificarAtivacaoCaptcha("numeroDeTentativasLogin",request));
        return resposta;
    }


    @RequestMapping("efetuarlogin")
    public @ResponseBody Map efetuarLoginCidadao(String cpf, String senha,String challenge,String captchaResponse) throws BindException {
        captcha.validarCaptchaSeAtivo(request, challenge, captchaResponse);

        Map<String, Object> retornoAutenticacao = pessoaParticipanteRepository.autenticaCidadao(cpf, senha);
        Map<String, Object> resposta = new HashMap<String, Object>();


        if (retornoAutenticacao.get("erro")!=null){
            resposta.put("loginInvalido", (String) retornoAutenticacao.get("erro"));
        }else {

            PessoaParticipante cidadaoLogando = (PessoaParticipante)retornoAutenticacao.get("pessoaParticipante");
            if((boolean)retornoAutenticacao.get("semCredencial")){
                resposta.put("semCredencial", true);
                resposta.put("urlRedirect", "/cidadao/conclusaoDadosPerfil");
                request.getSession().setAttribute(SESSION_ID_CIDADAO_SEM_CREDENC, cidadaoLogando.getId());
            } else {
                setSuccessMessage("Bem-vindo(a) ao Nota Fiscal Goiana!");
                resposta.put("semCredencial", false);
                resposta.put("urlRedirect", "/cidadao/inicioSite");
                logger.info("Setando CIDADAO " + cidadaoLogando.getId() + " na SESSAO " + request.getSession().getId());

                request.getSession().setAttribute(SESSION_CIDADAO_LOGADO, cidadaoLogando);
            }
        }
        resposta.put("ativarCaptchaLogin", Captcha.verificarAtivacaoCaptcha("numeroDeTentativasLogin", request));
        return resposta;
    }


    @RequestMapping("recuperarsenha")
    public @ResponseBody Map enviarEmailRecuperacaoSenhaCidadao(String cpf) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        PessoaParticipante pessoaExistente = pessoaParticipanteRepository.findByCPF(cpf);
        if (pessoaExistente != null) {
            if (pessoaParticipanteRepository.pessoaComErroNoCadastro(pessoaExistente)){
                Map<String, Object> erroCadastro = new HashMap<>();
                erroCadastro.put("semCredencial",true);
                erroCadastro.put("urlRedirect", "/cidadao/conclusaoDadosPerfil");
                request.getSession().setAttribute(SESSION_ID_CIDADAO_SEM_CREDENC, pessoaExistente.getId());
                response.setStatus(500);
                return erroCadastro;
            }
        }


        String emailDeEnvio = pessoaParticipanteRepository.enviaEmailRecuperacaoSenha(cpf);
        if (emailDeEnvio!=null) {
            resposta.put("status", "E-mail para recuperação de senha enviado para o e-mail " + emailDeEnvio + ", verifique a sua caixa de entrada!");
        } else {
            throw new NFGException("Falha no envio de e-mail. Favor entrar em contato com nfgoiana@sefaz.go.gov.br informando o problema.");
        }
        return resposta;
    }


    @RequestMapping("verificaInconsistencia")
    public @ResponseBody Map verificaInconsistencia(String nomePainel,String nomeTopo) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        HttpSession session = request.getSession();
        PessoaParticipante cidadao =(PessoaParticipante) session.getAttribute(BaseController.SESSION_CIDADAO_LOGADO);
        logger.info("INCONSISTENCIA DE CIDADAO ENCONTRADA. NOME NO TOPO:" + nomeTopo + "  NOME DO PAINEL MEU PERFIL:" + nomePainel + "  SESSAO:" + session.getId() + "   CIDADAO NA SESSAO:" + (cidadao != null ? cidadao.getId() : "null"));

        return resposta;
    }



    @RequestMapping("inicioSite")
    public ModelAndView viewPaginaInicialCidadaoSite() throws ParseException {
        ModelAndView modelAndView;
        logger.info("getCidadaoLogado() em: viewPaginaInicialCidadaoSite" + request.getSession().getId());
        PessoaParticipante cidadao = getCidadaoLogado();


        if (cidadao!=null){
            logger.info("CIDADAO MEU PERFIL:"+cidadao.getId()+". Sessao:"+request.getSession().getId());
        }

        modelAndView = new ModelAndView("cidadao/telaInicialSite");
        Map<String, Object> enderecoCadastrado = pessoaParticipanteRepository.consultaCepCadastrado(cidadao.getGenPessoaFisica().getCpf());
        modelAndView.addObject("cidadao", cidadao);
        List<RegraSorteio> sorteios = regraSorteioRepository.listSorteios();

        modelAndView.addObject("sorteios", sorteios);
        modelAndView.addObject("endereco", enderecoCadastrado);
        return modelAndView;
    }

    @RequestMapping("conclusaoDadosPerfil")
    public ModelAndView concluirPerfil() throws Exception {

        Integer idCidadao= (Integer) request.getSession().getAttribute(SESSION_ID_CIDADAO_SEM_CREDENC);

        PessoaParticipante cidadao = pessoaParticipanteRepository.get(idCidadao);

        ModelAndView modelAndView = new ModelAndView("cidadao/conclusaoDadosPerfil");
        Map<String, Object> enderecoCadastrado = pessoaParticipanteRepository.consultaCepCadastrado(cidadao.getGenPessoaFisica().getCpf());

        modelAndView.addObject("cep", (Integer) enderecoCadastrado.get("cep"));
        modelAndView.addObject("nomeLogradouro", (String) enderecoCadastrado.get("nomeLogradouro"));
        modelAndView.addObject("numero", (String) enderecoCadastrado.get("numero"));
        modelAndView.addObject("nomeBairro", (String) enderecoCadastrado.get("nomeBairro"));
        modelAndView.addObject("complemento", (String) enderecoCadastrado.get("complemento"));

        modelAndView.addObject("nomeTipoLogradouro", (String) enderecoCadastrado.get("tipoLogradouro"));
        modelAndView.addObject("nomeUf", (String) enderecoCadastrado.get("uf"));
        modelAndView.addObject("nomeMunicipio", (String) enderecoCadastrado.get("municipio"));
        modelAndView.addObject("enderecoHomolog", (Character) enderecoCadastrado.get("indiHomologCadastro"));

        modelAndView.addObject("cidadao", cidadao);
        modelAndView.addObject("ufs", ufRepository.listar("codgUf"));
        modelAndView.addObject("tiposLogradouro", tipoLogradouroRepository.listar("descTipoLogradouro"));
        modelAndView.addObject("participaSorteio", cidadao.getParticipaSorteio().toString().equals("S"));
        modelAndView.addObject("recebeEmail", cidadao.getRecebeEmail().toString().equals("S"));
        return modelAndView;
    }

    @RequestMapping("atualizaPerfil")
    public ModelAndView viewAtualizaPerfilCidadao() throws Exception {
        logger.info("getCidadaoLogado() em: atualizaPerfil"+request.getSession().getId());
        PessoaParticipante cidadao = getCidadaoLogado();
        ModelAndView modelAndView = new ModelAndView("cidadao/perfil");
        Map<String, Object> enderecoCadastrado = pessoaParticipanteRepository.consultaCepCadastrado(cidadao.getGenPessoaFisica().getCpf());

        modelAndView.addObject("cep", (Integer) enderecoCadastrado.get("cep"));
        modelAndView.addObject("nomeLogradouro", (String) enderecoCadastrado.get("nomeLogradouro"));
        modelAndView.addObject("numero", (String) enderecoCadastrado.get("numero"));
        modelAndView.addObject("nomeBairro", (String) enderecoCadastrado.get("nomeBairro"));
        modelAndView.addObject("complemento", (String) enderecoCadastrado.get("complemento"));

        modelAndView.addObject("nomeTipoLogradouro", (String) enderecoCadastrado.get("tipoLogradouro"));
        modelAndView.addObject("nomeUf", (String) enderecoCadastrado.get("uf"));
        modelAndView.addObject("nomeMunicipio", (String) enderecoCadastrado.get("municipio"));
        modelAndView.addObject("enderecoHomolog", (Character) enderecoCadastrado.get("indiHomologCadastro"));

        modelAndView.addObject("cidadao", cidadao);
        modelAndView.addObject("ufs", ufRepository.listar("codgUf"));
        modelAndView.addObject("tiposLogradouro", tipoLogradouroRepository.listar("descTipoLogradouro"));
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
        modelAndView.addObject("bancos", genBancoRepository.listarBancos());
        return modelAndView;
    }

    /**diferenciando do metodo "grava perfil" por uma questao de permissao de acesso no CidadaoInterceptor*/
    @RequestMapping("gravaConclusaoPerfil")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
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
        return dadosCidadao(cpf,email,telefone,participaSortreio,recebeEmail,cep,tipoLogradouro,nomeLogradouro,nomeBairro,numero,complemento,municipio,endHomologado,senha);
    }

    @RequestMapping("gravaPerfil")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map atualizarPerfilCidadao(String cpf, String email, String telefone, Boolean participaSortreio, Boolean recebeEmail,
                                                    Integer cep,
                                                    Integer tipoLogradouro,
                                                    String nomeLogradouro,
                                                    String nomeBairro,
                                                    String numero,
                                                    String complemento,
                                                    Integer municipio,
                                                    String endHomologado
    ) {
        return dadosCidadao(cpf,email,telefone,participaSortreio,recebeEmail,cep,tipoLogradouro,nomeLogradouro,nomeBairro,numero,complemento,municipio,endHomologado, null);
    }

    public Map dadosCidadao(String cpf, String email, String telefone, Boolean participaSortreio, Boolean recebeEmail,
                            Integer cep,
                            Integer tipoLogradouro,
                            String nomeLogradouro,
                            String nomeBairro,
                            String numero,
                            String complemento,
                            Integer municipio,
                            String endHomologado, String senha){
        Map<String, Object> resposta = new HashMap<String, Object>();
        Integer dddTelefone, nrTelefone;
        try {
            dddTelefone = Integer.parseInt(telefone.split(" ")[0]);
            nrTelefone = Integer.parseInt(telefone.split(" ")[1]);
        } catch (Exception e) {
            throw new NFGException("Campo Telefone em formato errado!", true);
        }

        GENPessoaFisica genPessoaFisica = genPessoaRepository.findByCpf(cpf);

        if (pessoaParticipanteRepository.emailCadastrado(email, genPessoaFisica.getIdPessoa())!=null) {
            throw new NFGException("Já existe um cadastro com este endereço de e-mail.", true);
        }

        PessoaParticipante cidadao = pessoaParticipanteRepository.atualizaDados(cpf, email, dddTelefone, nrTelefone, participaSortreio,
                recebeEmail, cep, tipoLogradouro, nomeLogradouro, nomeBairro, numero, complemento, municipio, endHomologado);
        if (cidadao != null) {
            if (senha!=null){
                pessoaParticipanteRepository.gravaNovaSenha(cidadao.getGenPessoaFisica().getCpf(),senha);
            }
            logger.info("Setando CIDADAO "+cidadao.getId()+" na SESSAO " +request.getSession().getId());

            request.getSession().setAttribute(SESSION_CIDADAO_LOGADO, cidadao);
            setSuccessMessage("Perfil atualizado com sucesso!");
            resposta.put("urlRedirect", "/cidadao/inicioSite");
            return resposta;
        } else {
            throw new NFGException("Falha ao atualizar o perfil!", true);
        }
    }

    @RequestMapping("alterarSenhaPerfil")
    public ModelAndView viewAlterarSenhaDoPerfil() {
        logger.info("getCidadaoLogado() em: viewAlterarSenhaDoPerfil"+request.getSession().getId());
        PessoaParticipante cidadao = getCidadaoLogado();
        ModelAndView modelAndView = new ModelAndView("cidadao/alterarSenhaPerfil");
        modelAndView.addObject("cidadao", cidadao);
        return modelAndView;
    }

    @RequestMapping("gravaNovaSenhaPerfil")
    public @ResponseBody Map alterarSenhaDoPerfil(String cpf, String senhaPerfilAntiga, String senhaPerfilNova, String senhaPerfilConfirm) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        Credencial credencial = pessoaParticipanteRepository.consultaSenhaPorCpf(cpf);
        String senhaServidor = credencial!=null? credencial.getInfoSenha():null;


        boolean senhaValida = Encrypter.encryptSHA512(senhaPerfilAntiga.toUpperCase()).equalsIgnoreCase(senhaServidor);
        if (!senhaValida) {
            throw new NFGException("A senha atual está incorreta!", true);
        }

        senhaValida &= pessoaParticipanteRepository.gravaNovaSenha(cpf, senhaPerfilNova);
        if (!senhaValida) {
            throw new NFGException("Falha na gravação da nova senha!", true);
        }

        setSuccessMessage("Nova senha gravada com sucesso!");
        resposta.put("urlRedirect", "/cidadao/inicioSite");
        return resposta;
    }

    @RequestMapping("efetuarlogout")
    public String efetuarLogoutCidadao() {
        HttpSession session = request.getSession();
        logger.info("efetuarlogout: Setando NULL no cidadao da sessao: "+session.getId());
        session.setAttribute(SESSION_CIDADAO_LOGADO, null);
        session.setAttribute(ContadorLoginService.SESSION_CONTADOR_LOGADO, null);
        session.setAttribute(CertificateInterceptor.SESSION_EMPRESA_LOGADA, null);
        return "redirect:login";
    }

    @RequestMapping("efetuarlogoutSite")
    public ModelAndView efetuarLogoutCidadaoSite() {
        HttpSession session = request.getSession();
        logger.info("efetuarlogoutSite: Setando NULL no cidadao da sessao: "+session.getId());
        session.setAttribute(SESSION_CIDADAO_LOGADO, null);
        session.setAttribute(ContadorLoginService.SESSION_CONTADOR_LOGADO, null);
        session.setAttribute(CertificateInterceptor.SESSION_EMPRESA_LOGADA, null);
        return new ModelAndView(new RedirectView(urlHome));
    }

    @RequestMapping("redirecionarHome")
    public ModelAndView redirecionarHome() {
        return new ModelAndView(new RedirectView(urlHome));
    }

    @RequestMapping("listarPremiacao/{page}")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public
    @ResponseBody
    Map<String, Object> listarPremiacao(@PathVariable(value = "page") Integer page) throws ParseException {
        Integer max = 5;

        PessoaParticipante cidadaoLogado = getCidadaoLogado();

        List<DTOPremiacao> listDtoPremiacao = pessoaParticipanteRepository.listarPremiacao(cidadaoLogado.getId());

        listDtoPremiacao = pessoaParticipanteRepository.efetuaCalculoDeValoresPremiacao(listDtoPremiacao);


        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("listDtoPremiacao", listDtoPremiacao);
        return resposta;
    }


    @RequestMapping("listarNotasCidadao/{page}")
    public @ResponseBody Map<String, Object> listarNotasCidadao(@PathVariable(value = "page") Integer page, String cpfFiltro, BindException bind) throws ParseException {
        Integer max = 5;
        List<DTOMinhasNotas> docsFiscais = pessoaParticipanteRepository.findDocumentosFiscaisPorCpf(cpfFiltro, null, null, max, page, false, 5);

        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("minhasNotas", docsFiscais);
        return resposta;
    }

    @RequestMapping("viewMinhasNotasEmDetalhe")
    public ModelAndView viewMinhasNotasEmDetalhe() {
        logger.info("getCidadaoLogado() em: viewMinhasNotasEmDetalhe" + request.getSession().getId());
        PessoaParticipante cidadao = getCidadaoLogado();
        ModelAndView modelAndView = new ModelAndView("cidadao/minhasNotasDetalhe");
        modelAndView.addObject("cidadao", cidadao);
        return modelAndView;
    }


    @RequestMapping("listarNotasCidadaoEmDetalhe/{page}")
    public @ResponseBody Map<String, Object> listarNotasCidadaoEmDetalhe(@PathVariable(value = "page") Integer page, String cpfFiltro, String referenciaInicial, String referenciaFinal, BindException bind) throws ParseException {
        Integer max = 10;
        Integer count;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataInicial = referenciaInicial != null && referenciaInicial.length() > 0 ? sdf.parse(referenciaInicial) : null;
        Date dataFinal = referenciaFinal != null && referenciaFinal.length() > 0 ? sdf.parse(referenciaFinal) : null;

        List<DTOMinhasNotas> docsFiscais = pessoaParticipanteRepository.findDocumentosFiscaisPorCpf(cpfFiltro, dataInicial, dataFinal, max, page, false, 5);

        count = ((List<Integer>) pessoaParticipanteRepository.findDocumentosFiscaisPorCpf(cpfFiltro, dataInicial, dataFinal, max, page, true,5)).get(0);

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);


        resposta.put("minhasNotas", docsFiscais);
        resposta.put("pagination", pagination);

        return resposta;
    }


    @RequestMapping("meuPlacar")
    public @ResponseBody Map<String, Object> meuPlacar() throws ParseException {
        logger.info("getCidadaoLogado() em: meuPlacar" + request.getSession().getId());
        PessoaParticipante cidadao = getCidadaoLogado();
        Map<String, Object> resposta = new HashMap<String, Object>();
        resposta.put("numeroDeNotas", pessoaParticipanteRepository.numeroDeNotasPorCpf(cidadao.getGenPessoaFisica().getCpf()));
        resposta.put("pontosProximoSorteio", regraSorteioRepository.pontuacaoSemSorteio(cidadao.getId()));
        resposta.put("totalPremiacaoMeuPlacar", "Em breve.");
        return resposta;
    }


    @RequestMapping("geraCartao")
    public @ResponseBody HttpServletResponse geraCartao(HttpServletResponse response) throws IOException {
        Map<String, Object> resposta = new HashMap<String, Object>();
        CdBarrasUtil cdBarrasUtil = new CdBarrasUtil();
        logger.info("getCidadaoLogado() em: geraCartao"+request.getSession().getId());
        GENPessoaFisica cidadaoLogado = getCidadaoLogado().getGenPessoaFisica();

        InputStream streamTemplateCartao = servletContext.getResourceAsStream("/resources/images/cartao_cidadao_novo.jpg");
        File imagemCdBarras = cdBarrasUtil.geraCodigoDeBarras(response, cidadaoLogado.getCpf());

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

        return null;
    }

    @RequestMapping("detalheBilhetes/{idSorteio}")
    public ModelAndView viewBilhetesEmDetalhe(@PathVariable(value = "idSorteio") Integer idSorteio){
        logger.info("getCidadaoLogado() em: viewBilhetesEmDetalhe"+request.getSession().getId());
        PessoaParticipante cidadao = getCidadaoLogado();
        Integer totalDePontos = null;

        List<BilhetePessoa> bilhetesCidadao = bilhetePessoaRepository.listBilhetes(cidadao.getId(), idSorteio, null, null);
        Long totalBilhetes = bilhetePessoaRepository.retornaTotalDeBilhetes(cidadao.getId(), idSorteio);

        RegraSorteio sorteio = regraSorteioRepository.buscaSorteioPorId(idSorteio);
        totalDePontos = regraSorteioRepository.totalDePontosPorSorteio(idSorteio, cidadao.getId());

        ModelAndView modelAndView = new ModelAndView("cidadao/bilhetes");
        modelAndView.addObject("sorteio", sorteio);
        modelAndView.addObject("bilhetes", bilhetesCidadao);
        modelAndView.addObject("total", totalBilhetes);
        modelAndView.addObject("totalDePontos", totalDePontos);

        return modelAndView;
    }

    @RequestMapping("detalhePontos/{idSorteio}")
    public ModelAndView viewPontosEmDetalhe(@PathVariable(value = "idSorteio") Integer idSorteio){
        logger.info("getCidadaoLogado() em: viewPontosEmDetalhe"+request.getSession().getId());
        PessoaParticipante cidadao = getCidadaoLogado();
        RegraSorteio sorteio;

        Integer totalDePontos = null;
        Long totalBilhetes = null;
        try{
            sorteio = regraSorteioRepository.buscaSorteioPorId(idSorteio);
        }catch (Exception e){
            throw new NFGException("Ops, parece que esse sorteio não existe. Tente novamente.",new ModelAndView("cidadao/telainicial"));
        }

        try{
            totalDePontos = regraSorteioRepository.totalDePontosPorSorteio(idSorteio, cidadao.getId());
            totalBilhetes = bilhetePessoaRepository.retornaTotalDeBilhetes(cidadao.getId(), idSorteio);
        }catch (Exception e){
            logger.error("Erro ao carregar os totalizadores prtct.:"+cidadao.getId()+" "+e.getMessage());
        }

        ModelAndView modelAndView = new ModelAndView("cidadao/pontos");
        modelAndView.addObject("sorteio", sorteio);
        modelAndView.addObject("totalDePontos", totalDePontos);
        modelAndView.addObject("totalDeBilhetes", totalBilhetes);
        return modelAndView;
    }


    @RequestMapping("carregaDadosDaPremiacao")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map<String, Object>
    carregaDadosDaPremiacao(Integer idSorteio){
        Map<String, Object> resposta = new HashMap<>();
        PessoaParticipante cidadaoLogado = getCidadaoLogado();
        Boolean isUsuarioPremiado = pessoaParticipanteRepository.verificarSeUsuarioPremiado(cidadaoLogado.getId());

        resposta.put("isUsuarioPremiado", new Boolean(isUsuarioPremiado));
        return resposta;
    }

    @RequestMapping("carregaDadosDoSorteioParaCidadao")
    public @ResponseBody Map<String, Object> carregaDadosDoSorteio(Integer idSorteio, BindException bind) throws ParseException {
        logger.info("getCidadaoLogado() em: carregaDadosDoSorteioParaCidadao" + request.getSession().getId());
        PessoaParticipante cidadao = getCidadaoLogado();
        return regraSorteioRepository.carregaDadosDoSorteioParaCidadao(idSorteio, cidadao.getId());

    }

    @RequestMapping("listarBilhetes/ {page}")
    public @ResponseBody Map<String, Object> listarBilhetes(@PathVariable(value = "page") Integer page, Integer idSorteio, BindException bind){
        Integer max = 3*3;
        Long count = 0L;
        logger.info("getCidadaoLogado() em: listarBilhetes. Session:"+request.getSession().getId());
        PessoaParticipante cidadao = getCidadaoLogado();

        List dadosBilhetes = bilhetePessoaRepository.listBilheteMap(cidadao.getId(), idSorteio, max, page, 3);
        if(dadosBilhetes.size() > 0){
            count = bilhetePessoaRepository.retornaTotalDeBilhetes(cidadao.getId(), idSorteio);
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
        Integer max = 7;
        Integer count = 0;
        logger.info("getCidadaoLogado() em: listarPontosDasNotas"+request.getSession().getId());
        Integer idPessoa = getCidadaoLogado().getId();

        List dadosPontuacaoNotas = regraSorteioRepository.consultaPontuacaoDocsFiscaisPorSorteio(idSorteio, idPessoa, max, page, false);
        if (dadosPontuacaoNotas.size()>0){
            count =  (Integer)(regraSorteioRepository.consultaPontuacaoDocsFiscaisPorSorteio(idSorteio, idPessoa, max, page, true).get(0));
        }

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("dadosPontuacaoNotas", dadosPontuacaoNotas);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("listarPontosBonus/{page}")
    public @ResponseBody Map<String, Object> listarPontosBonus(@PathVariable(value = "page") Integer page, Integer idSorteio, BindException bind)   {
        Integer max = 3;
        Integer count = 0;
        logger.info("getCidadaoLogado() em: listarPontosBonus"+request.getSession().getId());
        Integer idPessoa = getCidadaoLogado().getId();

        List dadosPontuacaoBonus = regraSorteioRepository.consultaPontuacaoBonusPorSorteio(idSorteio, idPessoa, max, page, false);
        if (dadosPontuacaoBonus.size()>0){
            count =  (Integer)(regraSorteioRepository.consultaPontuacaoBonusPorSorteio(idSorteio, idPessoa, max, page, true).get(0));
        }

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
        if (pessoaParticipanteRepository.verificaTokenSenha(token, cpf)) {
            ModelAndView m = new ModelAndView("cidadao/novasenha");
            m.addObject("token", token);
            m.addObject("cpf", cpf);
            return m;
        } else {
            throw new NFGException("Falha na validação do token!");
        }

    }

    @RequestMapping("gravaNovaSenha")
    public @ResponseBody Map gravaNovaSenhaCidadao(String cpf, String tokenSenha, String novaSenha, String novaSenhaConfirm) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        boolean senhaValida = pessoaParticipanteRepository.gravaNovaSenha(cpf, novaSenha);
        if (!senhaValida) throw new NFGException("Falha na gravação da nova senha!");

        setSuccessMessage("Nova senha gravada com sucesso!");
        resposta.put("urlRedirect", "/cidadao/login");
        return resposta;
    }


    @RequestMapping("certificado")
    public ModelAndView certificadoDigitalDeCidadao() throws Exception {
        GENPessoaFisica genPessoaFisica = genPessoaRepository.findByCpf(getCertificadoPf().getPfCpf());
        if (genPessoaFisica != null) {
            PessoaParticipante cidadao = pessoaParticipanteRepository.findByGenPessoaCPF(genPessoaFisica);
            if (cidadao != null) {
                logger.info("Setando CIDADAO "+cidadao.getId()+" na SESSAO " +request.getSession().getId());

                request.getSession().setAttribute(SESSION_CIDADAO_LOGADO, cidadao);
                return viewTelaInicialCertificado(cidadao);
            } else {
                return viewCadastroCertificado();
            }
        } else {
            return viewCadastroCertificado();
        }
    }

    public ModelAndView viewCadastroCertificado() throws Exception {
        ModelAndView modelAndView = new ModelAndView("cidadao/cadastro");
        modelAndView.addObject("certificado", getCertificadoPf());
        modelAndView.addObject("ufs", ufRepository.listar("codgUf"));
        modelAndView.addObject("tiposLogradouro", tipoLogradouroRepository.listar("descTipoLogradouro"));
        modelAndView.addObject("isCertificado", true);
        return modelAndView;
    }

    public ModelAndView viewTelaInicialCertificado(PessoaParticipante cidadao) {
        ModelAndView modelAndView = new ModelAndView("cidadao/telaInicialSite");
        Map<String, Object> enderecoCadastrado = pessoaParticipanteRepository.consultaCepCadastrado(cidadao.getGenPessoaFisica().getCpf());
        modelAndView.addObject("cidadao", cidadao);
        List<RegraSorteio> sorteios = regraSorteioRepository.listSorteios();
        modelAndView.addObject("sorteios", sorteios);
        modelAndView.addObject("endereco", enderecoCadastrado);
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
        Integer count;

        PessoaParticipante cidadao = getCidadaoLogado();
        Map reclamacoesPaginate = reclamacaoRepository.findReclamacoesDoCidadao(cidadao, page, max);
        List<DocumentoFiscalReclamado> reclamacoes = (List<DocumentoFiscalReclamado>) reclamacoesPaginate.get("list");
        count = (Integer) reclamacoesPaginate.get("count");

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("cidadao", cidadao);
        resposta.put("reclamacoes", reclamacoes);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("salvarNovaReclamacao")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map salvarNovaReclamacao(
            Integer tipoDocFiscalReclamacao,
            Integer codgMotivo,
            Date dataEmissaoDocFiscal,
            Integer numeroReclamacao,
            Integer iEReclamacao,
            Double valorReclamacao,
            boolean dataDentroDoPrazo,
            @RequestParam("file") MultipartFile fileReclamacao
    ) {
        PessoaParticipante cidadao = getCidadaoLogado();
        Map retorno = reclamacaoRepository.cadastraNovaReclamacao(tipoDocFiscalReclamacao, codgMotivo, dataEmissaoDocFiscal, numeroReclamacao, iEReclamacao, valorReclamacao, fileReclamacao, cidadao,dataDentroDoPrazo);

        if((Boolean)retorno.get("sucesso")){
            setSuccessMessage("A sua reclamação foi registrada com sucesso e a empresa será notificada. Acompanhe o processo pelo painel Minhas Reclamações.");
        }

        return retorno;
    }

    @RequestMapping("alterarSituacaoReclamacao")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map alterarSituacaoReclamacao(Integer idReclamacao,Integer novoCodgTipoCompl, String infoReclamacao ) {
        PessoaParticipante cidadao = getCidadaoLogado();
        DocumentoFiscalReclamado reclamacao = reclamacaoRepository.findReclamacaoPorId(idReclamacao);
        Map retorno = reclamacaoRepository.alteracaoDeSituacaoReclamacaoPorCidadao(reclamacao, novoCodgTipoCompl, infoReclamacao, cidadao);

        return retorno;
    }

    @RequestMapping("selectAcoesDisponiveis")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @HistoricoNFG
    public @ResponseBody Map selectAcoesDisponiveis(Integer idReclamacao) {
        PessoaParticipante cidadao = getCidadaoLogado();
        DocumentoFiscalReclamado reclamacao = reclamacaoRepository.findReclamacaoPorId(idReclamacao);
        Map retorno = new HashMap();
        List<ComplSituacaoReclamacao> acoesDisponiveis = reclamacaoRepository.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CIDADAO, reclamacao);
        retorno.put("acoesDisponiveis", acoesDisponiveis);
        return retorno;
    }

    @RequestMapping("verReclamacaoDetalhe/{idReclamacao}")
    public ModelAndView verReclamacaoDetalhe(@PathVariable("idReclamacao") Integer idReclamacao){
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("cidadao/reclamacaoDetalhe");

        DocumentoFiscalReclamado reclamacao = reclamacaoRepository.findReclamacaoPorId(idReclamacao);

        List<ComplSituacaoReclamacao> statusDisponiveis = reclamacaoRepository.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CIDADAO,reclamacao);

        modelAndView.addObject("reclamacao", reclamacao);
        modelAndView.addObject("statusDisponiveis", statusDisponiveis);
        String nomeFantasia = cceRepository.getFodendoNomeFantasiaPeloCnpj(reclamacao.getNumeroCnpjEmpresa());
        modelAndView.addObject("nomeFantasia", nomeFantasia != null ? nomeFantasia : "");
        modelAndView.addObject("dataEmissaoStr", simpleDateFormat.format(reclamacao.getDataDocumentoFiscal()));
        modelAndView.addObject("dataReclamacaoStr", simpleDateFormat.format(reclamacao.getDataReclamacao()));

        return modelAndView;
    }

    @RequestMapping("listarAndamentoReclamacao/{page}")
    public @ResponseBody Map<String, Object> listarAndamentoReclamacao(@PathVariable(value = "page") Integer page, Integer idReclamacao,BindException bind) throws ParseException {
        Integer max = 5;
        Integer count=0;

        Map andamentoReclamacoesPaginate = reclamacaoRepository.findAndamentoDaReclamacao(idReclamacao, page, max);
        List<SituacaoDocumentoFiscalReclamado> situacoesReclamacao = (List<SituacaoDocumentoFiscalReclamado>) andamentoReclamacoesPaginate.get("list");
        count = (Integer) andamentoReclamacoesPaginate.get("count");

        Map<String, Object> resposta = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();

        pagination.put("total", count);
        pagination.put("page", ++page);
        pagination.put("max", max);

        resposta.put("situacoesReclamacao", situacoesReclamacao);
        resposta.put("pagination", pagination);

        return resposta;
    }

    @RequestMapping("buscarEmpresaPorIe")
    public
    @ResponseBody
    Map buscarEmpresaPorIe(Integer inscricao) {
        Map<String, Object> resposta = new HashMap<String, Object>();

        String nomeFantasia = cceRepository.getFodendoNomeFantasiaPelaInscricao(inscricao);

        resposta.put("nomeFantasia", nomeFantasia);
        return resposta;
    }
}