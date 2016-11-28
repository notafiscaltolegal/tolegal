package gov.goias.persistencia;

import gov.goias.entidades.*;
import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;
import gov.goias.entidades.enums.TipoMotivoReclamacao;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.exceptions.NFGException;
import gov.goias.services.NotificacaoService;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Query;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.*;

/**
 * Created by bruno-cff on 30/09/2015.
 */
public class DocumentoFiscalReclamadoRepository extends GenericRepository<Integer, DocumentoFiscalDigitado>{

    private static final Logger logger = Logger.getLogger(DocumentoFiscalReclamadoRepository.class);

    @Autowired
    NotificacaoService notificacaoService;

    @Autowired
    CCEContribuinteRepository cceContribuinteRepository;

    public Map listDocumentoFiscalReclamado(String numeroCnpj, Integer max, Integer page) {
        String sql= "select documento.ID_DOCUMENTO_FISCAL_RECLAMADO,  " +
                "           documento.DATA_RECLAMACAO, " +
                "           documento.NUMR_DOCUMENTO_FISCAL, " +
                "           documento.VALR_DOCUMENTO_FISCAL, " +

                "           documento.DATA_DOCUMENTO_FISCAL, " +
                "           documento.TIPO_EXTENSAO_ARQUIVO_DOC_FISC, " +
                "           genPessoaFisica.NOME_PESSOA, " +

                "           motivo.DESC_MOTIVO_RECLAMACAO " +

                "     from NFG_DOCUMENTO_FISCAL_RECLAMADO documento " +
                "       join NFG_PESSOA_PARTICIPANTE pessoaParticipante " +
                "           on documento.ID_PESSOA_PARTCT_RECLAMANTE = pessoaParticipante.ID_PESSOA_PARTCT " +
                "       join GEN_PESSOA_FISICA genPessoaFisica " +
                "           on pessoaParticipante.ID_PESSOA = genPessoaFisica.ID_PESSOA " +
                "       join NFG_MOTIVO_RECLAMACAO_DOC_FISC motivo " +
                "           on documento.CODG_MOTIVO_RECLAMACAO = motivo.CODG_MOTIVO_RECLAMACAO " +
                "     where documento.NUMR_CNPJ_RECLAMADO = ? ";

        String ordenacao="DATA_RECLAMACAO DESC";
        return paginateMappableClassObjects(max, page, sql, ordenacao, DocumentoFiscalReclamado.class, numeroCnpj);
    }

    public DocumentoFiscalReclamado retornaDocumentoFiscalPorId(Integer idDocumento){
        DocumentoFiscalReclamado documentoFiscalReclamado;
        try{
            String sql = "select documento from DocumentoFiscalReclamado documento where documento.id = :idDocumento";
            Query query = this.entityManager().createQuery(sql);
            query.setParameter("idDocumento", idDocumento);
            documentoFiscalReclamado = (DocumentoFiscalReclamado) query.getSingleResult();
            return documentoFiscalReclamado;
        }catch (Exception e){
            return null;
        }
    }

