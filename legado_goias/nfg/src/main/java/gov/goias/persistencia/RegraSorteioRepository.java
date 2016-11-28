package gov.goias.persistencia;

import gov.goias.entidades.*;
import gov.goias.exceptions.NFGException;
import gov.goias.persistencia.GenericRepository;
import gov.goias.persistencia.historico.HistoricoNFG;
import gov.goias.util.Encoding;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by bruno-cff on 06/05/2015.
 */
public class RegraSorteioRepository extends GenericRepository<Integer, RegraSorteio>{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    RegraPontuacaoDocumentoFiscal regraPontuacaoDocumentoFiscalRepository;
    @Autowired
    BilhetePessoa bilhetePessoaRepository;

    private static final Logger logger = Logger.getLogger(RegraSorteioRepository.class);

    public List<RegraSorteio> listSorteios() {
        List<RegraSorteio> sorteios;
        try {
            String hql = "from RegraSorteio sorteio where sorteio.divulgaSorteio = 'S' order by sorteio.id DESC";
            Query query = this.entityManager().createQuery(hql);
            sorteios= (List<RegraSorteio>) query.getResultList();

        } catch (Exception e) {
            throw new NFGException("Algo de errado ocorreu ao tentar listar os sorteios");
        }

        return sorteios;
    }

    public RegraSorteio buscaSorteioPorId(Integer idSorteio) {
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getClass());
        Root<RegraSorteio> sorteio = criteriaQuery.from(RegraSorteio.class);
        criteriaQuery.where(criteriaBuilder.equal(sorteio.get("id"),  idSorteio));

