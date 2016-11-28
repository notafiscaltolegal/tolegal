package gov.goias.persistencia;

import entidade.Credencial;
import entidade.endereco.Endereco;

import entidade.endereco.Logradouro;
import entidade.endereco.Municipio;
import entidade.endereco.TipoLogradouro;
import gov.goias.dtos.DTOContatoGanhadores;
import gov.goias.dtos.DTOMinhasNotas;
import gov.goias.dtos.DTOPremiacao;
import gov.goias.dtos.DTOPremiacaoPortal;
import gov.goias.entidades.*;
import gov.goias.exceptions.NFGException;
import gov.goias.mappers.MapperDTOContatoGanhadores;
import gov.goias.mappers.MapperDTOPremiacao;
import gov.goias.services.IGenPessoaFisicaService;
import gov.goias.util.Encrypter;
import org.apache.log4j.Logger;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lucas-mp
 * @since 14/10/2014
 */
public class PessoaParticipanteRepository extends GenericRepository<Integer, PessoaParticipante> {

    private static final Logger logger = Logger.getLogger(PessoaParticipanteRepository.class);

    @Autowired
    GENPessoaFisica genPessoaFisicaRepository;

    @Autowired
    ParametrosRepository parametrosRepository;

    @Autowired
    MonitorRepository monitorRepository;

    @Autowired
    RFEPessoaFisica rfePessoaFisicaRepository;

    @Autowired
    RegraSorteio regraSorteioRepository;

    @Autowired
    IGenPessoaFisicaService genService;

    @Value("${nfg.host}")
    String host;

    public PessoaParticipante efetuaCadastroPessoaParticipante(GENPessoaFisica genPessoaFisica, String email,
                                                               Integer dddTelefone, Integer nrTelefon, String senha,
                                                               Integer cep, Integer tipoLogradouro, String nomeLogradouro,
                                                               String nomeBairro, String numero, Boolean participaSortreio, Boolean recebeEmail, String complemento,
                                                               Integer municipio, String endHomologado, String senhaServidor, String senhaAtiva){
        try {
            boolean response = true;
            String servicoPessoaFisica=genService.setPessoaFisicaWs(genPessoaFisica);

            genPessoaFisica = genPessoaFisicaRepository.findByCpf(genPessoaFisica.getCpf());
            if (genPessoaFisica==null) return null;

            String servicoEmail = genService.setEmailPessoaFisicaWs(email, genPessoaFisica.getIdPessoa(),genPessoaFisica.getMatricula());
            String servicoTelefone = genService.setTelefonePessoaFisicaWs(dddTelefone, nrTelefon, genPessoaFisica.getIdPessoa(),genPessoaFisica.getMatricula());

            response &= servicoPessoaFisica != null && (servicoEmail != null || email.isEmpty()) && servicoTelefone != null;

            if (senhaServidor==null || !(senhaServidor.length()>0) || senhaAtiva.equals("I")){
                String servicoSenha = genService.setSenhaPessoaFisicaWs(genPessoaFisica.getCpf(), senha.toUpperCase(), genPessoaFisica.getMatricula());
                response &= servicoSenha != null ;
            }

            if (!endHomologado.equals("S")){
                String servicoEndereco =  gravaDadosEndereco(cep,tipoLogradouro,nomeLogradouro,nomeBairro,numero,complemento,municipio,genPessoaFisica);
                response &= servicoEndereco != null ;
            }

            if(!response){
                throw new NFGException("Erro ao efetuar cadastro devido indisponibilidade dos serviços corporativos, tente novamente posteriormente.");
            }

            //Salvando pessoa participante do NFG:
            PessoaParticipante pessoaParticipante = new PessoaParticipante();
            pessoaParticipante.setGenPessoaFisica(genPessoaFisica);
            pessoaParticipante.setParticipaSorteio(participaSortreio ? 'S' : 'N');
            pessoaParticipante.setRecebeEmail(recebeEmail ? 'S' : 'N');
            pessoaParticipante.setDataCadastro(new Date());
            pessoaParticipante.save();


            return pessoaParticipante;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException(e.getMessage());
        }
    }

    public String gravaDadosEndereco(Integer cep, Integer codgTipoLogradouro, String nomeLogradouro, String nomeBairro, String numero, String complemento,Integer codgMunicipio,GENPessoaFisica genPessoaFisica){
        try{
            TipoLogradouro tipoDeLogradouro = new TipoLogradouro();
            tipoDeLogradouro.setTipoLogradouro(codgTipoLogradouro);

            Municipio municipio = new Municipio();
            municipio.setCodigo(codgMunicipio);

            Endereco endereco = new Endereco();
            endereco.setCep(cep);
            endereco.setNomeLogradouro(nomeLogradouro);
            endereco.setNomeBairro(nomeBairro);
            endereco.setNumero(numero);
            endereco.setComplemento(complemento);
            endereco.setMunicipio(municipio);
            endereco.setTipoLogradouro(tipoDeLogradouro);

            String servicoEndereco = genService.setEnderecoPessoaFisicaWs(endereco,genPessoaFisica.getMatricula(),genPessoaFisica.getIdPessoa());
            logger.info("Tentando gravar o Endereco Pessoa Fisica atraves do GenServico:" + servicoEndereco);

            return servicoEndereco;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao efetuar cadastro de Endereco!");
        }
    }


