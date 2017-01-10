package gov.goias.persistencia;

//import gov.go.corporativo.entidade.PessoaFisica;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import gov.goias.entidades.GENPessoa;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.RFEPessoaFisica;
import gov.goias.services.IGenPessoaFisicaService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: diogo
 * Date: 8/27/13
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GENPessoaFisicaRepository extends GenericRepository<Long, GENPessoaFisica> implements IRepositorioPessoaFisica{

    @Autowired
    IGenPessoaFisicaService genService;


    private static final Logger logger = Logger.getLogger(GENPessoaFisicaRepository.class);

    public GENPessoaFisica findByNumeroBase(Long numeroPessoaBase){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getClass());
        Root<GENPessoaFisica> pessoa = criteriaQuery.from(GENPessoaFisica.class);
        criteriaQuery.where(criteriaBuilder.equal(pessoa.get("numeroPessoaBase"), numeroPessoaBase));

        TypedQuery<GENPessoaFisica> query = entityManager().createQuery(criteriaQuery);

        return query.getSingleResult();
    }

    public GENPessoaFisica findByCpf(String cpf){
        CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getClass());
        Root<GENPessoaFisica> pessoa = criteriaQuery.from(GENPessoaFisica.class);
        criteriaQuery.where(criteriaBuilder.equal(pessoa.get("cpf"),  cpf ));

        TypedQuery<GENPessoaFisica> query = entityManager().createQuery(criteriaQuery);

        try{
            return query.getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public String getEmailWs(){
        try{
            return genService.getEmailPessoaFisicaWs(getIdPessoa().toString());
        }catch (Exception e){
            return null;
        }
    }


    public String getTelefoneWs(){
        try{
            return genService.getTelefonePessoaFisicaWs(getIdPessoa().toString());
        } catch (Exception e){
            return null;
        }

    }

}