        TypedQuery<RegraSorteio> query = entityManager().createQuery(criteriaQuery);
        try{
            return query.getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public int totalDeDocumentosPorSorteio(Integer idSorteio, Integer idCidadao) {
        String sql = "select count(*) as totalDocsFiscais from nfg_pontuacao_pessoa p" +
                "   inner join nfg_sorteio_pontuacao sp on sp.id_pontuacao_pessoa=p.id_pontuacao_pessoa" +
                "   inner join nfg_regra_sorteio s on s.id_regra_sorteio = sp.id_regra_sorteio" +
                "   inner join nfg_pontuacao_doc_fiscal_pes df on df.id_pontuacao_pessoa=sp.id_pontuacao_pessoa" +
                "   where s.id_regra_sorteio= ? " +
                "   and p.id_pessoa_partct= ? ";
        try{
            Integer totalDocsFiscais = jdbcTemplate.queryForObject(sql, Integer.class, idSorteio,idCidadao);
            return totalDocsFiscais != null? totalDocsFiscais : 0;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public int totalDePontosPorSorteio(Integer idSorteio, Integer idCidadao) {
        String sql = "select sum(p.qtde_ponto) as totalPontos from nfg_pontuacao_pessoa p " +
                "  inner join nfg_sorteio_pontuacao sp on sp.id_pontuacao_pessoa=p.id_pontuacao_pessoa " +
                "  inner join nfg_regra_sorteio s on s.id_regra_sorteio = sp.id_regra_sorteio " +
                "   where s.id_regra_sorteio= ? " +
                "   and p.id_pessoa_partct= ? ";
        try{
            Integer totalPontos = jdbcTemplate.queryForObject(sql, Integer.class, idSorteio,idCidadao);
            return totalPontos != null? totalPontos : 0;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public List consultaPontuacaoDocsFiscaisPorSorteio(Integer idSorteio,Integer idPessoa, Integer max, Integer page,  boolean count) throws UnsupportedEncodingException {
        
    	if (idPessoa==null || idSorteio==null){
            logger.error("Consulta Pontuacao Bonus: IDSorteio e IDPessoa nao podem ser nulos.");
            return null;
        }

        logger.info("LISTAR PONTUACAO DOCS FISCAIS PARTCT."+idPessoa+" IDSORTEIO: "+idSorteio);

        String sql = " select " +
                "  pj.NUMR_CNPJ AS cnpj, " +
                "  (CASE WHEN ge.NOME_EMPRESAR IS NULL THEN 'Em branco' ELSE ge.NOME_EMPRESAR END) AS estabelecimento,  " +
                "  (CASE WHEN pj.NOME_FANTASIA IS NULL THEN 'Em branco' ELSE pj.NOME_FANTASIA END) AS fantasia,  " +
                "  NUMR_DOCUMENTO_FISCAL AS numero, " +
                "  DATA_EMISSAO_DOCUMENTO_FISCAL AS emissao, " +
                "  DATA_IMPORTACAO AS referencia, " +
                "  VALR_TOTAL_DOCUMENTO_FISCAL AS valor, " +
                "  p.qtde_ponto, " +
                "  p.stat_pontuacao_nfg as status, " +
                "  (case pdf.TIPO_REGRA_PONTUACAO_DOC_FISC " +
                "       when 0 then 'Pontuação integral' " +
                "       when 1 then 'Limite máximo de documentos atingido' " +
                "       when 2 then 'Limite máximo de documentos do estabelecimento' " +
                "       when 3 then 'Limite máximo de pontos atingido' " +
                "       when 4 then 'Limite máximo do documento fiscal atingido' " +
                "       else 'Operações fiscais não participantes do programa NFG' end" +
                "   ) as detalhe " +
                "  from nfg_pontuacao_pessoa p " +
                "  inner join nfg_sorteio_pontuacao sp on sp.id_pontuacao_pessoa=p.id_pontuacao_pessoa " +
                "  inner join nfg_regra_sorteio s on s.id_regra_sorteio = sp.id_regra_sorteio " +
                "  inner join nfg_pontuacao_doc_fiscal_pes pdf on pdf.id_pontuacao_pessoa=sp.id_pontuacao_pessoa " +
                "  inner join nfg_documento_fiscal_partct df on df.id_documento_fiscal_partct = pdf.id_documento_fiscal_partct " +
                "  inner join  GEN_PESSOA_JURIDICA pj on pj.NUMR_CNPJ = df.NUMR_CNPJ_ESTAB " +
                "  inner join GEN_EMPRESA ge on   ge.NUMR_CNPJ_BASE = pj.NUMR_CNPJ_BASE  " +
                "  where s.id_regra_sorteio= :id_regra_sorteio " +
                "      and p.id_pessoa_partct= :id_pessoa_partct ";

        sql +="      order by p.qtde_ponto desc, df.DATA_IMPORTACAO desc, NUMR_DOCUMENTO_FISCAL";

        Query query = entityManager().createNativeQuery(sql);
        query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("");

        query.setParameter("id_regra_sorteio", idSorteio);
        query.setParameter("id_pessoa_partct", idPessoa);

        if (count){
            List countList = new ArrayList();
            countList.add(new Integer(query.getResultList().size()));
            return countList;
        }

        query.setMaxResults(max);
        query.setFirstResult(page * max);

        List<Object[]> resultList = query.getResultList();
        List listOfMapResults = new ArrayList();
        for(Object[] obj : resultList) {
            Map mapResults = new HashMap();
            mapResults.put("cnpj",obj[0]!=null? obj[0].toString():"");
            mapResults.put("estabelecimento",obj[1]!=null? obj[1].toString():"");
            mapResults.put("fantasia",obj[2]!=null? obj[2].toString():"");
            mapResults.put("numero",obj[3]!=null? obj[3].toString():"");
            mapResults.put("emissao", obj[4]!=null? new Date(((Timestamp)obj[4]).getTime()):null);
            mapResults.put("registro", obj[5]!=null? new Date(((Timestamp)obj[5]).getTime()):null);
            mapResults.put("valor", obj[6]!=null? Double.parseDouble(obj[6].toString()):null);
            mapResults.put("qtdePontos", obj[7]!=null? obj[7].toString():"");
            mapResults.put("status", obj[8]!=null? obj[8].toString():"");
            mapResults.put("detalhe", obj[9] !=null? obj[9].toString()  :"") ;
            listOfMapResults.add(mapResults);
        }

        logger.info("NR DE REGISTROS PONTUACAO DOCS FISCAIS ENCONTRADOS: "+listOfMapResults.size());



        return listOfMapResults;
    }



    public List consultaPontuacaoBonusPorSorteio(Integer idSorteio, Integer idPessoa,Integer max, Integer page, boolean count) {

        if (idPessoa==null || idSorteio==null){
            logger.error("Consulta Pontuacao Bonus: IDSorteio e IDPessoa nao podem ser nulos.");
            return null;
        }

        logger.info("LISTAR PONTUACAO BONUS PARTCT."+idPessoa+" IDSORTEIO: "+idSorteio);

        String sql = "select" +
                "  p.DATA_PONTUACAO," +
                "  nrp.desc_pontuacao_extra," +
                "  p.qtde_ponto" +
                "  from nfg_pontuacao_pessoa p" +
                "  inner join nfg_sorteio_pontuacao sp on sp.id_pontuacao_pessoa=p.id_pontuacao_pessoa" +
                "  inner join nfg_regra_sorteio s on s.id_regra_sorteio = sp.id_regra_sorteio" +
                "  inner join nfg_pontuacao_extra_pessoa pe on pe.id_pontuacao_pessoa=sp.id_pontuacao_pessoa" +
                "  inner join nfg_regra_pontuacao_extra nrp on nrp.id_pontuacao_extra=pe.id_pontuacao_extra" +
                "  where s.id_regra_sorteio=:id_regra_sorteio" +
                "  and p.id_pessoa_partct= :id_pessoa_partct";

        sql +="      order by p.DATA_PONTUACAO desc,nrp.desc_pontuacao_extra";

        Query query = entityManager().createNativeQuery(sql);
        query.unwrap(org.hibernate.SQLQuery.class).addSynchronizedQuerySpace("");

        query.setParameter("id_regra_sorteio", idSorteio);
        query.setParameter("id_pessoa_partct", idPessoa);

        if (count){
            List countList = new ArrayList();
            countList.add(new Integer(query.getResultList().size()));
            return countList;
        }

        query.setMaxResults(max);
        query.setFirstResult(page * max);

        List<Object[]> resultList = query.getResultList();
        List listOfMapResults = new ArrayList();
        for(Object[] obj : resultList) {
            Map mapResults = new HashMap();
            mapResults.put("data", obj[0]!=null? new Date(((Timestamp)obj[0]).getTime()):null);
            mapResults.put("descricao",obj[1]!=null? obj[1].toString():"");
            mapResults.put("qtdePontos",obj[2]!=null? obj[2].toString():"");
            listOfMapResults.add(mapResults);
        }
        logger.info("NR DE REGISTROS PONTUACAO BONUS ENCONTRADOS: "+listOfMapResults.size());

        return listOfMapResults;
    }

    public boolean adicionaRegraPontuacaoExtra(String descricao, Integer qtdePontos, Date dIni, Date dFin) {
        try{
            RegraPontuacaoExtra regraPontuacaoExtra = new RegraPontuacaoExtra();
            regraPontuacaoExtra.setDescPontuacaoExtra(descricao);
            regraPontuacaoExtra.setQtdePontos(qtdePontos);
            regraPontuacaoExtra.setDataInicioRegra(dIni);
            regraPontuacaoExtra.setDataFimRegra(dFin);
            regraPontuacaoExtra.save();
            return true;
        }catch (Exception e){
            logger.error("Erro ao adicionar regra prontuacao extra:"+e.getMessage());
            return false;
        }
    }

    public boolean adicionaRegraPontuacaoNotas(Integer numrMaximoDocRef, Integer numrMaximoEstabRef, Integer numrMaximoPontoDocumento, Integer numrMaximoPontoRef, Double valrFatorConversao, Date dIni, Date dFin) {
        try{
            RegraPontuacaoDocumentoFiscal regraPontuacaoDocumentoFiscal = new RegraPontuacaoDocumentoFiscal();
            regraPontuacaoDocumentoFiscal.setDataInicioRegra(dIni);
            regraPontuacaoDocumentoFiscal.setDataFimRegra(dFin);
            regraPontuacaoDocumentoFiscal.setValorFatorConversao(valrFatorConversao);
            regraPontuacaoDocumentoFiscal.setNumrMaximoDocRef(numrMaximoDocRef);
            regraPontuacaoDocumentoFiscal.setNumrMaximoEstabRef(numrMaximoEstabRef);
            regraPontuacaoDocumentoFiscal.setNumrMaximoPontoDocumento(numrMaximoPontoDocumento);
            regraPontuacaoDocumentoFiscal.setNumrMaximoPontoRef(numrMaximoPontoRef);
            regraPontuacaoDocumentoFiscal.save();
            return true;
        }catch (Exception e){
            logger.error("Erro ao adicionar regra prontuacao extra:"+e.getMessage());
            return false;
        }

    }


    public boolean adicionaRegraSorteio(String infoSorteio, Date dataRealizacao,
                                        Integer statProcesmSorteio, Date dataCadastro,
                                        Integer tipoSorteio, Double numrConversaoPontoBilhete,
                                        Character indiDivulgaSite, Date dataExtracaoLoteriaFederal,
                                        Integer numrExtracaoLoteriaFederal, Character indiSorteioRealizado) {
        try{
            RegraSorteio regraSorteio = new RegraSorteio();
            regraSorteio.setInformacao(infoSorteio);
            regraSorteio.setDataRealizacao(dataRealizacao);
            regraSorteio.setStatus(statProcesmSorteio);
            regraSorteio.setDataCadastro(dataCadastro);
            regraSorteio.setTipo(tipoSorteio);
            regraSorteio.setNumeroConversao(numrConversaoPontoBilhete);
            regraSorteio.setDivulgaSorteio(indiDivulgaSite.toString());
            regraSorteio.setDataExtracaoLoteria(dataExtracaoLoteriaFederal);
            regraSorteio.setRealizado(indiSorteioRealizado);
            regraSorteio.setNumeroLoteria(numrExtracaoLoteriaFederal);
            regraSorteio.save();
            return true;
        }catch (Exception e){
            logger.error("Erro ao adicionar regra sorteio:"+e.getMessage());
            return false;
        }
    }

    public boolean adicionaPontuacaoBonusCadastro(PessoaParticipante pessoaParticipante) {
        try{
            RegraPontuacaoExtra regraPontuacaoExtra = new RegraPontuacaoExtra();
            Map dados = new HashMap<>();
            dados.put("descPontuacaoExtra", "CADASTRO NO NFG");
            dados.put("qtdePontos", 100);
            List<RegraPontuacaoExtra> regrasBonus = regraPontuacaoExtra.list(dados,"descPontuacaoExtra");
            regraPontuacaoExtra = regrasBonus.get(0);

            PontuacaoPessoa pontuacaoPessoa = new PontuacaoPessoa();
            pontuacaoPessoa.setQtdePontos(regraPontuacaoExtra.getQtdePontos());
            pontuacaoPessoa.setDataPontuacao(new Date());
            pontuacaoPessoa.setIndiOrigem(1);
            pontuacaoPessoa.setPessoaParticipante(pessoaParticipante);
            pontuacaoPessoa.setStatusPontuacao(1);
            pontuacaoPessoa.save();

            PontuacaoExtraPessoa pontuacaoExtraPessoa = new PontuacaoExtraPessoa();
            pontuacaoExtraPessoa.setPontuacaoPessoa(pontuacaoPessoa);
            pontuacaoExtraPessoa.setRegraPontuacaoExtra(regraPontuacaoExtra);
            pontuacaoExtraPessoa.save();

            return true;
        }catch (Exception e){

            return false;
        }
    }

    public Integer pontuacaoBonusSemSorteio(Integer idCidadao) {
        String sql = "select sum(p.QTDE_PONTO) as totalBonus " +
                " from nfg_pontuacao_pessoa p " +
                " where not exists( " +
                "     SELECT id_pontuacao_pessoa  " +
                "     from nfg_sorteio_pontuacao  " +
                "     where id_pontuacao_pessoa=p.id_pontuacao_pessoa " +
                " )AND p.INDI_ORIGEM_PONTUACAO_NFG=1 " +
                " AND p.ID_PESSOA_PARTCT= ? ";
        try{
            Integer totalBonusSemSorteio = jdbcTemplate.queryForObject(sql, Integer.class, idCidadao);
            return totalBonusSemSorteio != null? totalBonusSemSorteio : 0;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public Integer pontuacaoSemSorteio(Integer idCidadao) {
        String sql = "select sum(p.QTDE_PONTO) as totalBonus " +
                " from nfg_pontuacao_pessoa p " +
                " where not exists( " +
                "     SELECT id_pontuacao_pessoa  " +
                "     from nfg_sorteio_pontuacao  " +
                "     where id_pontuacao_pessoa=p.id_pontuacao_pessoa " +
                " )AND p.ID_PESSOA_PARTCT= ? ";
        try{
            Integer totalBonusSemSorteio = jdbcTemplate.queryForObject(sql, Integer.class, idCidadao);
            return totalBonusSemSorteio != null? totalBonusSemSorteio : 0;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }


    public Map<String, Object> carregaDadosDoSorteioParaCidadao(Integer idSorteio, Integer idCidadao) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();
        RegraSorteio sorteio = buscaSorteioPorId(idSorteio);
        int totalDocs = totalDeDocumentosPorSorteio(idSorteio, idCidadao);
        int totalPontos = totalDePontosPorSorteio(idSorteio, idCidadao);
        Long totalBilhetes = bilhetePessoaRepository.retornaTotalDeBilhetes(idCidadao, idSorteio);
        Date dataFimRegra = regraPontuacaoDocumentoFiscalRepository.retornaDataFimRegra(idSorteio);

        resposta.put("sorteio", sorteio);
        resposta.put("totalDocs", totalDocs);
        resposta.put("totalPontos", totalPontos);
        resposta.put("totalBilhetes", totalBilhetes != null ? totalBilhetes : 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        resposta.put("dataFimRegra", dataFimRegra != null ? sdf.format(dataFimRegra) : "Em Defini&ccedil;&atilde;o");
        return resposta;

    }

    public Map<String, Object> carregaDadosDoSorteioParaUsuario(Integer idSorteio) throws ParseException {
        Map<String, Object> resposta = new HashMap<String, Object>();
        RegraSorteio sorteio = buscaSorteioPorId(idSorteio);
        Date dataFimRegra = regraPontuacaoDocumentoFiscalRepository.retornaDataFimRegra(idSorteio);

        resposta.put("sorteio", sorteio);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        resposta.put("dataFimRegra", dataFimRegra != null ? sdf.format(dataFimRegra) : "Em Defini&ccedil;&atilde;o");
        return resposta;

    }

}