    public Map preConsultaDeCidadaoNasBases(String cpf, Date dataDeNascimento, String nomeDaMae, HttpServletResponse response, boolean ehCertificado){
        IRepositorioPessoaFisica repositorio;
        Map<String, Object> resposta;

        //For de duas itereções: 1 e 2.
        //A primeira retorna se achar o repositorio e nao conter nenhuma mensagem de erro
        //A segunda retorna de qualquer maneira, encontrando ou nao o repositorio com ou sem erros
        //O objetivo do loop: dar prioridade de retorno nos casos onde acha repositorio.
        for (int nrIteracao=1; nrIteracao<=2; nrIteracao++){

            repositorio = consultaGenPessoa(cpf); //Repo. Gen Pessoa Fisica
            if (repositorio!=null){
                resposta = geraRespostaPreConsultaDeCidadao(repositorio,dataDeNascimento, nomeDaMae, response,ehCertificado);
                if (resposta.get("message") == null || nrIteracao==2){ //message nao nulo significa que ocorreu um erro
                    return resposta;
                }
            }

            repositorio = consultaRfePessoa(cpf); //Repo. Receita Federal
            if (repositorio!=null){
                resposta = geraRespostaPreConsultaDeCidadao(repositorio, dataDeNascimento, nomeDaMae, response, ehCertificado);
                if (resposta.get("message") == null || nrIteracao==2){
                    return resposta;
                }
            }

            repositorio = consultaWebServiceRF(cpf); //Repo. WebService da Receita Federal
            if (repositorio!=null){
                resposta = geraRespostaPreConsultaDeCidadao(repositorio,dataDeNascimento, nomeDaMae, response, ehCertificado);
                if (resposta.get("message") == null || nrIteracao==2){
                    return resposta;
                }
            }

            if (nrIteracao==2){
                return geraRetornoParaErro("O CPF informado não foi encontrado!",response);
            }
        } //fim for

        return null;
    }


    public Map<String, Object> geraRespostaPreConsultaDeCidadao(IRepositorioPessoaFisica repositorio, Date dataDeNascimento, String nomeDaMae, HttpServletResponse response, boolean ehCertificado){
        Map<String, Object> dadosCidadaoDaBase = new HashMap<String, Object>();

        dadosCidadaoDaBase.put("cpf", repositorio.getCpf());
        dadosCidadaoDaBase.put("nome", repositorio.getNome());

        //o parametro true, permite que a data seja nula, ignorando a validacao
        if (textoConfereComOBanco(nomeDaMae, repositorio.getNomeDaMae(), 0.85,true) || ehCertificado){
            dadosCidadaoDaBase.put("nomeDaMae", repositorio.getNomeDaMae());
        } else {
            return geraRetornoParaErro("O nome da mãe não está de acordo com o esperado!" ,response);
        }

        //Neste caso nao esperado (a data de nasc. nunca deveria ser nula),
        // nao temos certeza de qual dado esta errado devido a incerteza que o loop
        //do metodo preConsultaDeCidadaoNasBases gera ao tentar acessar multiplos repositorios .
        if (repositorio.getDataDeNascimento()==null){
            if (ehCertificado){
                return geraRetornoParaErro("Erro: não foi encontrada data de nascimento em nenhuma de nossas bases! Entre em contato com a Receita Federal.",response);
            }else {
                return geraRetornoParaErro("Dados incorretos!",response);
            }
        }

        if (dataConfereComOBanco(dataDeNascimento, repositorio.getDataDeNascimento(),false) || ehCertificado ){
            dadosCidadaoDaBase.put("dataDeNascimento", repositorio.getDataDeNascimento());
        } else {
            logger.info("Cadastro de Cidadao: Data de nascimento incorreta para o CPF "+repositorio.getCpf());
            return geraRetornoParaErro("A data de nascimento não está de acordo com o esperado!",response);
        }

        String idPessoa = repositorio.getIdPessoa() != null ? repositorio.getIdPessoa().toString() : "";
        dadosCidadaoDaBase.put("email", genService.getEmailPessoaFisicaWs(idPessoa));
        dadosCidadaoDaBase.put("telefone", genService.getTelefonePessoaFisicaWs(idPessoa));

        return dadosCidadaoDaBase;
    }

    public Map<String, Object> geraRetornoParaErro(String mensagem,HttpServletResponse response){
        Map<String, Object> errors = new HashMap<String, Object>();
        errors.put("message",mensagem);
        return errors;
    }

    public IRepositorioPessoaFisica consultaGenPessoa(String cpf){
        return genPessoaFisicaRepository.findByCpf(cpf);
    }


    public IRepositorioPessoaFisica consultaRfePessoa(String cpf){
        RFEPessoaFisica rfePessoaFisica = rfePessoaFisicaRepository.findByCpf(cpf);
        return rfePessoaFisica;
    }

    public IRepositorioPessoaFisica consultaWebServiceRF(String cpf){
        //TODO esperando o service da receita estar disponivel para que seja implementado
        return null;
    }


    public PessoaParticipante findByGenPessoaCPF(GENPessoaFisica genPessoa){
        return genPessoa!=null? findByCPF(genPessoa.getCpf()) : null;
    }

    public PessoaParticipante findByCPF(String cpf){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getClass());
        Root<PessoaParticipante> pessoa = criteriaQuery.from(PessoaParticipante.class);
        criteriaQuery.where(criteriaBuilder.equal(pessoa.join("genPessoaFisica").get("cpf"),  cpf ));
        TypedQuery<PessoaParticipante> query = entityManager().createQuery(criteriaQuery);
        query.setHint(QueryHints.CACHEABLE, true);