    public Integer countDocumentoFiscalReclamado(String numeroCnpj){
        String sql = "select count(*) from NFG_DOCUMENTO_FISCAL_RECLAMADO documento " +
                "                  join NFG_PESSOA_PARTICIPANTE pessoaParticipante " +
                "                      on documento.ID_PESSOA_PARTCT_RECLAMANTE = pessoaParticipante.ID_PESSOA_PARTCT " +
                "                  join GEN_PESSOA_FISICA genPessoaFisica " +
                "                      on pessoaParticipante.ID_PESSOA = genPessoaFisica.ID_PESSOA " +
                "                  join NFG_MOTIVO_RECLAMACAO_DOC_FISC motivo " +
                "                      on documento.CODG_MOTIVO_RECLAMACAO = motivo.CODG_MOTIVO_RECLAMACAO " +
                "                  join NFG_SITUACAO_DOC_FISC_RECLAMA situacao " +
                "                      on documento.ID_DOCUMENTO_FISCAL_RECLAMADO = situacao.ID_DOCUMENTO_FISCAL_RECLAMADO " +
                "                  join NFG_COMPL_SITUACAO_RECLAMACAO comp " +
                "                      on situacao.CODG_COMPL_SITUACAO_RECLAMACAO = comp.CODG_COMPL_SITUACAO_RECLAMACAO " +
                "                where documento.NUMR_CNPJ_RECLAMADO = ? ";
        try{
            return jdbcTemplate.queryForObject(sql, Integer.class, numeroCnpj);
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public Map findReclamacoesDoCidadao(PessoaParticipante cidadao, Integer page, Integer max) {
        String sql="select ID_DOCUMENTO_FISCAL_RECLAMADO,NUMR_DOCUMENTO_FISCAL,VALR_DOCUMENTO_FISCAL, DATA_RECLAMACAO ,NUMR_CNPJ_RECLAMADO, PJ.NOME_FANTASIA, EMP.NOME_EMPRESAR " +
                " from nfg_documento_fiscal_reclamado" +
                " join GEN_PESSOA_JURIDICA PJ ON PJ.NUMR_CNPJ = NUMR_CNPJ_RECLAMADO" +
                " join GEN_EMPRESA EMP ON PJ.NUMR_CNPJ_BASE = EMP.NUMR_CNPJ_BASE " +
                " WHERE ID_PESSOA_PARTCT_RECLAMANTE=? ";
        String ordenacao="DATA_RECLAMACAO DESC";
        return paginateMappableClassObjects(max,page,sql,ordenacao,DocumentoFiscalReclamado.class,cidadao.getId());
    }

//    public Map findReclamacoesUsuarioGestor(Integer page, Integer max) {
//        String sql= "  SELECT   documento.ID_DOCUMENTO_FISCAL_RECLAMADO, documento.NUMR_DOCUMENTO_FISCAL, " +
//                "        documento.VALR_DOCUMENTO_FISCAL, documento.DATA_RECLAMACAO," +
//                "        documento.NUMR_CNPJ_RECLAMADO, pj.NOME_FANTASIA, emp.NOME_EMPRESAR  " +
//                "       FROM NFG_DOCUMENTO_FISCAL_RECLAMADO documento" +
//                "       JOIN GEN_PESSOA_JURIDICA pj ON pj.NUMR_CNPJ = documento.NUMR_CNPJ_RECLAMADO" +
//                "       JOIN GEN_EMPRESA emp ON pj.NUMR_CNPJ_BASE = emp.NUMR_CNPJ_BASE" +
//                "       WHERE" +
//                "       Documento.Id_Documento_Fiscal_Reclamado IN" +
//                "       (" +
//                "          SELECT Id_Documento_Fiscal_Reclamado FROM NFG_SITUACAO_DOC_FISC_RECLAMA" +
//                "          WHERE CODG_COMPL_SITUACAO_RECLAMACAO IN (6,8,10,14)" +
//                "       )";
//
//        String ordenacao="DATA_RECLAMACAO DESC";
//        return paginateMappableClassObjects(max,page,sql,ordenacao,DocumentoFiscalReclamado.class);
//    }

    public Map findReclamacoesUsuarioGestor(Integer page, Integer max) {
        String sql= "SELECT DFR.ID_DOCUMENTO_FISCAL_RECLAMADO, DFR.NUMR_DOCUMENTO_FISCAL, DFR.VALR_DOCUMENTO_FISCAL, DFR.DATA_RECLAMACAO, " +
                "       DFR.NUMR_CNPJ_RECLAMADO, PJ.NOME_FANTASIA, EMP.NOME_EMPRESAR " +
                "  FROM NFG_DOCUMENTO_FISCAL_RECLAMADO DFR " +
                "  LEFT JOIN    ( " +
                "            SELECT SDR.CODG_COMPL_SITUACAO_RECLAMACAO,SDR.DATA_CADASTRO_SITUACAO, " +
                "              SDR.ID_DOCUMENTO_FISCAL_RECLAMADO, " +
                "              ROW_NUMBER() OVER (PARTITION BY ID_DOCUMENTO_FISCAL_RECLAMADO ORDER BY DATA_CADASTRO_SITUACAO desc, ID_SITUACAO_DOC_FISC_RECLAMA DESC) AS rn " +
                "           FROM  NFG_SITUACAO_DOC_FISC_RECLAMA SDR " +
                "          ) SDR " +
                "    ON      SDR.ID_DOCUMENTO_FISCAL_RECLAMADO = DFR.ID_DOCUMENTO_FISCAL_RECLAMADO " +
                "            AND rn = 1 " +
                "  JOIN GEN_PESSOA_JURIDICA pj ON pj.NUMR_CNPJ = DFR.NUMR_CNPJ_RECLAMADO " +
                "  JOIN GEN_EMPRESA emp ON pj.NUMR_CNPJ_BASE = emp.NUMR_CNPJ_BASE " +
                "WHERE   SDR.CODG_COMPL_SITUACAO_RECLAMACAO in (6,8,10,14) ";

        String ordenacao="DATA_RECLAMACAO DESC";
        return paginateMappableClassObjects(max,page,sql,ordenacao,DocumentoFiscalReclamado.class);
    }


    public Map findReclamacoesUsuarioRelatorio(Integer page, Integer max, Date data, Date dataFim, Integer situacao, Integer motivo, Integer tipoDocFisc, Integer noPrazo) {
        int prazoEmDias = 30;

        String clausulaPrazo="",
                clausulaSituacao="",
                clausulaMotivo="",
                clausulaTipoDocFisc="",
                clausulaData="";

        switch (noPrazo){
            case 1:
                clausulaPrazo= " and sysdate - SDR.DATA_CADASTRO_SITUACAO < " + prazoEmDias;
                break;
            case 2:
                clausulaPrazo=" and sysdate - SDR.DATA_CADASTRO_SITUACAO > "+prazoEmDias;
                break;
        }

        if (situacao>0){
            clausulaSituacao=" and SDR.CODG_COMPL_SITUACAO_RECLAMACAO = "+situacao;
        }
        if (motivo>0){
            clausulaMotivo=" and DFR.CODG_MOTIVO_RECLAMACAO = "+motivo;
        }

        if (tipoDocFisc>0){
            clausulaTipoDocFisc=" and DFR.TIPO_DOCUMENTO_FISCAL_RECLAMA= "+tipoDocFisc;
        }

        if (data!=null && dataFim!=null){
            clausulaData=" AND TRUNC(DFR.DATA_RECLAMACAO) between ? and ?  ";
        }else if (dataFim!=null){
            clausulaData=" AND TRUNC(DFR.DATA_RECLAMACAO) <= ?  ";
        }else if (data!=null){
            clausulaData=" AND TRUNC(DFR.DATA_RECLAMACAO) >= ?  ";
        }

        String ordenacao="DATA_RECLAMACAO DESC";

        String sql= "SELECT DFR.ID_DOCUMENTO_FISCAL_RECLAMADO, DFR.NUMR_DOCUMENTO_FISCAL, DFR.VALR_DOCUMENTO_FISCAL, DFR.DATA_RECLAMACAO, " +
                "       DFR.NUMR_CNPJ_RECLAMADO, PJ.NOME_FANTASIA, EMP.NOME_EMPRESAR, DFR.DATA_DOCUMENTO_FISCAL,con.NUMR_INSCRICAO " +
                "  FROM NFG_DOCUMENTO_FISCAL_RECLAMADO DFR " +
                "  LEFT JOIN    ( " +
                "            SELECT SDR.CODG_COMPL_SITUACAO_RECLAMACAO,SDR.DATA_CADASTRO_SITUACAO, " +
                "              SDR.ID_DOCUMENTO_FISCAL_RECLAMADO, " +
                "              ROW_NUMBER() OVER (PARTITION BY ID_DOCUMENTO_FISCAL_RECLAMADO ORDER BY DATA_CADASTRO_SITUACAO desc, ID_SITUACAO_DOC_FISC_RECLAMA DESC) AS rn " +
                "           FROM  NFG_SITUACAO_DOC_FISC_RECLAMA SDR " +
                "          ) SDR " +
                "    ON      SDR.ID_DOCUMENTO_FISCAL_RECLAMADO = DFR.ID_DOCUMENTO_FISCAL_RECLAMADO " +
                "            AND rn = 1 " +
                "  JOIN GEN_PESSOA_JURIDICA pj ON pj.NUMR_CNPJ = DFR.NUMR_CNPJ_RECLAMADO " +
                "  JOIN GEN_EMPRESA emp ON pj.NUMR_CNPJ_BASE = emp.NUMR_CNPJ_BASE " +
                "  JOIN CCE_CONTRIBUINTE con on con.ID_PESSOA = pj.ID_PESSOA " +
                " WHERE   1=1 " ;


        if (!clausulaPrazo.isEmpty()) sql+=clausulaPrazo;
        if (!clausulaMotivo.isEmpty()) sql+=clausulaMotivo;
        if (!clausulaSituacao.isEmpty()) sql+=clausulaSituacao;
        if (!clausulaTipoDocFisc.isEmpty()) sql+=clausulaTipoDocFisc;


        if(!clausulaData.isEmpty()){
            sql+=clausulaData;

            if (data!=null && dataFim!=null){
                return paginateMappableClassObjects(max,page,sql,ordenacao,DocumentoFiscalReclamado.class
                        ,data,dataFim); // GERA FILTRO NO INTERVALO DAS DATAS
            }else if (dataFim!=null){
                return paginateMappableClassObjects(max,page,sql,ordenacao,DocumentoFiscalReclamado.class
                        ,dataFim); // GERA FILTRO ANTERIOR A DATA FINAL
            }

            return paginateMappableClassObjects(max,page,sql,ordenacao,DocumentoFiscalReclamado.class
                    ,data); // GERA FILTRO A PARTIR DE DATA INICIAL
        }else{
            return paginateMappableClassObjects(max,page,sql,ordenacao,DocumentoFiscalReclamado.class
            ); // NAO GERA FILTRO POR DATA
        }

    }

    public Map cadastraNovaReclamacao(Integer tipoDocFiscalReclamacao, Integer codgMotivo, Date dataEmissaoDocFiscal, Integer numeroReclamacao, Integer iEReclamacao, Double valorReclamacao, MultipartFile fileReclamacao, PessoaParticipante cidadao, boolean dataDentroDoPrazo) {
        Map retorno = new HashMap();
        retorno.put("sucesso", false);

        try{
            DocumentoFiscalReclamado reclamacao = new DocumentoFiscalReclamado();
            reclamacao.setDataDocumentoFiscal(dataEmissaoDocFiscal);
            reclamacao.setValor(valorReclamacao);
            reclamacao.setNumero(numeroReclamacao);
            reclamacao.setDataReclamacao(new Date());
            reclamacao.setReclamacaoResolvida("N");
            reclamacao.setTipoDocumentoFiscal(tipoDocFiscalReclamacao);

            String cnpjDaInscricao = cceContribuinteRepository.getFodendoCnpjPelaInscricao(iEReclamacao);
            if (cnpjDaInscricao!=null){
                reclamacao.setNumeroCnpjEmpresa(cnpjDaInscricao);
            }else {
                retorno.put("error","Não foi encontrado nenhum número de CNPJ válido para a Inscrição Estadual(I.E.) informada!");
                return retorno;
            }

            MotivoReclamacao motivoReclamacao = new MotivoReclamacao();
            motivoReclamacao.setTipoMotivo(TipoMotivoReclamacao.get(codgMotivo));
            reclamacao.setPessoaParticipante(cidadao);
            reclamacao.setMotivoReclamacao(motivoReclamacao);

            if (fileReclamacao!=null && !fileReclamacao.isEmpty()){
                try {
                    Blob blob = new SerialBlob(fileReclamacao.getBytes());
                    reclamacao.setImgDocumentoFiscal(blob);
                    String extensao = FilenameUtils.getExtension(fileReclamacao.getOriginalFilename());
                    switch (extensao.toLowerCase()){
                        case "pdf":
                            reclamacao.setTipoExtensao(1);
                            break;
                        case "jpg":
                            reclamacao.setTipoExtensao(2);
                            break;
                        case "jpeg":
                            reclamacao.setTipoExtensao(2);
                            break;
                        case "png":
                            reclamacao.setTipoExtensao(3);
                            break;
                        default:
                            logger.error("A extensao do arquivo nao pode ser aceita: " +fileReclamacao.getName()+" da pessoa "+cidadao.getId());
                            retorno.put("error", "A extensao do arquivo nao pôde ser aceita.");
                            return retorno;

                    }
                } catch (Exception e) {
                    logger.error("Erro ao tentar ler o arquivo de reclamacao " +fileReclamacao.getName()+" da pessoa "+cidadao.getId());
                    retorno.put("error", "Erro ao tentar ler o arquivo de reclamacao. Verifique se o arquivo não está corrompido.");
                    return retorno;
                }
            }

            if(existeReclamacao(reclamacao) ){
                logger.error("Já existe uma reclamação criada com estes dados da pessoa "+cidadao.getId());
                retorno.put("error","Já existe uma reclamação criada com os dados informados! Verifique no painel 'Minhas Reclamações'.");
                return retorno;
            }

            if(reclamacaoDeNotaImportadaComCPF(reclamacao)){
                logger.error("Já existe um documento importado com CPF para os dados informados "+cidadao.getId());
                retorno.put("error","Já existe um documento importado com o seu CPF para os dados informados! Verifique no painel 'Minhas Notas'.");
                return retorno;
            }

            reclamacao.save();

            SituacaoDocumentoFiscalReclamado situacao = new SituacaoDocumentoFiscalReclamado();
            situacao.setPerfilCadastro(TipoPerfilCadastroReclamacao.CIDADAO);
            situacao.setDataCadastroSituacao(new Date());
            situacao.setComplSituacaoReclamacao(new ComplSituacaoReclamacao(TipoComplSituacaoReclamacao.CIDADAO_CRIOU_RECLAMACAO.getValue()));
            situacao.setDocumentoFiscalReclamado(reclamacao);
            situacao.setPessoaResponsavelCadastro(new GENPessoa(cidadao.getGenPessoaFisica()));
            situacao.save();

            efetuaBuscaAutomaticaDeDocumentoReclamado(reclamacao,cidadao.getGenPessoaFisica().getCpf(),false,dataDentroDoPrazo);


            retorno.put("sucesso", true);
            return retorno;
        }catch (Exception e){
            logger.error("Erro ao tentar gravar nova reclamacao da pessoa "+cidadao.getId()+": " +e.getMessage()+" "+e.getStackTrace());
            retorno.put("error", "Ocorreu uma falha ao tentar gravar uma nova reclamação. Tente de novo posteriormente.");
            return retorno;
        }
    }

    private void gravaSituacaoDocumentoDentroDoPrazo(DocumentoFiscalReclamado reclamacao) {
        SituacaoDocumentoFiscalReclamado situacao = new SituacaoDocumentoFiscalReclamado();
        situacao.setPerfilCadastro(TipoPerfilCadastroReclamacao.SISTEMA);
        situacao.setDataCadastroSituacao(new Date());
        situacao.setDocumentoFiscalReclamado(reclamacao);
        situacao.setPessoaResponsavelCadastro(null);

        ComplSituacaoReclamacao complSituacao = new ComplSituacaoReclamacao(TipoComplSituacaoReclamacao.EMPRESA_ESTA_DENTRO_DO_PRAZO);
        situacao.setComplSituacaoReclamacao(complSituacao);

        if(gravouNovaSituacaoReclamacao(situacao)){
            notificacaoService.notificaCidadaoReclamacao(reclamacao, complSituacao.getTipoComplSituacaoReclamacao());
        }
    }

    private boolean reclamacaoDeNotaImportadaComCPF(DocumentoFiscalReclamado reclamacao) {
        String sql =
                "select count(ID_DOCUMENTO_FISCAL_PARTCT) from NFG_DOCUMENTO_FISCAL_PARTCT ndf " +
                        " WHERE NUMR_CPF_ADQUIRENTE = ? " +
                        " AND NUMR_DOCUMENTO_FISCAL = ? " +
                        "   AND VALR_TOTAL_DOCUMENTO_FISCAL = ? " +
                        "   AND STAT_PROCESM_DOCUMENTO_FISCAL = 1 " +
                        " AND TRUNC(DATA_EMISSAO_DOCUMENTO_FISCAL) = TO_DATE(?,'dd/MM/yyyy') " +
                        " AND NUMR_CNPJ_ESTAB = ? ";
        Integer countNotas = jdbcTemplate.queryForObject(sql, Integer.class,
                reclamacao.getPessoaParticipante().getGenPessoaFisica().getCpf(),
                reclamacao.getNumero(),
                reclamacao.getValor(),
                reclamacao.getDataDocumentoFiscalStr(),
                reclamacao.getNumeroCnpjEmpresa());
        return countNotas > 0;
    }

    private boolean existeReclamacao(DocumentoFiscalReclamado reclamacao) {
        String sql=" select count(ID_DOCUMENTO_FISCAL_RECLAMADO) from nfg_documento_fiscal_reclamado rec" +
                " where DATA_DOCUMENTO_FISCAL=  TO_DATE(?,'dd/MM/yyyy') " +
                " and NUMR_DOCUMENTO_FISCAL=?" +
                " and VALR_DOCUMENTO_FISCAL=?" +
                " and ID_PESSOA_PARTCT_RECLAMANTE=?" +
                " and NUMR_CNPJ_RECLAMADO =? "+
                " and (select ID_DOCUMENTO_FISCAL_RECLAMADO from NFG_SITUACAO_DOC_FISC_RECLAMA where ID_DOCUMENTO_FISCAL_RECLAMADO= rec.ID_DOCUMENTO_FISCAL_RECLAMADO " +
                "       and CODG_COMPL_SITUACAO_RECLAMACAO = ? ) is null "; //reclamacao cancelada nao conta
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                reclamacao.getDataDocumentoFiscalStr(),
                reclamacao.getNumero(),
                reclamacao.getValor(),
                reclamacao.getPessoaParticipante().getId(),
                reclamacao.getNumeroCnpjEmpresa(),
                TipoComplSituacaoReclamacao.CIDADAO_CANCELOU_RECLAMACAO.getValue() );
        return count > 0;
    }


    public boolean refazerBuscaAutomaticaDeReclamacaoExistente(Integer idReclamacao){
        DocumentoFiscalReclamado reclamacao = findReclamacaoPorId(idReclamacao);
        logger.info("reclamacao "+idReclamacao+" null?");
        if (reclamacao!=null){
            logger.info("reclamacao encontrada. reclamante "+idReclamacao+" null?");
            PessoaParticipante reclamante = reclamacao.getPessoaParticipante();
            if (reclamante!=null){
                logger.info("reclamante encontrado. pessoaReclamante "+idReclamacao+" null?");
                GENPessoaFisica pessoaReclamante = reclamante.getGenPessoaFisica();
                if ( pessoaReclamante !=null){
                    logger.info("pessoaReclamante encontrado!!! Tudo ok");
                    efetuaBuscaAutomaticaDeDocumentoReclamado(reclamacao, pessoaReclamante.getCpf(), true, false);
                }
            }
        }
        return false;
    }

    private void efetuaBuscaAutomaticaDeDocumentoReclamado(DocumentoFiscalReclamado reclamacao, String cpf, boolean jaExecutou, boolean dataDentroDoPrazo) {
        String sqlBuscaAutomatica =
                "select ID_DOCUMENTO_FISCAL_PARTCT from NFG_DOCUMENTO_FISCAL_PARTCT ndf " +
                        " WHERE 1=1 " +
                        (jaExecutou?"" : " AND NUMR_CPF_ADQUIRENTE is null ") +
                        " AND NUMR_DOCUMENTO_FISCAL = ? " +
                        "   AND VALR_TOTAL_DOCUMENTO_FISCAL = ? " +
                        "   AND STAT_PROCESM_DOCUMENTO_FISCAL = 1 " +
                        " AND TRUNC(DATA_EMISSAO_DOCUMENTO_FISCAL) = TO_DATE(?,'dd/MM/yyyy') " +
                        " AND NUMR_CNPJ_ESTAB = ? order by ID_DOCUMENTO_FISCAL_PARTCT";

        logger.info("Rodando  consulta de notas para a reclamacao "+reclamacao.getId()+" cpf "+cpf);

        List<Integer> idNotas = jdbcTemplate.queryForList(sqlBuscaAutomatica, Integer.class,
                reclamacao.getNumero(),
                reclamacao.getValor(),
                reclamacao.getDataDocumentoFiscalStr(),
                reclamacao.getNumeroCnpjEmpresa());

        SituacaoDocumentoFiscalReclamado situacao = new SituacaoDocumentoFiscalReclamado();
        situacao.setPerfilCadastro(TipoPerfilCadastroReclamacao.SISTEMA);
        situacao.setDataCadastroSituacao(new Date());
        situacao.setDocumentoFiscalReclamado(reclamacao);
        situacao.setPessoaResponsavelCadastro(null);

        ComplSituacaoReclamacao complSituacao;

        if (!idNotas.isEmpty()){
            Integer idNota = idNotas.get(0);
            atualizaCpfNotaEncontradaReclamacao(idNota,cpf);
            complSituacao = new ComplSituacaoReclamacao(TipoComplSituacaoReclamacao.SISTEMA_ACHOU_AUTOMATICAMENTE);
            atribuiDocFiscalParaReclamacao(reclamacao, idNota);
            situacao.setComplSituacaoReclamacao(complSituacao);
            if(gravouNovaSituacaoReclamacao(situacao)){
                notificacaoService.notificaCidadaoReclamacao(reclamacao, complSituacao.getTipoComplSituacaoReclamacao());
            }

        }else{ //nao achou nota
            if(jaExecutou) return;


            if (dataDentroDoPrazo){
                gravaSituacaoDocumentoDentroDoPrazo(reclamacao);
            }else{
                complSituacao = new ComplSituacaoReclamacao(TipoComplSituacaoReclamacao.SISTEMA_NAO_ACHOU_AUTOMATICAMENTE);
                situacao.setComplSituacaoReclamacao(complSituacao);
                if (gravouNovaSituacaoReclamacao(situacao)){
                    notificacaoService.notificaEnvolvidosReclamacao(reclamacao, complSituacao.getTipoComplSituacaoReclamacao());
                }
            }

        }

    }

    private boolean gravouNovaSituacaoReclamacao(SituacaoDocumentoFiscalReclamado situacao){
        String sqlInsert= "insert into NFG_SITUACAO_DOC_FISC_RECLAMA( " +
                "  DATA_CADASTRO_SITUACAO,TIPO_PERFIL_USUARIO_NFG, " +
                "  ID_DOCUMENTO_FISCAL_RECLAMADO, " +
                "  CODG_COMPL_SITUACAO_RECLAMACAO) " +
                "  values(sysdate,?,?,?)";
        try {
            final Integer codigoCompl = situacao.getComplSituacaoReclamacao().getCodigo();
            final Integer idReclamacao = situacao.getDocumentoFiscalReclamado().getId();
            int retorno = jdbcTemplate.update(sqlInsert,
                    situacao.getTipoPerfil(), idReclamacao,
//                    situacao.getDocumentoFiscalReclamado().getPessoaParticipante().getGenPessoaFisica().getIdPessoa(),
                    codigoCompl);
            if (retorno > 0) {
                logger.info("Nova situacao "+codigoCompl+" gravada para a reclamacao "+idReclamacao);
                return true;
            } else {
                logger.error("Erro ao tentar gravar situacao "+codigoCompl+" para a reclamacao "+idReclamacao+"!!");
            }
        }   catch (Exception e){
            logger.error("Exception ao tentar gravar situacao para a reclamacao: "+e.getMessage());
        }

        return false;
    }

    private void atribuiDocFiscalParaReclamacao(DocumentoFiscalReclamado docReclamado, Integer idNota) {
        String sqlUpdate= " UPDATE NFG_DOCUMENTO_FISCAL_RECLAMADO set ID_DOCUMENTO_FISCAL_PARTCT= ? "+
                " WHERE ID_DOCUMENTO_FISCAL_RECLAMADO= ?";
        try {
            int retorno = jdbcTemplate.update(sqlUpdate,
                    idNota,
                    docReclamado.getId());
            if (retorno > 0) {
                logger.info("Documento fiscal associado a documento reclamado com sucesso! Id documento:" + idNota+ " Doc.Reclamado: " + docReclamado.getId());
            } else {
                logger.error("Erro nao esperado na gravacao do ID_DOCUMENTO_FISCAL_PARTCT no Documento reclamado.Id documento:" + idNota + " Doc.Reclamado: " + docReclamado.getId());
            }
        }   catch (Exception e){
            logger.error("Erro nao esperado na gravacao do ID_DOCUMENTO_FISCAL_PARTCT no Documento reclamado. Id documento:" + idNota+"  Doc.Reclamado " + docReclamado.getId()+":"+e.getMessage());
        }
    }

    private void atualizaCpfNotaEncontradaReclamacao(Integer idNota, String cpf) {
        String sqlUpdate="update NFG_DOCUMENTO_FISCAL_PARTCT set NUMR_CPF_ADQUIRENTE=? " +
                " WHERE ID_DOCUMENTO_FISCAL_PARTCT=? ";
        int update = jdbcTemplate.update(sqlUpdate, cpf, idNota);
        if (update<1){
            throw new NFGException("Erro ao tentar atualizar cpf de documento reclamado encontrado automaticamente");
        }
    }

    public String retornaStatusAndamentoString(Integer idReclamacao){
        String sql;
        sql = "select DESC_COMPL_SITUACAO_RECLAMACAO AS SITUACAO " +
                "from NFG_SITUACAO_DOC_FISC_RECLAMA " +
                "JOIN NFG_COMPL_SITUACAO_RECLAMACAO USING(CODG_COMPL_SITUACAO_RECLAMACAO) " +
                "WHERE ID_DOCUMENTO_FISCAL_RECLAMADO=? " +
                "ORDER BY DATA_CADASTRO_SITUACAO DESC, ID_SITUACAO_DOC_FISC_RECLAMA DESC";
        List<String> andamentos = jdbcTemplate.queryForList(sql,String.class, idReclamacao);

        if(andamentos.size() > 0 && !andamentos.isEmpty() && andamentos != null){
            return andamentos.get(0);
        }else {
            return "";
        }
    }

    public String listAndamentoReclamacaoString(Integer idReclamacao) {
        String sql;
        sql = "select DATA_CADASTRO_SITUACAO ||' - '|| DESC_COMPL_SITUACAO_RECLAMACAO AS SITUACAO " +
                "from NFG_SITUACAO_DOC_FISC_RECLAMA " +
                "JOIN NFG_COMPL_SITUACAO_RECLAMACAO USING(CODG_COMPL_SITUACAO_RECLAMACAO) " +
                "WHERE ID_DOCUMENTO_FISCAL_RECLAMADO=? " +
                "ORDER BY DATA_CADASTRO_SITUACAO DESC, ID_SITUACAO_DOC_FISC_RECLAMA DESC";
        logger.info("jdbcTemplate is "+(jdbcTemplate==null?"NULL":"NOT NULL")+"!!!!!!!!");
        List<String> andamentos = jdbcTemplate.queryForList(sql,String.class, idReclamacao);
        String andamentoStr="Andamento: "+" \n";
        for(String andamento:andamentos){
            andamentoStr+=andamento+" \n";
        }
        return andamentoStr;
    }


    public Map findAndamentoDaReclamacao(Integer idReclamacao, Integer page, Integer max) {
        String sql= " select sr.DATA_CADASTRO_SITUACAO," +
                " sr.INFO_SITUACAO_DOC_FISC_RECLAMA," +
                " sr.TIPO_PERFIL_USUARIO_NFG," +
                " cs.CODG_COMPL_SITUACAO_RECLAMACAO" +
                " from NFG_SITUACAO_DOC_FISC_RECLAMA sr" +
                " join NFG_COMPL_SITUACAO_RECLAMACAO cs on cs.CODG_COMPL_SITUACAO_RECLAMACAO=sr.CODG_COMPL_SITUACAO_RECLAMACAO" +
                " where ID_DOCUMENTO_FISCAL_RECLAMADO=?";
        String ordenacao="DATA_CADASTRO_SITUACAO DESC, ID_SITUACAO_DOC_FISC_RECLAMA DESC";
        return paginateMappableClassObjects(max, page, sql, ordenacao, SituacaoDocumentoFiscalReclamado.class, idReclamacao);
    }

    public List<DocumentoFiscalReclamado> findReclamacaoPorCnpj(String cnpj){
        if(cnpj == null) return null;
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("numeroCnpjEmpresa", cnpj);
        List<DocumentoFiscalReclamado> documentoFiscalReclamados = list(dados, "numeroCnpjEmpresa");
        if(documentoFiscalReclamados.isEmpty()) return null;
        return documentoFiscalReclamados;
    }

    public DocumentoFiscalReclamado findReclamacaoPorId(Integer idReclamacao) {
        if (idReclamacao==null) return null;
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("id", idReclamacao);
        List<DocumentoFiscalReclamado> reclamacaoList = list(dados, "id");
        if (reclamacaoList.isEmpty()) return null;
        return reclamacaoList.get(0);
    }

    public GENPessoaJuridica findPessoaJuridicaByCnpj(String cnpj){
        GENPessoaJuridica pessoaJuridica;
        try{
            //language=HSQL
            String sql = "select pj from GENPessoaJuridica pj where pj.numeroCnpj = :cnpj";

            Query query = this.entityManager().createQuery(sql);
            query.setParameter("cnpj", cnpj);
            pessoaJuridica =(GENPessoaJuridica) query.getSingleResult();
            return pessoaJuridica;
        }catch (Exception e){
            logger.error("Erro ao listar PessoaJuridicaByCnpj");
            return null;
        }
    }

    public List<ComplSituacaoReclamacao> acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao perfil,DocumentoFiscalReclamado reclamacao) {
        List<ComplSituacaoReclamacao> statusDisponiveis = new ArrayList<>();
        Integer codgUltimaSituacao = reclamacao.getCodgUltimaSituacao();
        if (codgUltimaSituacao==null)return null;

        switch (perfil){
            case SISTEMA:
                break;
            case CIDADAO:
                if (codgUltimaSituacao != TipoComplSituacaoReclamacao.CIDADAO_CANCELOU_RECLAMACAO.getValue()
                        && codgUltimaSituacao != TipoComplSituacaoReclamacao.SISTEMA_FINALIZOU_RECLAMACAO.getValue() ){
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.CIDADAO_CANCELOU_RECLAMACAO));
                }

