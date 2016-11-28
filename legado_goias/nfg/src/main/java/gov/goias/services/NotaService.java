package gov.goias.services;

import gov.goias.entidades.*;
import gov.goias.entidades.enums.StatusProcessamentoDocumentoFiscal;
import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Remisson Silva on 30/09/2014.
 */

@Service
@Configurable
public class NotaService
{
    @Autowired
    DocumentoFiscalDigitado documentoFiscalDigitado;
    @Autowired
    ProcesmLoteDocumentoFiscal procesmLoteDocumentoFiscalRepo;
    @Autowired
    EmpresaParticipante empresaParticipanteRepo;

    @Autowired
    NotificacaoService notificacaoService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CCEContribuinte cceContribuinteRepository;


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public void cadastrarNota(
            Integer numeroDocumentoFiscal, Integer serieNotaFiscal,
            String subSerieNotaFiscal, Date dataEmissao, String cpf,
            Double valorTotal, Integer tipoDocumentoFiscal,
            CCEContribuinte contribuinte, Date dataInclucaoDoctoFiscal,
            Integer inscricaoEstadual){

        logger.info("NOTA DIGITADA => TENTANDO CADASTRAR");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfSemHora = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfComHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        HashMap<String, Object> dados = new HashMap<>();
        dados.put("numrLote", sdf.format(new Date()));

        List<ProcesmLoteDocumentoFiscal> loteList = procesmLoteDocumentoFiscalRepo.list(dados, "numrLote");
        ProcesmLoteDocumentoFiscal procesmLoteDocumentoFiscal;
        if (loteList.size()>0){
            procesmLoteDocumentoFiscal = loteList.get(0);
        }else{
            procesmLoteDocumentoFiscal = new ProcesmLoteDocumentoFiscal();
            procesmLoteDocumentoFiscal.setOrigemDocumentoFiscal(4);
            procesmLoteDocumentoFiscal.setNumrLote(Integer.parseInt(sdf.format(new Date())));
            procesmLoteDocumentoFiscal.setStatProcesmNfg('1');
            try {
                procesmLoteDocumentoFiscal.setDataInicioProcessamento(sdfComHora.parse(sdfSemHora.format(new Date()) + " 00:00:00"));
                procesmLoteDocumentoFiscal.setDataFimProcessamento(sdfComHora.parse(sdfSemHora.format(new Date()) + " 23:59:59"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            procesmLoteDocumentoFiscal.save();
        }

        documentoFiscalDigitado = new DocumentoFiscalDigitado();
        documentoFiscalDigitado.setNumeroDocumentoFiscal(numeroDocumentoFiscal);
        documentoFiscalDigitado.setDataEmissao(dataEmissao);
        documentoFiscalDigitado.setCpf(cpf);
        documentoFiscalDigitado.setValorTotal(valorTotal);
        documentoFiscalDigitado.setTipoDocumentoFiscal(tipoDocumentoFiscal);
        documentoFiscalDigitado.setContribuinte(contribuinte);
        documentoFiscalDigitado.setDataInclucaoDoctoFiscal(dataInclucaoDoctoFiscal);
        documentoFiscalDigitado.setNumeroLote(procesmLoteDocumentoFiscal.getNumrLote());

        if(tipoDocumentoFiscal != null &&
                tipoDocumentoFiscal == DocumentoFiscalDigitado.TIPO_NOTA_MODELO_1 ||
                tipoDocumentoFiscal == DocumentoFiscalDigitado.TIPO_NOTA_MODELO_2)
        {
            documentoFiscalDigitado.setSerieNotaFiscal(serieNotaFiscal);
            documentoFiscalDigitado.setSubSerieNotaFiscal(subSerieNotaFiscal);
        }

        documentoFiscalDigitado.save();

        logger.info("NOTA DIGITADA => DOCUMENTO DIGITAL SALVO");

        DocumentoFiscalParticipante documentoFiscalParticipante = new DocumentoFiscalParticipante();
        documentoFiscalParticipante.setDataImportacao(new Date());
        documentoFiscalParticipante.setProcesmLoteDocumentoFiscal(procesmLoteDocumentoFiscal);
        documentoFiscalParticipante.setIdDocFiscalDigitado(documentoFiscalDigitado.getId());
        documentoFiscalParticipante.setNumeroCpf(cpf);
        documentoFiscalParticipante.setNumeroDocumentoFiscal(numeroDocumentoFiscal);
        documentoFiscalParticipante.setDataEmissaoDocumento(dataEmissao);
        documentoFiscalParticipante.setValorDocumento(valorTotal);
        documentoFiscalParticipante.setStatusProcessamento(StatusProcessamentoDocumentoFiscal.VALIDADO.getValue());
        documentoFiscalParticipante.setNumeroCnpjEmissor(cceContribuinteRepository.getFodendoCnpjPelaInscricao(inscricaoEstadual));
        documentoFiscalParticipante.save();


        logger.info("NOTA DIGITADA => DOCUMENTO PARTICIPANTE SALVO");

        atualizaReclamacaoParaDocumentoCasoExista(documentoFiscalParticipante);


        logger.info("NOTA DIGITADA => RECLAMACAO REFERENTE A DOCUMENTO ATUALIZADA (SE EXISTE)");

    }

    public void atualizarNota(
            DocumentoFiscalDigitado documentoFiscalDigitado,
            Integer numeroDocumentoFiscal, Integer serieNotaFiscal,
            String subSerieNotaFiscal,Date dataEmissao,String cpf,
            Double valorTotal,Integer tipoDocumentoFiscal, Integer inscricaoEstadual
    ){


        logger.info("NOTA DIGITADA => TENTANDO ATUALIZAR NOTA");

        if(numeroDocumentoFiscal != null)documentoFiscalDigitado.setNumeroDocumentoFiscal(numeroDocumentoFiscal);
        if(dataEmissao != null)documentoFiscalDigitado.setDataEmissao(dataEmissao);
        if(cpf != null)documentoFiscalDigitado.setCpf(cpf);
        if(valorTotal != null)documentoFiscalDigitado.setValorTotal(valorTotal);
        if(tipoDocumentoFiscal != null)documentoFiscalDigitado.setTipoDocumentoFiscal(tipoDocumentoFiscal);

        if(tipoDocumentoFiscal != null &&
                tipoDocumentoFiscal == DocumentoFiscalDigitado.TIPO_NOTA_MODELO_1 ||
                tipoDocumentoFiscal == DocumentoFiscalDigitado.TIPO_NOTA_MODELO_2)
        {
            if(serieNotaFiscal != null)documentoFiscalDigitado.setSerieNotaFiscal(serieNotaFiscal);
            if(subSerieNotaFiscal != null)documentoFiscalDigitado.setSubSerieNotaFiscal(subSerieNotaFiscal);
        }

        documentoFiscalDigitado.update();


        logger.info("NOTA DIGITADA => DOCUMENTO DIGITAL ATUALIZADO");

        DocumentoFiscalParticipante documentoFiscalParticipante = new DocumentoFiscalParticipante();
        documentoFiscalParticipante.setDataImportacao(new Date());
        documentoFiscalParticipante.setIdDocFiscalDigitado(documentoFiscalDigitado.getId());
        documentoFiscalParticipante.setNumeroCpf(cpf);
        documentoFiscalParticipante.setNumeroDocumentoFiscal(numeroDocumentoFiscal);
        documentoFiscalParticipante.setDataEmissaoDocumento(dataEmissao);
        documentoFiscalParticipante.setValorDocumento(valorTotal);
        documentoFiscalParticipante.setStatusProcessamento(StatusProcessamentoDocumentoFiscal.VALIDADO.getValue());
        documentoFiscalParticipante.setNumeroCnpjEmissor(cceContribuinteRepository.getFodendoCnpjPelaInscricao(inscricaoEstadual));


        logger.info("NOTA DIGITADA => DOCUMENTO PARTICIPANTE ATUALIZADO");

        atualizaReclamacaoParaDocumentoCasoExista(documentoFiscalParticipante);


        logger.info("NOTA DIGITADA => RECLAMACAO REFERENTE A DOCUMENTO ATUALIZADA");
    }

    public DocumentoFiscalDigitado getUltimaNotaValida(
            Integer inscricaoEstadual, Integer numeroDocumentoFiscal,
            Integer serieNotaFiscal, String subSerieNotaFiscal,
            Date dataEmissao, Integer tipoDocumentoFiscal
    ){
        logger.info("NOTA DIGITADA => TENTANDO ACHAR ULTIMA NOTA VALIDA");

        DocumentoFiscalDigitado ultimaNotaNaoRemovida = null;

        if(tipoDocumentoFiscal.equals(DocumentoFiscalDigitado.TIPO_NOTA_MODELO_1) ||
                tipoDocumentoFiscal.equals(DocumentoFiscalDigitado.TIPO_NOTA_MODELO_2))
        {
            ultimaNotaNaoRemovida = getUltimaNotaModeloValida(
                    inscricaoEstadual,
                    tipoDocumentoFiscal,
                    numeroDocumentoFiscal,
                    serieNotaFiscal,
                    subSerieNotaFiscal,
                    dataEmissao
            );
        }
        else if(tipoDocumentoFiscal.equals(DocumentoFiscalDigitado.TIPO_NOTA_ECF_ANTIGO))
        {
            ultimaNotaNaoRemovida = getUltimaNotaEcfValida(
                    inscricaoEstadual,
                    tipoDocumentoFiscal,
                    numeroDocumentoFiscal,
                    dataEmissao
            );
        }
        logger.info("NOTA DIGITADA => SEM ERROS TENTANDO ACHAR ULTIMA NOTA VALIDA");
        return ultimaNotaNaoRemovida;


    }

    public DocumentoFiscalDigitado getUltimaNotaEcfValida(
            Integer inscricaoEstadual,
            Integer tipoDocumentoFiscal,
            Integer numeroDocumentoFiscal,
            Date dataEmissao
    ){
        logger.info("NOTA DIGITADA => TENTANDO ACHAR ULTIMA NOTA ECF VALIDA");

        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String dataEmissaoParam = formatDate.format(dataEmissao);

        List<DocumentoFiscalDigitado> resultado = this.documentoFiscalDigitado.list(
                inscricaoEstadual,tipoDocumentoFiscal,numeroDocumentoFiscal,
                dataEmissaoParam,null,null, null, null,null,false
        );


        logger.info("NOTA DIGITADA => SEM ERROS TENTANDO ACHAR ULTIMA NOTA ECF VALIDA");

        return getUltimaNotaValidaDaLista(resultado);
    }

    public DocumentoFiscalDigitado getUltimaNotaModeloValida(
            Integer inscricaoEstadual,
            Integer tipoDocumentoFiscal,
            Integer numeroDocumentoFiscal,
            Integer serieNotaFiscal,
            String subSerieNotaFiscal,
            Date dataEmissao
    ){

        logger.info("NOTA DIGITADA =>  TENTANDO ACHAR ULTIMA NOTA MODELO VALIDA");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String dataEmissaoParam = formatDate.format(dataEmissao);

        List<DocumentoFiscalDigitado> resultado = this.documentoFiscalDigitado.list(
                inscricaoEstadual,tipoDocumentoFiscal,numeroDocumentoFiscal,
                dataEmissaoParam,serieNotaFiscal,subSerieNotaFiscal,null,null,null,false
        );

        logger.info("NOTA DIGITADA => SELECT EM NOTA COM OS FILTROS: " +
                "IE{"+inscricaoEstadual+"} TIPO{"+tipoDocumentoFiscal+"} NUMERO{"+numeroDocumentoFiscal+"} " +
                "EMISSAO{"+dataEmissaoParam+"} SERIE{"+serieNotaFiscal+"} SUBSERIE{"+subSerieNotaFiscal+"} ");

        logger.info("NOTA DIGITADA => NUMERO DE RESULTADOS DO SELECT PARA OS FILTROS: "+resultado.size());

        logger.info("NOTA DIGITADA => SEM ERROS TENTANDO ACHAR ULTIMA NOTA MODELO VALIDA");
        return getUltimaNotaValidaDaLista(resultado);
    }

    public DocumentoFiscalDigitado getUltimaNotaValidaDaLista(List<DocumentoFiscalDigitado> resultado)
    {

        logger.info("NOTA DIGITADA =>  TENTANDO ACHAR ULTIMA NOTA VALIDA DA LISTA");
        DocumentoFiscalDigitado ultimaNotaNaoRemovida = null;
        for(DocumentoFiscalDigitado doc: resultado)
        {
            if(doc != null && doc.getDataCancelDocumentoFiscal() == null){
                logger.info("ULTIMA NOTA NAO REMOVIDA ENCONTRADA: id="+doc.getId());
                ultimaNotaNaoRemovida = doc;
            }else{
                logger.error("ULTIMA NOTA NAO ENCONTRADA: DOC IS NULL? =" + (doc == null) + " dataCancelDoc is null?" + (doc.getDataCancelDocumentoFiscal()==null));

            }
        }

        logger.info("NOTA DIGITADA => SEM ERROS TENTANDO ACHAR ULTIMA NOTA VALIDA DA LISTA");
        return ultimaNotaNaoRemovida;
    }

    private void atualizaReclamacaoParaDocumentoCasoExista(DocumentoFiscalParticipante docFiscal) {
        Integer idReferenciaDocumento;

        try {
            if (docFiscal == null) {
                logger.error("Verificacao de reclamacao => DocFiscPartct nulo!");
                return;
            } else {
                idReferenciaDocumento = docFiscal.getIdReferenciaDocumento();
                logger.info("Verificacao de reclamacao => Id Referencia documento:" + idReferenciaDocumento);
            }

            StatusProcessamentoDocumentoFiscal statusProcessamento = StatusProcessamentoDocumentoFiscal.get(docFiscal.getStatusProcessamento());
            if (statusProcessamento == null || statusProcessamento.getValue() <= 0) {
                logger.error("Verificacao de reclamacao => Status invalido para o Id Referencia documento:" + idReferenciaDocumento);
                return;
            } else if (statusProcessamento  != StatusProcessamentoDocumentoFiscal.VALIDADO) {
                logger.info("Verificacao de reclamacao => Status diferente de 1 para o Id Referencia documento:" + idReferenciaDocumento);
                return;
            }

            DocumentoFiscalReclamado docReclamado = getDocumentoFiscalReclamado(docFiscal);

            if (docReclamado == null) return;

            Integer tipoDocumentoReclamacao = docReclamado.getTipoDocumentoFiscal();
            Integer tipoDocumentoFiscal = docFiscal.getTipoDocumentoFiscal();

            if (tipoDocumentoReclamacao != tipoDocumentoFiscal){
                logger.info("Verificacao de reclamacao => Tipo do Documento reclamado ("+tipoDocumentoReclamacao+") eh diferente do tipo do Documento fiscal encontrado ("+tipoDocumentoFiscal+").");
                return;
            }

            String numeroCpfDocFiscal = docFiscal.getNumeroCpf();

            String numrCpfReclamante;
            try{
                numrCpfReclamante = docReclamado.getPessoaParticipante().getGenPessoaFisica().getCpf();
            }catch (NullPointerException npe){
                logger.info("Verificacao de reclamacao =>  Não conseguiu pegar o cpf do reclamante: "+npe.getMessage());
                return;
            }

            if (numeroCpfDocFiscal == null) {
                logger.info("Verificacao de reclamacao => Cpf nao pode ser nulo para o Id Referencia documento:" + idReferenciaDocumento);
                return;
            } else {
                logger.info("Verificacao de reclamacao => Cpf " + numeroCpfDocFiscal + " encontrado para o Id Referencia documento:" + idReferenciaDocumento);
                if (numeroCpfDocFiscal.equals(numrCpfReclamante)) {
                    logger.info("Verificacao de reclamacao => Cpf condizente com o da reclamacao. Id Referencia documento:" + idReferenciaDocumento);
                    atribuiDocFiscalParaReclamacao(docReclamado, docFiscal);
                } else {
                    logger.info("Verificacao de reclamacao => Cpf do documento fiscal não é condizente com o da reclamacao!! Id Referencia documento:" + idReferenciaDocumento + "  DocReclamado: " + docReclamado.getId());
                    gravaSituacaoDoSistemaNaReclamacao(docReclamado, TipoComplSituacaoReclamacao.SISTEMA_ACHOU_ARQ_COM_CPF_INCONDIZENTE);
                }
            }
        }catch (Exception e){
            logger.error("Verificacao de reclamacao => ERRO NAO ESPERADO: " +e.getMessage());
        }
    }

    private void atribuiDocFiscalParaReclamacao(DocumentoFiscalReclamado docReclamado, DocumentoFiscalParticipante docFiscal) {
        String sqlSelect = "   SELECT ID_DOCUMENTO_FISCAL_PARTCT FROM ( " +
                "   SELECT ID_DOCUMENTO_FISCAL_PARTCT FROM NFG_DOCUMENTO_FISCAL_PARTCT DFP " +
                "   WHERE NUMR_DOCUMENTO_FISCAL =? " +
                "         and VALR_TOTAL_DOCUMENTO_FISCAL=? " +
                "         and DATA_EMISSAO_DOCUMENTO_FISCAL=? " +
                "         and NUMR_CNPJ_ESTAB=? " +
                "         and STAT_PROCESM_DOCUMENTO_FISCAL = 1 " +
                "         and NUMR_CPF_ADQUIRENTE=? " +
                "         and (select ID_DOCUMENTO_FISCAL_RECLAMADO FROM NFG_DOCUMENTO_FISCAL_RECLAMADO WHERE ID_DOCUMENTO_FISCAL_PARTCT =DFP.ID_DOCUMENTO_FISCAL_PARTCT ) IS NULL " +
                "         and (case when ID_NOTA_FISCAL_EMITIDA_ECF is not null then 3 " +
                "              when ID_CUPOM_FISCAL_ECF is not null then 3 " +
                "              when ID_DOCUMENTO_FISCAL_DIGITADO is not null then 1 " +
                "              when ID_NFE is not null then 2 " +
                "              END ) = ? " +
                " ) where rownum=1 " ;

        Double valor = docFiscal.getValorDocumento();

        String sqlUpdate=
                " UPDATE NFG_DOCUMENTO_FISCAL_RECLAMADO set ID_DOCUMENTO_FISCAL_PARTCT= ? WHERE ID_DOCUMENTO_FISCAL_RECLAMADO= ?";
        try {

            Integer idDocFisc = jdbcTemplate.queryForObject(sqlSelect,Integer.class,
                    docFiscal.getNumeroDocumentoFiscal(),
                    valor,
                    docFiscal.getDataEmissaoDocumento(),
                    docFiscal.getNumeroCnpjEmissor(),
                    docFiscal.getNumeroCpf(),
                    docFiscal.getTipoDocumentoFiscal() );

            if (idDocFisc!=null){
                int retorno = jdbcTemplate.update(sqlUpdate, idDocFisc, docReclamado.getId());
                if (retorno > 0) {
                    logger.info("Documento fiscal associado a documento reclamado com sucesso! Id Referencia documento:" + docFiscal.getIdReferenciaDocumento() + " Doc.Reclamado: " + docReclamado.getId());
                    gravaSituacaoDoSistemaNaReclamacao(docReclamado, TipoComplSituacaoReclamacao.SISTEMA_ACHOU_NOVO_ARQUIVO);
                } else {
                    logger.error("Erro nao esperado na gravacao do ID_DOCUMENTO_FISCAL_PARTCT no Documento reclamado. Id Referencia documento:" + docFiscal.getIdReferenciaDocumento() + " Doc.Reclamado: " + docReclamado.getId());
                }
            }else {
                logger.error("Nota para atualizacao em reclamacao nao encontrada (deve haver uma nota com esses dados ja associada a reclamacao). Id referencia:" + docFiscal.getIdReferenciaDocumento() + " Doc.Reclamado: " + docReclamado.getId());
                return;
            }


        }catch (EmptyResultDataAccessException er){
            logger.error("Nota para atualizacao em reclamacao nao encontrada (deve haver uma nota com esses dados ja associada a reclamacao). Id referencia: " + docFiscal.getIdReferenciaDocumento()+"  Doc.Reclamado " + docReclamado.getId()+":"+er.getMessage());

        }
        catch (Exception e){
            logger.error("Erro nao esperado na gravacao do ID_DOCUMENTO_FISCAL_PARTCT no Documento reclamado. Id Referencia documento:" + docFiscal.getIdReferenciaDocumento()+"  Doc.Reclamado " + docReclamado.getId()+":"+e.getMessage());
        }
    }

    private void gravaSituacaoDoSistemaNaReclamacao(DocumentoFiscalReclamado docReclamado,TipoComplSituacaoReclamacao complSituacao ) {
        String sqlInsert=" insert into NFG_SITUACAO_DOC_FISC_RECLAMA(" +
                " DATA_CADASTRO_SITUACAO," +
                " TIPO_PERFIL_USUARIO_NFG, " +
                " ID_DOCUMENTO_FISCAL_RECLAMADO," +
                " CODG_COMPL_SITUACAO_RECLAMACAO " +
                ") values(?,?,?,?)";
        try{
            int retorno = jdbcTemplate.update(sqlInsert,
                    new Date(),
                    TipoPerfilCadastroReclamacao.SISTEMA.getValue(),
                    docReclamado.getId(),
                    complSituacao.getValue());
            if (retorno <=0 ){
                logger.error("Erro nao esperado na gravacao de nova NFG_SITUACAO_DOC_FISC_RECLAMA:"+complSituacao.getDescricao()+". Doc.Reclamado " + docReclamado.getId());
            }else {
                logger.info("Nova NFG_SITUACAO_DOC_FISC_RECLAMA:"+complSituacao.getDescricao()+" criada. Doc.Reclamado " + docReclamado.getId());
                notificacaoService.notificaEnvolvidosReclamacao(docReclamado,complSituacao);
            }
        }   catch (Exception e){
            logger.error("Erro nao esperado na gravacao de nova NFG_SITUACAO_DOC_FISC_RECLAMA:"+complSituacao.getDescricao()+". Doc.Reclamado " + docReclamado.getId()+":"+e.getMessage());
        }
    }

    private DocumentoFiscalReclamado getDocumentoFiscalReclamado(DocumentoFiscalParticipante docFiscal) {
        try{
            String sql = "SELECT " +
                    "  DFR.ID_DOCUMENTO_FISCAL_RECLAMADO, " +
                    "  NUMR_DOCUMENTO_FISCAL, " +
                    "  VALR_DOCUMENTO_FISCAL, " +
                    "  NUMR_CNPJ_RECLAMADO, " +
                    "  DATA_DOCUMENTO_FISCAL, " +
                    "  DATA_RECLAMACAO, " +
                    "  TIPO_DOCUMENTO_FISCAL_RECLAMA, " +
                    "  PF.NUMR_CPF, " +
                    "  PF.ID_PESSOA " +
                    "  FROM    NFG_DOCUMENTO_FISCAL_RECLAMADO DFR " +
                    "  JOIN    ( " +
                    "            SELECT SDR.CODG_COMPL_SITUACAO_RECLAMACAO,SDR.DATA_CADASTRO_SITUACAO, " +
                    "              SDR.ID_DOCUMENTO_FISCAL_RECLAMADO, " +
                    "              ROW_NUMBER() OVER (PARTITION BY ID_DOCUMENTO_FISCAL_RECLAMADO ORDER BY DATA_CADASTRO_SITUACAO desc, ID_SITUACAO_DOC_FISC_RECLAMA DESC) AS rn " +
                    "            FROM  NFG_SITUACAO_DOC_FISC_RECLAMA SDR " +
                    "          ) SDR " +
                    "    ON      SDR.ID_DOCUMENTO_FISCAL_RECLAMADO = DFR.ID_DOCUMENTO_FISCAL_RECLAMADO " +
                    "            AND rn = 1 " +
                    "  JOIN NFG_PESSOA_PARTICIPANTE PP ON DFR.ID_PESSOA_PARTCT_RECLAMANTE=PP.ID_PESSOA_PARTCT " +
                    "  JOIN GEN_PESSOA_FISICA PF ON PF.ID_PESSOA=PP.ID_PESSOA " +
                    "  WHERE INDI_RECLAMACAO_RESOLVIDA = 'N' " +
//                    "      AND SDR.CODG_COMPL_SITUACAO_RECLAMACAO = ? " +
                    "      AND NUMR_DOCUMENTO_FISCAL= ? " +
                    "      AND DATA_DOCUMENTO_FISCAL= ? " +
                    "      AND VALR_DOCUMENTO_FISCAL= ? " +
                    "      AND NUMR_CNPJ_RECLAMADO= ? ";

            int complReclamacaoEmpresaEnviouNovoArq = TipoComplSituacaoReclamacao.EMPRESA_EVIOU_NOVO_ARQUIVO.getValue();

            Double valor = docFiscal.getValorDocumento();

            Object[] params = {
//                    complReclamacaoEmpresaEnviouNovoArq,
                    docFiscal.getNumeroDocumentoFiscal(),
                    docFiscal.getDataEmissaoDocumento(),
                    valor,
                    docFiscal.getNumeroCnpjEmissor()
            };

            return jdbcTemplate.queryForObject(sql, params, new RowMapper<DocumentoFiscalReclamado>() {
                public DocumentoFiscalReclamado mapRow(ResultSet rs, int rowNum) throws SQLException {
                    DocumentoFiscalReclamado docFiscalReclamado = new DocumentoFiscalReclamado();
                    docFiscalReclamado.setId(rs.getInt("ID_DOCUMENTO_FISCAL_RECLAMADO"));
                    docFiscalReclamado.setNumero(rs.getInt("NUMR_DOCUMENTO_FISCAL"));
                    docFiscalReclamado.setValor(rs.getDouble("VALR_DOCUMENTO_FISCAL"));
                    docFiscalReclamado.setNumeroCnpjEmpresa(rs.getString("NUMR_CNPJ_RECLAMADO"));
                    docFiscalReclamado.setDataDocumentoFiscal(rs.getTimestamp("DATA_DOCUMENTO_FISCAL"));
                    docFiscalReclamado.setDataReclamacao(rs.getTimestamp("DATA_RECLAMACAO"));
                    docFiscalReclamado.setTipoDocumentoFiscal(rs.getInt("TIPO_DOCUMENTO_FISCAL_RECLAMA"));

                    PessoaParticipante pessoaParticipante = new PessoaParticipante();
                    GENPessoaFisica genPessoaFisica = new GENPessoaFisica();
                    genPessoaFisica.setCpf(rs.getString("NUMR_CPF"));
                    genPessoaFisica.setIdPessoa(rs.getInt("ID_PESSOA"));
                    pessoaParticipante.setGenPessoaFisica(genPessoaFisica);
                    docFiscalReclamado.setPessoaParticipante(pessoaParticipante);

                    return docFiscalReclamado;
                }
            });
        }catch (EmptyResultDataAccessException ere){
            logger.info("Documento reclamado nao encontrado para o Id Referencia documento:" + docFiscal.getIdReferenciaDocumento());
            return null;
        }catch (Exception e){
            logger.error("Erro nao esperado na consulta de Documento reclamado para o Id Referencia documento:" + docFiscal.getIdReferenciaDocumento()+": "+e.getMessage());
            return null;
        }

    }
}