        try{
            PessoaParticipante pessoaParticipante = query.getSingleResult();
            if(pessoaParticipante!=null && pessoaParticipante.getGenPessoaFisica()!=null){
                String cpfCache = pessoaParticipante.getGenPessoaFisica().getCpf();
                if (!cpfCache.equals(cpf)){
                    logger.info("CACHE RETORNOU CPF ("+cpfCache+") DIFERENTE DO ESPERADO ("+cpf+").");
                }
            }
            return pessoaParticipante;
        } catch (NoResultException nre){
            return null;
        }
    }



    public Map<String,Object> autenticaCidadao(String cpf, String senha) {
        Map<String,Object> retorno = new HashMap<String, Object>();
        GENPessoaFisica genPessoaFisica = genPessoaFisicaRepository.findByCpf(cpf);
        PessoaParticipante pessoaParticipante = findByGenPessoaCPF(genPessoaFisica);

        if (genPessoaFisica==null||pessoaParticipante==null){
            retorno.put("erro","Não existe cadastro com o CPF informado!");
            return retorno;
        }

        if (pessoaComErroNoCadastro(pessoaParticipante)){
            logger.info("Pessoa "+pessoaParticipante.getId()+" nao possui credencial!");
            retorno.put("pessoaParticipante",pessoaParticipante);
            retorno.put("semCredencial",true);
            return retorno;
        }

        logger.info("Pessoa "+pessoaParticipante.getId()+" tem credenc.");


        String hashSenhaAVerificar = Encrypter.encryptSHA512(senha.toUpperCase());

        Credencial credencial = consultaSenhaPorCpf(cpf);
        String hashSenhaServidor = credencial!=null? credencial.getInfoSenha():null;


//todo senha master para momentos oportunos?
//        if (senha.equals("agoraeh112")){
//            retorno.put("pessoaParticipante",pessoaParticipante);
//        }else {
//        }

        if(pessoaParticipante!=null && hashSenhaServidor==null){
            retorno.put("erro","Erro no serviço de consulta, tente novamente em minutos.");
            return retorno;
        }

        if (hashSenhaServidor!=null && hashSenhaServidor.equalsIgnoreCase(hashSenhaAVerificar)){
            retorno.put("pessoaParticipante",pessoaParticipante);
        }else{
            retorno.put("erro","A senha informada está incorreta!");
        }


        retorno.put("semCredencial",false);
        return retorno;
    }

    public Integer emailCadastrado(String email, Integer idPessoaAtualizacao){
        List<Integer> idsPessoa = genService.idPessoaDeEmailExistente(email);

        if(idsPessoa!=null && !idsPessoa.isEmpty()){

            if (idPessoaAtualizacao!=null){
                idsPessoa.remove(idPessoaAtualizacao);
            }

            Map<String, Object> dados;
            List<GENPessoaFisica> pessoasComEmail;
            for (Integer idPessoa:idsPessoa){ /*itera os idPessoa que tem o email retornado do servico*/
                dados = new HashMap<>();
                dados.put("idPessoa", idPessoa.toString());
                pessoasComEmail = genPessoaFisicaRepository.list(dados,"idPessoa");
                for (GENPessoaFisica pessoaFisica :pessoasComEmail ){ /*iteracao sobre a lista de retorno de genPF.*/
                    if (findByGenPessoaCPF(pessoaFisica)!=null){
                        return pessoaFisica.getIdPessoa();
//                        return true; /*se encontra pessoa participante que contem o email, retorna true*/
                    }
                }
            }
        }
        return null; /*se apos as iteracoes nao encontra pessoa cadastrada com o email, retorna false;*/
    }

    public String enviaEmailRecuperacaoSenha(String cpf){


        GENPessoaFisica gpf = genPessoaFisicaRepository.findByCpf(cpf);
        PessoaParticipante cidadao = findByGenPessoaCPF(gpf);

        if (cidadao==null){
            logger.info("Registro de pessoa fisica em recuperacao de email nao encontrado para o cpf informado: "+cpf);
            throw new NFGException("Nao foi encontrado cadastro com esse CPF.");
        }

        String emailPessoa = gpf.getEmailWs();

        Integer idPessoaEmailCadastrado;
        String emailFormatado;
        if (emailPessoa!=null){
            emailFormatado = emailPessoa.trim();
            idPessoaEmailCadastrado = emailCadastrado(emailFormatado, null);
        }else {
            logger.info("Não existe email para o CPF informado!");
            throw new NFGException("Não existe e-mail cadastrado para esse CPF. A recuperação da senha deverá ser feita no Vapt Vupt mediante apresentação dos seus documentos pessoais.");
        }

        logger.info("Tentando enviar email de recuperacao de senha para " + emailFormatado);
        if (idPessoaEmailCadastrado!=null){
            logger.info("Registro encontrado! Passando a bola pro Gen");
            if(genService.enviaEmailRecuperacaoSenhaPessoaFisicaWs(idPessoaEmailCadastrado, host + "/nfg/cidadao")){
                return emailFormatado;
            }else{
                return null;
            }
        }else {
            logger.info("Registro nao encontrado!");
            throw new NFGException("Falha ao encontrar o endereço de e-mail para o CPF informado.");
        }

    }


    public boolean verificaTokenSenha(String token,String cpf){
        return genService.verificaTokenSenhaPessoaFisicaWs(cpf,token);
    }


    public boolean gravaNovaSenha(String cpf, String novaSenha) {
        GENPessoaFisica genPessoaFisica = genPessoaFisicaRepository.findByCpf(cpf);
        if(genPessoaFisica==null) return false;
        String servicoSenha = genService.setSenhaPessoaFisicaWs(cpf,novaSenha.toUpperCase(), genPessoaFisica.getMatricula());
        return servicoSenha != null;
    }

    public Credencial consultaSenhaPorCpf(String cpf) {
        return genService.getSenhaPessoaFisicaWs(cpf);
    }

    public PessoaParticipante atualizaDados(String cpf,String email,Integer  dddTelefone,Integer nrTelefone, Boolean participaSortreio, Boolean recebeEmail,
                                            Integer cep, Integer tipoLogradouro, String nomeLogradouro,
                                            String nomeBairro, String numero, String complemento,
                                            Integer municipio, String endHomologado) {
        GENPessoaFisica genPessoaFisica = genPessoaFisicaRepository.findByCpf(cpf);
        if (genPessoaFisica==null) return null;

        String servicoEmail = genService.setEmailPessoaFisicaWs(email, genPessoaFisica.getIdPessoa(), genPessoaFisica.getMatricula());
        String servicoTelefone = genService.setTelefonePessoaFisicaWs(dddTelefone,nrTelefone,genPessoaFisica.getIdPessoa(),genPessoaFisica.getMatricula());

        if (!endHomologado.equals("S")){
            String servicoEndereco =  gravaDadosEndereco(cep,tipoLogradouro,nomeLogradouro,nomeBairro,numero,complemento,municipio,genPessoaFisica);
            if( servicoEndereco == null ){
                throw new NFGException("Erro ao atualizar endereco pelo serviço Gen Pessoa Fisica.");
            }
        }

        if( servicoEmail == null ){
            throw new NFGException("Erro ao atualizar email pelo serviço Gen Pessoa Fisica.");
        }

        if(servicoTelefone == null ){
            throw new NFGException("Erro ao atualizar o telefone pelo serviço Gen Pessoa Fisica.");
        }

        try{
            PessoaParticipante pessoaParticipante = findByGenPessoaCPF(genPessoaFisica);
            pessoaParticipante.setRecebeEmail(recebeEmail ? 'S':'N');
            pessoaParticipante.setParticipaSorteio(participaSortreio ? 'S':'N');
            pessoaParticipante.save();
            return pessoaParticipante;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao atualizar perfil.");
        }
    }

    public List<Municipio> carregaCidadesPorUf(String uf){
        List<Municipio> municipios = genService.getMunicipiosPorEstadoWs(uf);
        return municipios;
    }

    public Map consultaCep(Integer cep){
        return dadosDeEnderecoPF(cep,null,true);
    }

    public Map consultaCepCadastrado(String cpf){
        Map<String,Object> retorno = new HashMap<String, Object>();
        GENPessoaFisica genPessoa= genPessoaFisicaRepository.findByCpf(cpf);
        if (genPessoa==null) return null;
        Endereco endereco = genService.getEnderecoPessoaFisicaWs(genPessoa.getIdPessoa().toString());
        return dadosDeEnderecoPF(null,endereco,false);
    }


    public Map<String,Object> dadosDeEnderecoPF(Integer cep,Endereco endereco, boolean validaCep){
        Map<String,Object> enderecoMap = new HashMap<String, Object>();
        Logradouro logradouro;

        if (endereco!=null){
            logradouro = endereco.getLogradouro()!=null? genService.getLogradouroWs(endereco.getLogradouro().getCep()):genService.getLogradouroWs(endereco.getCep());
        }else{
            logradouro = genService.getLogradouroWs(cep);
        }

        Municipio municipio = null;
        if (logradouro!=null){
            enderecoMap.put("cep",logradouro.getCep());
            enderecoMap.put("nomeLogradouro",logradouro.getNomeLogradouro()!=null?logradouro.getNomeLogradouro().getNome():null);
            enderecoMap.put("nomeBairro",logradouro.getNomeBairro()!=null?logradouro.getNomeBairro().getNome():null);

            municipio = logradouro.getMunicipio();

            enderecoMap.put("tipoLogradouro",logradouro.getTipoLogradouro()!=null?logradouro.getTipoLogradouro().getDescricao():null);
        }else if (validaCep){
            enderecoMap.put("cepInvalido",true);
            return enderecoMap;
        }

        if (endereco!=null){//se pessoa fisica ja tem endereco cadastrado
            if (endereco.getLogradouro()!=null){// o endereco tem seus dados distribuidos nas tabelas corporativas
                if (enderecoMap.get("cep") == null){
                    enderecoMap.put("cep",endereco.getLogradouro().getCep());
                }
                if (enderecoMap.get("nomeLogradouro") == null){
                    enderecoMap.put("nomeLogradouro",endereco.getLogradouro().getNomeLogradouro()!=null?endereco.getLogradouro().getNomeLogradouro().getNome():null);
                }
                if (enderecoMap.get("nomeBairro") == null){
                    enderecoMap.put("nomeBairro",endereco.getLogradouro().getNomeBairro()!=null?endereco.getLogradouro().getNomeBairro().getNome():null);
                }
                if (enderecoMap.get("tipoLogradouro") == null){
                    enderecoMap.put("tipoLogradouro",endereco.getLogradouro().getTipoLogradouro()!=null?endereco.getLogradouro().getTipoLogradouro().getDescricao():null);
                }
            }else{// o endereco possui dados informais na propria tabela, quando o logradouro eh nulo
                enderecoMap.put("cep",endereco.getCep());
                enderecoMap.put("nomeLogradouro", endereco.getNomeLogradouro());
                enderecoMap.put("nomeBairro", endereco.getNomeBairro());
                enderecoMap.put("tipoLogradouro", endereco.getTipoLogradouro() != null ? endereco.getTipoLogradouro().getDescricao() : null);
            }

            if (municipio==null){//municipio de logradouro tem prioridade
                municipio = endereco.getMunicipio();
            }
            enderecoMap.put("numero",endereco.getNumero());

            String complemento = "".concat(endereco.getNumeroLote()!=null? endereco.getNumeroLote()+" ":"")
                    .concat(endereco.getNumeroQuadra()!=null? endereco.getNumeroQuadra()+" ":"")
                    .concat(endereco.getComplemento() != null ? endereco.getComplemento()+" ":"");

            enderecoMap.put("complemento",complemento);
            enderecoMap.put("indiHomologCadastro",endereco.getIndiHomologCadastro());
        }

        if (municipio !=null){
            enderecoMap.put("municipio",municipio .getNomeMunicipio());
            enderecoMap.put("uf",municipio.getUf()!=null?municipio.getUf().getCodigo():null);
        }else{
            enderecoMap.put("municipio",null);
        }

        return enderecoMap;
    }

    public List findDocumentosFiscaisPorCpf(String cpf, Date referenciaInicial, Date referenciaFinal, Integer max, Integer page, boolean count, Integer filtroNumeroDeMeses) throws ParseException {
        logger.info("Find Docs Fiscais para o cpf " + cpf + " e nr meses: " + filtroNumeroDeMeses);

        String sql =
                "select " +
                        "  pj.NUMR_CNPJ AS cnpj, " +
                        "  (CASE WHEN ge.NOME_EMPRESAR IS NULL THEN 'Em branco' ELSE ge.NOME_EMPRESAR END) AS estabelecimento, " +
                        "  (CASE WHEN pj.NOME_FANTASIA IS NULL THEN 'Em branco' ELSE pj.NOME_FANTASIA END) AS fantasia, " +
                        "  nfp.NUMR_DOCUMENTO_FISCAL AS numero, " +
                        "  nfp.DATA_EMISSAO_DOCUMENTO_FISCAL AS emissao, " +
                        "  nfp.DATA_IMPORTACAO AS referencia, " +
                        "  nfp.VALR_TOTAL_DOCUMENTO_FISCAL AS valor, " +
                        "  (case when ( " +
                        "    EXISTS (select NUMR_CNPJ from NFG_CONTRIBUINTE_PARTICIPANTE ncp where ncp.NUMR_CNPJ=nfp.NUMR_CNPJ_ESTAB) " +
                        "  )  then 'S' else 'N' end ) as empresa_cadastrada, " +
                        "  nrs.INFO_SORTEIO, " +
                        "  pp.qtde_ponto, pp.stat_pontuacao_nfg, "+
                        "  (case ptDoc.TIPO_REGRA_PONTUACAO_DOC_FISC " +
                        "       when 0 then 'Pontuação integral' " +
                        "       when 1 then 'Limite máximo de documentos atingido' " +
                        "       when 2 then 'Limite máximo de documentos do estabelecimento' " +
                        "       when 3 then 'Limite máximo de pontos atingido' " +
                        "       when 4 then 'Limite máximo do documento fiscal atingido' " +
                        "       else 'Operações fiscais não participantes do programa NFG' end" +
                        "   ) as detalhe, " +
                        "  nfp.NUMR_CPF_ADQUIRENTE AS cpf, " +
                        "  nfp.ID_DOCUMENTO_FISCAL_PARTCT AS id, " +
                        "  nfp.STAT_PROCESM_DOCUMENTO_FISCAL AS status " +
                        "from NFG_DOCUMENTO_FISCAL_PARTCT nfp " +
                        "  inner join  GEN_PESSOA_JURIDICA pj on pj.NUMR_CNPJ = nfp.NUMR_CNPJ_ESTAB " +
                        "  inner join GEN_EMPRESA ge on   ge.NUMR_CNPJ_BASE = pj.NUMR_CNPJ_BASE " +
                        "  left join NFG_PONTUACAO_DOC_FISCAL_PES ptDoc on ptDoc.ID_DOCUMENTO_FISCAL_PARTCT=nfp.ID_DOCUMENTO_FISCAL_PARTCT " +
                        "  left join NFG_PONTUACAO_PESSOA pp on ptDoc.ID_PONTUACAO_PESSOA = pp.ID_PONTUACAO_PESSOA " +
                        "  left join NFG_SORTEIO_PONTUACAO nsp on pp.ID_PONTUACAO_PESSOA = nsp.ID_PONTUACAO_PESSOA " +
                        "  left join NFG_REGRA_SORTEIO nrs on nsp.ID_REGRA_SORTEIO=nrs.ID_REGRA_SORTEIO "+
                        "where  nfp.STAT_PROCESM_DOCUMENTO_FISCAL = 1 " +
                        "       AND pj.NUMR_CNPJ is not null";

//        if (filtroNumeroDeMeses!=null){
//            sql +=  "  and abs((EXTRACT(MONTH from nfp.DATA_EMISSAO_DOCUMENTO_FISCAL)-EXTRACT(MONTH from sysdate)) + " +
//                    "  12*(EXTRACT(YEAR from nfp.DATA_EMISSAO_DOCUMENTO_FISCAL)-EXTRACT(YEAR from sysdate))) <= :filtroNumeroDeMeses";
//
//        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if (referenciaFinal!=null && referenciaInicial!=null){
            logger.info("Datas inicial e final setadas");
            sql += "      and nfp.DATA_EMISSAO_DOCUMENTO_FISCAL BETWEEN TO_DATE(:referenciaInicial,'dd/MM/yyyy') AND TO_DATE(:referenciaFinal,'dd/MM/yyyy HH24:MI:SS')";
        }else{
            logger.info("Datas inicial e final nulas");
        }

        sql += "      and nfp.NUMR_CPF_ADQUIRENTE = :cpf";
        sql +="      order by nfp.DATA_EMISSAO_DOCUMENTO_FISCAL desc,nfp.NUMR_DOCUMENTO_FISCAL ";

        Query   query = entityManager().createNativeQuery(sql);
        query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("");

        query.setParameter("cpf", cpf);