                if (codgUltimaSituacao== TipoComplSituacaoReclamacao.EMPRESA_RECUSOU_RECLAMACAO.getValue()){
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.CIDADAO_CONCORDOU_COM_EMPRESA));
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.CIDADAO_NAO_CONCORDOU_COM_EMPRESA));
                }
                break;
            case CONTRIBUINTE:
                if(codgUltimaSituacao == TipoComplSituacaoReclamacao.SISTEMA_NAO_ACHOU_AUTOMATICAMENTE.getValue()){
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.EMPRESA_EVIOU_NOVO_ARQUIVO));
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.EMPRESA_RECUSOU_RECLAMACAO));
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.EMPRESA_ALEGOU_ERRO_NFG));
                }
                break;
            case CONTADOR:
                if(codgUltimaSituacao == TipoComplSituacaoReclamacao.SISTEMA_NAO_ACHOU_AUTOMATICAMENTE.getValue()){
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.EMPRESA_EVIOU_NOVO_ARQUIVO));
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.EMPRESA_RECUSOU_RECLAMACAO));
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.EMPRESA_ALEGOU_ERRO_NFG));
                }
                break;
            case GESTOR:
                if(codgUltimaSituacao != TipoComplSituacaoReclamacao.USUARIO_NOTIFICOU_ERRO_EMPRESA.getValue() &&
                        codgUltimaSituacao != TipoComplSituacaoReclamacao.USUARIO_NOTIFICOU_ERRO_CIDADAO.getValue()){
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.USUARIO_NOTIFICOU_ERRO_CIDADAO));
                    statusDisponiveis.add(ComplSituacaoReclamacao.instanciarTipo(TipoComplSituacaoReclamacao.USUARIO_NOTIFICOU_ERRO_EMPRESA));
                }
                break;
        }

        return statusDisponiveis;
    }

    public Map alteracaoDeSituacaoReclamacaoPorEmpresa(DocumentoFiscalReclamado reclamacao, Integer novoCodgTipoCompl, String infoReclamacao, GENPessoaJuridica pessoaJuridica){
        Map retorno = new HashMap();
        retorno.put("sucesso", true);

        try{
            SituacaoDocumentoFiscalReclamado situacao = new SituacaoDocumentoFiscalReclamado();
            situacao.setPerfilCadastro(TipoPerfilCadastroReclamacao.CONTRIBUINTE);
            situacao.setDataCadastroSituacao(new Date());
            situacao.setComplSituacaoReclamacao(new ComplSituacaoReclamacao(TipoComplSituacaoReclamacao.get(novoCodgTipoCompl)));
            situacao.setDocumentoFiscalReclamado(reclamacao);
            situacao.setInfo(infoReclamacao);
            situacao.setPessoaResponsavelCadastro(new GENPessoa(pessoaJuridica));
            situacao.save();

            notificacaoService.notificaCidadaoReclamacao(situacao.getDocumentoFiscalReclamado(), situacao.getComplSituacaoReclamacao().getTipoComplSituacaoReclamacao());

        }catch (Exception e){
            logger.error("Erro a Empresa tentar criar nova situação para reclamação.");
            retorno.put("sucesso", false);
        }
        return retorno;
    }

    public Map alteracaoDeSituacaoReclamacaoPorGestor(DocumentoFiscalReclamado reclamacao, Integer novoCodgTipoCompl, String infoReclamacao, GENPessoaFisica gestor){
        Map retorno = new HashMap();
        retorno.put("sucesso", true);

        try{
            SituacaoDocumentoFiscalReclamado situacao = new SituacaoDocumentoFiscalReclamado();
            situacao.setPerfilCadastro(TipoPerfilCadastroReclamacao.GESTOR);
            situacao.setDataCadastroSituacao(new Date());
            situacao.setComplSituacaoReclamacao(new ComplSituacaoReclamacao(TipoComplSituacaoReclamacao.get(novoCodgTipoCompl)));
            situacao.setDocumentoFiscalReclamado(reclamacao);
            situacao.setInfo(infoReclamacao);
            situacao.setPessoaResponsavelCadastro(new GENPessoa(gestor));
            situacao.save();

            notificacaoService.notificaEnvolvidosReclamacao(situacao.getDocumentoFiscalReclamado(),situacao.getComplSituacaoReclamacao().getTipoComplSituacaoReclamacao());

        }catch (Exception e){
            logger.error("Erro ao usuário gestor tentar criar nova situação para reclamação. Usuário:" + gestor.getIdPessoa() + ". Reclamacao:" + reclamacao.getId());
            retorno.put("sucesso",false);
        }

        return retorno;
    }

    public Map alteracaoDeSituacaoReclamacaoPorCidadao(DocumentoFiscalReclamado reclamacao, Integer novoCodgTipoCompl, String infoReclamacao, PessoaParticipante cidadao) {
        Map retorno = new HashMap();
        retorno.put("sucesso",true);

        try{

            SituacaoDocumentoFiscalReclamado situacao = new SituacaoDocumentoFiscalReclamado();
            situacao.setPerfilCadastro(TipoPerfilCadastroReclamacao.CIDADAO);
            situacao.setDataCadastroSituacao(new Date());
            situacao.setComplSituacaoReclamacao(new ComplSituacaoReclamacao(TipoComplSituacaoReclamacao.get(novoCodgTipoCompl)));
            situacao.setDocumentoFiscalReclamado(reclamacao);
            situacao.setInfo(infoReclamacao);
            situacao.setPessoaResponsavelCadastro(new GENPessoa(cidadao.getGenPessoaFisica()));
            situacao.save();

            notificacaoService.notiticaEmpresaReclamacao(reclamacao, TipoComplSituacaoReclamacao.get(novoCodgTipoCompl));
        }catch (Exception e){
            logger.error("Erro ao cidadão tentar criar nova situação para reclamação. Cidadao:" + cidadao.getId() + ". Reclamacao:" + reclamacao.getId());
            retorno.put("sucesso",false);

        }

        return retorno;
    }

    public void setContentType(DocumentoFiscalReclamado documento, HttpHeaders headers){
        String fileName = documento.getNumero().toString();
        Integer extensaoDoArquivo = documento.getTipoExtensao();

        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        switch (extensaoDoArquivo) {
            case 1:
                headers.setContentDispositionFormData(fileName.concat(".pdf"), fileName.concat(".pdf"));
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
                break;
            case 2:
                headers.setContentDispositionFormData(fileName.concat(".jpg"), fileName.concat(".jpg"));
                headers.setContentType(MediaType.parseMediaType("application/jpg"));
                break;
            case 3:
                headers.setContentDispositionFormData(fileName.concat(".png"), fileName.concat(".png"));
                headers.setContentType(MediaType.parseMediaType("application/png"));
                break;
        }
    }
}

