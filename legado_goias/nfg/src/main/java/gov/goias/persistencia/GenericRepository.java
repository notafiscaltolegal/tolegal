package gov.goias.persistencia;

import gov.goias.entidades.JdbcMappable;
import gov.goias.persistencia.util.TratamentoDeDados;
import gov.goias.util.StringSimilarity;
import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: diogo
 * Date: 8/27/13
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
public abstract class GenericRepository<Id extends Serializable, Clazz> implements IDao<Id>{

    @PersistenceContext
    private EntityManager entityManager;


    private Logger logger = LoggerFactory.getLogger(GenericRepository.class);

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public <T> void save() {
        entityManager.persist(this);
    }

    public <T> void update() {
        entityManager.merge(this);
    }

    public <T> void delete() {
        entityManager.remove(this);
    }

    public <T> T get(Id o) {
        return (T) entityManager.find(getClass(), o);
    }

    public <T> List<T> list() {
        CriteriaQuery<T> criteriaQuery = getCriteriaQuery();
        Query query = entityManager.createQuery(criteriaQuery);
        query.setHint(QueryHints.CACHEABLE, true);
        return query.getResultList();
    }

    //TODO com unwrap da query e order by
    @SuppressWarnings("unchecked")
    public <T> List<T> list(Map<String, Object> data, String orderBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getClass());
        Root<T> root = criteriaQuery.from(getClass());
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                criteriaQuery.where(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
            }
        }
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderBy)));
        Query query = entityManager.createQuery(criteriaQuery);
        query.setHint(QueryHints.CACHEABLE, true);
        return query.getResultList();
    }

    public <T> List<T> list(Integer offset, Integer max) {
        CriteriaQuery<T> criteriaQuery = getCriteriaQuery();

        Query query = entityManager.createQuery(criteriaQuery);
        query.setHint(QueryHints.CACHEABLE, true);
        return query
                .setFirstResult(offset)
                .setMaxResults(max)
                .getResultList();
    }

    public Long count() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getIdClass());
        criteriaQuery.select(criteriaBuilder.count((criteriaQuery.from(getClass()))));
        return (Long) entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    protected CriteriaQuery getCriteriaQuery() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getClass());
        criteriaQuery.from(getClass());
        return criteriaQuery;
    }

    protected EntityManager entityManager() {
        return entityManager;
    }

    private Class<Id> getIdClass() {
        return (Class<Id>) ((ParameterizedType) GenericRepository.class.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public boolean textoConfereComOBanco(String textoConferido, String textoReferencia, Double taxaSimilaridade, boolean nullable){
        textoConferido = TratamentoDeDados.removeAcentos(textoConferido);
        textoConferido = textoConferido.toUpperCase();
        textoConferido = textoConferido.trim();
        textoReferencia = TratamentoDeDados.removeAcentos(textoReferencia);
        textoReferencia = textoReferencia.toUpperCase();
        textoReferencia = textoReferencia.trim();
        boolean textoConfere = true;
        if (taxaSimilaridade!=null) {
            textoConfere &= StringSimilarity.razaoDeSimilaridade(textoConferido,textoReferencia) >= taxaSimilaridade;
        }else{
            textoConfere &= textoConferido.equals(textoReferencia);
        }
        if (nullable) textoConfere |=  textoReferencia == null || textoReferencia.length()==0 || textoReferencia.equals("NAO INFORMADO");
        return textoConfere;
    }

    public boolean dataConfereComOBanco(Date dataConferida, Date dataReferencia,boolean nullable){
        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");
        boolean dataConfere = false;
        if (dataReferencia==null){
            return nullable;
        }
        try{
            dataConfere |= (formatoData.format(dataConferida)).equals(formatoData.format(dataReferencia));
        }catch (NullPointerException npe){
            logger.info("ATENÇÃO: O cadastro de cidadao identificou uma situacao nao esperada de data de nascimento nula!");
            return false;
        }
        return dataConfere;

    }


    /**
     * Este cara aqui eh uma generalizacao dos metodos que se utilizam
     * do Spring JDBCTemplate pra fazer consulta paginada (tendo em vista que
     * sao varias ocorrencias no sistema, justificou-se a criacao deste metodo)
     *
     * PARAMETROS
     * * Os dois primeiros parametros sao referentes a paginacao,
     * * O terceiro parametro eh o sqlBase (sql nao paginado),
     * * O quarto parametro eh a string de ordenacao do sql,
     * * O quinto parametro eh a classe mapeavel (implementa a interface
     *      JdbcMappable que possui o metodo populate(), recebendo o ResultSet e
     *      populando o objeto do Rowmapper),
     * * Multiplos parametros que sao os parametros da consulta em si.
     *      Sua abrangencia eh exatamente a mesma do JDBCTemplate.
     *
     * RETORNO
     * * Retorna um mapa. O mapa contem:
     *      -uma lista (chave:'list') com os objetos paginados
     *      -uma contagem(chave:'count') do numero total de registros do sqlBase
     * */
    public Map<String,Object> paginateMappableClassObjects(Integer maxRegistrosPorPagina, Integer pagina, String sqlBase,String ordenacao, final Class classeMapeavel, Object... parametros) {
        Map<String,Object> mapaRetorno = new HashMap<String,Object>();

        String sqlPaginated = "select * from (select * from ( "
                +sqlBase.toUpperCase().replaceFirst("SELECT","SELECT row_number() over (order by "+ordenacao+" ) RN,")
                +" ) where rownum <=? "
                +" ) where rn >  ?";


        String sqlCount = "select count(*) from ( " +sqlBase +" )";

        try{
            Object[] parametrosComPagination = new Object[parametros.length+2];
            int aux=0;
            for (Object o:parametros){
                parametrosComPagination[aux]=parametros[aux];
                aux++;
            }
            parametrosComPagination[aux] = maxRegistrosPorPagina * (pagina + 1);
            parametrosComPagination[aux+1] = maxRegistrosPorPagina * pagina;

            List resultadoPaginacao = jdbcTemplate.query(
                    sqlPaginated,

                    new RowMapper<JdbcMappable>() {
                        @Override
                        public JdbcMappable mapRow(ResultSet rs, int i) throws SQLException {
                            JdbcMappable jdbcMappable = null;
                            try {
                                jdbcMappable = (JdbcMappable) classeMapeavel.newInstance();
                            } catch (Exception e) {
                                logger.error("Erro ao tentar instanciarTipo classe que implementa a interface JdbcMappable: "+e.getMessage());
                                return null;
                            }

                            try {
                                jdbcMappable.populate(rs);
                            } catch (Exception e) {
                                logger.error("Erro na implementacao do metodo populate() da interface JdbcMappable: " + e.getMessage());
                                return null;
                            }

                            return jdbcMappable;
                        }
                    },

                    parametrosComPagination
            );

            Integer count = null;
            try{
                count= jdbcTemplate.queryForObject(sqlCount,Integer.class,parametros);
            }catch (EmptyResultDataAccessException ere){
                logger.info("Consulta de count nao encontrou resultados para classe mapeavel:"+classeMapeavel.getName());
            }catch (Exception e){
                logger.error("Erro nao esperado na consulta de count: "+e.getMessage()+". Classe mapeavel:"+classeMapeavel.getName());
            }

            mapaRetorno.put("list",resultadoPaginacao);
            mapaRetorno.put("count",count);

            return mapaRetorno;
        }catch (EmptyResultDataAccessException ere){
            logger.info("Consulta paginada para a classe "+classeMapeavel.getName()+" nao encontrou resultados. Classe mapeavel:"+classeMapeavel.getName());
            return null;
        }catch (Exception e){
            logger.error("Erro nao esperado na consulta paginada para classe "+classeMapeavel.getName()+": "+e.getMessage());
            return null;
        }
    }

    /**
     * Este cara aqui eh uma generalizacao dos metodos que se utilizam
     * do Spring JDBCTemplate pra fazer uma consulta de count de registros
     *
     * Os parametros sao simplesmente o sql e os seus parametros, que abrangem todos
     * os parametros aceitos pelo JDBCTemplate

     * */
    public Integer countMappableClassObjects(String sql, Object... parametros) {
        String sqlCount = "select count(*) from ( " +sql +" ) )";
        try{
            return jdbcTemplate.queryForObject(sqlCount,Integer.class,parametros);
        }catch (EmptyResultDataAccessException ere){
            logger.info("Consulta de count nao encontrou resultados. " );
            return null;
        }catch (Exception e){
            logger.error("Erro nao esperado na consulta de count: "+e.getMessage());
            return null;
        }
    }

}