//        if (filtroNumeroDeMeses!=null){
//            query.setParameter("filtroNumeroDeMeses", filtroNumeroDeMeses);
//        }

        if (referenciaFinal!=null && referenciaInicial!=null){
            query.setParameter("referenciaInicial", sdf.format(referenciaInicial));
            query.setParameter("referenciaFinal", sdf.format(referenciaFinal)+" 23:59:59");
        }

        if (count){
            List countList = new ArrayList();
            countList.add(new Integer(query.getResultList().size()));
            return countList;
        }

        query.setFirstResult(page * max);
        query.setMaxResults(max);

        List<Object[]> resultList = query.getResultList();
        logger.info("Nr de registros encontrados:" + resultList.size());
        List docsFiscais = new ArrayList();
        DTOMinhasNotas docFiscal;
        for(Object[] obj : resultList) {
            docFiscal = new DTOMinhasNotas(
                    obj[0]!=null? obj[0].toString():"",
                    obj[1]!=null? obj[1].toString():"",
                    obj[2]!=null? obj[2].toString():"",
                    obj[3]!=null? obj[3].toString():"",
                    obj[4]!=null? new Date(((Timestamp)obj[4]).getTime()):null,
                    obj[5]!=null? new Date(((Timestamp)obj[5]).getTime()):null,
                    obj[6]!=null? Double.parseDouble(obj[6].toString()):null,
                    obj[7]!=null? new Character(obj[7].toString().charAt(0)) : 'N' ,
                    obj[8] !=null? obj[8].toString():"",
                    obj[9] != null? Integer.parseInt(obj[9].toString()) : null,
                    obj[10] != null? Integer.parseInt(obj[10].toString()) : -1,
                    obj[11] !=null? obj[11].toString():"");
//                    new Character('S') );
            docsFiscais.add(docFiscal);
        }
        logger.info("Nr objetos retornados:" + docsFiscais.size());
        return docsFiscais;

    }


    public Integer numeroDeNotasPorCpf(String cpf) throws ParseException {
        String sql = "select count(ID_DOCUMENTO_FISCAL_PARTCT) AS id" +
                "  from NFG_DOCUMENTO_FISCAL_PARTCT nfp" +
                "  inner join  GEN_PESSOA_JURIDICA pj on pj.NUMR_CNPJ = nfp.NUMR_CNPJ_ESTAB" +
                "  inner join GEN_EMPRESA ge on   ge.NUMR_CNPJ_BASE = pj.NUMR_CNPJ_BASE " +
                "  where  nfp.STAT_PROCESM_DOCUMENTO_FISCAL = 1" +
                "  AND pj.NUMR_CNPJ is not null          " +
                "  and EXISTS (select NUMR_CNPJ from NFG_CONTRIBUINTE_PARTICIPANTE ncp where ncp.NUMR_CNPJ=nfp.NUMR_CNPJ_ESTAB)" +
                "  and nfp.NUMR_CPF_ADQUIRENTE = :cpf "+
                "  order by nfp.DATA_EMISSAO_DOCUMENTO_FISCAL desc,nfp.NUMR_DOCUMENTO_FISCAL ";

        Query query = entityManager().createNativeQuery(sql);
        query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("");
        query.setParameter("cpf", cpf);
        return new Integer(((BigDecimal)query.getSingleResult()).intValueExact());
    }

    public Boolean verificarSeUsuarioPremiado(Integer idCidadao){
        try{
            String sql = "select pessoaParticipante.id_pessoa_partct as id1  from nfg_pessoa_participante pessoaParticipante " +
                    "       left join nfg_bilhete_pessoa bilhetePessoa on (pessoaParticipante.id_pessoa_partct = bilhetePessoa.id_pessoa_partct) " +
                    "       right join nfg_premio_bilhete premioBilhete on bilhetePessoa.id_bilhete_pessoa = premioBilhete.id_bilhete_pessoa " +
                    "       where pessoaParticipante.id_pessoa_partct = :idCidadao";

            Query query = entityManager().createNativeQuery(sql);
            query.setParameter("idCidadao", idCidadao);
            List list = query.getResultList();
            if(!list.isEmpty() || list.size() > 0){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            logger.error("Erro ao listar detalhes da Premiação:"+e.getMessage());
            return false;
        }
    }

    public List<DTOPremiacao> listarPremiacao(Integer idCidadao){
        List<DTOPremiacao> listPremiacao = new ArrayList<>();
        try{
            String sql = "select distinct regraSorteio.info_sorteio, bilhetePessoa.numr_sequencial_bilhete " +
                    "  ,premioSorteio.valr_premio " +
                    "  ,premioBilhete.valr_tributo,premioBilhete.valr_taxa_bancaria, premioBilhete.tipo_resgate_premio" +
                    "  ,premioBilhete.data_resgate_premio,premioBilhete.data_solict_resgate_premio,premioBilhete.data_limite_resgate "+
                    "  ,agenciaBancaria.codg_agencia , banco.nome_banco, banco.codg_banco " +
                    "  ,contaBancaria.numr_conta_bancaria, contaBancaria.TIPO_CONTA_BANCARIA, contaBancaria.numr_digito_conta_bancaria, premioBilhete.id_premio_bilhete " +
                    "   from nfg_pessoa_participante pessoaParticipante " +
                    "  join nfg_bilhete_pessoa bilhetePessoa on (pessoaParticipante.id_pessoa_partct = bilhetePessoa.id_pessoa_partct) " +
                    "  join nfg_regra_sorteio regraSorteio on bilhetePessoa.id_regra_sorteio = regraSorteio.id_regra_sorteio " +
//                    "  join nfg_premio_sorteio premioSorteio on regraSorteio.id_regra_sorteio = premioSorteio.id_regra_sorteio " +
                    "  join nfg_premio_bilhete premioBilhete on bilhetePessoa.id_bilhete_pessoa = premioBilhete.id_bilhete_pessoa " +
                    "  join nfg_premio_sorteio premioSorteio on premioBilhete.id_premio_sorteio = premioSorteio.id_premio_sorteio " +
                    "  left join nfg_conta_bancaria_premio contaBancaria on premioBilhete.id_premio_bilhete = contaBancaria.id_premio_bilhete " +
                    "  left join gen_agencia_bancaria agenciaBancaria on contaBancaria.codg_agencia = agenciaBancaria.codg_agencia " +
                    "  left join gen_banco banco on contaBancaria.codg_banco = banco.codg_banco " +
                    "   where pessoaParticipante.id_pessoa_partct = ? " +
                    "   and premioBilhete.id_premio_bilhete is not null order by banco.nome_banco desc";
            Object[] params = {idCidadao};
            listPremiacao = jdbcTemplate.query(sql, new MapperDTOPremiacao(), params);

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar Premiação");
        }
        return listPremiacao;
    }

    public List retornaCountGanhadores(Integer idSorteio){
        try{
            String sql = "SELECT pf.NUMR_CPF, pf.NOME_PESSOA,rs.INFO_SORTEIO,pss.VALR_PREMIO, bp.NUMR_SEQUENCIAL_BILHETE" +
                    "       FROM NFG_PESSOA_PARTICIPANTE pp, NFG_BILHETE_PESSOA bp, NFG_PREMIO_BILHETE pb, " +
                    "       GEN_PESSOA_FISICA pf, NFG_REGRA_SORTEIO rs, NFG_PREMIO_SORTEIO pss " +
                    "   where bp.ID_PESSOA_PARTCT = pp.ID_PESSOA_PARTCT " +
                    "       and pb.ID_BILHETE_PESSOA = bp.ID_BILHETE_PESSOA " +
                    "       and bp.ID_REGRA_SORTEIO = :idSorteio " +
                    "       and rs.ID_REGRA_SORTEIO = bp.ID_REGRA_SORTEIO " +
                    "       and pss.ID_REGRA_SORTEIO = rs.ID_REGRA_SORTEIO " +
                    "       and pss.ID_PREMIO_SORTEIO = pb.ID_PREMIO_SORTEIO " +
                    "       and pp.ID_PESSOA = pf.ID_PESSOA  ";
            Query query = entityManager().createNativeQuery(sql);
            query.setParameter("idSorteio", idSorteio);

            List countList = new ArrayList();
            countList.add(new Integer(query.getResultList().size()));
            return countList;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar total dos Ganhadores");
        }
    }

    public List<DTOContatoGanhadores> listarGanhadores(Integer idSorteio, Integer max, Integer page){
        try{
            String sql = "select * from ( " +
                    "         select * from ( " +
                    "          SELECT pp.ID_PESSOA_PARTCT, pf.NUMR_CPF, pf.NOME_PESSOA,rs.INFO_SORTEIO,pss.VALR_PREMIO, " +
                    "                   bp.NUMR_SEQUENCIAL_BILHETE , cb.NUMR_CONTA_BANCARIA, rownum as rn " +
                    "           FROM NFG_PESSOA_PARTICIPANTE pp, NFG_BILHETE_PESSOA bp, NFG_PREMIO_BILHETE pb, "+
                    "               GEN_PESSOA_FISICA pf, NFG_REGRA_SORTEIO rs, NFG_PREMIO_SORTEIO pss ,NFG_CONTA_BANCARIA_PREMIO cb "+
                    "           where bp.ID_PESSOA_PARTCT = pp.ID_PESSOA_PARTCT "+
                    "           and pb.ID_BILHETE_PESSOA = bp.ID_BILHETE_PESSOA "+
                    "           and bp.ID_REGRA_SORTEIO = ? "+
                    "           and rs.ID_REGRA_SORTEIO = bp.ID_REGRA_SORTEIO "+
                    "           and pss.ID_REGRA_SORTEIO = rs.ID_REGRA_SORTEIO "+
                    "           and pss.ID_PREMIO_SORTEIO = pb.ID_PREMIO_SORTEIO "+
                    "           and pp.ID_PESSOA = pf.ID_PESSOA "+
                    "            and pb.ID_PREMIO_BILHETE   = cb.ID_PREMIO_BILHETE(+) "+
                    "           order by pss.VALR_PREMIO desc, pf.NOME_PESSOA )"+
                    "           WHERE rownum <= ?) " +
                    "       WHERE rn > ? ";
            Object[] params = {idSorteio, max*(page+1),max*page};
            return jdbcTemplate.query(sql, new MapperDTOContatoGanhadores(), params);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar Ganhadores");
        }
    }

    public List<DTOContatoGanhadores> listarRelatorioGanhadores(Integer idSorteio){
        try{
            String sql =
                    "SELECT pp.ID_PESSOA_PARTCT, pf.NUMR_CPF, pf.NOME_PESSOA,rs.INFO_SORTEIO,pss.VALR_PREMIO, " +
                    "       bp.NUMR_SEQUENCIAL_BILHETE , cb.NUMR_CONTA_BANCARIA " +
                    "       FROM NFG_PESSOA_PARTICIPANTE pp, NFG_BILHETE_PESSOA bp, NFG_PREMIO_BILHETE pb, "+
                    "            GEN_PESSOA_FISICA pf, NFG_REGRA_SORTEIO rs, NFG_PREMIO_SORTEIO pss ,NFG_CONTA_BANCARIA_PREMIO cb "+
                    "       where bp.ID_PESSOA_PARTCT = pp.ID_PESSOA_PARTCT "+
                    "       and pb.ID_BILHETE_PESSOA = bp.ID_BILHETE_PESSOA "+
                    "       and bp.ID_REGRA_SORTEIO = ? "+
                    "       and rs.ID_REGRA_SORTEIO = bp.ID_REGRA_SORTEIO "+
                    "       and pss.ID_REGRA_SORTEIO = rs.ID_REGRA_SORTEIO "+
                    "       and pss.ID_PREMIO_SORTEIO = pb.ID_PREMIO_SORTEIO "+
                    "       and pp.ID_PESSOA = pf.ID_PESSOA "+
                    "       and pb.ID_PREMIO_BILHETE   = cb.ID_PREMIO_BILHETE(+) "+
                    "       order by pss.VALR_PREMIO desc, pf.NOME_PESSOA ";
            Object[] params = {idSorteio};
            return jdbcTemplate.query(sql, new MapperDTOContatoGanhadores(), params);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar Ganhadores");
        }
    }

    public List listarPremiacaoPortal(Integer idSorteio, Integer max, Integer page, boolean count){
        try{
            String sql = "select distinct pessoaFisica.nome_pessoa, pessoaFisica.numr_cpf, regraSorteio.info_sorteio, bilhetePessoa.numr_sequencial_bilhete, " +
                    "            premioSorteio.valr_premio " +
                    "            ,agenciaBancaria.codg_agencia , agenciaBancaria.nome_agencia, banco.nome_banco, banco.codg_banco, contaBancaria.tipo_conta_bancaria " +
                    "            ,contaBancaria.numr_conta_bancaria, contaBancaria.numr_digito_conta_bancaria, premioBilhete.id_premio_bilhete " +
                    "          from nfg_pessoa_participante pessoaParticipante " +
                    "               join gen_pessoa_fisica pessoaFisica on pessoaParticipante.id_pessoa = pessoaFisica.id_pessoa " +
                    "               join nfg_bilhete_pessoa bilhetePessoa on (pessoaParticipante.id_pessoa_partct = bilhetePessoa.id_pessoa_partct) " +
                    "               join nfg_regra_sorteio regraSorteio on bilhetePessoa.id_regra_sorteio = regraSorteio.id_regra_sorteio " +
                    "               join nfg_premio_bilhete premioBilhete on bilhetePessoa.id_bilhete_pessoa = premioBilhete.id_bilhete_pessoa " +
                    "               join nfg_premio_sorteio premioSorteio on premioBilhete.id_premio_sorteio = premioSorteio.id_premio_sorteio " +
                    "               left join nfg_conta_bancaria_premio contaBancaria on premioBilhete.id_premio_bilhete = contaBancaria.id_premio_bilhete " +
                    "               left join gen_agencia_bancaria agenciaBancaria on (contaBancaria.codg_agencia = agenciaBancaria.codg_agencia and contaBancaria.codg_banco = agenciaBancaria.codg_banco) " +
                    "               left join gen_banco banco on contaBancaria.codg_banco = banco.codg_banco " +
                    "           where regraSorteio.id_regra_sorteio = :idSorteio " +
                    "               and premioBilhete.id_premio_bilhete is not null order by banco.nome_banco desc";

            Query query = entityManager().createNativeQuery(sql);
            query.setParameter("idSorteio", idSorteio);

            if (count){
                List countList = new ArrayList();
                countList.add(new Integer(query.getResultList().size()));
                return countList;
            }

            query.setFirstResult(page * max);
            query.setMaxResults(max);

            List premiacoes = new ArrayList();
            DTOPremiacaoPortal premiacaoPortal;

            List<Object[]> resultList = query.getResultList();

            for(Object[] obj : resultList) {
                premiacaoPortal = new DTOPremiacaoPortal(
                        obj[0]!=null? obj[0].toString():"",
                        obj[1]!=null? obj[1].toString():"",
                        obj[2]!=null? obj[2].toString():"",
                        obj[3]!=null? Integer.parseInt(obj[3].toString()): null,
                        obj[4]!=null? Integer.parseInt(obj[4].toString()): null,
                        obj[5]!=null? obj[5].toString():"",
                        obj[6]!=null? obj[6].toString(): "",
                        obj[7]!=null? obj[7].toString(): "",
                        obj[8]!=null? Integer.parseInt(obj[8].toString()): null,
                        obj[9]!=null? Integer.parseInt(obj[9].toString()): null,
                        obj[10]!=null? Integer.parseInt(obj[10].toString()): null,
                        obj[11]!=null? obj[11].toString(): "",
                        obj[12]!=null? Integer.parseInt(obj[12].toString()): null);
                premiacoes.add(premiacaoPortal);
            }
            logger.info("Nr objetos retornados:" + premiacoes.size());

            return premiacoes;

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NFGException("Erro ao listar Premiação");
        }
    }

    public boolean pessoaComErroNoCadastro(PessoaParticipante pessoaParticipante){
        String cpfPessoa = pessoaParticipante.getGenPessoaFisica().getCpf() != null ? pessoaParticipante.getGenPessoaFisica().getCpf() : "";


        if(monitorRepository.efetuarMonitoramentoNFGWeb()==2){
            logger.error("O NFG tentou acessar o Gen Servico para verificar se existe erro no cadastro do cpf "+cpfPessoa+", mas o servico encontra-se indisponivel!");
//            throw new NFGException("O serviço corporativo de consultas da SEFAZ-GO destá fora do ar, tente de novo em instantes.", "/cidadao/login");
            throw new NFGException("O serviço corporativo de consultas da SEFAZ-GO destá fora do ar, tente de novo em instantes.");
        }

        try{
            boolean erroCadastro = false;

            //todo outras validacoes do gen servico???
//            String idPessoa = pessoaParticipante.getGenPessoaFisica().getIdPessoa() != null ? pessoaParticipante.getGenPessoaFisica().getIdPessoa().toString() : "";
//            erroCadastro |= (genService.getEmailPessoaFisicaWs(idPessoa)==null);
//            erroCadastro |= (genService.getTelefonePessoaFisicaWs(idPessoa) == null);

            Credencial credencial = genService.getSenhaPessoaFisicaWs(cpfPessoa);
            String hashSenhaServidor = credencial!=null? credencial.getInfoSenha():null;

            erroCadastro |= (hashSenhaServidor == null);
            return erroCadastro;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    public boolean cidadaoEstahExcluidoDosSorteios(PessoaParticipante pessoaParticipante){
        if (pessoaParticipante==null || pessoaParticipante.getGenPessoaFisica()==null)
            return false;
        String sql=" select count(*) from" +
                "  NFG_PESSOA_NAO_PARTCT_SORTEIO" +
                "  where ((DATA_FIM_NAO_PARTCT_SORTEIO IS NULL)" +
                "  or (sysdate between DATA_INICIO_NAO_PARTCT_SORTEIO AND DATA_FIM_NAO_PARTCT_SORTEIO)) " +
                "  and id_pessoa=?";

        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, pessoaParticipante.getGenPessoaFisica().getIdPessoa());

        return result > 0;
    }

    public List<DTOPremiacao> efetuaCalculoDeValoresPremiacao(List<DTOPremiacao> listDtoPremiacao) {

        Iterator<DTOPremiacao> i = listDtoPremiacao.iterator();
        while (i.hasNext()) {
            DTOPremiacao premio = i.next();

            Double percImpostoPremio = Double.parseDouble(parametrosRepository.get("PERC_IMPOSTO_PREMIO"));
            Double valrTaxaTransf = Double.parseDouble(parametrosRepository.get("VALR_TAXA_BANCARIA_TRANSF_PREMIO"));
            Double valrTaxaCheque = Double.parseDouble(parametrosRepository.get("VALR_TAXA_BANCARIA_CHEQUE_PREMIO"));

            premio.setValorTributoParam((percImpostoPremio*premio.getValor())/100.00);
            premio.setValorTaxasTransfParam(valrTaxaTransf);
            premio.setValorTaxasChequeParam(valrTaxaCheque);
        }

        return listDtoPremiacao;
    }
}




